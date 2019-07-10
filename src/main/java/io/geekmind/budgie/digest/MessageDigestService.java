package io.geekmind.budgie.digest;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class MessageDigestService {

    private MessageDigest md5Digester = null;

    @PostConstruct
    public void initialize() {
        try {
            this.md5Digester = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("MD5 digest algorithm is not available.");
        }
    }

    public String md5Digest(byte[] data) {
        return DatatypeConverter.printHexBinary(
            this.md5Digester.digest(data)
        );
    }

    public String base64Encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

}
