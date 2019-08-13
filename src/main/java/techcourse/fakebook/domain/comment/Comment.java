package techcourse.fakebook.domain.comment;

import techcourse.fakebook.domain.DateTime;
import techcourse.fakebook.domain.article.Article;

import javax.persistence.*;

@Entity
public class Comment extends DateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_COMMENT_TO_ARTICLE"))
    private Article article;

    @Column(nullable = false)
    private boolean isPresent;


    private Comment() {
    }

    public Comment(String content) {
        this.content = content;
        this.isPresent = true;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void update(String content) {
        this.content = content;
    }

    public boolean isNotPresent() {
        return !isPresent;
    }

    public void delete() {
        isPresent = false;
    }
}
