package techcourse.fakebook.service;

import org.springframework.stereotype.Service;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.article.ArticleRepository;
import techcourse.fakebook.exception.NotFoundArticleException;
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

    public ArticleResponse findById(Long id) {
        Article article = getArticle(id);
        return ArticleAssembler.toResponse(article);
    }

    public ArticleResponse save(ArticleRequest articleRequest) {
        Article article = articleRepository.save(ArticleAssembler.toEntity(articleRequest));
        return ArticleAssembler.toResponse(article);
    }

    public ArticleResponse update(Long id, ArticleRequest updatedRequest) {
        Article article = getArticle(id);
        article.update(updatedRequest.getContent());
        return ArticleAssembler.toResponse(article);
    }

    public void deleteById(Long id) {
        Article article = getArticle(id);
        article.delete();
    }

    public Article getArticle(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(NotFoundArticleException::new);
        if (article.isNotPresent()) {
            throw new NotFoundArticleException();
        }
        return article;
    }
}
