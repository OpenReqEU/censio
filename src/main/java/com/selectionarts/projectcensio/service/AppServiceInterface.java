package com.selectionarts.projectcensio.service;

import com.selectionarts.projectcensio.apicontroller.viewmodel.AppViewModel;
import com.selectionarts.projectcensio.model.App;

import java.util.List;
import java.util.Optional;

public interface AppServiceInterface {

    App createApp(App app, AppViewModel appViewModel);

    App updateApp(App app, AppViewModel appViewModel);

    void deleteApp(App app);

    List<App> findAll();

    Optional<App> findById(String id);

    App save(App app);


}
