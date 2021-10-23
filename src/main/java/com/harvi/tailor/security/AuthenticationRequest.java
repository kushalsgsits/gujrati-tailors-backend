package com.harvi.tailor.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticationRequest {

    private String username;
    private String password;

    public static void main(String[] args) {
        System.out.println("encryptedPassword=" + new BCryptPasswordEncoder().encode("123"));
    }
}
