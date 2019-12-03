
package com.selectionarts.projectcensio.apicontroller.mvc;


import com.selectionarts.projectcensio.repository.UserRepository;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBUserService;
import com.selectionarts.projectcensio.service.securityserviceimpl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.ResourceBundle;

@Controller
public class LoginController
{

    @Autowired
    private DBUserService userDetailService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "newpassword", required = false) String newpassword,
                            Model model) {
        String errorMessge = null;
        String infobox = null;
        if(error != null) {
            errorMessge = "Username or Password is incorrect !!";
        }
        if(logout != null) {
            infobox = "You have been successfully logged out !!";
        }

        if(newpassword != null) {
            infobox = "A new password has been requested";
        }
        model.addAttribute("errorMessge", errorMessge);
        model.addAttribute("infobox", infobox);


        return "login";
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout=true";
    }


    @RequestMapping(value = "/forgotpassword", method = RequestMethod.GET)
    public String forgotpassword(Model model, String error, String logout, Locale locale) {

        return "forgotpassword";
    }


    @RequestMapping(value = "/forgotpassword", method = RequestMethod.POST)
    public String forgotpasswordform(@RequestParam("username") String username) {

        userDetailService.requestPassword(username);

        return "redirect:/login?newpassword=true";
    }





}
