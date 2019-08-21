package techcourse.fakebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Fakebook {
    public static void main(String[] args) {
        SpringApplication.run(Fakebook.class, args);
    }
}
