package techcourse.fakebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.fakebook.domain.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
