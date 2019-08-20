package techcourse.fakebook.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
    boolean existsByUserIdAndArticleId(Long userId, Long articleId);

    ArticleLike findByUserIdAndArticleId(Long userId, Long articleId);

    Integer countArticleLikeByArticleId(Long articleId);
}
