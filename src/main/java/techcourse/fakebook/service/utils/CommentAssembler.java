package techcourse.fakebook.service.utils;

import org.springframework.stereotype.Component;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.comment.Comment;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.service.dto.CommentRequest;
import techcourse.fakebook.service.dto.CommentResponse;
import techcourse.fakebook.service.dto.UserOutline;

@Component
public class CommentAssembler {
    private CommentAssembler() {
    }

    public Comment toEntity(CommentRequest commentRequest, Article article, User user) {
        return new Comment(commentRequest.getContent(), article, user);
    }

    public CommentResponse toResponse(Comment comment) {
        UserOutline userOutline = UserAssembler.toUserOutline(comment.getUser());
        return new CommentResponse(comment.getId(), comment.getContent(), comment.getCreatedDate(), userOutline);
    }
}
