package com.jc.template.common.config;

import com.jc.template.common.entity.User;
import com.jc.template.feature.usermgmt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service object for spring security
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User byEmail = userRepository.findByEmail(email);
        if(byEmail!=null) {
            return new CustomUser(byEmail);
        }
        throw new UsernameNotFoundException("User not available");
    }
}
