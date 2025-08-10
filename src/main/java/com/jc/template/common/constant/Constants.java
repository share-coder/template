package com.jc.template.common.constant;

public class Constants {
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    public static final String ALPHA_NUMERIC_WITH_SPACE_REGEX = "^[A-Za-z0-9- ]+$";
    public static final String ALPHA_NUMERIC_WITH_UNDERSCORE_REGEX = "^[a-zA-Z0-9_]+$";
    public static final String ALPHA_NUMERIC_WITH_SPACE_UNDERSCORE_REGEX = "^[a-zA-Z0-9_ -]+$";
    public static final String ALPHA_NUMERIC_WITH_SPACE_UNDERSCORE_HYPHEN_REGEX = "^[a-zA-Z0-9-_ ]+$";
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$";
    public static final String ALPHABETS_REGEX = "^[a-zA-Z]*$";
    public static final String ALPHABETS_WITH_SPACE_REGEX = "^[a-zA-Z ]*$";
    public static final int LOGIN_ATTEMPTS = 7;
    public static final String PENDING = "PENDING";
    public static final String COMMA = ",";
    public static final String ACTIVE = "ACTIVE";
    public static final String SORT_ORDER_DESC = "desc";
    public static final String INDEX_HTML = "home.html";
}
