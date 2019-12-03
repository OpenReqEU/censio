package com.selectionarts.projectcensio.model.mapperViewEntity;


import com.selectionarts.projectcensio.apicontroller.viewmodel.UserViewModel;
import com.selectionarts.projectcensio.model.User;
import org.hibernate.usertype.UserVersionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class UserMapper {


    public static User convertToUserEntity(UserViewModel userViewModel) {
        User entity = new User(userViewModel.getEmail(), userViewModel.getFirstName(), userViewModel.getLastName(), "USER");
        return entity;
    }

    public static User convertToUserAfterUpdateEntity(UserViewModel userViewModel, BindingResult bindingResult, User user) {

        user.setFirstName(userViewModel.getFirstName());
        user.setLastName(userViewModel.getLastName());
        user.setFirstlogin(false);

        if(!userViewModel.getOldpassword().isEmpty() && !userViewModel.getNewpassword().isEmpty() && !bindingResult
                .hasErrors())
        {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            user.setPassword(bCryptPasswordEncoder.encode(userViewModel.getNewpassword()));
        }

        return user;
    }





    public static UserViewModel convertToUserViewModel(User user) {
        UserViewModel userViewModel =
                UserViewModel.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                        .build();

        return userViewModel;
    }


}
