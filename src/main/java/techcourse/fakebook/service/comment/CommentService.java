package techcourse.fakebook.service.comment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.comment.Comment;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.service.article.ArticleService;
import techcourse.fakebook.service.comment.dto.CommentRequest;
import techcourse.fakebook.service.comment.dto.CommentResponse;
import techcourse.fakebook.service.user.UserService;
import techcourse.fakebook.service.user.dto.UserOutline;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    private final CommentInnerService commentInnerService;
    private final CommentLikeService commentLikeService;
    private final ArticleService articleService;
    private final UserService userService;

    public CommentService(CommentInnerService commentInnerService, CommentLikeService commentLikeService, ArticleService articleService, UserService userService) {
        this.commentInnerService = commentInnerService;
        this.commentLikeService = commentLikeService;
        this.articleService = articleService;
        this.userService = userService;
    }

    public CommentResponse findById(Long id) {
        log.debug("CommentService.findById : {}", id);
        return commentInnerService.findById(id);
    }

    public List<CommentResponse> findAllByArticleId(Long articleId) {
        log.debug("CommentService.findAllByArticleId : {}", articleId);
        return commentInnerService.findAllByArticleId(articleId);
    }

    public CommentResponse save(Long articleId, CommentRequest commentRequest, UserOutline userOutline) {
        log.debug("CommentService.save : (articleId : {}, comment : {}, user : {})", articleId, commentRequest, userOutline);
        User user = userService.getUser(userOutline.getId());
        Article article = articleService.getArticle(articleId);
        return commentInnerService.save(article, user, commentRequest);
    }

    public CommentResponse update(Long id, CommentRequest updatedRequest, UserOutline userOutline) {
        log.debug("CommentService.save : (commentId : {}, comment : {}, user : {})", id, updatedRequest, userOutline);
        return commentInnerService.update(id, updatedRequest, userOutline);
    }

    public void deleteById(Long id, UserOutline userOutline) {
        log.debug("CommentService.deleteById : (commentId : {}, user : {})", id, userOutline);
        commentInnerService.deleteById(id, userOutline);
    }

    public boolean like(Long commentId, UserOutline userOutline) {
        if (isLiked(commentId, userOutline)) {
            commentLikeService.cancelLike(userOutline.getId(), commentId);
            return false;
        }

        commentLikeService.save(userService.getUser(userOutline.getId()), getComment(commentId));
        return true;
    }

    public boolean isLiked(Long commentId, UserOutline userOutline) {
        return commentLikeService.isLiked(userOutline.getId(), commentId);
    }

    public Integer getLikeCountOf(Long commentId) {
        return commentLikeService.countByCommentId(commentId);
    }

    public Integer getCommentsCountOf(Long articleId) {
        return commentInnerService.getCommentsCountOf(articleId);
    }

    private Comment getComment(Long id) {
        return commentInnerService.getComment(id);
    }
}
