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
    private Boolean readable;

    private Chat() {
    }

    public Chat(String content, User fromUser, User toUser, Boolean readable) {
        this.content = content;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.readable = readable;
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

    public Boolean getReadable() {
        return readable;
    }
}
