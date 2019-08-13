package techcourse.fakebook.utils;

import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.comment.Comment;
import techcourse.fakebook.service.dto.CommentRequest;
import techcourse.fakebook.service.dto.CommentResponse;

public class CommentAssembler {
    private CommentAssembler() {}

    public static Comment toEntity(CommentRequest commentRequest, Article article) {
        return new Comment(commentRequest.getContent(), article);
    }

    public static CommentResponse toResponse(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getContent(), comment.getCreatedDate());
    }
}
