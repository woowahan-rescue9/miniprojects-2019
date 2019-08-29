package techcourse.fakebook.service.attachment;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.article.ArticleAttachment;
import techcourse.fakebook.domain.article.ArticleAttachmentRepository;
import techcourse.fakebook.domain.user.UserProfileImage;
import techcourse.fakebook.exception.FileSaveException;
import techcourse.fakebook.exception.NotImageTypeException;
import techcourse.fakebook.service.attachment.assembler.AttachmentAssembler;
import techcourse.fakebook.service.attachment.dto.AttachmentResponse;
import techcourse.fakebook.utils.s3.S3Uploader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AttachmentService {
    private static final Logger log = LoggerFactory.getLogger(AttachmentService.class);

    private final ArticleAttachmentRepository articleAttachmentRepository;
    private final AttachmentAssembler attachmentAssembler;
    private final S3Uploader s3Uploader;

    public AttachmentService(ArticleAttachmentRepository articleAttachmentRepository, AttachmentAssembler attachmentAssembler, S3Uploader s3Uploader) {
        this.articleAttachmentRepository = articleAttachmentRepository;
        this.attachmentAssembler = attachmentAssembler;
        this.s3Uploader = s3Uploader;
    }

    @Transactional
    public AttachmentResponse saveAttachment(MultipartFile file, Article article) {
        try {
            String hashingName = getHashedName(file.getOriginalFilename());

            checkImageType(file);

            String filePath = s3Uploader.upload(file, ArticleAttachment.ARTICLE_STATIC_FILE_PATH, hashingName);

            ArticleAttachment articleAttachment = new ArticleAttachment(file.getOriginalFilename(), filePath, article);

            return getAttachmentResponse(articleAttachmentRepository.save(articleAttachment));
        } catch (IOException e) {
            throw new FileSaveException();
        }
    }

    public void checkImageType(MultipartFile file) throws IOException {
        Tika tika = new Tika();

        String mimeType = tika.detect(file.getBytes());
        log.debug("### MIME Type = {}", mimeType);
        if (!mimeType.startsWith("image")) {
            throw new NotImageTypeException();
        }
    }

    public UserProfileImage getDefaultProfileImage() {
        return new UserProfileImage(UserProfileImage.DEFAULT_IMAGE_NAME, UserProfileImage.DEFAULT_IMAGE_PATH);
    }

    public UserProfileImage getProfileImage(MultipartFile file) {
        try {
            String hashingName = getHashedName(file.getOriginalFilename());

            String filePath = s3Uploader.upload(file, UserProfileImage.USER_STATIC_FILE_PATH, hashingName);

            return new UserProfileImage(file.getOriginalFilename(), filePath);
        } catch (IOException e) {
            throw new FileSaveException();
        }
    }

    private String getHashedName(String originalFileName) {
        String hashCodeOfFile = UUID.randomUUID().toString();

        List<String> splits = Arrays.asList(originalFileName.split("\\."));
        if (splits.size() < 1) {
            throw new FileSaveException();
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
