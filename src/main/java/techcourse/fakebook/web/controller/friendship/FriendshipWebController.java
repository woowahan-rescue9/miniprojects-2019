package techcourse.fakebook.web.controller.friendship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import techcourse.fakebook.service.friendship.FriendRecommendationService;
import techcourse.fakebook.service.friendship.dto.FriendRecommendation;
import techcourse.fakebook.service.user.dto.UserOutline;
import techcourse.fakebook.web.argumentresolver.SessionUser;

import java.util.List;

@Controller
@RequestMapping("/friendships")
public class FriendshipWebController {
    private static final Logger log = LoggerFactory.getLogger(FriendshipWebController.class);

    private final FriendRecommendationService friendRecommendationService;

    public FriendshipWebController(FriendRecommendationService friendRecommendationService) {
        this.friendRecommendationService = friendRecommendationService;
    }

    @GetMapping("/candidates")
    public String show(@SessionUser UserOutline loggedInUserOutline, Model model) {
        log.debug("begin");

        List<FriendRecommendation> friendRecommendations = friendRecommendationService.recommendate(loggedInUserOutline.getId());

        model.addAttribute("friendCandidates", friendRecommendations);

        return "candidates";
    }
}
