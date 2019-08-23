package techcourse.fakebook.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.fakebook.controller.utils.SessionUser;
import techcourse.fakebook.exception.NotFoundUserException;
import techcourse.fakebook.service.FriendshipService;
import techcourse.fakebook.service.dto.FriendshipRequest;
import techcourse.fakebook.service.dto.UserOutline;

@RestController
@RequestMapping("/api/friendships")
public class FriendshipApiController {
    private static final Logger log = LoggerFactory.getLogger(FriendshipApiController.class);

    private final FriendshipService friendshipService;

    public FriendshipApiController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody FriendshipRequest friendshipRequest, @SessionUser UserOutline userOutline) {
        log.debug("begin");

        log.debug("friendshipRequest: {}", friendshipRequest);
        log.debug("userOutline: {}", userOutline);
        try {
            friendshipService.makeThemFriends(userOutline.getId(), friendshipRequest.getFriendId());
        } catch (NotFoundUserException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam("friendId") Long friendId, @SessionUser UserOutline userOutline) {
        log.debug("begin");

        log.debug("friendId: {}", friendId);
        log.debug("userOutline: {}", userOutline);
        try {
            friendshipService.breakFriendship(userOutline.getId(), friendId);
        } catch (NotFoundUserException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
