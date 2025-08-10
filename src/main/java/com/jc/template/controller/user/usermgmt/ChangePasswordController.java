package com.jc.template.controller.user.usermgmt;

import com.jc.template.common.entity.User;
import com.jc.template.controller.BaseController;
import com.jc.template.feature.usermgmt.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/user")
public class ChangePasswordController extends BaseController {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ChangePasswordController(RestTemplate restTemplate, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        super(restTemplate);
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @GetMapping("/changePassword")
    public String loadChangePassword(Model model, Principal principal) {
        return "user/change_password";
    }

    /**
     * Change old password using /updatePassword and added this path in change password form action
     *
     * @param principal spring security principal
     * @param oldPass   old password
     * @param newPass   new password
     * @param session   session to store message for change password message
     * @return redirect to user/changePassword api
     */
    @PostMapping("/updatePassword")
    public String changePassword(Principal principal, Model model, @RequestParam("oldPass") String oldPass, @RequestParam("newPass") String newPass, HttpSession session) {
        try {
            String email = principal.getName();
            User loggedInUser = userRepository.findByEmail(email);
            if (loggedInUser != null) {
                boolean matches = bCryptPasswordEncoder.matches(oldPass, loggedInUser.getPassword());
                if (matches) {
                    loggedInUser.setPassword(bCryptPasswordEncoder.encode(newPass));
                    userRepository.save(loggedInUser);
                    session.setAttribute("msg", "Password changed successfully");
                } else {
                    session.setAttribute("msg", "Wrong Old Password");
                }
            }
        } catch (Exception e) {
            log.error("Internal server error: {}", e.getMessage(), e);
            model.addAttribute("userError", "Server error");
            model.addAttribute("technicalError", e.getMessage());
        }
        return "redirect:/user/changePassword";
    }
}
