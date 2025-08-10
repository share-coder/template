package com.jc.template.common.config;

import com.jc.template.common.entity.User;
import com.jc.template.feature.usermgmt.service.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

/**
 * Role based authorization by redirect to admin page or user page based on role.
 * Added this CustomSuccessHandler in SecurityConfig class
 */
@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private UserServiceImpl userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        //in case of success we need to reset attempt
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        User user = customUser.getUser();
        if (user != null) {
            userService.resetAttempt(user.getEmail());
        }

        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin/");
        } else {
            response.sendRedirect("/user/");
        }
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }
}
