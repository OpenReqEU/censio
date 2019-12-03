package com.selectionarts.projectcensio.apicontroller.viewmodel;

import lombok.Data;

@Data
public class UserFormModel    {

    private boolean acceptedMail;

    private boolean confirmRegistration;

    private boolean confirmSave;

    private boolean requestNewRegistrationLink;

    private boolean confirmedPasswordChange;

    private boolean requestNewPassword;

    private String emailAddress;

    private String password;

    private String passwordConfirm;

    private boolean privacy;

    private String firstname;

    private String lastname;

    private String image;

    private String token;

    private String url;

    public UserFormModel()
    {
        acceptedMail = false;
        confirmRegistration = false;
        confirmedPasswordChange = false;
        requestNewPassword = false;
        requestNewRegistrationLink = false;
        confirmSave = false;
        privacy = false;

    }

    public String getImageFromFile() {
        return "/files/"+image;
    }


}
