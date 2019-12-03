package com.selectionarts.projectcensio.apicontroller.viewmodel;


import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class UserViewModel implements Comparable<UserViewModel>{

    private String email;
    private String firstName;
    private String lastName;
    private String oldpassword;
    private String newpassword;
    private boolean newuser;


    @Override
    public int compareTo(UserViewModel o) {
        return this.getEmail().compareTo(o.getEmail());
    }


    public UserViewModel(String email, String firstName, String lastName, String oldpassword, String newpassword, boolean newuser) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.oldpassword = oldpassword;
        this.newpassword = newpassword;
        this.newuser = newuser;
    }


}
