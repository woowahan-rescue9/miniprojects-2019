package techcourse.fakebook.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    boolean existsByUserIdAndCommentId(Long userId, Long commentId);

    CommentLike findByUserIdAndCommentId(Long userId, Long commentId);
}
