package techcourse.fakebook.service.comment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.fakebook.domain.comment.Comment;
import techcourse.fakebook.domain.like.CommentLike;
import techcourse.fakebook.domain.like.CommentLikeRepository;
import techcourse.fakebook.domain.user.User;

@Service
@Transactional
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;

    public CommentLikeService(CommentLikeRepository commentLikeRepository) {
        this.commentLikeRepository = commentLikeRepository;
    }

    public CommentLike save(User user, Comment comment) {
        return commentLikeRepository.save(new CommentLike(user, comment));
    }

    public boolean isLiked(Long userId, Long commentId) {
        return commentLikeRepository.existsByUserIdAndCommentId(userId, commentId);
    }

    public void cancelLike(Long userId, Long commentId) {
        commentLikeRepository.deleteByUserIdAndCommentId(userId, commentId);
    }

    public Integer countByCommentId(Long commentId) {
        return commentLikeRepository.countCommentLikeByCommentId(commentId);
    }
}
