package techcourse.fakebook.service;

import org.springframework.stereotype.Service;
import techcourse.fakebook.service.dto.TotalArticleResponse;
import techcourse.fakebook.service.utils.ArticleAssembler;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TotalService {
    private ArticleService articleService;
    private CommentService commentService;
    private ArticleAssembler articleAssembler;

    public TotalService(ArticleService articleService, CommentService commentService, ArticleAssembler articleAssembler) {
        this.articleService = articleService;
        this.commentService = commentService;
        this.articleAssembler = articleAssembler;
    }

    public List<TotalArticleResponse> findAll() {
        return articleService.findAll().stream()
                .map(articleResponse ->
                        articleAssembler.toTotalArticleResponse(articleResponse,
                                commentService.getCommentsCountOf(articleResponse.getId()),
                                articleService.getLikeCountOf(articleResponse.getId())))
                .collect(Collectors.toList());
    }
}
