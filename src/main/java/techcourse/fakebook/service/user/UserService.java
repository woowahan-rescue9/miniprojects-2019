package techcourse.fakebook.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.domain.user.UserRepository;
import techcourse.fakebook.exception.NotFoundUserException;
import techcourse.fakebook.service.article.AttachmentService;
import techcourse.fakebook.service.user.assembler.UserAssembler;
import techcourse.fakebook.service.user.dto.UserOutline;
import techcourse.fakebook.service.user.dto.UserResponse;
import techcourse.fakebook.service.user.dto.UserSignupRequest;
import techcourse.fakebook.service.user.dto.UserUpdateRequest;
import techcourse.fakebook.service.user.encryptor.Encryptor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserAssembler userAssembler;
    private final AttachmentService attachmentService;
    private final Encryptor encryptor;

    public UserService(UserRepository userRepository, UserAssembler userAssembler, AttachmentService attachmentService, Encryptor encryptor) {
        this.userRepository = userRepository;
        this.userAssembler = userAssembler;
        this.attachmentService = attachmentService;
        this.encryptor = encryptor;
    }

    public UserResponse save(UserSignupRequest userSignupRequest) {
        log.debug("begin");

        User user = userAssembler.toEntity(userSignupRequest, attachmentService.getDefaultProfileImage());
        User savedUser = userRepository.save(user);
        log.debug("savedUser: {}", savedUser);

        return userAssembler.toResponse(user);
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

    public List<UserOutline> findFriends(List<Long> userIds) {
        return findByIdIn(userIds).stream()
                .map(userAssembler::toUserOutline)
                .collect(Collectors.toList());
    }

    public UserResponse update(Long userId, UserUpdateRequest userUpdateRequest) {
        log.debug("begin");
        String password = userUpdateRequest.getPassword();
        User user = getUser(userId);

        user.updateModifiableFields(userUpdateRequest.getName(), encryptor.encrypt(password),
                userUpdateRequest.getIntroduction(), attachmentService.getProfileImage(userUpdateRequest.getProfileImage()));

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
                .map(userAssembler::toUserOutline)
                .orElseThrow(NotFoundUserException::new);
    }

    public List<UserResponse> findUserNamesByKeyword(String keyword) {
        return userRepository.findByNameContaining(keyword).stream()
                .map(userAssembler::toResponse)
                .collect(Collectors.toList());
    }

    public boolean hasNotUserWithEmail(String email) {
        return !userRepository.findByEmail(email).isPresent();
    }
}
