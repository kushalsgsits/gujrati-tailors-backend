package com.harvi.tailor.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String ADMIN_USER = "Admin";
    private static final String ADMIN_USER_PASSWORD = "$2a$10$IewqSnmKeaw2rLE6hdXAEeDGi6DM.z6uF6reoCvAKoVqg/OOL1D8u";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (ADMIN_USER.equals(username)) {
            return new User(ADMIN_USER, ADMIN_USER_PASSWORD, new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
