package com.jc.template.feature.usermgmt.util;

import com.jc.template.common.enums.UserAccountStatusEnum;
import com.jc.template.common.exception.InvalidInputException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import static com.jc.template.common.constant.Constants.*;
import static com.jc.template.common.constant.UserApiMessage.*;


@Slf4j
public final class UMInputValidator {

    public static void validateFirstName(String name) {
        if (StringUtils.isBlank(name)) {
            throw InvalidInputException.builder().code(FIRST_NAME_MANDATORY.getCode())
                    .errorMessage(FIRST_NAME_MANDATORY.getMessage()).build();
        } else if (StringUtils.length(name) > 50) {
            log.error("First name is not valid. It should not be > 50 chars");
            throw InvalidInputException.builder().code(INVALID_NAME_LENGTH.getCode())
                    .errorMessage(INVALID_NAME_LENGTH.getMessage()).build();
        } else if (!name.matches(ALPHA_NUMERIC_WITH_SPACE_UNDERSCORE_REGEX)) {
            log.error("Name is not valid. It should be alpha numeric with only underscore character allowed.");
            throw InvalidInputException.builder().code(INVALID_NAME.getCode())
                    .errorMessage(INVALID_NAME.getMessage()).build();
        }
    }

    public static void validateLastName(String name) {
        if (StringUtils.isBlank(name)) {
            throw InvalidInputException.builder().code(LAST_NAME_MANDATORY.getCode())
                    .errorMessage(LAST_NAME_MANDATORY.getMessage()).build();
        }
        if (StringUtils.length(name) > 50) {
            log.error("Last name is not valid. It should not be > 50 chars");
            throw InvalidInputException.builder().code(INVALID_NAME_LENGTH.getCode())
                    .errorMessage(INVALID_NAME_LENGTH.getMessage()).build();
        }
        if (!name.matches(ALPHA_NUMERIC_WITH_SPACE_UNDERSCORE_REGEX)) {
            log.error("Name is not valid. It should be alpha numeric with only underscore character allowed");
            throw InvalidInputException.builder().code(INVALID_NAME.getCode())
                    .errorMessage(INVALID_NAME.getMessage()).build();
        }
    }

    public static void validateUserId(String name) {
        if (StringUtils.isBlank(name)) {
            throw InvalidInputException.builder().code(USER_ID_MANDATORY.getCode())

                    .errorMessage(USER_ID_MANDATORY.getMessage()).build();
        } else if (StringUtils.length(name) > 50) {
            log.error("UserId is not valid. It should not be > 50 chars");
            throw InvalidInputException.builder().code(INVALID_NAME_LENGTH.getCode())
                    .errorMessage(INVALID_NAME_LENGTH.getMessage()).build();
        } else if (!name.matches(ALPHA_NUMERIC_WITH_SPACE_UNDERSCORE_HYPHEN_REGEX)) {
            log.error("Name is not valid. It should be alpha numeric with underscore, hyphen and space character allowed");
            throw InvalidInputException.builder().code(INVALID_NAME_WITH_HYPHEN.getCode())
                    .errorMessage(INVALID_NAME_WITH_HYPHEN.getMessage()).build();
        }
    }

    public static void validateMobileNumber(String phoneNumber) {
        if(!phoneNumber.matches("^\\d{10}$")){
            log.error("Invalid phone number. It should be 10 digits only");
            throw InvalidInputException.builder().code(INVALID_MOBILE_NUMBER.getCode())
                    .errorMessage(INVALID_MOBILE_NUMBER.getMessage()).build();
        }
    }

    public static void validateEmail(String email) {
        if (StringUtils.isBlank(email)) {
            throw InvalidInputException.builder().code(EMAIL_MANDATORY.getCode())
                    .errorMessage(EMAIL_MANDATORY.getMessage()).build();
        }
        if (!email.matches(EMAIL_REGEX)) {
            log.error("Invalid Email Id");
            throw InvalidInputException.builder().code(INVALID_EMAIL.getCode())
                    .errorMessage(INVALID_EMAIL.getMessage()).build();
        }
    }

    public static void validatePassword(String password) {
        if (StringUtils.isBlank(password)) {
            throw InvalidInputException.builder().code(PASSWORD_MANDATORY.getCode())
                    .errorMessage(PASSWORD_MANDATORY.getMessage()).build();
        }
        if (!password.matches(PASSWORD_REGEX)) {
            log.error(
                    """
                            Invalid password
                            -A digit must occur at least once.
                            -A lower case alphabet must occur at least once.
                            -An upper case alphabet that must occur at least once.\
                            
                            -A special character that must occur at least once.
                            -White spaces don’t allowed in the entire string.
                            -At least 8 characters and at most 20 characters.""");
            throw InvalidInputException.builder().code(INVALID_PASSWORD.getCode())
                    .errorMessage(INVALID_PASSWORD.getMessage()).build();
        }
    }

    public static void validateRole(String role) {
        if (role != null && !(
                (role.equals("ROLE_ADMIN") || role.equals("ROLE_USER") ))
        ) {
            log.error("Invalid role");
            throw InvalidInputException.builder().code(INVALID_ROLE.getCode())
                    .errorMessage(INVALID_ROLE.getMessage()).build();
        }
    }

    public static void validateStatus(String status) {
        if (status != null && !(status.equals(UserAccountStatusEnum.PENDING.name()) || status.equals(UserAccountStatusEnum.ACTIVE.name())
                || status.equals(UserAccountStatusEnum.SUSPEND.name()) || status.equals(UserAccountStatusEnum.REJECT.name()))) {
            log.error("Invalid status, possible value is one among these -  ACTIVE | PENDING | SUSPEND | REJECT");
            throw InvalidInputException.builder().code(INVALID_STATUS.getCode())
                    .errorMessage(INVALID_STATUS.getMessage()).build();
        }
    }


    public static void validateAddress1(String address1) {
        if (StringUtils.isBlank(address1)) {
            log.error("Address1 is mandatory");
            throw InvalidInputException.builder().code(ADDRESS_1_MANDATORY.getCode())
                    .errorMessage(ADDRESS_1_MANDATORY.getMessage()).build();
        }
        if (address1.length()>300) {
            log.error("Invalid address1 .Length should not be more than 300 characters");
            throw InvalidInputException.builder().code(INVALID_ADDRESS_1.getCode())
                    .errorMessage(INVALID_ADDRESS_1.getMessage()).build();
        }
    }

    public static void validatePagination(Integer pageNo, String sortBy, String orderBy) {
        validatePageNo(pageNo);
        validateSortBy(sortBy);
        validateOrderBy(orderBy);
    }

    public static void validateOrderBy(String orderBy) {
        if (!("asc".equals(orderBy) || "desc".equals(orderBy))) {
            log.error("orderBy should be asc | desc only");
            throw InvalidInputException.builder()
                    .code(INVALID_ORDER_BY.getCode()).errorMessage(INVALID_ORDER_BY.getMessage()).build();
        }
    }

    public static void validateSortBy(String sortBy) {
        if (!sortBy.matches(ALPHABETS_REGEX)) {
            log.error("sortBy should be alphabets with underscore allowed");
            throw InvalidInputException.builder()
                    .code(INVALID_SORT_BY.getCode()).errorMessage(INVALID_SORT_BY.getMessage()).build();
        }
    }

    public static void validatePageNo(Integer pageNo) {
        if (pageNo < 1) {
            log.error("pageNo should be >=1");
            throw InvalidInputException.builder()
                    .code(INVALID_PAGE_NO.getCode()).errorMessage(INVALID_PAGE_NO.getMessage()).build();
        }
    }

}
