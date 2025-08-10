package com.jc.template.controller.admin;

import com.jc.template.common.entity.User;
import com.jc.template.controller.BaseController;
import com.jc.template.feature.usermgmt.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Controller
@RequestMapping("/admin")
public class AdminHomeController extends BaseController {
    final
    UserRepository userRepository;

    public AdminHomeController(RestTemplate restTemplate, UserRepository userRepository) {
        super(restTemplate);
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String userHome(Principal principal, Model model, HttpSession session) {
        try {
            principal.getName(); // Ensure principal is not null
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            session.setAttribute("userDto", user);
            session.setAttribute("role", user.getRole());
            session.setAttribute("jwtToken", user.getJwtToken());
            session.setAttribute("id", user.getId());
            model.addAttribute("user", user);
            model.addAttribute("userCount", userRepository.count());
        } catch (Exception ex) {
            log.error("Internal server error: {}", ex.getMessage(), ex);
            model.addAttribute("userError", "Server error");
            model.addAttribute("technicalError", ex.getMessage());
        }
        return "admin/home";
    }

    //this is important to have this method and set user in model so that it will use in bse html
    @ModelAttribute
    public void commonUser(Principal principal, Model model) {
        try {
            if (principal != null) {
                String email = principal.getName();
                User user = userRepository.findByEmail(email);
                model.addAttribute("user", user);
            }
        } catch (Exception ex) {
            log.error("Internal server error: {}", ex.getMessage(), ex);
            model.addAttribute("userError", "Server error");
            model.addAttribute("technicalError", ex.getMessage());
        }
    }
}
