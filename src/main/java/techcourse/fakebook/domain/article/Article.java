package techcourse.fakebook.domain.article;

import techcourse.fakebook.domain.BaseEntity;
import techcourse.fakebook.domain.user.User;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Article extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String content;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private boolean deleted;

    private Article() {
    }

    public Article(String content, User user) {
        this.content = content;
        this.user = user;
        this.deleted = false;
    }

    public void update(String content) {
        this.content = content;
    }

    public void delete() {
        deleted = true;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public boolean isNotAuthor(Long id) {
        return !user.isSameWith(id);
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
