package techcourse.fakebook.service.article;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.like.ArticleLike;
import techcourse.fakebook.domain.like.ArticleLikeRepository;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.service.notification.NotificationService;

@Service
@Transactional
public class ArticleLikeService {
    private final ArticleLikeRepository articleLikeRepository;
    private final NotificationService notificationService;

    public ArticleLikeService(ArticleLikeRepository articleLikeRepository, NotificationService notificationService) {
        this.articleLikeRepository = articleLikeRepository;
        this.notificationService = notificationService;
    }

    public ArticleLike save(User user, Article article) {
        ArticleLike articleLike = articleLikeRepository.save(new ArticleLike(user, article));
        if (article.isNotAuthor(user.getId())) {
            notificationService.likeFromTo(user.getId(), article);
        }
        return articleLike;
    }

    public boolean isLiked(Long userId, Long articleId) {
        return articleLikeRepository.existsByUserIdAndArticleId(userId, articleId);
    }

    public void cancelLike(Long userId, Long articleId) {
        articleLikeRepository.deleteByUserIdAndArticleId(userId, articleId);
    }

    public Integer countByArticleId(Long articleId) {
        return articleLikeRepository.countArticleLikeByArticleId(articleId);
    }
}
