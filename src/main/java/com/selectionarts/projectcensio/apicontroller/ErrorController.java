package com.selectionarts.projectcensio.apicontroller;


import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.NestedServletException;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController  {
	
	@Override
    public String getErrorPath() {
        return "/error";
    }
	
	@RequestMapping("/error")
	public String handleError(HttpServletRequest request, 
			Model model, 
    		Authentication authentication) {
		
	    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

	    if (status != null) {
	    	model.addAttribute("statusCode", status.toString());
	    	
	    	Object message = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
	    	if (message != null && message instanceof NestedServletException) {
	    		model.addAttribute("errorText", ((NestedServletException)message).getMessage());
	    	} else {
	    		model.addAttribute("errorText", "");
	    	}
	    } else {
	    	model.addAttribute("statusCode", "500");
	    	model.addAttribute("errorText", "");
	    }
	    
	    return "error";
	}
}



