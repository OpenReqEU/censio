package com.selectionarts.projectcensio.repository;


import com.selectionarts.projectcensio.model.App;
import com.selectionarts.projectcensio.model.AppRegistration;
import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface AppRegistrationRepository extends JpaRepository<AppRegistration, Long> {


    @Query(" SELECT app.user FROM  AppRegistration app  " +
            "WHERE  app.app = :app ")
    public Set<User> getAllUsersFromApp(@Param("app") App app);


    public AppRegistration getAppRegistrationByAppAndUser(App app, User user);

    public void deleteAppRegistrationByAppAndUser(App app, User user);
    


}
