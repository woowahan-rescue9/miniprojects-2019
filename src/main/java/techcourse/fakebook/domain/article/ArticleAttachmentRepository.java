package techcourse.fakebook.domain.article;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleAttachmentRepository extends JpaRepository<ArticleAttachment, Long> {
}
