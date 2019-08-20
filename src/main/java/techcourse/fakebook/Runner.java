//package techcourse.fakebook;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//import techcourse.fakebook.service.UserService;
//import techcourse.fakebook.service.dto.UserSignupRequest;
//
//@Component
//public class Runner implements ApplicationRunner {
//    private static final Logger log = LoggerFactory.getLogger(Runner.class);
//
//    private static Long newUserRequestId = 1L;
//
//    private final UserService userService;
//
//    public Runner(UserService userService) {
//        log.debug("begin");
//
//        this.userService = userService;
//    }
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        log.info("begin");
//
//        int numUsers = 10;
//
//        for (int i = 0; i < numUsers; i++) {
//            userService.save(newUserSignupRequest());
//        }
//    }
//
//    private UserSignupRequest newUserSignupRequest() {
//        UserSignupRequest userSignupRequest = new UserSignupRequest(String.format("email%d@hello.com", newUserRequestId++),
//                "성",
//                "이름",
//                "Password1!",
//                "gender",
//                "birth");
//
//        log.debug("userSignupRequest: {}", userSignupRequest);
//
//        return userSignupRequest;
//    }
//}
