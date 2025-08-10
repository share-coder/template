package com.jc.template.feature.usermgmt.service;

import com.jc.template.common.constant.UserApiMessage;
import com.jc.template.common.entity.User;
import com.jc.template.common.exception.InvalidInputException;
import com.jc.template.common.dto.usermgmt.UserDto;
import com.jc.template.feature.usermgmt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class UserRegisterService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserRegisterService(PasswordEncoder passwordEncoder,  UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public boolean createUser(List<UserDto> userDtos) {
        List<User> users = new ArrayList<>();
        for (UserDto userDto : userDtos) {
//            validateUser(userDto);
            encryptPassword(userDto);
            checkUserExistence(userDto);
            users.add(UserDto.to(userDto));
        }
        userRepository.saveAll(users);
        log.info("User created successfully.");
        return true;
    }

    private void validateUser(UserDto userDto) {
//        validateFirstName(userDto.getName());
//        validateEmail(userDto.getEmail());
//        validateUserId(userDto.getUserId());
//        validateMobileNumber(userDto.getMobileNumber());
//        validatePassword(userDto.getPassword());
//        validateRole(userDto.getRole());
//        validateStatus(userDto.getStatus());
//        validateAddress1(userDto.getAddress());
        log.info("User validation done successfully");
    }

    private void encryptPassword(UserDto userDTO) {
        if (userDTO.getPassword() != null) {
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
    }

    private void checkUserExistence(UserDto userDto) {
        if (userDto.getUserId() != null) {
            isUserExistByUserId(userDto.getUserId());
        }
        if (userDto.getEmail() != null) {
            isUserExistByEmail(userDto.getEmail());
        }
    }

    private void isUserExistByUserId(String userId) {
        User userExistByUserId = userRepository.findByUserId(userId);
        if (userExistByUserId != null) {
            log.error("Unable to create user as user already in system by user id: {}", userId);
            throw InvalidInputException.builder().code(UserApiMessage.USER_EXIST_BY_USER_ID.getCode())
                    .errorMessage(UserApiMessage.USER_EXIST_BY_USER_ID.getMessage()).build();
        }
    }

    private void isUserExistByEmail(String email) {
        User userExistByEmail = userRepository.findByEmail(email);
        if (userExistByEmail != null) {
            log.error("Unable to create user as user already in system by email: {}", email);
            throw InvalidInputException.builder().code(UserApiMessage.USER_EXIST_BY_EMAIL.getCode())
                    .errorMessage(UserApiMessage.USER_EXIST_BY_EMAIL.getMessage()).build();
        }
    }
}
