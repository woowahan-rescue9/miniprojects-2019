package techcourse.fakebook.service.utils;

import techcourse.fakebook.service.dto.UserOutline;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.comment.Comment;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.service.dto.CommentRequest;
import techcourse.fakebook.service.dto.CommentResponse;

public class CommentAssembler {
    private CommentAssembler() {}

    public static Comment toEntity(CommentRequest commentRequest, Article article, User user) {
        return new Comment(commentRequest.getContent(), article, user);
    }

    public static CommentResponse toResponse(Comment comment) {
        UserOutline userOutline = UserAssembler.toUserOutline(comment.getUser());
        return new CommentResponse(comment.getId(), comment.getContent(), comment.getCreatedDate(), userOutline);
    }
}
