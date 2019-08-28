package techcourse.fakebook.domain.article;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.fakebook.domain.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class ArticleAttachment extends BaseEntity {
    public static String ARTICLE_STATIC_FILE_PATH = "article/";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String path;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Article article;

    private ArticleAttachment() {
    }

    public ArticleAttachment(String name, String path, Article article) {
        this.name = name;
        this.path = path;
        this.article = article;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public Article getArticle() {
        return article;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleAttachment that = (ArticleAttachment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
