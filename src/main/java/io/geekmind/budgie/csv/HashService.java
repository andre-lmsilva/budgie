package io.geekmind.budgie.csv;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class HashService {

    private MessageDigest md5Digest;

    @PostConstruct
    public void initialize() throws NoSuchAlgorithmException {
        this.md5Digest = MessageDigest.getInstance("md5");
    }

    public String calculateMD5(String value) {
        return Base64.getEncoder().encodeToString(
            this.md5Digest.digest(value.getBytes(StandardCharsets.UTF_8))
        );
    }

}
