package com.jc.template.controller.admin;

import com.jc.template.controller.BaseController;
import com.jc.template.feature.usermgmt.service.UserDeleteService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UsersDeleteController extends BaseController {

    @Autowired
    private UserDeleteService userDeleteService;

    public UsersDeleteController(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @GetMapping("/deleteUserBySu/{userId}")
    public String deleteUserSubmit(@PathVariable String userId, Model model, HttpSession session) {
        try{
        userDeleteService.deleteUserByUserId(Integer.parseInt(userId),"");
        session.setAttribute("deleteUserSuccess", true);
        session.setAttribute("deleteUserSuccessMessage", "Successfully deleted user");
        } catch (Exception e) {
            log.error("Internal server error: {}", e.getMessage(), e);
            model.addAttribute("userError", "Server error");
            model.addAttribute("technicalError", e.getMessage());
        }
        return "redirect:/admin/users"; // redirect to users page
    }
}
