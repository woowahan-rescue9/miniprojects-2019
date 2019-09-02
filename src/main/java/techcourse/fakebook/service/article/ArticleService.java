package techcourse.fakebook.service.article;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.service.article.dto.ArticleRequest;
import techcourse.fakebook.service.article.dto.ArticleResponse;
import techcourse.fakebook.service.user.UserService;
import techcourse.fakebook.service.user.dto.UserOutline;

import java.util.List;

@Service
@Transactional
public class ArticleService {
    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleInnerService articleInnerService;
    private final ArticleLikeService articleLikeService;
    private final UserService userService;

    public ArticleService(ArticleInnerService articleInnerService, ArticleLikeService articleLikeService, UserService userService) {
        this.articleInnerService = articleInnerService;
        this.articleLikeService = articleLikeService;
        this.userService = userService;
    }

    public ArticleResponse findById(Long id) {
        log.debug("articleService.findById : {}", id);
        return articleInnerService.findById(id);
    }

    public List<ArticleResponse> findByUser(User user) {
        log.debug("articleService.findByUser : {}", user);
        return articleInnerService.findByUser(user);
    }

    public List<ArticleResponse> findByUserIn(List<User> users) {
        log.debug("articleService.findByUserIn : {}", users);
        return articleInnerService.findByUserIn(users);
    }

    public ArticleResponse save(ArticleRequest articleRequest, UserOutline userOutline) {
        log.debug("articleService.save : (article : {}, user : {})", articleRequest, userOutline);
        return articleInnerService.save(articleRequest, userService.getUser(userOutline.getId()));
    }

    public ArticleResponse update(Long id, ArticleRequest updatedRequest, UserOutline userOutline) {
        log.debug("articleService.update : (id : {}, article : {}, user : {})", id, updatedRequest, userOutline);
        return articleInnerService.update(id, updatedRequest, userOutline);
    }

    public void deleteById(Long id, UserOutline userOutline) {
        log.debug("articleService.deleteById : (id : {}, user : {})", id, userOutline);
        articleInnerService.deleteById(id, userOutline);
    }

    public boolean like(Long articleId, UserOutline userOutline) {
        if (isLiked(articleId, userOutline)) {
            articleLikeService.cancelLike(userOutline.getId(), articleId);
            return false;
        }

        articleLikeService.save(userService.getUser(userOutline.getId()), getArticle(articleId));
        return true;
    }


    public boolean isLiked(Long articleId, UserOutline userOutline) {
        return articleLikeService.isLiked(userOutline.getId(), articleId);
    }

    public Integer getLikeCountOf(Long articleId) {
        return articleLikeService.countByArticleId(articleId);
    }

    public Article getArticle(Long id) {
        return articleInnerService.getArticle(id);
    }
}
