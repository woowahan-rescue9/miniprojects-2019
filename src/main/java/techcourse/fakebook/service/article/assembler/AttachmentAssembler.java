package techcourse.fakebook.service.article.assembler;

import org.springframework.stereotype.Component;
import techcourse.fakebook.domain.article.ArticleAttachment;
import techcourse.fakebook.domain.user.UserProfileImage;
import techcourse.fakebook.service.article.dto.AttachmentResponse;

@Component
public class AttachmentAssembler {
    private AttachmentAssembler() {
    }

    public AttachmentResponse toResponse(ArticleAttachment articleAttachment) {
        return new AttachmentResponse(articleAttachment.getName(), articleAttachment.getPath());
    }

    public AttachmentResponse toResponse(UserProfileImage articleAttachment) {
        return new AttachmentResponse(articleAttachment.getName(), articleAttachment.getPath());
    }
}
