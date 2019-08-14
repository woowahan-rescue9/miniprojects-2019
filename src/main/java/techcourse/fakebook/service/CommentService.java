package techcourse.fakebook.service;

import org.springframework.stereotype.Service;
import techcourse.fakebook.service.dto.UserOutline;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.comment.Comment;
import techcourse.fakebook.domain.comment.CommentRepository;
import techcourse.fakebook.domain.like.CommentLike;
import techcourse.fakebook.domain.like.CommentLikeRepository;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.exception.InvalidAuthorException;
import techcourse.fakebook.exception.NotFoundCommentException;
import techcourse.fakebook.service.dto.CommentLikeResponse;
import techcourse.fakebook.service.dto.CommentRequest;
import techcourse.fakebook.service.dto.CommentResponse;
import techcourse.fakebook.service.utils.CommentAssembler;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {
    private ArticleService articleService;
    private UserService userService;
    private CommentRepository commentRepository;
    private CommentLikeRepository commentLikeRepository;

    public CommentService(ArticleService articleService, UserService userService, CommentRepository commentRepository, CommentLikeRepository commentLikeRepository) {
        this.articleService = articleService;
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.commentLikeRepository = commentLikeRepository;
    }

    public CommentResponse findById(Long id) {
        Comment comment = getComment(id);
        return CommentAssembler.toResponse(comment);
    }

    public List<CommentResponse> findAllByArticleId(Long id) {
        return commentRepository.findAllByArticleId(id).stream()
                .map(CommentAssembler::toResponse)
                .collect(Collectors.toList());
    }

    public CommentResponse save(Long articleId, CommentRequest commentRequest, UserOutline userOutline) {
        User user = userService.getUser(userOutline.getId());
        Article article = articleService.getArticle(articleId);
        Comment comment = commentRepository.save(CommentAssembler.toEntity(commentRequest, article, user));
        return CommentAssembler.toResponse(comment);
    }

    public CommentResponse update(Long id, CommentRequest updatedRequest, UserOutline userOutline) {
        Comment comment = getComment(id);
        checkAuthor(userOutline, comment);
        comment.update(updatedRequest.getContent());
        return CommentAssembler.toResponse(comment);
    }

    public void deleteById(Long id, UserOutline userOutline) {
        Comment comment = getComment(id);
        checkAuthor(userOutline, comment);
        comment.delete();
    }

    private Comment getComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(NotFoundCommentException::new);
        if (comment.isNotPresent()) {
            throw new NotFoundCommentException();
        }
        return comment;
    }

    private void checkAuthor(UserOutline userOutline, Comment comment) {
        if (comment.getUser().isNotAuthor(userOutline.getId())) {
            throw new InvalidAuthorException();
        }
    }

    public CommentLikeResponse like(Long id, UserOutline userOutline) {
        CommentLike commentLike = new CommentLike(userService.getUser(userOutline.getId()), getComment(id));
        if (commentLikeRepository.existsByUserIdAndCommentId(userOutline.getId(), id)) {
            commentLikeRepository.delete(commentLike);
            return new CommentLikeResponse(id, false);
        }
        commentLikeRepository.save(commentLike);
        return new CommentLikeResponse(id, true);
    }

    public CommentLikeResponse isLiked(Long commentId, UserOutline userOutline) {
        if (commentLikeRepository.existsByUserIdAndCommentId(userOutline.getId(), commentId)) {
            return new CommentLikeResponse(commentId, true);
        }
        return new CommentLikeResponse(commentId, false);
    }
}
