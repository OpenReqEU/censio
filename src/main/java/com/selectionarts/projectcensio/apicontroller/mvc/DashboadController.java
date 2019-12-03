package com.selectionarts.projectcensio.apicontroller.mvc;


import com.selectionarts.projectcensio.apicontroller.viewmodel.AppViewModel;
import com.selectionarts.projectcensio.apicontroller.viewmodel.DashboardViewModel;
import com.selectionarts.projectcensio.model.App;
import com.selectionarts.projectcensio.model.User;
import com.selectionarts.projectcensio.model.UserDetailsDbo;
import com.selectionarts.projectcensio.model.mapperViewEntity.AppMapper;
import com.selectionarts.projectcensio.repository.AppRepository;
import com.selectionarts.projectcensio.service.AppServiceInterface;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBAppService;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBTaskService;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@Transactional
@RequestMapping("/dashboard")
public class DashboadController {

    @Autowired
    private DBAppService appService;

    @Autowired
    private DBTaskService taskService;

    @Autowired
    private DBUserService userService;

    @Autowired
    private AppRepository appRepository;

    @GetMapping
    public String showDashboard(Model model, Authentication authentication)
    {
        DashboardViewModel dashboardViewModel = new DashboardViewModel();

        long userid = ((UserDetailsDbo)authentication.getPrincipal()).getId();
        User user = userService.findUserById(userid).get();

        Set<App> apps = appRepository.findAppsBySubscriptionsInAndEnabledIsTrue(user.getSubscriptions());
        apps.forEach( app -> {
                    AppViewModel temp = AppMapper.convertToAppViewModel(app, new HashSet<>());
                    dashboardViewModel.getAppViewModelSet().add(temp);
                }
        );


        //TODO Call any method for calculating the the new tasks

        taskService.calculateTasksForUser(user);


        model.addAttribute("dashboard",dashboardViewModel);
        return "dashboard";
    }
}
