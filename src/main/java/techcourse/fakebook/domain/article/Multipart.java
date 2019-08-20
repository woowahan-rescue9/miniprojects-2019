package techcourse.fakebook.domain.article;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.fakebook.domain.BaseEntity;

import javax.persistence.*;

@Entity
public class Multipart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String imagePath;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Article article;

    private Multipart() {}

    public Multipart(String imagePath, Article article) {
        this.imagePath = imagePath;
        this.article = article;
    }

    public Long getId() {
        return id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Article getArticle() {
        return article;
    }
}
