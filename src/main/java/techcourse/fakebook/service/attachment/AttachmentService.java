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
import techcourse.fakebook.utils.uploader.Uploader;
import techcourse.fakebook.utils.uploader.UploaderConfig;
import techcourse.fakebook.utils.uploader.s3.S3Uploader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AttachmentService {
    private static final String IMAGE = "image";
    private static final Logger log = LoggerFactory.getLogger(AttachmentService.class);

    private final ArticleAttachmentRepository articleAttachmentRepository;
    private final AttachmentAssembler attachmentAssembler;
    private final Uploader uploader;
    private final UploaderConfig uploaderConfig;

    public AttachmentService(ArticleAttachmentRepository articleAttachmentRepository, AttachmentAssembler attachmentAssembler,
                             S3Uploader uploader, UploaderConfig uploaderConfig) {
        this.articleAttachmentRepository = articleAttachmentRepository;
        this.attachmentAssembler = attachmentAssembler;
        this.uploader = uploader;
        this.uploaderConfig = uploaderConfig;
    }

    public AttachmentResponse saveAttachment(MultipartFile file, Article article) {
        String hashingName = getHashedName(file.getOriginalFilename());

        checkImageType(file);

        String filePath = uploader.upload(file, uploaderConfig.getArticlePath(), hashingName);
        ArticleAttachment articleAttachment = new ArticleAttachment(file.getOriginalFilename(), filePath, article);

        return getAttachmentResponse(articleAttachmentRepository.save(articleAttachment));
    }

    public void checkImageType(MultipartFile file) {
        Tika tika = new Tika();

        try {
            String mimeType = tika.detect(file.getBytes());
            log.debug("### MIME Type = {}", mimeType);
            if (!mimeType.startsWith(IMAGE)) {
                throw new NotImageTypeException();
            }
        } catch (IOException e) {
            log.error("FileSaveError : file.getByte 실패");
            throw new FileSaveException();
        }
    }

    public UserProfileImage getDefaultProfileImage() {
        return new UserProfileImage(uploaderConfig.getUserProfileDefaultName(), uploaderConfig.getUserProfileDefaultPath());
    }

    public UserProfileImage saveProfileImage(MultipartFile file) {
        String hashingName = getHashedName(file.getOriginalFilename());
        checkImageType(file);

        String filePath = uploader.upload(file, uploaderConfig.getUserProfilePath(), hashingName);
        return new UserProfileImage(file.getOriginalFilename(), filePath);
    }

    private String getHashedName(String originalFileName) {
        String hashCodeOfFile = UUID.randomUUID().toString();

        List<String> splits = Arrays.asList(originalFileName.split("\\."));
        if (splits.size() < 1) {
            log.error("FileSaveError : 파일의 확장자를 확인할 수 없습니다.");
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
