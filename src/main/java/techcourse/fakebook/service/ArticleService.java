package techcourse.fakebook.service;

import org.springframework.stereotype.Service;
import techcourse.fakebook.domain.Article;
import techcourse.fakebook.repository.ArticleRepository;
import techcourse.fakebook.service.dto.ArticleRequest;
import techcourse.fakebook.service.dto.ArticleResponse;
import techcourse.fakebook.utils.ArticleAssembler;

import javax.transaction.Transactional;

@Service
@Transactional
public class ArticleService {
    private ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public ArticleResponse save(ArticleRequest articleRequest) {
        Article article = articleRepository.save(ArticleAssembler.toEntity(articleRequest));
        return ArticleAssembler.toResponse(article);
    }
}
