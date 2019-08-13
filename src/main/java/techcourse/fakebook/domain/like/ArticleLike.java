package techcourse.fakebook.domain.like;

import techcourse.fakebook.domain.article.Article;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class ArticleLike extends Like {
    @ManyToOne
    private Article article;
}
