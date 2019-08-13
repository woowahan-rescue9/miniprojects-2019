package techcourse.fakebook.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import techcourse.fakebook.domain.User;
import techcourse.fakebook.domain.UserRepository;
import techcourse.fakebook.exception.NotFoundUserException;
import techcourse.fakebook.service.dto.UserRequest;
import techcourse.fakebook.service.dto.UserResponse;
import techcourse.fakebook.utils.UserAssembler;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserAssembler userAssembler;

    public UserService(UserRepository userRepository, UserAssembler userAssembler) {
        this.userRepository = userRepository;
        this.userAssembler = userAssembler;
    }

    public UserResponse save(UserRequest userRequest) {
        log.debug("begin");

        User user = userAssembler.toEntity(userRequest);


        User savedUser = userRepository.save(user);

        log.debug("savedUser: {}", savedUser);

        return userAssembler.toResponse(savedUser);
    }

    public UserResponse findById(Long userId) {
        log.debug("begin");

        return userRepository
                .findById(userId)
                .map(userAssembler::toResponse)
                .orElseThrow(NotFoundUserException::new);
    }
}
