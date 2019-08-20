package techcourse.fakebook.domain.like;

import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.user.User;

import javax.persistence.*;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleLike that = (ArticleLike) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
