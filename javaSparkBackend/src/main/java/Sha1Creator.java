import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha1Creator {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Good.class);

    public String createSha1(String password) {
        String hash = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("utf-8"));
            hash = new BigInteger(1, crypt.digest()).toString(16);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return hash;
    }
}