package com.selectionarts.projectcensio.service.securityserviceimpl;

import com.selectionarts.projectcensio.model.User;
import com.selectionarts.projectcensio.model.UserDetailsDbo;
import com.selectionarts.projectcensio.repository.UserRepository;
import com.selectionarts.projectcensio.service.EmailService;
import com.selectionarts.projectcensio.service.UserServiceInterface;

import com.selectionarts.projectcensio.util.generator.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserServiceInterface userServiceInterface;

    @Autowired
    private UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);

        if(user == null){
            throw new UsernameNotFoundException("Username not found");
        }

        //TODO Mapper
        UserDetailsDbo myUser = new UserDetailsDbo();
        myUser.setEnabled(true);
        myUser.setFirstName(user.getFirstName());
        myUser.setLastName(user.getLastName());
        myUser.setPassword(user.getPassword());
        myUser.setUsername(user.getEmail());
        myUser.setId(user.getId());
        myUser.setRole(user.getRole());

        return myUser;
    }






}