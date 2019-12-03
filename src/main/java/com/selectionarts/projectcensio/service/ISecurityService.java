package com.selectionarts.projectcensio.service;

public interface ISecurityService {

    String findLoggedInUsername();

    void autologin(String email, String password);
}
