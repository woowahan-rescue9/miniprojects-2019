package techcourse.fakebook.service;

import org.springframework.stereotype.Service;
import techcourse.fakebook.controller.utils.SessionUser;
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

    public ArticleResponse save(ArticleRequest articleRequest, SessionUser sessionUser) {
        User user = userService.getUser(sessionUser.getId());
        Article article = articleRepository.save(ArticleAssembler.toEntity(articleRequest, user));
        return ArticleAssembler.toResponse(article);
    }

    public ArticleResponse update(Long id, ArticleRequest updatedRequest, SessionUser sessionUser) {
        Article article = getArticle(id);
        checkAuthor(sessionUser, article);
        article.update(updatedRequest.getContent());
        return ArticleAssembler.toResponse(article);
    }

    public void deleteById(Long id, SessionUser sessionUser) {
        Article article = getArticle(id);
        checkAuthor(sessionUser, article);
        article.delete();
    }

    Article getArticle(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(NotFoundArticleException::new);
        if (article.isNotPresent()) {
            throw new NotFoundArticleException();
        }
        return article;
    }

    public ArticleLikeResponse like(Long id, SessionUser sessionUser) {
        ArticleLike articleLike = new ArticleLike(userService.getUser(sessionUser.getId()), getArticle(id));
        if (articleLikeRepository.existsByUserIdAndArticleId(sessionUser.getId(), id)) {
            articleLikeRepository.delete(articleLike);
            return new ArticleLikeResponse(id, false);
        }
        articleLikeRepository.save(articleLike);
        return new ArticleLikeResponse(id, true);
    }

    public ArticleLikeResponse isLiked(Long id, SessionUser sessionUser) {
        if (articleLikeRepository.existsByUserIdAndArticleId(sessionUser.getId(), id)) {
            return new ArticleLikeResponse(id, true);
        }
        return new ArticleLikeResponse(id, false);
    }

    private void checkAuthor(SessionUser sessionUser, Article article) {
        if (article.getUser().isNotAuthor(sessionUser.getId())) {
            throw new InvalidAuthorException();
        }
    }
}
