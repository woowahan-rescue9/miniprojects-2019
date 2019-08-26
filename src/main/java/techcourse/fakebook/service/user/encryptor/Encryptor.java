package techcourse.fakebook.service.user.encryptor;

public interface Encryptor {
    String encrypt(String data);

    boolean matches(String data, String encrypted);
}
