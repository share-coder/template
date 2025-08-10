package com.jc.template.controller.admin;

import com.jc.template.controller.BaseController;
import com.jc.template.common.dto.PageInfo;
import com.jc.template.common.dto.Pair;
import com.jc.template.common.dto.usermgmt.UserDto;
import com.jc.template.feature.usermgmt.service.UserSearchService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Controller
@RequestMapping("/admin")
public class UsersController extends BaseController {
    private final UserSearchService userSearchService;

    public UsersController(RestTemplate restTemplate, UserSearchService userSearchService) {
        super(restTemplate);
        this.userSearchService = userSearchService;
    }

    @GetMapping("/users")
    public String users() {
        return "/admin/users";
    }

    // In your controller
    // Add a 'search' parameter to your service call
    @GetMapping("/usersData")
    @ResponseBody
    public Map<String, Object> getUsersData(Model model,
                                            @RequestParam int draw,
                                            @RequestParam int start,
                                            @RequestParam int length,
                                            @RequestParam(name = "order[0][column]", required = false) Integer orderColumn,
                                            @RequestParam(name = "order[0][dir]", required = false) String orderDir,
                                            @RequestParam(name = "search[value]", required = false) String searchValue,
                                            @RequestParam Map<String, String> requestParams,
                                            HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            String[] columns = {"id", "userId", "name", "mobileNumber", "email", "address", "dob", "status"};
            String sortColumn = "name";
            String sortDir = "asc";

            if (orderColumn != null && orderColumn >= 0 && orderColumn < columns.length) {
                sortColumn = columns[orderColumn];
            }
            if (orderDir != null && (orderDir.equalsIgnoreCase("asc") || orderDir.equalsIgnoreCase("desc"))) {
                sortDir = orderDir;
            }

            int page = start / length;
            // Pass searchValue to your service (implement filtering in your service)
            Pair<List<UserDto>, PageInfo> allUsers = userSearchService.findAllUsers(
                    searchValue, page, length, sortColumn, sortDir);

            List<?> users = allUsers.getElement1();
            long totalCount = allUsers.getElement2().getTotalElements();


            result.put("draw", draw);
            result.put("recordsTotal", totalCount);
            result.put("recordsFiltered", totalCount);
            result.put("data", users);
        } catch (Exception e) {
            log.error("Internal server error: {}", e.getMessage(), e);
            model.addAttribute("userError", "Server error");
            model.addAttribute("technicalError", e.getMessage());
        }
        return result;
    }
}
