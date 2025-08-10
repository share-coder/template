package com.jc.template.common.constant;

import lombok.Getter;

@Getter
public enum UserApiMessage {
    USER_CREATED("UM-1", "User account created successfully. Please wait for admin approval"),
    INVALID_EMAIL("UM-2", "Invalid email address"),
    INVALID_NAME_LENGTH("UM-3", "Maximum 50 characters are allowed in name"),
    INVALID_PASSWORD("UM-4", "Invalid password"),
    INVALID_CITY("UM-5", "Invalid city, only alphabets allowed"),
    INVALID_STATE("UM-6", "Invalid state, only alphabets allowed"),
    INVALID_COUNTRY("UM-7", "Invalid country. It should be alphabets with space, underscore and number only"),
    INVALID_PIN_CODE("UM-8", "Invalid pin code, only numbers allowed"),
    INVALID_ROLE("UM-9", "Invalid role id. It should be either of 1 | 2 | 3 | 4 "),
    INVALID_STATUS("UM-10", "Invalid status"),
    USER_RETRIEVED("UM-11", "User account retrieved successfully"),
    USER_RETRIEVED_BY_ID("UM-12", "User account retrieved successfully"),
    USER_DELETED("UM-13", "User account deleted successfully"),
    USER_DOES_NOT_EXIST("UM-14", "User does not exist"),
    USER_UPDATED("UM-15", "User account(s) updated successfully"),
    AUTH_TOKEN("UM-16", "Authentication token retrieved successfully"),
    AUTHENTICATION_ERROR("UM-17", "User authentication failed"),
    FORGOT_PASSWORD_SUCCESS("UM-18", "Email sent to your registered email address for reset password"),
    FIRST_NAME_MANDATORY("UM-19", "First name is mandatory"),
    LAST_NAME_MANDATORY("UM-20", "Last name is mandatory"),
    USER_ID_MANDATORY("UM-21", "User id is mandatory"),
    EMAIL_MANDATORY("UM-22", "Email is mandatory"),
    PASSWORD_MANDATORY("UM-23", "Password is mandatory"),
    USER_EXIST_BY_USER_ID("UM-24", "User account already exist by user id, please provide unique user id"),
    USER_EXIST_BY_EMAIL("UM-25", "User account already exist by email, please provide unique email"),
    INVALID_USER_STATUS_FOR_LOGIN("UM-26", "Registration approval is pending, please contact admin"),
    INVALID_USER_ID("UM-27", "Invalid user id"),
    TOKEN_RETIRED_FORCEFULLY("UM-28", "Token expired successfully"),
    INVALID_CURRENT_PASSWORD("UM-29", "Invalid current password"),
    CHANGE_PASSWORD_SUCCESS("UM-30", "Password changed successfully"),
    BLOCK_USER("UM-31", "Login blocked due to too many failed attempts, please contact admin"),
    INVALID_NAME("UM-32", "Name should have only alpha numeric with underscore and space characters"),
    FIND_USER_BY_FILTER("UM-33", "Filtered user retrieved successfully"),
    INVALID_NEW_PASSWORD("UM-34", "New password should not be same as current password"),
    RESET_PASSWORD_SUCCESS("UM-35", "Password reset successfully"),
    INVALID_PAGE_NO("UM-36", "Invalid page no. It should be >= 1"),
    INVALID_SORT_BY("UM-37", "Invalid sortBy. It should be alphabets only"),
    INVALID_ORDER_BY("UM-38", "Invalid orderBy. It should be asc | desc"),
    USER_PARTIAL_UPDATED("UM-39", "User account(s) update completed with few failures"),
    USER_FULL_UPDATE_FAILED("UM-40", "User account(s) update failed for all ids"),
    USER_PARTIAL_DELETE("UM-41", "User account(s) delete completed with few failures"),
    USER_FULL_DELETE_FAILED("UM-42", "User account(s) delete failed for all ids"),
    DELETE_USER_BY_USER_ID("UM-43", "User deleted by user id successfully"),
    FINANCE_FIND_ALL("UM-44", "Retrieved all financial data successfully"),
    INVALID_EMAIL_NAME_LENGTH("UM-45", "Maximum 200 characters are allowed in name"),
    INVALID_NAME_WITH_HYPHEN("UM-46", "Name should have only alpha numeric, hyphen, underscore and space characters"),
    USER_DOWNLOAD_ERROR("UM-47", "Users download failed. Contact admin"),
    INVALID_MOBILE_NUMBER("UM-48", "Invalid phone number. It should be 10 digits only"),
    COUNTRY_MANDATORY("UM-49", "Country is mandatory"),
    STATE_MANDATORY("UM-50", "State is mandatory"),
    CITY_MANDATORY("UM-51", "City is mandatory"),
    ADDRESS_1_MANDATORY("UM-52", "Address 1 is mandatory"),
    INVALID_ADDRESS_1("UM-53", "Invalid address1 .Length should not be more than 300 characters"),
    USER_CREATED_BY_SU("UM-54", "User account created successfully by Super user"),
    ALL_USER_COUNT("UM-55", "Retrieved all user count successfully"),

    ;
    private final String code;
    private final String message;

    UserApiMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
