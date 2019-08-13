package techcourse.fakebook.service;

import org.springframework.stereotype.Service;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.comment.Comment;
import techcourse.fakebook.domain.comment.CommentRepository;
import techcourse.fakebook.exception.NotFoundCommentException;
import techcourse.fakebook.service.dto.CommentRequest;
import techcourse.fakebook.service.dto.CommentResponse;
import techcourse.fakebook.utils.CommentAssembler;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {
    private ArticleService articleService;
    private CommentRepository commentRepository;

    public CommentService(ArticleService articleService, CommentRepository commentRepository) {
        this.articleService = articleService;
        this.commentRepository = commentRepository;
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

    public CommentResponse save(Long articleId, CommentRequest commentRequest) {
        Article article = articleService.getArticle(articleId);
        Comment comment = commentRepository.save(CommentAssembler.toEntity(commentRequest, article));
        return CommentAssembler.toResponse(comment);
    }

    public CommentResponse update(Long id, CommentRequest updatedRequest) {
        Comment comment = getComment(id);
        comment.update(updatedRequest.getContent());
        return CommentAssembler.toResponse(comment);
    }

    public void deleteById(Long id) {
        Comment comment = getComment(id);
        comment.delete();
    }

    private Comment getComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(NotFoundCommentException::new);
        if (comment.isNotPresent()) {
            throw new NotFoundCommentException();
        }
        return comment;
    }
}
