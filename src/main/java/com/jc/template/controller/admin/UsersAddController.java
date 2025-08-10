package com.jc.template.controller.admin;

import com.jc.template.common.entity.User;
import com.jc.template.common.enums.RoleEnum;
import com.jc.template.controller.BaseController;
import com.jc.template.common.dto.usermgmt.UserDto;
import com.jc.template.feature.usermgmt.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Controller
@RequestMapping("/admin")
public class UsersAddController extends BaseController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsersAddController(RestTemplate restTemplate) {
        super(restTemplate);
    }


    /**
     * ADD USER VIEW
     */
    @GetMapping("/addUser")
    public String addUserView() {
        return "/admin/add_user";
    }

    @PostMapping("/addUserSubmit")
    public String addUserViewSubmit(@ModelAttribute("userDto") UserDto userDto, Model model, HttpSession session) {
        try {
            // Encrypt the password before saving
            userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            userDto.setEnabled(true);
            userDto.setAccountNonLocked(true);
            userDto.setStatus("ACTIVE");
            userDto.setRole(RoleEnum.ROLE_USER.name());
            User user = UserDto.to(userDto);
            userRepository.save(user);
            session.setAttribute("msg", "User added successfully");
            return "redirect:/admin/users"; // redirect to users page
        } catch (Exception e) {
            session.setAttribute("error", "Error adding user: " + e.getMessage());
            log.error("Internal server error: {}", e.getMessage(), e);
            model.addAttribute("userError", "Server error");
            model.addAttribute("technicalError", e.getMessage());
        }
        return "redirect:/admin/users"; // return to register page with error
    }
}
