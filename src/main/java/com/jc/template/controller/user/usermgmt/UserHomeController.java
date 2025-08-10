package com.jc.template.controller.user.usermgmt;

import com.jc.template.common.entity.User;
import com.jc.template.controller.BaseController;
import com.jc.template.feature.usermgmt.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserHomeController extends BaseController {
    private final UserRepository userRepository;

    public UserHomeController(RestTemplate restTemplate, UserRepository userRepository) {
        super(restTemplate);
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String userHome(Principal principal, HttpSession session) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        session.setAttribute("userDto", user);
        return "user/home";
    }

    //this is important to have this method and set user in model so that it will use in bse html
    @ModelAttribute
    public void commonUser(Principal principal, Model model) {
        if (principal != null) {
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            model.addAttribute("user", user);
            Integer id = user.getId();
            long voiceCount = 0L;
            long messageTemplateCount = 0L;
            model.addAttribute("messageCount", 0);
            model.addAttribute("voiceCount", voiceCount);
            model.addAttribute("recipientlistCount", 0);
            model.addAttribute("messageTemplateCount", messageTemplateCount);
        }
    }
}
