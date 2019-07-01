package io.geekmind.budgie.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

@Component
public class DefaultAuthenticationProvider implements AuthenticationProvider {

    private final String encodedUserName;
    private final String encodedPassword;
    private final PasswordEncoder passwordEncoder;

    public DefaultAuthenticationProvider(@Value("${budgie.auth.username}") String encodedUserName,
                                         @Value("${budgie.auth.password}") String encodedPassword,
                                         PasswordEncoder passwordEncoder) {
        this.encodedUserName = new String(
            Base64.getDecoder().decode(encodedUserName.getBytes()),
            StandardCharsets.UTF_8
        );
        this.encodedPassword = new String(
            Base64.getDecoder().decode(encodedPassword.getBytes()),
            StandardCharsets.UTF_8
        );
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String plainTextUserName = authentication.getName();
        String plainTextPassword = authentication.getCredentials().toString();

        if (this.passwordEncoder.matches(plainTextPassword, this.encodedPassword) &&
            this.passwordEncoder.matches(plainTextUserName, this.encodedUserName)) {
            return new UsernamePasswordAuthenticationToken(
                plainTextUserName,
                plainTextPassword,
                Collections.singletonList(new SimpleGrantedAuthority("USER"))
            );
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
