package com.jc.template.feature.usermgmt.service;

import com.jc.template.common.constant.UserApiMessage;
import com.jc.template.common.dto.usermgmt.PasswordDto;
import com.jc.template.common.entity.User;
import com.jc.template.common.exception.InvalidInputException;
import com.jc.template.feature.usermgmt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.jc.template.common.constant.UserApiMessage.INVALID_CURRENT_PASSWORD;
import static com.jc.template.common.constant.UserApiMessage.INVALID_NEW_PASSWORD;
import static com.jc.template.feature.usermgmt.util.UMInputValidator.validatePassword;

@Service
@Transactional
@Slf4j
public class UserChangePasswordService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserChangePasswordService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void changePassword(Integer userId, PasswordDto passwordDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> InvalidInputException.builder().code(UserApiMessage.USER_DOES_NOT_EXIST.getCode())
                      .errorMessage(UserApiMessage.USER_DOES_NOT_EXIST.getMessage())
                        .build());
        boolean isCurrentPasswordValid = passwordEncoder.matches(passwordDto.getCurrentPassword(), user.getPassword());
        if (!isCurrentPasswordValid) {
            throw InvalidInputException.builder().code(INVALID_CURRENT_PASSWORD.getCode())
                   .errorMessage(INVALID_CURRENT_PASSWORD.getMessage())
                    .build();
        }
        String newPassword = passwordDto.getNewPassword();
        if (newPassword != null) {
            validatePassword(newPassword);
            boolean isNewPasswordMatchWithCurrentPassword = passwordEncoder.matches(newPassword, user.getPassword());
            if (isNewPasswordMatchWithCurrentPassword) {
                throw InvalidInputException.builder().code(INVALID_NEW_PASSWORD.getCode())
                        .errorMessage(INVALID_NEW_PASSWORD.getMessage())
                        .build();
            }
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        userRepository.save(user);
    }

    public void resetPassword( Integer userId, PasswordDto passwordDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> InvalidInputException.builder().code(UserApiMessage.USER_DOES_NOT_EXIST.getCode())
                       .errorMessage(UserApiMessage.USER_DOES_NOT_EXIST.getMessage())
                        .build());
        String newPassword = passwordDto.getNewPassword();
        if (newPassword != null) {
            validatePassword(newPassword);
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        userRepository.save(user);
    }
}
