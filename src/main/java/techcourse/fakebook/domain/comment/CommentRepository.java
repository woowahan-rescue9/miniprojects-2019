package techcourse.fakebook.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Integer countCommentByArticleId(Long articleId);

    List<Comment> findAllByArticleIdOrderByModifiedDateDescCreatedDateDesc(Long articleId);
}
