package com.jc.template.controller.home;

import com.jc.template.common.enums.RoleEnum;
import com.jc.template.common.enums.UserAccountStatusEnum;
import com.jc.template.common.exception.InvalidInputException;
import com.jc.template.controller.BaseController;
import com.jc.template.common.dto.usermgmt.UserDto;
import com.jc.template.feature.usermgmt.service.UserRegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Controller
public class RegisterController extends BaseController {

    private final UserRegisterService userRegisterService;

    public RegisterController(RestTemplate restTemplate, UserRegisterService userRegisterService) {
        super(restTemplate);
        this.userRegisterService = userRegisterService;
    }

    @GetMapping("/register")
    public String signUpView(Model model) {
        //model.addAttribute("userDto", new UserDto());
        return "register";
    }

    /**
     * Handle the registration form submission.
     */
    @PostMapping("/submitSignup")
    public String submitRegistrationForm(@ModelAttribute("userDto") UserDto userDto, Model model) {
        userDto.setAccountNonLocked(false);
        userDto.setEnabled(false);
        userDto.setRole(RoleEnum.ROLE_USER.name());
        userDto.setStatus(UserAccountStatusEnum.PENDING.name());
        try {
            //throw new Exception("Error in registration process"); // Simulating an error for demonstration purposes
            boolean user = userRegisterService.createUser(List.of(userDto));
            if (!user) {
                model.addAttribute("registerError", "Registration failed");
            } else {
                model.addAttribute("userDto", new UserDto()); // Add this line after registration
                model.addAttribute("registerSuccessMessage", "Registration successful. Please wait for admin approval.");
            }
        } catch (InvalidInputException e) {
            log.error("Invalid input during registration: {}", e.getMessage());
            model.addAttribute("registerError", " Unable to create user as user already in system by user id: test");
        } catch (Exception ex) {
            log.error("Error during registration: {}", ex.getMessage(), ex);
            model.addAttribute("registerError", "Server error!!");
            model.addAttribute("userError", "Registration failed. Please try again.");
            model.addAttribute("technicalError", ex.getMessage());
        }
        return "register";
    }
}
