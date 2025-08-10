package com.jc.template.common.util;

import com.jc.template.common.enums.RoleEnum;
import com.jc.template.common.exception.InvalidInputException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.jc.template.common.constant.Constants.ALPHABETS_REGEX;
import static com.jc.template.common.constant.Constants.SORT_ORDER_DESC;
import static com.jc.template.common.constant.UserApiMessage.INVALID_ORDER_BY;
import static com.jc.template.common.constant.UserApiMessage.INVALID_SORT_BY;


@Slf4j
public final class Utils {

    public static String getTokenWithoutBearer(String authorization) {
        return authorization.substring(7);
    }

    public static PageRequest getPageable(Integer pageNo, Integer pageSize, String sortBy, String orderBy) {
        validatePagination(pageNo, sortBy, orderBy);
        return PageRequest.of(pageNo, pageSize, orderBy.equals(SORT_ORDER_DESC) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
    }

    public static String getFileNameOnlyFromFileNamePath(String fileName) {
        return fileName.substring(fileName.lastIndexOf("/") + 1);
    }

    public static InputStream getFileFromResourceAsStream(String fileName) {
        return Utils.class.getClassLoader().getResourceAsStream(fileName);
    }
    public static void validatePagination(Integer pageNo, String sortBy, String orderBy) {
//        validatePageNo(pageNo);
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

//    public static void validatePageNo(Integer pageNo) {
//        if (pageNo < 1) {
//            log.error("pageNo should be >=1");
//            throw InvalidInputException.builder()
//                    .code(INVALID_PAGE_NO.getCode()).errorMessage(INVALID_PAGE_NO.getMessage()).build();
//        }
//    }

    public static String fromRoleNumberToRole(String role) {
        String roleStr;
        if ("1".equals(role)) {
            roleStr = RoleEnum.ROLE_ADMIN.name();
        } else if ("2".equals(role)) {
            roleStr = RoleEnum.ROLE_USER.name();
        } else {
            roleStr = "";
        }
        return roleStr;
    }

    public static String getQueryParam(String lpUrl, String pUrl) {
        log.info("Entering getQueryParam - lpUrl: {}, pUrl: {}", lpUrl, pUrl);
        String[] lpArr = lpUrl.split("&");
        Map<String, Object> lpMap = new LinkedHashMap<>();
        for (int i = 1, lpArrLength = lpArr.length; i < lpArrLength; i++) {
            String lp = lpArr[i];
            String[] split = lp.split("=");
            if (split[1].contains("#")) {
                split[1] = split[1].substring(0, split[1].indexOf("#"));
            }
            lpMap.put(split[0], split[1]);
        }
        log.info("## lpMap: {}", lpMap);
        Map<String, Object> pMap = new LinkedHashMap<>();
        String[] split = pUrl.split("\\?");
        if (split.length > 1) {
            String[] pArr = split[1].split("&");

            for (int i = 0, pArrLength = pArr.length; i < pArrLength; i++) {
                String p = pArr[i];
                String[] splitP = p.split("=");
                pMap.put(splitP[0], splitP[1]);
            }
            lpMap.forEach((k, v) -> {
                Object pmapVal = pMap.get(k);
                if (pmapVal != null) {
                    lpMap.put(k, pmapVal);
                    pMap.remove(k);
                }
            });
        }
        lpMap.putAll(pMap);
        StringBuilder sb = new StringBuilder();
        lpMap.forEach((k, v) -> {
            sb.append(k).append("=").append(v).append("&");
        });
        if (StringUtils.isNotEmpty(sb.toString())) {
            sb.deleteCharAt(sb.toString().length() - 1);
        }
        return sb.toString();
    }
}
