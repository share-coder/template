package com.jc.template.feature.usermgmt.service;

import com.jc.template.common.constant.Constants;
import com.jc.template.common.constant.UserApiMessage;
import com.jc.template.common.dto.usermgmt.BulkDto;
import com.jc.template.common.dto.Pair;
import com.jc.template.common.dto.usermgmt.UserDto;
import com.jc.template.common.entity.User;
import com.jc.template.common.enums.UserAccountStatusEnum;
import com.jc.template.common.exception.InvalidInputException;
import com.jc.template.feature.usermgmt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static com.jc.template.common.constant.UserApiMessage.*;
import static com.jc.template.feature.usermgmt.util.UMInputValidator.*;

@Service
@Transactional
@Slf4j
public class UserUpdateService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserUpdateService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }


    public Pair<UserApiMessage, BulkDto> updateUsers(BulkDto bulkDto) {
        UserApiMessage UserApiMessage;
        List<Integer> failedIds = new ArrayList<>();
        StringBuilder sb = new StringBuilder(100);
        for (int id : bulkDto.getIds()) {
            User user = getUserWithoutException(id);
            if (user == null) {
                failedIds.add(id);
                sb.append("Bulk update - User id: ").append(id).append(" does not exist in database").append("\n");
            } else {
                String requestStatus = bulkDto.getStatus();
                String userStatus = user.getStatus();
                if ((UserAccountStatusEnum.ACTIVE.name().equals(requestStatus)) || (UserAccountStatusEnum.REJECT.name()
                        .equals(requestStatus))) {
                    if (UserAccountStatusEnum.PENDING.name().equals(userStatus) || UserAccountStatusEnum.SUSPEND.name()
                            .equals(userStatus)) {
                        updateUser(id, bulkDto.getStatus());
                        //Send Email to user on activate
                        User userFromDB = getUser(id);
                    } else if (UserAccountStatusEnum.REJECT.name().equals(userStatus)) {
                        sb.append("Bulk update - User id: ").append(id).append(" cannot be updated. User is rejected by Admin")
                                .append("\n");
                        failedIds.add(user.getId());
                    }
                } else if ((UserAccountStatusEnum.SUSPEND.name().equals(requestStatus))) {
                    if (UserAccountStatusEnum.ACTIVE.name().equals(userStatus)) {
                        updateUser(id, bulkDto.getStatus());
                    } else {
                        sb.append("User id: ").append(id).append(" is not ACTIVE. Failed to Suspend").append("\n");
                    }
                    failedIds.add(user.getId());
                } else {
                    sb.append("Bulk update - User id: ").append(id).append(" cannot be updated. Request status is not valid").append("\n");
                    failedIds.add(user.getId());
                }
            }
        }
        int failedIdSize = failedIds.size();
        if (failedIdSize > 0) {
            int requestIdsCount = bulkDto.getIds().size();
            bulkDto.getIds().clear();
            bulkDto.setIds(failedIds);
            log.info("Buck update failed status\n{}", sb);
            UserApiMessage = failedIdSize == requestIdsCount ? USER_FULL_UPDATE_FAILED : USER_PARTIAL_UPDATED;
            bulkDto.setStatus(null);
        } else {
            bulkDto = null;
            UserApiMessage = USER_UPDATED;
        }
        return new Pair<>(UserApiMessage, bulkDto);
    }

    public void updateUser(Integer id, String status) {
        User user = getUser(id);
        validateStatus(status);
        user.setStatus(status);
        user.setUpdatedAt(Calendar.getInstance().getTime());
        if (status.equalsIgnoreCase("ACTIVE")) {
            if (Integer.valueOf(user.getFailedAttempts()) > Constants.LOGIN_ATTEMPTS) {
                user.setFailedAttempts(0);
            }
        }
        userRepository.save(user);
    }


    public void softDeleteUser(Integer id) {
        User user = getUser(id);
        //validateStatus(status);
        user.setStatus("DELETED");
        user.setUpdatedAt(Calendar.getInstance().getTime());
        userRepository.save(user);
        log.info("User marked as deleted");
    }

    public UserDto updateUser(String authorization, UserDto userDto) {
        log.info("Updating user: {}", userDto.getUserId());
        User user = getUser(userDto.getId());
        ;
        if (StringUtils.isNotEmpty(userDto.getName())) {
            validateFirstName(userDto.getName());
            user.setName(userDto.getName());
        }
        if (StringUtils.isNotEmpty(userDto.getEmail())) {
            validateEmail(userDto.getEmail());
            //isUserExistByEmail(userDto.getEmail());
            user.setEmail(userDto.getEmail());
        }
        if (StringUtils.isNotEmpty(userDto.getPassword())) {
            validatePassword(userDto.getPassword());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        if (StringUtils.isNotEmpty(userDto.getUserId())){
            validateUserId(userDto.getUserId());
            user.setUserId(userDto.getUserId());
        }
        if (StringUtils.isNotEmpty(userDto.getRole())) {
            validateRole(userDto.getRole());
            user.setRole(userDto.getRole());
        }
        if (StringUtils.isNotEmpty(userDto.getStatus())) {
            validateStatus(userDto.getStatus());
            user.setStatus(userDto.getStatus());
        }
        if (StringUtils.isNotEmpty(userDto.getAddress())) {
            user.setAddress(userDto.getAddress());
        }
        user.setFailedAttempts(userDto.getFailedAttempts());
//        String userId = null;
//        if (authorization != null) {
//            org.springframework.data.util.Pair<String, String> usernameAndRoleFromToken =
//                    jwtTokenUtil.getUsernameAndRoleFromToken(getTokenWithoutBearer(authorization));
//            userId = usernameAndRoleFromToken.getFirst();
//        }
        user.setUpdatedAt(Calendar.getInstance().getTime());
        user = userRepository.save(user);
        log.info("User updated successfully");
        log.info("updateUser - END");
        return UserDto.from(user);
    }

    public User getUser(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> InvalidInputException.builder().code(UserApiMessage.USER_DOES_NOT_EXIST.getCode())
                        .errorMessage(UserApiMessage.USER_DOES_NOT_EXIST.getMessage())
                        .build());
    }

    public User getUserWithoutException(Integer id) {
        Optional<User> byId = userRepository.findById(id);
        return byId.orElse(null);
    }

    private void validateUser(UserDto userDto) {
        validateFirstName(userDto.getName());
        validateLastName(userDto.getName());
        validateEmail(userDto.getEmail());
        validateUserId(userDto.getUserId());
        validateMobileNumber(userDto.getMobileNumber());
        validatePassword(userDto.getPassword());
        validateRole(userDto.getRole());
        validateStatus(userDto.getStatus());
        validateAddress1(userDto.getAddress());
        log.info("User validation done successfully");
    }
    public void unlock(Integer id) {
        User user = getUser(id);
        user.setAccountNonLocked(true);
        user.setEnabled(true);
        user.setStatus(UserAccountStatusEnum.ACTIVE.name());

        user.setUpdatedAt(Calendar.getInstance().getTime());
        userRepository.save(user);
    }

    public void lock(Integer id) {
        User user = getUser(id);
        user.setAccountNonLocked(false);
        user.setEnabled(false);
        user.setStatus(UserAccountStatusEnum.SUSPEND.name());

        user.setUpdatedAt(Calendar.getInstance().getTime());
        userRepository.save(user);
    }
}
