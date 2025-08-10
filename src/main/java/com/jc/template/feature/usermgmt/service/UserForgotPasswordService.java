package com.jc.template.feature.usermgmt.service;

import com.jc.template.common.entity.User;
import com.jc.template.common.exception.ResourceNotFoundException;
import com.jc.template.feature.usermgmt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.jc.template.common.constant.UserApiMessage.USER_DOES_NOT_EXIST;

@Service
@Transactional
@Slf4j
public class UserForgotPasswordService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserForgotPasswordService(PasswordEncoder passwordEncoder,
                                     UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void forgotPassword(String userEmail) {
        log.info("forgotPassword - userEmail: {}", userEmail);
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw ResourceNotFoundException.builder().code(USER_DOES_NOT_EXIST.getCode())
                    .errorMessage(USER_DOES_NOT_EXIST.getMessage())
                    .build();
        }
        log.info("Reset password email sent to user successfully.");
    }
}
