package techcourse.fakebook.domain.like;

import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.user.User;

import javax.persistence.*;

@Entity
public class ArticleLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Article article;

    private ArticleLike() {
    }

    public ArticleLike(User user, Article article) {
        this.user = user;
        this.article = article;
    }

    public User getUser() {
        return user;
    }
}
