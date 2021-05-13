package com.ecommerceshop.admin.config.security;

import com.ecommerceshop.admin.user.UserRepository;
import com.ecommerceshop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ShopUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userByEmail = userRepository.findUserByEmail(email);
        if (userByEmail != null) {
            return new ShopUserDetails(userByEmail);
        }
        throw new UsernameNotFoundException("Could not find user with email: " + email);
    }
}
