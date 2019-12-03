package com.selectionarts.projectcensio.service.dbserviceimpl;

import com.selectionarts.projectcensio.model.User;
import com.selectionarts.projectcensio.repository.UserRepository;
import com.selectionarts.projectcensio.service.EmailService;
import com.selectionarts.projectcensio.service.UserServiceInterface;
import com.selectionarts.projectcensio.util.generator.PasswordGenerator;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class DBUserService implements UserServiceInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    private EmailService emailService;
    
    @Value( "${censio.email-link}" )
    private String emaillink;

    @Override
    public User createUser(User user) {

        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public Optional<User> findUserById(long id) {
        return userRepository.findById(id);
    }

    public void requestPassword(String username) throws UsernameNotFoundException
    {
        User user = userRepository.findByEmail(username);

        if(user == null){
            throw new UsernameNotFoundException("Username not found");
        }


        String password = PasswordGenerator.generatePassword(10);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setFirstlogin(true);

        userRepository.save(user);
        
        String message = "";
		try {
			
			InputStream input = new ClassPathResource("static/assets/mailTemplate/mailPassword.html").getInputStream();
			message = IOUtils.toString(input);
			
			message = message.replace("###USERNAME###", user.getEmail());
			message = message.replace("###PASSWORD###", password);
			message = message.replace("###URL###", emaillink);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        emailService.sendEmailAsync(username,"[Censio] Reset Password",
                message);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
