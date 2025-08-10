package com.jc.template.common.dto.usermgmt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jc.template.common.entity.User;
import com.jc.template.common.enums.RoleEnum;
import com.jc.template.common.enums.UserAccountStatusEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto implements Serializable {
    Integer id;

    String name;

    String userId;

    String password;

    String email;

    String mobileNumber;

    String address;

    String dob;

    String role;

    String status;

    String jwtToken;

    private boolean enabled;
    private String verificationCode;

    private boolean accountNonLocked;
    private int failedAttempts;
    private Date lockTime;

    Date createdAt;

    Date updatedAt;

    public static UserDto from(User user, String userId) {
        UserDto userDto = new UserDto();
        if (user.getUserId() != null && user.getUserId().equals(userId)) {
            userDto.setUserId(user.getUserId());
        }
        return userDto;
    }

    public static User to(UserDto dto) {
        User user = new User();
        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getUserId() != null) {
            user.setUserId(dto.getUserId());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null) {
            user.setPassword(dto.getPassword());
        }
        if (dto.getMobileNumber() != null) {
            user.setMobileNumber(dto.getMobileNumber());
        }
        if (dto.getAddress() != null) {
            user.setAddress(dto.getAddress());
        }
        if (dto.getDob() != null) {
            user.setDob(dto.getDob());
        }
        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }
        //user.setStatus(UserAccountStatusEnum.ACTIVE.name());
        if (dto.getStatus() != null) {
            switch (dto.getStatus()) {
                case "ACTIVE":
                    user.setStatus(UserAccountStatusEnum.ACTIVE.name());
                    break;
                case "PENDING":
                    user.setStatus(UserAccountStatusEnum.PENDING.name());
                    break;
                case "SUSPENDED":
                    user.setStatus(UserAccountStatusEnum.SUSPEND.name());
                    break;
                default:
                    user.setStatus(UserAccountStatusEnum.UNKNOWN.name());
            }
        }
        if (dto.getJwtToken() != null) {
            user.setJwtToken(dto.getJwtToken());
        }
        user.setEnabled(dto.isEnabled());
        if (dto.getVerificationCode() != null) {
            user.setVerificationCode(dto.getVerificationCode());
        }
        user.setAccountNonLocked(dto.isAccountNonLocked());
        user.setFailedAttempts(0);

        Date date = Calendar.getInstance().getTime();
        //user.setLockTime(date);
        user.setCreatedAt(date);
        user.setUpdatedAt(date);
        return user;
    }

    public static UserDto from(User user) {
        UserDto dto = new UserDto();
        if (user.getId() != null) {
            dto.setId(user.getId());
        }
        if (user.getName() != null) {
            dto.setName(user.getName());
        }
        if (user.getUserId() != null) {
            dto.setUserId(user.getUserId());
        }
        if (user.getEmail() != null) {
            dto.setEmail(user.getEmail());
        }
        if (user.getMobileNumber() != null) {
            dto.setMobileNumber(user.getMobileNumber());
        }
        if (user.getAddress() != null) {
            dto.setAddress(user.getAddress());
        }
        if (user.getDob() != null) {
            dto.setDob(user.getDob());
        }
        String role = user.getRole();
        if (role != null) {
            switch (role) {
                case "ROLE_ADMIN":
                    dto.setRole(RoleEnum.ROLE_ADMIN.getRoleId());
                    break;
                case "ROLE_USER":
                    dto.setRole(RoleEnum.ROLE_USER.getRoleId());
                    break;
            }
        }
        if (user.getStatus() != null) {
            dto.setStatus(user.getStatus());
        }
        if (user.getJwtToken() != null) {
            dto.setJwtToken(user.getJwtToken());
        }
        if (user.isEnabled()) {
            dto.setEnabled(true);
        }
        if (user.getVerificationCode() != null) {
            dto.setJwtToken(user.getVerificationCode());
        }
        if (user.isAccountNonLocked()) {
            dto.setAccountNonLocked(true);
        }
        dto.setFailedAttempts(user.getFailedAttempts());
        if (user.getCreatedAt() != null) {
            dto.setCreatedAt(user.getCreatedAt());
        }
        if (user.getUpdatedAt() != null) {
            dto.setUpdatedAt(user.getUpdatedAt());
        }
        return dto;
    }
}
