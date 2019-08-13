package techcourse.fakebook.domain.like;

import techcourse.fakebook.domain.comment.Comment;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class CommentLike extends Like {
    @ManyToOne
    private Comment comment;
}
