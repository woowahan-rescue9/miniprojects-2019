package techcourse.fakebook.domain.article;

import techcourse.fakebook.domain.DateTime;

import javax.persistence.*;

@Entity
public class Article extends DateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isPresent;

    private Article() {}

    public Article(String content) {
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
