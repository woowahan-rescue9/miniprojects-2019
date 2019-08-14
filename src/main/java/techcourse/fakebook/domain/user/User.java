package techcourse.fakebook.domain.user;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    private User() {
    }

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public boolean isNotAuthor(Long id) {
        return !this.id.equals(id);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

}
