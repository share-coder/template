package com.jc.template.feature.usermgmt.service;

import com.jc.template.common.dto.usermgmt.UserDto;
import com.jc.template.common.entity.User;
import com.jc.template.common.exception.InvalidInputException;
import com.jc.template.common.exception.ResourceNotFoundException;
import com.jc.template.feature.usermgmt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

import static com.jc.template.common.constant.UserApiMessage.USER_DOES_NOT_EXIST;

@Service
@Transactional
@Slf4j
public class UserDeleteService {

    private final PasswordEncoder passwordEncoder;

//    private final JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;

    public UserDeleteService(PasswordEncoder passwordEncoder,
//                                JwtTokenUtil jwtTokenUtil,
                             UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
//        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }

    public void softDeleteUser(Integer id) {
        User user = getUser(id);
        //validateStatus(status);
        user.setStatus("DELETED");
        user.setUpdatedAt(Calendar.getInstance().getTime());
        userRepository.save(user);
        log.info("User marked as deleted");
    }

    public User getUser(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> InvalidInputException.builder().code(USER_DOES_NOT_EXIST.getCode())
                        .errorMessage(USER_DOES_NOT_EXIST.getMessage())
                        .build());
    }

    public UserDto findUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> InvalidInputException.builder().code(USER_DOES_NOT_EXIST.getCode())
                        .errorMessage(USER_DOES_NOT_EXIST.getMessage())
                        .build());
        return UserDto.from(user);
    }

    public void deleteUserById(Integer userId, String loggedInUser) {
        softDeleteUser(userId);
    }

//    public Pair<UMApiMessage, BulkStatusDto> deleteUsers(String authorization, List<Integer> ids) {
//        org.springframework.data.util.Pair<String, String> usernameAndRoleFromToken = jwtTokenUtil.getUsernameAndRoleFromToken(
//                getTokenWithoutBearer(authorization));
//        List<Integer> failedIds = new ArrayList<>();
//        StringBuilder sb = new StringBuilder();
//        for (Integer id : ids) {
//            try {
//                deleteUserById(id, usernameAndRoleFromToken.getFirst());
//            } catch (Exception e) {
//                failedIds.add(id);
//            }
//        }
//        BulkStatusDto bulkStatusDto = null;
//        if (failedIds.size() > 0) {
//            bulkStatusDto = new BulkStatusDto();
//            bulkStatusDto.setIds(failedIds);
//            log.info("Buck delete failed status\n{}", sb);
//        }
//        UMApiMessage UMApiMessage;
//        if (bulkStatusDto == null) {
//            UMApiMessage = USER_DELETED;
//        } else if (bulkStatusDto.getIds().size() == ids.size()) {
//            UMApiMessage = USER_FULL_DELETE_FAILED;
//        } else {
//            UMApiMessage = USER_PARTIAL_DELETE;
//        }
//        log.info("User(s) delete completed");
//        return new Pair<>(UMApiMessage, bulkStatusDto);
//    }

    public void deleteUserByUserId(Integer userId, String authorization) {
        UserDto userToBeDeleted = findUserById(userId);
        if (userToBeDeleted != null) {
            userRepository.deleteUserByUserId(userToBeDeleted.getUserId());
        } else {
            throw ResourceNotFoundException.builder().code(USER_DOES_NOT_EXIST.getCode())
                    .errorMessage(USER_DOES_NOT_EXIST.getMessage())
                    .build();
        }
        //softDeleteUser(userToBeDeleted.getId());
    }
}
