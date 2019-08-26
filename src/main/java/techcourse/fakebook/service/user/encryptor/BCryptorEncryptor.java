package techcourse.fakebook.service.user.encryptor;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BCryptorEncryptor implements Encryptor {
    @Override
    public String encrypt(String data) {
        return BCrypt.hashpw(data, BCrypt.gensalt());
    }

    @Override
    public boolean matches(String data, String encrypted) {
        return BCrypt.checkpw(data, encrypted);
    }
}
