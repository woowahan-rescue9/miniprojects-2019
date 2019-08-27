package techcourse.fakebook.web.controller.article;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import techcourse.fakebook.service.article.TotalService;
import techcourse.fakebook.service.article.dto.TotalArticleResponse;
import techcourse.fakebook.service.user.dto.UserOutline;
import techcourse.fakebook.web.argumentresolver.SessionUser;

import java.util.List;

@Controller
public class NewsfeedController {
    private static final Logger log = LoggerFactory.getLogger(NewsfeedController.class);

    private final TotalService totalService;

    public NewsfeedController(TotalService totalService) {
        this.totalService = totalService;
    }

    @GetMapping("/newsfeed")
    public String newsfeed(Model model, @SessionUser UserOutline userOutline) {
        log.debug("begin");

        model.addAttribute("articles", totalService.findArticlesByUserIncludingFriendsArticles(userOutline.getId()));
        return "newsfeed";
    }

    @GetMapping("/api/newfeed")
    @ResponseBody
    public ResponseEntity<List<TotalArticleResponse>> newsfeed() {
        log.debug("begin");

        return ResponseEntity.ok(totalService.findAll());
    }
}
