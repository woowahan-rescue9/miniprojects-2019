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
import techcourse.fakebook.service.utils.UserAssembler;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {
    private ArticleService articleService;
    private UserService userService;
    private CommentRepository commentRepository;
    private CommentLikeRepository commentLikeRepository;
    private CommentAssembler commentAssembler;

    public CommentService(ArticleService articleService, UserService userService,
                          CommentRepository commentRepository, CommentLikeRepository commentLikeRepository, CommentAssembler commentAssembler) {
        this.articleService = articleService;
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.commentLikeRepository = commentLikeRepository;
        this.commentAssembler = commentAssembler;
    }

    public CommentResponse findById(Long id) {
        Comment comment = getComment(id);
        return commentAssembler.toResponse(comment);
    }

    public List<CommentResponse> findAllByArticleId(Long id) {
        return commentRepository.findAllByArticleIdOrderByModifiedDateDescCreatedDateDesc(id).stream()
                .map(commentAssembler::toResponse)
                .collect(Collectors.toList());
    }

    public CommentResponse save(Long articleId, CommentRequest commentRequest, UserOutline userOutline) {
        User user = userService.getUser(userOutline.getId());
        Article article = articleService.getArticle(articleId);
        Comment comment = commentRepository.save(commentAssembler.toEntity(commentRequest, article, user));
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

    private Comment getComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(NotFoundCommentException::new);
        if (comment.isNotPresent()) {
            throw new NotFoundCommentException();
        }
        return comment;
    }

    private void checkAuthor(UserOutline userOutline, Comment comment) {
        if (comment.isNotAuthor(userOutline.getId())) {
            throw new InvalidAuthorException();
        }
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
}
