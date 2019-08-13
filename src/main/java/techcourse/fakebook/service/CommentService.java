package techcourse.fakebook.service;

import org.springframework.stereotype.Service;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.comment.Comment;
import techcourse.fakebook.domain.comment.CommentRepository;
import techcourse.fakebook.service.dto.CommentRequest;
import techcourse.fakebook.service.dto.CommentResponse;
import techcourse.fakebook.utils.CommentAssembler;

import javax.transaction.Transactional;

@Service
@Transactional
public class CommentService {
    private ArticleService articleService;
    private CommentRepository commentRepository;

    public CommentService(ArticleService articleService, CommentRepository commentRepository) {
        this.articleService = articleService;
        this.commentRepository = commentRepository;
    }

    public CommentResponse save(Long articleId, CommentRequest commentRequest) {
        Article article = articleService.getArticle(articleId);
        Comment comment = commentRepository.save(CommentAssembler.toEntity(commentRequest, article));
        return CommentAssembler.toResponse(comment);
    }
}
