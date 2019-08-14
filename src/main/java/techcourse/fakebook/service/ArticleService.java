package techcourse.fakebook.service;

import org.springframework.stereotype.Service;
import techcourse.fakebook.service.dto.UserOutline;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.article.ArticleRepository;
import techcourse.fakebook.domain.like.ArticleLike;
import techcourse.fakebook.domain.like.ArticleLikeRepository;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.exception.InvalidAuthorException;
import techcourse.fakebook.exception.NotFoundArticleException;
import techcourse.fakebook.service.dto.ArticleLikeResponse;
import techcourse.fakebook.service.dto.ArticleRequest;
import techcourse.fakebook.service.dto.ArticleResponse;
import techcourse.fakebook.service.utils.ArticleAssembler;

import javax.transaction.Transactional;

@Service
@Transactional
public class ArticleService {
    private ArticleRepository articleRepository;
    private ArticleLikeRepository articleLikeRepository;
    private UserService userService;

    public ArticleService(ArticleRepository articleRepository, ArticleLikeRepository articleLikeRepository, UserService userService) {
        this.articleRepository = articleRepository;
        this.articleLikeRepository = articleLikeRepository;
        this.userService = userService;
    }

    public ArticleResponse findById(Long id) {
        Article article = getArticle(id);
        return ArticleAssembler.toResponse(article);
    }

    public ArticleResponse save(ArticleRequest articleRequest, UserOutline userOutline) {
        User user = userService.getUser(userOutline.getId());
        Article article = articleRepository.save(ArticleAssembler.toEntity(articleRequest, user));
        return ArticleAssembler.toResponse(article);
    }

    public ArticleResponse update(Long id, ArticleRequest updatedRequest, UserOutline userOutline) {
        Article article = getArticle(id);
        checkAuthor(userOutline, article);
        article.update(updatedRequest.getContent());
        return ArticleAssembler.toResponse(article);
    }

    public void deleteById(Long id, UserOutline userOutline) {
        Article article = getArticle(id);
        checkAuthor(userOutline, article);
        article.delete();
    }

    Article getArticle(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(NotFoundArticleException::new);
        if (article.isNotPresent()) {
            throw new NotFoundArticleException();
        }
        return article;
    }

    public ArticleLikeResponse like(Long id, UserOutline userOutline) {
        ArticleLike articleLike = new ArticleLike(userService.getUser(userOutline.getId()), getArticle(id));
        if (articleLikeRepository.existsByUserIdAndArticleId(userOutline.getId(), id)) {
            articleLikeRepository.delete(articleLike);
            return new ArticleLikeResponse(id, false);
        }
        articleLikeRepository.save(articleLike);
        return new ArticleLikeResponse(id, true);
    }

    public ArticleLikeResponse isLiked(Long id, UserOutline userOutline) {
        if (articleLikeRepository.existsByUserIdAndArticleId(userOutline.getId(), id)) {
            return new ArticleLikeResponse(id, true);
        }
        return new ArticleLikeResponse(id, false);
    }

    private void checkAuthor(UserOutline userOutline, Article article) {
        if (article.getUser().isNotAuthor(userOutline.getId())) {
            throw new InvalidAuthorException();
        }
    }
}
