package com.selectionarts.projectcensio.apicontroller;

import com.selectionarts.projectcensio.apicontroller.viewmodel.AppViewModel;
import com.selectionarts.projectcensio.model.App;
import com.selectionarts.projectcensio.model.mapperViewEntity.AppMapper;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/apps")
@CrossOrigin
public class AppController {

    @Autowired
    private DBAppService dbAppService;

    @Autowired
    private AppMapper appMapper;

    @GetMapping("/all")
    public Set<AppViewModel> all() {
        List<App> allApps = this.dbAppService.findAll();


        // map from entity to view model
        Set<AppViewModel> appsViewModel = allApps.stream()
                .map(app -> appMapper.convertToAppViewModel(app, new HashSet<>()))
                .collect(Collectors.toSet());

        return appsViewModel;
    }

    @PostMapping
    public App save(@RequestBody AppViewModel appViewModel,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException();
        }


        App app = appMapper.convertToAppEntity(appViewModel);
        if(Long.valueOf(appViewModel.getId()) != 0)
        {
            dbAppService.updateApp(app, appViewModel);
        }
        else
        {
            dbAppService.createApp(app, appViewModel);
        }


        return app;
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable String id) {

        try{
            Optional<App> app = dbAppService.findById(id);
            if(app.isPresent())
            {
                dbAppService.deleteApp(app.get());
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }
}
