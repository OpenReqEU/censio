package com.selectionarts.projectcensio.apicontroller.mvc;


import com.selectionarts.projectcensio.apicontroller.viewmodel.UserValidator;
import com.selectionarts.projectcensio.apicontroller.viewmodel.UserViewModel;
import com.selectionarts.projectcensio.model.User;
import com.selectionarts.projectcensio.model.UserDetailsDbo;
import com.selectionarts.projectcensio.model.mapperViewEntity.UserMapper;
import com.selectionarts.projectcensio.repository.UserRepository;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBUserService;
import com.selectionarts.projectcensio.service.securityserviceimpl.UserDetailServiceImpl;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ResourceBundle;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
public class UserController {

    @Autowired
    private DBUserService userService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/changeprofile")
    public String changeprofile(Authentication authentication, Model model)
    {
        long userid = ((UserDetailsDbo)authentication.getPrincipal()).getId();
        User user = userService.findUserById(userid).get();

        UserViewModel userViewModel = UserMapper.convertToUserViewModel(user);


        model.addAttribute("userViewModel", userViewModel);
        return "changeprofile";
    }


    @PostMapping("/changeprofile")
    public String updateprofile(@ModelAttribute("userViewModel") UserViewModel userViewModel, Authentication authentication, BindingResult bindingResult)
    {

        long userid = ((UserDetailsDbo)authentication.getPrincipal()).getId();


        User user = userService.findUserById(userid).get();


        if(user == null)
            return "redirect:/login";


        if(!userViewModel.getOldpassword().isEmpty() && !userViewModel.getNewpassword().isEmpty())
            userValidator.validateonlyPassword(userViewModel, bindingResult, user);

        if (bindingResult.hasErrors()) {
            return "changeprofile";
        }


        user = UserMapper.convertToUserAfterUpdateEntity(userViewModel,bindingResult,user);
        userRepository.save(user);

        UserDetails userDetails = userDetailService.loadUserByUsername(user.getEmail());

                String password = userViewModel.getNewpassword().isEmpty() ? userViewModel.getOldpassword(): userViewModel.getNewpassword();



        Authentication reAuth = new UsernamePasswordAuthenticationToken(userDetails,
                new BCryptPasswordEncoder().encode(password), ((UserDetailsDbo)authentication.getPrincipal()).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(reAuth);
        //HttpSession session = request.getSession(true);
        //session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);


        return "redirect:/dashboard";
    }
}
