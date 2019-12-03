package com.selectionarts.projectcensio.service.dbserviceimpl;

import com.selectionarts.projectcensio.apicontroller.viewmodel.AppViewModel;
import com.selectionarts.projectcensio.apicontroller.viewmodel.TaskViewModel;
import com.selectionarts.projectcensio.apicontroller.viewmodel.UserViewModel;
import com.selectionarts.projectcensio.model.*;
import com.selectionarts.projectcensio.model.mapperViewEntity.UserMapper;
import com.selectionarts.projectcensio.repository.*;
import com.selectionarts.projectcensio.service.AppServiceInterface;
import com.selectionarts.projectcensio.service.EmailService;
import com.selectionarts.projectcensio.service.securityserviceimpl.UserDetailServiceImpl;
import com.selectionarts.projectcensio.util.generator.PasswordGenerator;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.selectionarts.projectcensio.util.generator.RandomKeyGenerator.randomString;

@Service
public class DBAppService implements AppServiceInterface {

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private TaskTypesRepository taskTypesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppRegistrationRepository appRegistrationRepository;

    @Autowired
    private EmailService emailService;


    @Autowired
    private DBUserService userService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Value( "${censio.email-link}" )
    private String emaillink;

    @Override
    public App createApp(App app, AppViewModel appViewModel) {


        app = appRepository.save(app);
        addnewParticipations(app,appViewModel.getUserViewModelSet());
        addnewTaskAttribute(app,appViewModel.getTaskAdditionalTypes());

        return appRepository.save(app);
    }

    @Override
    public App updateApp(App app, AppViewModel appViewModel) {

        Optional<App> appstored = appRepository.findById(app.getId());
        if(appstored.isPresent())
        {
            appstored.get().setTitle(app.getTitle());
            appstored.get().setDescription(app.getDescription());
            if (app.getImgaeLocation() != null && !app.getImgaeLocation().isEmpty()) {
            	appstored.get().setImgaeLocation(app.getImgaeLocation());
            }
            addnewParticipations(appstored.get(),appViewModel.getUserViewModelSet());
            addnewTaskAttribute(appstored.get(),appViewModel.getTaskAdditionalTypes());

        }
        return appRepository.save(appstored.get());
    }

    @Override
    public void deleteApp(App app) {
        appRepository.delete(app);
    }

    @Override
    public List<App> findAll() {
        return  appRepository.findAll();
    }

    @Override
    public Optional<App> findById(String id) {
        return appRepository.findById(Long.parseLong((id)));
    }

    @Override
    public App save(App app) {
        return appRepository.save(app);
    }


    public void addnewParticipations(App app, Set<UserViewModel> userViewModelSet)
    {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean userisnew = false;
        for(UserViewModel userViewModel: userViewModelSet)
        {
            String password = "";
            User userA = null;
            if(userRepository.findByEmail(userViewModel.getEmail()) == null)
            {
                User usertoadd = UserMapper.convertToUserEntity(userViewModel);

                password = PasswordGenerator.generatePassword(10);
                usertoadd.setPassword(bCryptPasswordEncoder.encode(password));
                usertoadd.setFirstlogin(true);


                userA = userRepository.save(usertoadd);
                Notification notification = new Notification();
                notification.setUser(userA);
                notificationRepository.save(notification);
                userisnew = true;

            }
            else {
                userA = userRepository.findByEmail(userViewModel.getEmail());
            }


            Set<User> participants = appRegistrationRepository.getAllUsersFromApp(app);

            if(participants != null && !participants.contains(userA))
            {
                System.out.println("Not containing User: " + userA.getEmail());
                
                String message = "";
				try {

                    InputStream input = new ClassPathResource("static/assets/mailTemplate/mailUserResend.html").getInputStream();
					if(userisnew)
                    {
                         input = new ClassPathResource("static/assets/mailTemplate/mailUser.html").getInputStream();

                    }

					message = IOUtils.toString(input);

					if(userisnew)
                        message = message.replace("###PASSWORD###", password);

					message = message.replace("###PROJECT###", app.getTitle());
					message = message.replace("###USERNAME###", userViewModel.getEmail());
					message = message.replace("###URL###", emaillink + "apps/"+app.getId());
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
                emailService.sendEmailAsync(userViewModel.getEmail(),"[Censio] Added to "+app.getTitle(), message);

                AppRegistration appRegistration = new AppRegistration();
                appRegistration.setApp(app);
                appRegistration.setUser(userA);
                appRegistration.setRegisteredAt(new Date());
                this.appRegistrationRepository.save(appRegistration);

            }



        }
    }

    public void addnewTaskAttribute(App app, List<Types> typesList){

        for(Types t : typesList)
        {
            t.setApp(app);
            taskTypesRepository.save(t);
        }

    }


}
