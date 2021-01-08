package io.geekmind.budgie;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Base64;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@ActiveProfiles("dev")
@Ignore
public class PasswordEncoderTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void encodesUsername() {
        String encodedPassword = passwordEncoder.encode("username");
        System.out.println(Base64.getEncoder().encodeToString(encodedPassword.getBytes()));
    }

    @Test
    public void encodesPassword() {
        String encodedPassword = passwordEncoder.encode("password");
        System.out.println(Base64.getEncoder().encodeToString(encodedPassword.getBytes()));
    }

}
