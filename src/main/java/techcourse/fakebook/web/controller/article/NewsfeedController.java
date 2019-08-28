package techcourse.fakebook.web.controller.article;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.fakebook.service.article.TotalService;

@Controller
public class NewsfeedController {
    private static final Logger log = LoggerFactory.getLogger(NewsfeedController.class);

    private final TotalService totalService;

    public NewsfeedController(TotalService totalService) {
        this.totalService = totalService;
    }

    @GetMapping("/newsfeed")
    public String newsfeed() {
        log.debug("begin");

        return "newsfeed";
    }
}
