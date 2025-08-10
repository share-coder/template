package com.jc.template.common.config;

import com.jc.template.common.entity.User;
import com.jc.template.common.enums.UserAccountStatusEnum;
import com.jc.template.feature.usermgmt.repository.UserRepository;
import com.jc.template.feature.usermgmt.service.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String email = request.getParameter("username");
        if (StringUtils.isBlank(email)) {
            exception = new LockedException("Email cannot be blank");
        } else {
            User userInfo = userRepository.findByEmail(email);
            if (userInfo != null) {
                if (userInfo.getStatus().equals(UserAccountStatusEnum.SUSPEND.name())) {
                    exception = new LockedException("Your account is suspended by admin. Please contact support for assistance");
                } else {
                    if (userInfo.isEnabled()) {
                        if (userInfo.isAccountNonLocked()) {
                            if (userInfo.getFailedAttempts() < UserServiceImpl.attemptTime - 1) {
                                userService.increaseFailedAttempts(userInfo);
                            } else {
                                userService.lock(userInfo);
                                exception = new LockedException("Your account is locked !! Failed attempt 3 times");
                            }
                        } else {
                            if (userService.unlockAccountAsLockTimeExpired(userInfo)) {
                                exception = new LockedException("Account is unlocked! Please try to login");
                            } else {
                                exception = new LockedException("Account is still locked! Please try after sometime");
                            }
                        }
                    } else {
                        exception = new LockedException("Account is inactive. Wait for admin approval or contact support");
                    }
                }
            } else {
                exception = new LockedException("User does not exist");
            }
        }
        super.setDefaultFailureUrl("/signin?error");
        super.onAuthenticationFailure(request, response, exception);
    }

}
