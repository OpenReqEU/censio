package com.selectionarts.projectcensio.apicontroller.mvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.selectionarts.projectcensio.apicontroller.viewmodel.AppViewModel;
import com.selectionarts.projectcensio.apicontroller.viewmodel.TaskCommentViewModel;
import com.selectionarts.projectcensio.apicontroller.viewmodel.TaskViewModel;
import com.selectionarts.projectcensio.apicontroller.viewmodel.UserViewModel;
import com.selectionarts.projectcensio.model.*;
import com.selectionarts.projectcensio.model.mapperViewEntity.AppMapper;
import com.selectionarts.projectcensio.model.mapperViewEntity.TaskCommentMapper;
import com.selectionarts.projectcensio.model.mapperViewEntity.TaskMapper;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.entity.EdemocracyVoting;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.VotingResultView;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.report.EDemoReport;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.service.EDemocracyService;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.service.IEDemocracyService;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.service.IEDemocracyVotingService;
import com.selectionarts.projectcensio.repository.TaskCommentRepository;
import com.selectionarts.projectcensio.repository.TaskParticipationRepository;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBAppService;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBTaskService;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBUserService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.net.Authenticator;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("users")
public class TaskMvcController {

    @Autowired
    private DBAppService appService;

    @Autowired
    private DBTaskService taskService;

    @Autowired
    private DBUserService userService;

    @Autowired
    private TaskParticipationRepository participationRepository;

    @Autowired
    private TaskCommentRepository commentRepository;

    @Autowired
    private TaskCommentMapper taskCommentMapper;
    
    @Autowired
    private IEDemocracyService eDemocracyService;

    @Autowired
    private IEDemocracyVotingService eDemocracyVotingService;
    
    @Value( "${censio.email-link}" )
    private String emaillink;
    
    @GetMapping("/apps/{id}/tasks/{taskid}")
    public String getTaskDetail(@PathVariable("id") String id, @PathVariable("taskid") long taskid, Model model)
    {
        App app = appService.findById(id).get();
        Task task = taskService.findById(taskid).get();

        System.out.println("Show my current Size 1: " + app.getTasktypes().size());
        System.out.println("Show my current Size Subscripteion " + app.getSubscriptions().size());



        AppViewModel appViewModel = AppMapper.convertToAppViewModel(app, new HashSet<>());
        TaskViewModel taskViewModel = TaskMapper.convertToTaskViewModel(task);

        model.addAttribute("app", appViewModel);
        model.addAttribute("task", taskViewModel);
        model.addAttribute("appId", id);
        model.addAttribute("taskcomment", new TaskCommentViewModel());
        
        return "taskdetail";
    }


    @GetMapping("/apps/{id}/tasks/create")
    public String adminCreateTask(@PathVariable("id") String id, Model model)
    {
        App app = appService.findById(id).get();

        System.out.println("Show my current Size 2: " + app.getTasktypes().size());

        TaskViewModel taskViewModel = new TaskViewModel();
        taskViewModel.setAppid(id);

        AppViewModel appViewModel = AppMapper.convertToAppViewModel(app, new HashSet<>());
        System.out.println("Show my current Size " + appViewModel.getTaskAdditionalTypes().size());



        model.addAttribute("app", appViewModel);
        model.addAttribute("attributes", app.getTasktypes());

        model.addAttribute("task", taskViewModel);
        model.addAttribute("appId", id);

        /**TODO check if current User is creator**/
        String path = "/apps/"+id+"/tasks/create";


        model.addAttribute("path", path);
        return "taskedit";
    }


    @GetMapping("/apps/{appid}/tasks/update")
    public String updatetask(@PathVariable("appid") String appid, @RequestParam(name="id")long id, Model model)
    {
        App app = appService.findById(appid).get();

        Task task = taskService.findById(id).get();
        TaskViewModel taskViewModel = TaskMapper.convertToTaskViewModel(task);

        AppViewModel appViewModel = AppMapper.convertToAppViewModel(app, new HashSet<>());
        System.out.println("Show my current Size " + appViewModel.getTaskAdditionalTypes().size());



        model.addAttribute("app", appViewModel);
        model.addAttribute("attributes", app.getTasktypes());

        model.addAttribute("task", taskViewModel);
        model.addAttribute("appId", appid);

        /**TODO check if current User is creator**/
        String path = "/apps/"+appid+"/tasks/"+id+"/update";


        model.addAttribute("path", path);
        return "taskedit";
    }




    @PostMapping("/apps/{id}/tasks/create")
    public String adminSubmitNewTask( @PathVariable("id") String id, @ModelAttribute TaskViewModel taskViewModel, Authentication authentication)
    {

        /**TODO CHECK if user or Creator**/

        long userid = ((UserDetailsDbo)authentication.getPrincipal()).getId();
        User user = userService.findUserById(userid).get();

        App app = appService.findById(id).get();


        Task task = TaskMapper.convertToTaskEntity(taskViewModel);
            task.setTaskAdditionalTypes(taskViewModel.getTaskAdditionalTypes());
            task.setApp(app);
            task.setCreator(user);


        taskService.save(task);

        System.out.println("Jumped here");

        return "redirect:/apps/" + app.getId();
    }

    @PostMapping("/apps/{id}/tasks/{taskid}/update")
    public String adminSubmitUpdateTask( @PathVariable("id") String id, @PathVariable("taskid") long taskid, @ModelAttribute TaskViewModel taskViewModel, Authentication authentication)
    {

        /**TODO CHECK if user or Creator**/

        long userid = ((UserDetailsDbo)authentication.getPrincipal()).getId();
        User user = userService.findUserById(userid).get();

        App app = appService.findById(id).get();

        Task task = taskService.findById(taskid).get();
        task.setTitle(taskViewModel.getTitle());
        task.setDescription(taskViewModel.getDescription());
        task.setTaskAdditionalTypes(taskViewModel.getTaskAdditionalTypes());

        taskService.save(task);

        System.out.println("Jumped here");

        return "redirect:/apps/" + app.getId();
    }





    @RequestMapping(value="/apps/{id}/tasks/{taskid}/vote",params="upvote",method=RequestMethod.POST)
    public String upVote(@PathVariable("id") String id, @PathVariable("taskid") long taskid,
                         Authentication authentication)
    {

        Task task = taskService.findById(taskid).get();

        long userid = ((UserDetailsDbo)authentication.getPrincipal()).getId();
        User user = userService.findUserById(userid).get();

        TaskParticipation storedtask = this.participationRepository.findByTaskAndUser(task,user);

        if(storedtask == null)
        {

            storedtask = new TaskParticipation();
            storedtask.setRatedat(new Date());
            storedtask.setRating(1);
            storedtask.setUser(user);
            storedtask.setTask(task);


        }
        else {
            storedtask.setRating(1);
            storedtask.setRatedat(new Date());
        }


        this.participationRepository.save(storedtask);

        return "redirect:/apps/{id}/tasks/{taskid}";
    }

    @RequestMapping(value="/apps/{id}/tasks/{taskid}/vote",params="downvote",method=RequestMethod.POST)
    public String downVote(@PathVariable("id") String id, @PathVariable("taskid") long taskid,
                         Authentication authentication)
    {

        Task task = taskService.findById(taskid).get();

        long userid = ((UserDetailsDbo)authentication.getPrincipal()).getId();
        User user = userService.findUserById(userid).get();

        TaskParticipation storedtask = this.participationRepository.findByTaskAndUser(task,user);

        if(storedtask == null)
        {

            storedtask = new TaskParticipation();
            storedtask.setRatedat(new Date());
            storedtask.setRating(0);
            storedtask.setUser(user);
            storedtask.setTask(task);


        }
        else {
            storedtask.setRating(0);
            storedtask.setRatedat(new Date());
        }


        this.participationRepository.save(storedtask);

        return "redirect:/apps/{id}/tasks/{taskid}";
    }
    
    /*
    @RequestMapping(value="/apps/{id}/tasks/{taskid}/vote",params="edemocracy",method=RequestMethod.POST)
    public String edemocracy(@PathVariable("id") String id, @PathVariable("taskid") long taskid,
    		Authentication authentication, Model model)
    {

        Task task = taskService.findById(taskid).get();

        EdemocracyVoting edemocracyVoting = eDemocracyVotingService.getByTaskId(task.getId());
        List<String> addedUsers = null;
        if (edemocracyVoting != null) {
        	addedUsers = edemocracyVoting.getParticipations().stream().map(p -> p.getUsername()).collect(Collectors.toList());
        	
        	VotingResultView votingResultView = this.eDemocracyService.retrieveProjectReport(edemocracyVoting.getEDemoProjectId());
        	if (votingResultView == null) {
        		model.addAttribute("votingResultView", new VotingResultView(0, 0, addedUsers.size()));
        	} else {
        		model.addAttribute("votingResultView", votingResultView);
        	}
        } else {
        	addedUsers = new ArrayList<String>();
        	model.addAttribute("votingResultView", new VotingResultView(0, 0, 0));
        	edemocracyVoting = this.eDemocracyService.createEmptyVoting(task);
        }
        model.addAttribute("addedUsers", addedUsers);
        
        model.addAttribute("publicUuid", edemocracyVoting.getPublicUuid());
        model.addAttribute("publicUrl", emaillink + "eDemocracy/public/" + edemocracyVoting.getPublicUuid());
        
        model.addAttribute("addUserUrl", "/apps/" + id + "/tasks/" + task.getId() + "/addUser");
        model.addAttribute("task", task);
        
        List<String> users = new ArrayList<String>();
        model.addAttribute("users", users);

        return "edemocracy";
    } */
    
    @RequestMapping(value="/apps/{id}/tasks/{taskid}/edemocracy", method=RequestMethod.GET)
    public String edemocracy(@PathVariable("id") String id, @PathVariable("taskid") long taskid,
    		Authentication authentication, Model model)
    {

        Task task = taskService.findById(taskid).get();

        EdemocracyVoting edemocracyVoting = eDemocracyVotingService.getByTaskId(task.getId());
        List<String> addedUsers = null;
        if (edemocracyVoting != null) {
        	addedUsers = edemocracyVoting.getParticipations().stream().map(p -> p.getUsername()).collect(Collectors.toList());
        	
        	VotingResultView votingResultView = this.eDemocracyService.retrieveProjectReport(edemocracyVoting.getEDemoProjectId());
        	if (votingResultView == null) {
        		model.addAttribute("votingResultView", new VotingResultView(0, 0, addedUsers.size()));
        	} else {
        		model.addAttribute("votingResultView", votingResultView);
        	}
        } else {
        	addedUsers = new ArrayList<String>();
        	model.addAttribute("votingResultView", new VotingResultView(0, 0, 0));
        	edemocracyVoting = this.eDemocracyService.createEmptyVoting(task);
        }
        model.addAttribute("addedUsers", addedUsers);
        
        model.addAttribute("publicUuid", edemocracyVoting.getPublicUuid());
        model.addAttribute("publicUrl", emaillink + "eDemocracy/public/" + edemocracyVoting.getPublicUuid());
        
        model.addAttribute("addUserUrl", "/apps/" + id + "/tasks/" + task.getId() + "/addUser");
        model.addAttribute("task", task);
        
        List<String> users = new ArrayList<String>();
        model.addAttribute("users", users);

        return "edemocracy";
    }
    
    @RequestMapping(value = "/apps/{id}/tasks/{taskid}/addUser", method = RequestMethod.POST)
    public String addUserToEdemocracy(@RequestParam("user") String user, @SessionAttribute("users") List<String> users, Model model)
    {	
        if(user != null && !user.isEmpty())
        {
        	if (users == null) {
        		users = new ArrayList<String>();
        	}
        	users.add(user);
        } 

        model.addAttribute("users", users);

        return "fragments/edemuserlistfragment :: edemocracyUserlist";
    }
    
    @RequestMapping(value = "/apps/{id}/tasks/{taskid}/inviteUser", method = RequestMethod.POST)
    public String inviteUserToEdemocracy(@PathVariable("id") String id, @PathVariable("taskid") long taskid,
    		@SessionAttribute("users") List<String> users, Model model)
    {	
    	Task task = taskService.findById(taskid).get();
    	
    	EdemocracyVoting edemocracyVoting = this.eDemocracyService.registerEdemocracyUser(users, task.getApp(), task);
    	
    	users.clear();
    	
    	List<String> usernames = edemocracyVoting.getParticipations().stream().map(p -> p.getUsername()).collect(Collectors.toList());
    	
    	VotingResultView votingResultView = this.eDemocracyService.retrieveProjectReport(edemocracyVoting.getEDemoProjectId());
    	if (votingResultView == null) {
    		model.addAttribute("votingResultView", new VotingResultView(0, 0, usernames.size()));
    	} else {
    		model.addAttribute("votingResultView", votingResultView);
    	}
    	
    	model.addAttribute("addedUsers", usernames);
        model.addAttribute("addUserUrl", "/apps/" + id + "/tasks/" + task.getId() + "/addUser");
        model.addAttribute("task", task);
        model.addAttribute("users", users);
        
        return "redirect:/apps/" + id + "/tasks/" + task.getId() + "/edemocracy";
    }


    @RequestMapping(value="/apps/{id}/tasks/{taskid}/comment",method=RequestMethod.POST)
    public String addComments(@PathVariable("id") String id, @PathVariable("taskid") long taskid,
                              Authentication authentication,
                              @ModelAttribute("TaskComment")TaskCommentViewModel taskCommentViewModel)
    {

        Task task = taskService.findById(taskid).get();

        long userid = ((UserDetailsDbo)authentication.getPrincipal()).getId();
        User user = userService.findUserById(userid).get();

        TaskComment taskComment = TaskCommentMapper.convertToTaskEntity(taskCommentViewModel,task,user);
        commentRepository.save(taskComment);


        return "redirect:/apps/{id}/tasks/{taskid}";
    }




    @RequestMapping(value = "/apps/{appid}/tasks/delete", method = RequestMethod.GET)
    public String handleDeleteUser(@PathVariable("appid") String appid, @RequestParam(name="id")long id) {

        Task task = taskService.findById(id).get();
        task.setEnabled(false);

        taskService.save(task);

        return "redirect:/apps/" +appid+"";
    }


}
