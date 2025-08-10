package com.jc.template.controller.home;

import com.jc.template.controller.BaseController;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class LoginController extends BaseController {

    public LoginController(RestTemplate restTemplate) {
        super(restTemplate);
    }

    // Control comes here after submit login form.
    @GetMapping("/signin")
    public String signinView(Model model, HttpServletRequest request) {
        String loginErrorMsg = request.getSession() != null
                && request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION") != null
                ? request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION").toString() : "";
        if (StringUtils.isNotEmpty(loginErrorMsg)) {
            loginErrorMsg = loginErrorMsg.substring(loginErrorMsg.lastIndexOf(":") + 1).trim();
            switch (loginErrorMsg) {
                case "Bad credentials":
                case "User account is locked":
                case "User account is disabled":
                case "User does not exist":
                case "Email cannot be blank":
                case "Your account is locked !! Failed attempt 3 times":
                case "Your account is suspended by admin. Please contact support for assistance":
                case "Account is inactive. Wait for admin approval or contact support":
                    model.addAttribute("emailError", loginErrorMsg);
                    break;
                default:
                    model.addAttribute("emailError", "Unknow error");
                    break;
            }
            request.getSession().removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        }
        return "login";
    }
}
