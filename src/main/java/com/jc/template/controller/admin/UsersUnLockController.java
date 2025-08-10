package com.jc.template.controller.admin;

import com.jc.template.controller.BaseController;
import com.jc.template.feature.usermgmt.service.UserUpdateService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Controller
@RequestMapping("/admin")
public class UsersUnLockController extends BaseController {
    private final UserUpdateService userUpdateService;

    public UsersUnLockController(RestTemplate restTemplate, UserUpdateService userUpdateService) {
        super(restTemplate);
        this.userUpdateService = userUpdateService;
    }

    @GetMapping("/unlockUser/{userId}")
    public String unlockUser(@PathVariable Integer userId, Model model, HttpSession session) {
        try{
        userUpdateService.unlock(userId);
        session.setAttribute("msg", "User unlocked successfully");
        } catch (Exception e) {
            log.error("Internal server error: {}", e.getMessage(), e);
            model.addAttribute("userError", "Server error");
            model.addAttribute("technicalError", e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
