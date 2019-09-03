package techcourse.fakebook.service.comment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.comment.Comment;
import techcourse.fakebook.domain.comment.CommentRepository;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.exception.InvalidAuthorException;
import techcourse.fakebook.exception.NotFoundCommentException;
import techcourse.fakebook.service.comment.assembler.CommentAssembler;
import techcourse.fakebook.service.comment.dto.CommentRequest;
import techcourse.fakebook.service.comment.dto.CommentResponse;
import techcourse.fakebook.service.notification.NotificationService;
import techcourse.fakebook.service.user.dto.UserOutline;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentInnerService {
    private final CommentRepository commentRepository;
    private final CommentAssembler commentAssembler;
    private final NotificationService notificationService;

    public CommentInnerService(CommentRepository commentRepository, CommentAssembler commentAssembler, NotificationService notificationService) {
        this.commentRepository = commentRepository;
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

    public CommentResponse save(Article article, User user, CommentRequest commentRequest) {
        Comment comment = commentRepository.save(commentAssembler.toEntity(commentRequest, article, user));
        if (article.isNotAuthor(user.getId())) {
            notificationService.commentFromTo(comment, user.getId(), article);
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

    public Integer getCommentsCountOf(Long articleId) {
        return commentRepository.countCommentByArticleId(articleId);
    }

    Comment getComment(Long id) {
        return commentRepository.findById(id).orElseThrow(NotFoundCommentException::new);
    }

    private void checkAuthor(UserOutline userOutline, Comment comment) {
        if (comment.isNotAuthor(userOutline.getId())) {
            throw new InvalidAuthorException();
        }
    }
}
