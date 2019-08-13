package techcourse.fakebook.utils.encryptor;

public interface Encryptor {
    String encrypt(String data);

    boolean isMatch(String data, String encrypted);
}
