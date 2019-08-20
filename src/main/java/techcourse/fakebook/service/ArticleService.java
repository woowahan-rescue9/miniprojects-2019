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
import java.util.Optional;

@Service
@Transactional
public class ArticleService {
    private ArticleRepository articleRepository;
    private ArticleLikeRepository articleLikeRepository;
    private UserService userService;
    private ArticleAssembler articleAssembler;

    public ArticleService(ArticleRepository articleRepository, ArticleLikeRepository articleLikeRepository, UserService userService, ArticleAssembler articleAssembler) {
        this.articleRepository = articleRepository;
        this.articleLikeRepository = articleLikeRepository;
        this.userService = userService;
        this.articleAssembler = articleAssembler;
    }

    public ArticleResponse findById(Long id) {
        Article article = getArticle(id);
        return articleAssembler.toResponse(article);
    }

    public ArticleResponse save(ArticleRequest articleRequest, UserOutline userOutline) {
        User user = userService.getUser(userOutline.getId());
        Article article = articleRepository.save(articleAssembler.toEntity(articleRequest, user));
        return articleAssembler.toResponse(article);
    }

    public ArticleResponse update(Long id, ArticleRequest updatedRequest, UserOutline userOutline) {
        Article article = getArticle(id);
        checkAuthor(userOutline, article);
        article.update(updatedRequest.getContent());
        return articleAssembler.toResponse(article);
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
        Optional<ArticleLike> articleLike = Optional.ofNullable(articleLikeRepository.findByUserIdAndArticleId(userOutline.getId(), id));
        if (articleLike.isPresent()) {
            articleLikeRepository.delete(articleLike.get());
            return new ArticleLikeResponse(id, false);
        }
        articleLikeRepository.save(new ArticleLike(userService.getUser(userOutline.getId()), getArticle(id)));
        return new ArticleLikeResponse(id, true);
    }

    public ArticleLikeResponse isLiked(Long id, UserOutline userOutline) {
        if (articleLikeRepository.existsByUserIdAndArticleId(userOutline.getId(), id)) {
            return new ArticleLikeResponse(id, true);
        }
        return new ArticleLikeResponse(id, false);
    }

    private void checkAuthor(UserOutline userOutline, Article article) {
        if (!article.getUser().isSameWith(userOutline.getId())) {
            throw new InvalidAuthorException();
        }
    }

    public Integer getLikeCountOf(Long articleId) {
        return articleLikeRepository.countArticleLikeByArticleId(articleId);
    }
}
