package techcourse.fakebook.service.comment;

import org.springframework.stereotype.Service;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.comment.Comment;
import techcourse.fakebook.domain.comment.CommentRepository;
import techcourse.fakebook.domain.like.CommentLike;
import techcourse.fakebook.domain.like.CommentLikeRepository;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.exception.InvalidAuthorException;
import techcourse.fakebook.exception.NotFoundCommentException;
import techcourse.fakebook.service.article.ArticleService;
import techcourse.fakebook.service.comment.assembler.CommentAssembler;
import techcourse.fakebook.service.comment.dto.CommentRequest;
import techcourse.fakebook.service.comment.dto.CommentResponse;
import techcourse.fakebook.service.notification.NotificationService;
import techcourse.fakebook.service.user.UserService;
import techcourse.fakebook.service.user.dto.UserOutline;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {
    private final ArticleService articleService;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentAssembler commentAssembler;
    private final NotificationService notificationService;

    public CommentService(ArticleService articleService, UserService userService,
                          CommentRepository commentRepository, CommentLikeRepository commentLikeRepository, CommentAssembler commentAssembler,
                          NotificationService notificationService) {
        this.articleService = articleService;
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.commentLikeRepository = commentLikeRepository;
        this.commentAssembler = commentAssembler;
        this.notificationService = notificationService;
    }

    public CommentResponse findById(Long id) {
        Comment comment = getComment(id);
        return commentAssembler.toResponse(comment);
    }

    public List<CommentResponse> findAllByArticleId(Long id) {
        return commentRepository.findAllByArticleIdOrderByCreatedDateAsc(id).stream()
                .map(commentAssembler::toResponse)
                .collect(Collectors.toList());
    }

    public CommentResponse save(Long articleId, CommentRequest commentRequest, UserOutline userOutline) {
        User user = userService.getUser(userOutline.getId());
        Article article = articleService.getArticle(articleId);
        Comment comment = commentRepository.save(commentAssembler.toEntity(commentRequest, article, user));
        if (article.isNotAuthor(userOutline.getId())) {
            notificationService.commentFromTo(userOutline.getId(), article, comment);
        }
        return commentAssembler.toResponse(comment);
    }

    public CommentResponse update(Long id, CommentRequest updatedRequest, UserOutline userOutline) {
        Comment comment = getComment(id);
        checkAuthor(userOutline, comment);
        comment.update(updatedRequest.getContent());
        return commentAssembler.toResponse(comment);
    }

    public void deleteById(Long id, UserOutline userOutline) {
        Comment comment = getComment(id);
        checkAuthor(userOutline, comment);
        comment.delete();
    }

    public boolean like(Long commentId, UserOutline userOutline) {
        Optional<CommentLike> commentLike = Optional.ofNullable(commentLikeRepository.findByUserIdAndCommentId(userOutline.getId(), commentId));
        if (commentLike.isPresent()) {
            commentLikeRepository.delete(commentLike.get());
            return false;
        }
        commentLikeRepository.save(new CommentLike(userService.getUser(userOutline.getId()), getComment(commentId)));
        return true;
    }

    public boolean isLiked(Long commentId, UserOutline userOutline) {
        return commentLikeRepository.existsByUserIdAndCommentId(userOutline.getId(), commentId);
    }

    public Integer getLikeCountOf(Long commentId) {
        return commentLikeRepository.countCommentLikeByCommentId(commentId);
    }

    public Integer getCommentsCountOf(Long articleId) {
        return commentRepository.countCommentByArticleId(articleId);
    }

    private Comment getComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(NotFoundCommentException::new);
        if (comment.isDeleted()) {
            throw new NotFoundCommentException();
        }
        return comment;
    }

    private void checkAuthor(UserOutline userOutline, Comment comment) {
        if (comment.isNotAuthor(userOutline.getId())) {
            throw new InvalidAuthorException();
        }
    }
}
