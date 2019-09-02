package techcourse.fakebook.service.article;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.article.ArticleRepository;
import techcourse.fakebook.domain.like.ArticleLike;
import techcourse.fakebook.domain.like.ArticleLikeRepository;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.exception.InvalidAuthorException;
import techcourse.fakebook.exception.NotFoundArticleException;
import techcourse.fakebook.service.article.assembler.ArticleAssembler;
import techcourse.fakebook.service.article.dto.ArticleRequest;
import techcourse.fakebook.service.article.dto.ArticleResponse;
import techcourse.fakebook.service.attachment.AttachmentService;
import techcourse.fakebook.service.attachment.dto.AttachmentResponse;
import techcourse.fakebook.service.notification.NotificationService;
import techcourse.fakebook.service.user.UserService;
import techcourse.fakebook.service.user.dto.UserOutline;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArticleService {
    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final ArticleAssembler articleAssembler;
    private final AttachmentService attachmentService;
    private final ArticleLikeService articleLikeService;

    public ArticleService(ArticleRepository articleRepository, UserService userService, ArticleAssembler articleAssembler, AttachmentService attachmentService, ArticleLikeService articleLikeService) {
        this.articleRepository = articleRepository;
        this.userService = userService;
        this.articleAssembler = articleAssembler;
        this.attachmentService = attachmentService;
        this.articleLikeService = articleLikeService;
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

    private ArticleResponse getArticleResponse(Article article) {
        List<AttachmentResponse> attachments = article.getAttachments().stream()
                .map(attachmentService::getAttachmentResponse)
                .collect(Collectors.toList());
        return articleAssembler.toResponse(article, attachments);
    }

    public ArticleResponse save(ArticleRequest articleRequest, UserOutline userOutline) {
        Article.validateArticle(articleRequest.getContent(), Optional.ofNullable(articleRequest.getFiles()));
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

    public boolean like(Long articleId, UserOutline userOutline) {
        if (articleLikeService.isLiked(userOutline.getId(), articleId)) {
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
