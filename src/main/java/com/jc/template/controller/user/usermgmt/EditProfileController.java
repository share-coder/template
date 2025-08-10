package com.jc.template.controller.user.usermgmt;

import com.jc.template.common.dto.usermgmt.UserDto;
import com.jc.template.common.entity.User;
import com.jc.template.controller.BaseController;
import com.jc.template.feature.usermgmt.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/user")
public class EditProfileController extends BaseController {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public EditProfileController(RestTemplate restTemplate, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        super(restTemplate);
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @GetMapping("/editProfileView")
    public String loadChangePassword(Model model, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        model.addAttribute("user", user);
        return "user/edit_profile";
    }

    @PostMapping("/submitProfile")
    public String editProfile(@ModelAttribute("userDto") UserDto userDto, Principal principal, Model model, HttpSession session) {
        try {
            User existingUser = userRepository.findByEmail(userDto.getEmail());
            if(StringUtils.isNotEmpty(userDto.getName())){
                existingUser.setName(userDto.getName());
            }
            if(StringUtils.isNotEmpty(userDto.getMobileNumber())){
                existingUser.setMobileNumber(userDto.getMobileNumber());
            }
            if(StringUtils.isNotEmpty(userDto.getAddress())){
                existingUser.setAddress(userDto.getAddress());
            }
            if(StringUtils.isNotEmpty(userDto.getDob())){
                existingUser.setDob(userDto.getDob());
            }
            if(StringUtils.isNotEmpty(userDto.getUserId())){
                existingUser.setUserId(userDto.getUserId());
            }
            userRepository.save(existingUser);
        } catch (Exception e) {
            log.error("Internal server error: {}", e.getMessage(), e);
            model.addAttribute("userError", "Server error");
            model.addAttribute("technicalError", e.getMessage());
        }
        return "redirect:/user/";
    }

}
