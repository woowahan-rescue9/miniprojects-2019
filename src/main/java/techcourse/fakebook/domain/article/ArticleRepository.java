package techcourse.fakebook.domain.article;

import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.fakebook.domain.user.User;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllByOrderByModifiedDateDescCreatedDateDesc();

    List<Article> findArticlesByUserOrderByCreatedDateDesc(User user);

    List<Article> findByUserInOrderByCreatedDateDesc(List<User> users);
}
