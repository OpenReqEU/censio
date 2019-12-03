package com.selectionarts.projectcensio.config;

import com.selectionarts.projectcensio.model.User;
import com.selectionarts.projectcensio.model.UserDetailsDbo;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBTaskService;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        System.out.println("jumped in customesuccesshandler");
        response.setStatus(HttpServletResponse.SC_OK);

        logger.info("AT onAuthenticationSuccess(...) function!");
        System.out.println("jumped in customesuccesshandler");



        response.sendRedirect("/dashboard");



    }
}
