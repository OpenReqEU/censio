package com.selectionarts.projectcensio.apicontroller.mvc;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
public class DefaultController {
	
	@RequestMapping("/")
    public String base() {
        return "redirect:/dashboard";
    }
	
    @RequestMapping("/404")
    public String error404() {
        return "404";
    }

    @RequestMapping("/500")
    public String error500() {
        return "500";
    }

    /*@RequestMapping("/login")
    public String loginpage() {
        return "login";
    }
    
    @RequestMapping("/logout")
    public String logoutpage() {
        return "login";
    }*/

    @RequestMapping("/register")
    public String registerpage() {
        return "register";
    }

    @RequestMapping("/forgotpassword")
    public String forgotpasswordpage() {
        return "forgotpassword";
    }

    @RequestMapping("/index")
    public String baseindex() {
        return "index";
    }



}



