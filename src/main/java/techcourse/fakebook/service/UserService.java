package techcourse.fakebook.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.fakebook.domain.User;
import techcourse.fakebook.domain.UserRepository;
import techcourse.fakebook.exception.NotFoundUserException;
import techcourse.fakebook.service.dto.UserSignupRequest;
import techcourse.fakebook.service.dto.UserResponse;
import techcourse.fakebook.service.dto.UserUpdateRequest;
import techcourse.fakebook.service.utils.UserAssembler;

@Service
@Transactional
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserAssembler userAssembler;

    public UserService(UserRepository userRepository, UserAssembler userAssembler) {
        this.userRepository = userRepository;
        this.userAssembler = userAssembler;
    }

    public UserResponse save(UserSignupRequest userSignupRequest) {
        log.debug("begin");

        User user = userAssembler.toEntity(userSignupRequest);
        User savedUser = userRepository.save(user);
        log.debug("savedUser: {}", savedUser);

        return userAssembler.toResponse(savedUser);
    }

    // tx readonly
    public UserResponse findById(Long userId) {
        log.debug("begin");

        return userRepository
                .findById(userId)
                .map(userAssembler::toResponse)
                .orElseThrow(NotFoundUserException::new);
    }

    public UserResponse update(Long userId, UserUpdateRequest userUpdateRequest) {
        log.debug("begin");

        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        user.updateModifiableFields(userUpdateRequest.getCoverUrl(), userUpdateRequest.getIntroduction());

        log.debug("user: {}", user);
        return userAssembler.toResponse(user);
    }

    public void deleteById(Long userId) {
        log.debug("begin");

        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        userRepository.delete(user);
    }
}
