package techcourse.fakebook.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.article.ArticleMultipart;
import techcourse.fakebook.domain.article.ArticleMultipartRepository;
import techcourse.fakebook.domain.article.ArticleRepository;
import techcourse.fakebook.domain.like.ArticleLike;
import techcourse.fakebook.domain.like.ArticleLikeRepository;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.exception.FileSaveException;
import techcourse.fakebook.exception.InvalidAuthorException;
import techcourse.fakebook.exception.NotFoundArticleException;
import techcourse.fakebook.service.dto.ArticleRequest;
import techcourse.fakebook.service.dto.ArticleResponse;
import techcourse.fakebook.service.dto.UserOutline;
import techcourse.fakebook.service.utils.ArticleAssembler;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArticleService {
    private static final String DEFAULT_PATH = "src/main/resources/static";
    private static final String ARTICLE_STATIC_FILE_PATH = "file/article/";

    private final ArticleRepository articleRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final UserService userService;
    private final ArticleAssembler articleAssembler;
    private final ArticleMultipartRepository articleMultipartRepository;

    public ArticleService(ArticleRepository articleRepository, ArticleLikeRepository articleLikeRepository, UserService userService, ArticleAssembler articleAssembler, ArticleMultipartRepository articleMultipartRepository) {
        this.articleRepository = articleRepository;
        this.articleLikeRepository = articleLikeRepository;
        this.userService = userService;
        this.articleAssembler = articleAssembler;
        this.articleMultipartRepository = articleMultipartRepository;
    }

    public ArticleResponse findById(Long id) {
        Article article = getArticle(id);
        return articleAssembler.toResponse(article);
    }

    public List<ArticleResponse> findAll() {
        return articleRepository.findAllByOrderByModifiedDateDescCreatedDateDesc().stream()
                .map(articleAssembler::toResponse)
                .collect(Collectors.toList());
    }

    public ArticleResponse save(ArticleRequest articleRequest, UserOutline userOutline) {
        User user = userService.getUser(userOutline.getId());
        Article article = articleRepository.save(articleAssembler.toEntity(articleRequest, user));
        List<ArticleMultipart> files = Optional.ofNullable(articleRequest.getFiles()).orElse(new ArrayList<>()).stream()
                .map(file -> saveMultipart(file, article))
                .collect(Collectors.toList());
        return articleAssembler.toResponse(article, files);
    }

    public ArticleResponse update(Long id, ArticleRequest updatedRequest, UserOutline userOutline) {
        Article article = getArticle(id);
        checkAuthor(userOutline, article);
        article.update(updatedRequest.getContent());
        return articleAssembler.toResponse(article);
    }

    public void deleteById(Long id, UserOutline userOutline) {
        Article article = getArticle(id);
        checkAuthor(userOutline, article);
        article.delete();
    }

    public boolean like(Long id, UserOutline userOutline) {
        Optional<ArticleLike> articleLike = Optional.ofNullable(articleLikeRepository.findByUserIdAndArticleId(userOutline.getId(), id));
        if (articleLike.isPresent()) {
            articleLikeRepository.delete(articleLike.get());
            return false;
        }
        articleLikeRepository.save(new ArticleLike(userService.getUser(userOutline.getId()), getArticle(id)));
        return true;
    }

    public boolean isLiked(Long id, UserOutline userOutline) {
        return articleLikeRepository.existsByUserIdAndArticleId(userOutline.getId(), id);
    }

    public Integer getLikeCountOf(Long articleId) {
        return articleLikeRepository.countArticleLikeByArticleId(articleId);
    }

    Article getArticle(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(NotFoundArticleException::new);
        if (article.isDeleted()) {
            throw new NotFoundArticleException();
        }
        return article;
    }

    private void checkAuthor(UserOutline userOutline, Article article) {
        if (article.isNotAuthor(userOutline.getId())) {
            throw new InvalidAuthorException();
        }
    }

    private ArticleMultipart saveMultipart(MultipartFile file, Article article) {
        try {
            String hashingName = getHashedName(file.getOriginalFilename());

            Path filePath = writeFile(file, hashingName);

            ArticleMultipart articleMultipart = new ArticleMultipart(file.getOriginalFilename(), filePath.toString(), article);

            return articleMultipartRepository.save(articleMultipart);
        } catch (IOException e) {
            throw new FileSaveException(e.getMessage());
        }
    }

    private Path writeFile(MultipartFile file, String hashingName) throws IOException {
        byte[] bytes = file.getBytes();
        Path staticFilePath = Paths.get(ARTICLE_STATIC_FILE_PATH + hashingName);
        return Files.write(staticFilePath, bytes);
    }

    private String getHashedName(String originalFileName) {
        String hashCodeOfFile = UUID.randomUUID().toString();

        List<String> splits = Arrays.asList(originalFileName.split("\\."));
        if (splits.size() < 1) {
            throw new FileSaveException("파일형식이 올바르지 않습니다.");
        }
        String extension = splits.get(splits.size() - 1);

        return hashCodeOfFile + "." + extension;
    }
}
