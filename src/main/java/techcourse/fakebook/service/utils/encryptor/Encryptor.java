package techcourse.fakebook.service.utils.encryptor;

public interface Encryptor {
    String encrypt(String data);

    boolean matches(String data, String encrypted);
}
