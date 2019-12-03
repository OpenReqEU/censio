package com.selectionarts.projectcensio.service;

import com.selectionarts.projectcensio.model.User;

import java.util.Optional;

public interface UserServiceInterface {

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(User user);

    Optional<User> findUserById(long id);

    User findByEmail(String email);
}
