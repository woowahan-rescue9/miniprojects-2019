package techcourse.fakebook.domain.like;

import techcourse.fakebook.domain.comment.Comment;
import techcourse.fakebook.domain.user.User;

import javax.persistence.*;

@Entity
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Comment comment;

    public CommentLike(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }
}
