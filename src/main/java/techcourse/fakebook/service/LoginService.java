package techcourse.fakebook.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.domain.user.UserRepository;
import techcourse.fakebook.exception.NotFoundUserException;
import techcourse.fakebook.exception.NotMatchPasswordException;
import techcourse.fakebook.service.dto.LoginRequest;
import techcourse.fakebook.service.dto.UserOutline;
import techcourse.fakebook.service.utils.encryptor.Encryptor;

@Service
@Transactional
public class LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    private final UserRepository userRepository;
    private final Encryptor encryptor;

    public LoginService(UserRepository userRepository, Encryptor encryptor) {
        this.userRepository = userRepository;
        this.encryptor = encryptor;
    }

    @Transactional(readOnly = true)
    public UserOutline login(LoginRequest loginRequest) {
        log.debug("begin");

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(NotFoundUserException::new);

        if (!encryptor.matches(loginRequest.getPassword(), user.getEncryptedPassword())) {
            throw new NotMatchPasswordException();
        }

        return new UserOutline(user.getId(), user.getName(), user.getCoverUrl());
    }
}
