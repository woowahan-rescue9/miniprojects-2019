package techcourse.fakebook.service.article;

import org.springframework.stereotype.Service;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.article.ArticleRepository;
import techcourse.fakebook.domain.like.ArticleLike;
import techcourse.fakebook.domain.like.ArticleLikeRepository;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.exception.InvalidAuthorException;
import techcourse.fakebook.exception.NotFoundArticleException;
import techcourse.fakebook.service.article.assembler.ArticleAssembler;
import techcourse.fakebook.service.user.UserService;
import techcourse.fakebook.service.user.dto.UserOutline;
import techcourse.fakebook.service.article.dto.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final UserService userService;
    private final ArticleAssembler articleAssembler;
    private final AttachmentService attachmentService;

    public ArticleService(ArticleRepository articleRepository, ArticleLikeRepository articleLikeRepository,
                          UserService userService, ArticleAssembler articleAssembler, AttachmentService attachmentService) {
        this.articleRepository = articleRepository;
        this.articleLikeRepository = articleLikeRepository;
        this.userService = userService;
        this.articleAssembler = articleAssembler;
        this.attachmentService = attachmentService;
    }

    public ArticleResponse findById(Long id) {
        Article article = getArticle(id);
        return getArticleResponse(article);
    }

    public List<ArticleResponse> findByUser(User user) {
        return articleRepository.findArticlesByUserOrderByCreatedDateDesc(user).stream()
                .map(this::getArticleResponse)
                .collect(Collectors.toList());
    }

    public List<ArticleResponse> findByUserIn(List<User> users) {
        return articleRepository.findByUserInOrderByCreatedDateDesc(users).stream()
                .map(this::getArticleResponse)
                .collect(Collectors.toList());
    }

    public List<ArticleResponse> findAll() {
        List<Article> articles = articleRepository.findAllByOrderByCreatedDateDesc();
        return articles.stream()
                .map(this::getArticleResponse)
                .collect(Collectors.toList());
    }

    private ArticleResponse getArticleResponse(Article article) {
        List<AttachmentResponse> attachments = article.getAttachments().stream()
                .map(attachmentService::getAttachmentResponse)
                .collect(Collectors.toList());
        return articleAssembler.toResponse(article, attachments);
    }

    public ArticleResponse save(ArticleRequest articleRequest, UserOutline userOutline) {
        User user = userService.getUser(userOutline.getId());
        Article article = articleRepository.save(articleAssembler.toEntity(articleRequest, user));
        List<AttachmentResponse> files = Optional.ofNullable(articleRequest.getFiles()).orElse(new ArrayList<>()).stream()
                .map(file -> attachmentService.saveAttachment(file, article))
                .collect(Collectors.toList());
        return articleAssembler.toResponse(article, files);
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

    public boolean like(Long id, UserOutline userOutline) {
        Optional<ArticleLike> articleLike = Optional.ofNullable(articleLikeRepository.findByUserIdAndArticleId(userOutline.getId(), id));
        if (articleLike.isPresent()) {
            articleLikeRepository.delete(articleLike.get());
            return false;
        }
        articleLikeRepository.save(new ArticleLike(userService.getUser(userOutline.getId()), getArticle(id)));
        return true;
    }

    public boolean isLiked(Long id, UserOutline userOutline) {
        return articleLikeRepository.existsByUserIdAndArticleId(userOutline.getId(), id);
    }

    public Integer getLikeCountOf(Long articleId) {
        return articleLikeRepository.countArticleLikeByArticleId(articleId);
    }

    public Article getArticle(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(NotFoundArticleException::new);
        if (article.isDeleted()) {
            throw new NotFoundArticleException();
        }
        return article;
    }

    private void checkAuthor(UserOutline userOutline, Article article) {
        if (article.isNotAuthor(userOutline.getId())) {
            throw new InvalidAuthorException();
        }
    }
}
