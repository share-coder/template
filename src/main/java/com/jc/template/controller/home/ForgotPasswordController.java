package com.jc.template.controller.home;

import com.jc.template.common.entity.User;
import com.jc.template.controller.BaseController;
import com.jc.template.feature.usermgmt.repository.UserRepository;
import com.jc.template.feature.usermgmt.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
public class ForgotPasswordController extends BaseController {

    public ForgotPasswordController(RestTemplate restTemplate,
                                    UserRepository userRepository,
                                    UserServiceImpl userServiceImpl,
                                    BCryptPasswordEncoder bCryptPasswordEncoder
                                    ) {
        super(restTemplate);
        this.userRepository = userRepository;
        this.userServiceImpl = userServiceImpl;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    private final UserRepository userRepository;

    private final UserServiceImpl userServiceImpl;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping(value = "/loadForgotPassword")
    public String loadForgotPasswordPage() {
        return "forgot_password";
    }


    @PostMapping("/forgotPassword")
    public String forgotPassword(@RequestParam("email") String email, @RequestParam("mobileNumber") String mobileNumber, HttpSession session) {
        User user = userServiceImpl.checkEmailAndMobile(email, mobileNumber);
        if (user != null) {
            return "redirect:/loadResetPassword/" + user.getId();
        } else {
            session.setAttribute("msg", "Invalid email or mobile number");
            return "redirect:/loadForgotPassword";
        }
    }

    @PostMapping("/changePassword")
    public String resetPassword(@RequestParam String psw, @RequestParam Integer id, HttpSession session) {
        User userInfo = userRepository.findById(id).orElse(null);
        if (userInfo != null && !psw.isBlank()) {
            if (psw.length() < 8) {
                session.setAttribute("msg", "Password must be at least 8 characters long");
                return "redirect:/loadResetPassword/" + id;
            }
        } else {
            session.setAttribute("msg", "User not found or password is empty");
            return "redirect:/loadForgotPassword";
        }
        String encode = bCryptPasswordEncoder.encode(psw);
        userInfo.setPassword(encode);
        userRepository.save(userInfo);
        session.setAttribute("msg", "Password changed successfully");
        return "redirect:/loadForgotPassword";
    }

    @GetMapping(value = "/loadResetPassword/{id}")
    public String loadResetPasswordPage(@PathVariable int id, Model m) {
        m.addAttribute("id", id);
        return "reset_password";
    }

    @GetMapping(value = "/verify")
    public String loadVerifyAccountPage(@Param("code") String code, Model model) {
        if (userServiceImpl.verifyAccount(code)) {
            model.addAttribute("msg", "Account verified successfully");
            return "verify_success";
        } else {
            model.addAttribute("msg", "Sorry, we could not verify account. It may be already verified, or verification code is incorrect");
            return "verify_fail";
        }
    }
}
