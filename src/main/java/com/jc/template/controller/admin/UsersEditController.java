package com.jc.template.controller.admin;

import com.jc.template.controller.BaseController;
import com.jc.template.common.dto.usermgmt.UserDto;
import com.jc.template.feature.usermgmt.service.UserSearchService;
import com.jc.template.feature.usermgmt.service.UserUpdateService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Controller
@RequestMapping("/admin")
public class UsersEditController extends BaseController {

    @Autowired
    private UserSearchService userSearchService;

    @Autowired
    private UserUpdateService userUpdateService;

    public UsersEditController(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @GetMapping("/editUser/{id}")
    public String editUserView(@PathVariable String id, Model model, HttpSession session) {
        try{
        model.addAttribute("editUser", userSearchService.findUserById(Integer.parseInt(id)));
        } catch (Exception e) {
            log.error("Internal server error: {}", e.getMessage(), e);
            model.addAttribute("userError", "Server error");
            model.addAttribute("technicalError", e.getMessage());
        }
        return "/admin/edit_user";
    }

    @PostMapping("/editSubmitUser")
    public String editUserViewSubmit(@ModelAttribute("userDto") UserDto userDto, Model model, HttpSession session) {
        try{
        userUpdateService.updateUser("",userDto);
        } catch (Exception e) {
            log.error("Internal server error: {}", e.getMessage(), e);
            model.addAttribute("userError", "Server error");
            model.addAttribute("technicalError", e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
