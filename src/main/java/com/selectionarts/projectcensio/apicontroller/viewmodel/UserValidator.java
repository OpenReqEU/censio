package com.selectionarts.projectcensio.apicontroller.viewmodel;

import com.selectionarts.projectcensio.model.User;
import com.selectionarts.projectcensio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void validate(Object o, Errors errors) {

        UserViewModel user = (UserViewModel) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailAddress", "NotEmpty");
        if (user.getEmail().length() < 6 ) {
            errors.rejectValue("emailAddress", "Size.userForm.username");
        }

        User currentuser = userRepository.findByEmail(user.getEmail());

        if ( currentuser != null) {
            errors.rejectValue("emailAddress", "","Duplicated User");
        }


       ValidationUtils.rejectIfEmptyOrWhitespace(errors, "oldpassword", "NotEmpty");
        if (user.getOldpassword().length() == 0) {
            errors.rejectValue("oldpassword", null, "Password too short");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newpassword", "NotEmpty");
        if (!user.getNewpassword().equals(user.getOldpassword())) {
            errors.rejectValue("oldpassword", null, "Passwords are not equal");
        }

    }

    public void validateonlyPassword(Object o, Errors errors, User userdb ) {

        UserViewModel user = (UserViewModel) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "oldpassword", "NotEmpty");
        if (user.getOldpassword().length() == 0) {
            errors.rejectValue("oldpassword", null, "Password too short");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "oldpassword", "NotEmpty");
        if (!bCryptPasswordEncoder.matches(user.getOldpassword(), userdb.getPassword())) {
            errors.rejectValue("oldpassword", null, "No Match with old password");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newpassword", "NotEmpty");
        if (user.getNewpassword().equals(user.getOldpassword())) {
            errors.rejectValue("oldpassword", null, "New password should not match with the previous password");

        }

    }

}
