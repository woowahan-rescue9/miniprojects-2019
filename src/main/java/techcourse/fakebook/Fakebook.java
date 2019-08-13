package techcourse.fakebook;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.article.ArticleRepository;
import techcourse.fakebook.service.dto.ArticleRequest;

@EnableJpaAuditing
@SpringBootApplication
public class Fakebook {

    public static void main(String[] args) {
        SpringApplication.run(Fakebook.class, args);
    }

    @Bean
    public CommandLineRunner runner(ArticleRepository articleRepository) {
        return (args -> {
           articleRepository.save(new Article("hello"));
        });
    }
}
