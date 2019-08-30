package techcourse.fakebook.service.comment.assembler;

import org.springframework.stereotype.Component;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.comment.Comment;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.service.comment.dto.CommentRequest;
import techcourse.fakebook.service.comment.dto.CommentResponse;
import techcourse.fakebook.service.user.assembler.UserAssembler;
import techcourse.fakebook.service.user.dto.UserOutline;

@Component
public class CommentAssembler {
    private final UserAssembler userAssembler;

    public CommentAssembler(UserAssembler userAssembler) {
        this.userAssembler = userAssembler;
    }

    public Comment toEntity(CommentRequest commentRequest, Article article, User user) {
        return new Comment(commentRequest.getContent(), article, user);
    }

    public CommentResponse toResponse(Comment comment) {
        UserOutline userOutline = userAssembler.toUserOutline(comment.getUser());
        return new CommentResponse(comment.getId(), comment.getContent(), comment.getCreatedDate(), userOutline);
    }
}