package techcourse.fakebook.domain.chat;

import org.hibernate.annotations.ColumnDefault;
import techcourse.fakebook.domain.BaseEntity;
import techcourse.fakebook.domain.user.User;

import javax.persistence.*;

@Entity
public class Chat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String content;

    @OneToOne
    private User fromUser;

    @OneToOne
    private User toUser;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean read;

    private Chat() {
    }

    public Chat(String content, User fromUser, User toUser, Boolean read) {
        this.content = content;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.read = read;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public User getFromUser() {
        return fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public Boolean getRead() {
        return read;
    }
}
