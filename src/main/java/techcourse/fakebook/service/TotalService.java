package techcourse.fakebook.service;

import org.springframework.stereotype.Service;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.service.dto.TotalArticleResponse;
import techcourse.fakebook.service.utils.ArticleAssembler;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TotalService {
    private final ArticleService articleService;
    private final CommentService commentService;
    private final UserService userService;
    private final ArticleAssembler articleAssembler;

    public TotalService(ArticleService articleService, CommentService commentService, UserService userService, ArticleAssembler articleAssembler) {
        this.articleService = articleService;
        this.commentService = commentService;
        this.userService = userService;
        this.articleAssembler = articleAssembler;
    }

    public List<TotalArticleResponse> findAll() {
        return articleService.findAll().stream()
                .map(articleResponse ->
                        articleAssembler.toTotalArticleResponse(articleResponse,
                                commentService.getCommentsCountOf(articleResponse.getId()),
                                articleService.getLikeCountOf(articleResponse.getId()),
                                commentService.findAllByArticleId(articleResponse.getId())))
                .collect(Collectors.toList());
    }

    public List<TotalArticleResponse> findArticlesByUser(Long userId) {
        User user = userService.getUser(userId);
        return articleService.findByUser(user).stream()
                .map(articleResponse ->
                        articleAssembler.toTotalArticleResponse(articleResponse,
                                commentService.getCommentsCountOf(articleResponse.getId()),
                                articleService.getLikeCountOf(articleResponse.getId()),
                                commentService.findAllByArticleId(articleResponse.getId())))
                .collect(Collectors.toList());
    }
}
