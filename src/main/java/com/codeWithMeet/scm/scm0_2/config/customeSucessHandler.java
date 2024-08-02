package com.codeWithMeet.scm.scm0_2.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class customeSucessHandler implements AuthenticationSuccessHandler {



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {


        Set<String> roles1 = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        System.out.println(  " ****************" + roles1);

     if(roles1.contains("ROLE_USER")){
         response.sendRedirect("/user/dashboard");
     }
     else if (roles1.contains("ROLE_ADMIN")){
         response.sendRedirect("/admin/dashboard");
     }

    }
}
