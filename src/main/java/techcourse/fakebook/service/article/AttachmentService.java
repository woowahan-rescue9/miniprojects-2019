package techcourse.fakebook.service.article;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.article.ArticleAttachment;
import techcourse.fakebook.domain.article.ArticleAttachmentRepository;
import techcourse.fakebook.domain.user.UserProfileImage;
import techcourse.fakebook.exception.FileSaveException;
import techcourse.fakebook.service.article.assembler.AttachmentAssembler;
import techcourse.fakebook.service.article.dto.AttachmentResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AttachmentService {
    private final ArticleAttachmentRepository articleAttachmentRepository;
    private final AttachmentAssembler attachmentAssembler;

    public AttachmentService(ArticleAttachmentRepository articleAttachmentRepository, AttachmentAssembler attachmentAssembler) {
        this.articleAttachmentRepository = articleAttachmentRepository;
        this.attachmentAssembler = attachmentAssembler;
    }

    public AttachmentResponse saveAttachment(MultipartFile file, Article article) {
        try {
            String hashingName = getHashedName(file.getOriginalFilename());

            Path filePath = writeFile(file, hashingName, ArticleAttachment.ARTICLE_STATIC_FILE_PATH);

            ArticleAttachment articleAttachment = new ArticleAttachment(file.getOriginalFilename(), filePath.toString(), article);

            return getAttachmentResponse(articleAttachmentRepository.save(articleAttachment));
        } catch (IOException e) {
            throw new FileSaveException(e.getMessage());
        }
    }

    public UserProfileImage getDefaultProfileImage() {
        Path filesPath = Paths.get(UserProfileImage.USER_STATIC_FILE_PATH + UserProfileImage.DEFAULT_IMAGE_NAME);
        return new UserProfileImage(UserProfileImage.DEFAULT_IMAGE_NAME, filesPath.toString());
    }

    public UserProfileImage getProfileImage(MultipartFile file) {
        try {
            String hashingName = getHashedName(file.getOriginalFilename());

            Path filePath = writeFile(file, hashingName, UserProfileImage.USER_STATIC_FILE_PATH);

            return new UserProfileImage(file.getOriginalFilename(), filePath.toString());
        } catch (IOException e) {
            throw new FileSaveException(e.getMessage());
        }
    }

    private Path writeFile(MultipartFile file, String hashingName, String path) throws IOException {
        byte[] bytes = file.getBytes();
        Path staticFilePath = Paths.get(path + hashingName);
        return Files.write(staticFilePath, bytes);
    }

    private String getHashedName(String originalFileName) {
        String hashCodeOfFile = UUID.randomUUID().toString();

        List<String> splits = Arrays.asList(originalFileName.split("\\."));
        if (splits.size() < 1) {
            throw new FileSaveException("파일형식이 올바르지 않습니다.");
        }
        String extension = splits.get(splits.size() - 1);

        return hashCodeOfFile + "." + extension;
    }

    public AttachmentResponse getAttachmentResponse(ArticleAttachment attachment) {
        return attachmentAssembler.toResponse(attachment);
    }

    public AttachmentResponse getAttachmentResponse(UserProfileImage attachment) {
        return attachmentAssembler.toResponse(attachment);
    }
}
