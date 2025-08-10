package com.jc.template.feature.usermgmt.service;

import com.jc.template.common.constant.UserApiMessage;
import com.jc.template.common.dto.PageInfo;
import com.jc.template.common.dto.Pair;
import com.jc.template.common.dto.usermgmt.UserDto;
import com.jc.template.common.dto.usermgmt.UserFilterDto;
import com.jc.template.common.entity.User;
import com.jc.template.common.exception.InvalidInputException;
import com.jc.template.feature.usermgmt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.jc.template.common.constant.Constants.SORT_ORDER_DESC;
import static com.jc.template.feature.usermgmt.util.UMInputValidator.validateRole;
import static com.jc.template.feature.usermgmt.util.UMInputValidator.validateStatus;
import static com.jc.template.common.util.Utils.fromRoleNumberToRole;
import static com.jc.template.common.util.Utils.getPageable;


@Service
@Transactional
@Slf4j
public class UserSearchService {
    private final UserRepository userRepository;

    public UserSearchService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    public Pair<List<UserDto>, PageInfo> findAllUsers(String searchKeyword,
//                                                      Integer pageNo, Integer pageSize, String sortBy, String orderBy) {
//        log.info("findAllUsers");
////        PageRequest pageable = getPageable(pageNo, pageSize, sortBy, orderBy);
////        validatePagination(pageNo, sortBy, orderBy);
//        PageRequest pageable = PageRequest.of(pageNo , pageSize, orderBy.equals(SORT_ORDER_DESC) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
//        Page<User> page = userRepository.findAll(pageable);
//        List<UserDto> userDTOS = page.stream()
//                .map(UserDto::from)
//                .filter(userDto -> !"super".equalsIgnoreCase(userDto.getName()))
//                .collect(Collectors.toList());
//        int totalPages = page.getTotalPages();
//        long totalElements = page.getTotalElements();
//
//        Pair<List<UserDto>, PageInfo> p = new Pair<>();
//        p.setElement1(userDTOS);
//        p.setElement2(new PageInfo(totalPages, totalElements));
//        return p;
//    }

public Pair<List<UserDto>, PageInfo> findAllUsers(String searchKeyword,
                                                  Integer pageNo, Integer pageSize, String sortBy, String orderBy) {
    log.info("findAllUsers");
    PageRequest pageable = PageRequest.of(pageNo, pageSize, orderBy.equals(SORT_ORDER_DESC) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
    Page<User> page;
    if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
        String keyword = searchKeyword.trim().toLowerCase();
        page = userRepository.searchAllFields(keyword, pageable);
    } else {
        page = userRepository.findAll(pageable);
    }

    List<UserDto> userDTOS = page.stream()
            .map(UserDto::from)
            .filter(userDto -> !"admin".equalsIgnoreCase(userDto.getName()))
            .collect(Collectors.toList());

    int totalPages = page.getTotalPages();
    long totalElements = page.getTotalElements();

    Pair<List<UserDto>, PageInfo> p = new Pair<>();
    p.setElement1(userDTOS);
    p.setElement2(new PageInfo(totalPages, totalElements));
    return p;
}

    public UserDto findUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> InvalidInputException.builder().code(UserApiMessage.USER_DOES_NOT_EXIST.getCode())
                        .errorMessage(UserApiMessage.USER_DOES_NOT_EXIST.getMessage())
                        .build());
        return UserDto.from(user);
    }

//    public List<UserDto> searchUserByKey(String key) {
//        List<User> userList = userRepository.seachUserByKey(key);
//        List<UserDto> userDtoList = new ArrayList<>();
//        if (userList != null) {
//            for (User u : userList) {
//                userDtoList.add(UserDto.from(u));
//            }
//        }
//        return userDtoList;
//    }

    public long findAllCount() {
        log.info("findAllCount");
        long count = userRepository.count();
        return count;
    }

    public org.springframework.data.util.Pair<List<UserDto>, PageInfo> filterUser(UserFilterDto userFilterDto, Integer pageNo,
                                                                                  Integer pageSize, String sortBy, String orderBy) {
        log.info("Filtering user based on userFilterDto: {}", userFilterDto);
        String roleStr = null;
        if (userFilterDto.getRole() != null) {
            roleStr = fromRoleNumberToRole(userFilterDto.getRole().toString());
            validateRole(roleStr);
            userFilterDto.setRole(roleStr);
        }
        validateStatus(userFilterDto.getStatus());

        // Example: apply filters in repository (you need to implement this method in UserRepository)
        Page<User> page = userRepository.findByFilters(
                userFilterDto.getUserId(),
                userFilterDto.getRole(),
                userFilterDto.getStatus(),
                getPageable(pageNo, pageSize, sortBy, orderBy)
        );

        return org.springframework.data.util.Pair.of(
                page.stream().map(UserDto::from).collect(Collectors.toList()),
                new PageInfo(page.getTotalPages(), page.getTotalElements())
        );
    }
}
