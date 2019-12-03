package com.selectionarts.projectcensio.repository;

import com.selectionarts.projectcensio.model.App;
import com.selectionarts.projectcensio.model.AppRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface AppRepository extends JpaRepository<App, Long> {


    Set<App> findAppsBySubscriptionsInAndEnabledIsTrue(Set<AppRegistration> appRegistrations);



}
