package techcourse.fakebook.domain.article;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleMultipartRepository extends JpaRepository<ArticleMultipart, Long> {
}
