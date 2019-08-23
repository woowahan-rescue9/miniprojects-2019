package techcourse.fakebook.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.domain.user.UserRepository;
import techcourse.fakebook.exception.NotFoundUserException;
import techcourse.fakebook.service.dto.*;
import techcourse.fakebook.service.utils.UserAssembler;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public UserResponse findById(Long userId) {
        log.debug("begin");

        return userRepository
                .findById(userId)
                .map(userAssembler::toResponse)
                .orElseThrow(NotFoundUserException::new);
    }

    public List<User> findByIdIn(List<Long> userIds) {
        log.debug("begin");

        return userRepository.findByIdIn(userIds);
    }

    public UserResponse update(Long userId, UserUpdateRequest userUpdateRequest) {
        log.debug("begin");

        User user = getUser(userId);
        user.updateModifiableFields(userUpdateRequest.getCoverUrl(), userUpdateRequest.getIntroduction());

        log.debug("user: {}", user);
        return userAssembler.toResponse(user);
    }

    public void deleteById(Long userId) {
        log.debug("begin");

        User user = getUser(userId);
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public User getUser(Long userId) {
        log.debug("begin");

        return userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
    }

    @Transactional(readOnly = true)
    public UserOutline getUserOutline(Long userId) {
        return userRepository.findById(userId)
                .map(UserAssembler::toUserOutline)
                .orElseThrow(NotFoundUserException::new);
    }

    public List<UserResponse> findUserNamesByKeyword(String keyword) {
        return userRepository.findByNameContaining(keyword).stream()
                .map(user -> userAssembler.toResponse(user))
                .collect(Collectors.toList());
    }

    public boolean hasNotUserWithEmail(String email) {
        return !userRepository.findByEmail(email).isPresent();
    }
}
