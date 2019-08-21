package techcourse.fakebook.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.fakebook.service.TotalService;

@Controller
public class NewsfeedController {
    private static final Logger log = LoggerFactory.getLogger(NewsfeedController.class);

    private final TotalService totalService;

    public NewsfeedController(TotalService totalService) {
        this.totalService = totalService;
    }

    @GetMapping("/newsfeed")
    public String newsfeed(Model model) {
        model.addAttribute("articles", totalService.findAll());
        return "newsfeed";
    }
}
