package com.selectionarts.projectcensio.apicontroller.mvc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.selectionarts.projectcensio.apicontroller.viewmodel.TaskViewModel;
import com.selectionarts.projectcensio.model.App;
import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.model.mapperViewEntity.TaskMapper;
import com.selectionarts.projectcensio.openReqServices.crossReferenceDetection.service.ICrossReferenceDetectionService;
import com.selectionarts.projectcensio.openReqServices.dependencyDetection.model.bo.DependencyTask;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBAppService;

@Controller
@RequestMapping("/crossReferenceDetection")
public class CrossReferenceDetectionMvcController {

	@Autowired
	private ICrossReferenceDetectionService iCrossReferenceDetectionService;
	
    @Autowired
    private DBAppService appService;
    
    @GetMapping("/{appId}/{taskId}")
    public String checkWithTmpTask(@PathVariable("appId") String appId,
    		@PathVariable("taskId") String taskId, 
    		@ModelAttribute TaskViewModel taskViewModel, 
    		Model model)
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
        
        List<DependencyTask> returnTasks = this.iCrossReferenceDetectionService.getCrossReferences(app.getTasks());
        
        model.addAttribute("tasks", returnTasks);
        model.addAttribute("appId", appId);
        model.addAttribute("taskId", taskId);

        return "fragments/crossRefDetectionFragment :: dependencies";
    }
}
