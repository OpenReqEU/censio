package com.selectionarts.projectcensio.apicontroller.mvc;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.selectionarts.projectcensio.apicontroller.viewmodel.TaskViewModel;
import com.selectionarts.projectcensio.model.App;
import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.model.UserDetailsDbo;
import com.selectionarts.projectcensio.model.mapperViewEntity.TaskMapper;
import com.selectionarts.projectcensio.openReqServices.similarityDetection.service.ISimilarityDetectionService;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBAppService;

@Controller
@RequestMapping("/similarityDetection")
public class SimilarityDetectionMvcController {

	@Autowired
	private ISimilarityDetectionService iSimilarityDetectionService;
	
    @Autowired
    private DBAppService appService;
    
    @GetMapping("/{appId}/{taskId}")
    public String checkWithTmpTask(@PathVariable("appId") String appId,
    		@PathVariable("taskId") String taskId, 
    		@ModelAttribute TaskViewModel taskViewModel,
    		Authentication authentication)
    {
    	/**TODO CHECK if user or Creator**/
        App app = appService.findById(appId).get();

        if (taskViewModel != null &&  
           	 ( (taskViewModel.getTitle() != null && !taskViewModel.getTitle().isEmpty()) ||
           	   (taskViewModel.getDescription() != null && !taskViewModel.getDescription().isEmpty())) ) {
        	Task task = TaskMapper.convertToTaskEntity(taskViewModel);
            task.setApp(app);
            
            app.getTasks().add(task);
        }
        
        String userId = "0";
        if (authentication != null && authentication.getPrincipal() != null &&
        		authentication.getPrincipal() instanceof UserDetailsDbo) {
        	userId = String.valueOf(((UserDetailsDbo)authentication.getPrincipal()).getId());
        }
        
        String similarityId = this.iSimilarityDetectionService.addRequirements(app.getTasks(), appId, taskId, userId);
        
        return "404";
    }
}
