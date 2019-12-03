package com.selectionarts.projectcensio.apicontroller.mvc;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.selectionarts.projectcensio.apicontroller.viewmodel.TaskViewModel;
import com.selectionarts.projectcensio.model.App;
import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.model.User;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.entity.EdemocracyVoting;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.AnonymousUser;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.EDemoProjectDto;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.EDemoUserDto;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.EdemoVotingDto;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.mapper.CensioUserEDemoUserMapper;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.service.IEDemocracyService;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.service.IEDemocracyVotingService;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBAppService;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBTaskService;

@Controller

@RequestMapping("/eDemocracy")
public class EDemocracyMvcController {
	
	@Autowired
	private IEDemocracyService eDemocracyService;
	
	@Autowired
	private IEDemocracyVotingService eDemocracyVotingService;

    @Autowired
    private DBAppService appService;
    
    @Autowired
    private DBTaskService taskService;

	@GetMapping("/{uuid}")
	public String eDemocracyVotingHome(@PathVariable("uuid") String uuid, Model model)
	{
		
		EdemocracyVoting edemocracyVoting = this.eDemocracyService.getByUserUUID(uuid);
		if (edemocracyVoting != null) {
			model.addAttribute("task", edemocracyVoting.getTask());
		}
		
		model.addAttribute("uuid", uuid);
		
		return "edemVote";
	}
	
	@GetMapping("/public/{uuid}")
	public String eDemocracyVotingPublicHome(@PathVariable("uuid") String uuid, Model model)
	{
		
		EdemocracyVoting edemocracyVoting = this.eDemocracyVotingService.getPublicVoting(uuid);
		if (edemocracyVoting != null) {
			model.addAttribute("task", edemocracyVoting.getTask());
		}
		
		model.addAttribute("uuid", uuid);
		model.addAttribute("anonymousUser", new AnonymousUser());
		
		return "edemVotePublic";
	}
	
    @RequestMapping(value="/apps/{id}/tasks/{taskid}/vote/{uuid}",params="upvote",method=RequestMethod.POST)
    public String upVote(@PathVariable("id") String id, @PathVariable("taskid") String taskid, @PathVariable("uuid") String uuid,
    		 Model model)
    {
    	App app = appService.findById(id).get();
    	
    	eDemocracyService.vote(uuid, app, true);
    	
    	model.addAttribute("finish", "finished");

        return "edemVote";
    }

    @RequestMapping(value="/apps/{id}/tasks/{taskid}/vote/{uuid}",params="downvote",method=RequestMethod.POST)
    public String downVote(@PathVariable("id") String id, @PathVariable("taskid") String taskid, @PathVariable("uuid") String uuid, Model model)
    {
    	App app = appService.findById(id).get();
    	
    	eDemocracyService.vote(uuid, app, false);

    	model.addAttribute("finish", "finished");
    	
        return "edemVote";
    }
    
    @RequestMapping(value="/apps/{id}/tasks/{taskid}/publicvote/{uuid}",params="upvote",method=RequestMethod.POST)
    public String publicUpVote(@PathVariable("id") String id, @PathVariable("taskid") long taskid, @PathVariable("uuid") String uuid,
    		@Valid @ModelAttribute AnonymousUser anonymousUser, BindingResult bindingResult, Model model)
    {
    	if (bindingResult.hasErrors()) {
    		
    		Task task = this.taskService.findById(taskid).get();
        	model.addAttribute("task", task);
            return "edemVotePublic";
        }
    	
    	App app = appService.findById(id).get();
    	
    	eDemocracyService.publicVote(uuid, anonymousUser.getNickname(), app, true);
    	
    	model.addAttribute("finish", "finished");

        return "edemVote";
    }

    @RequestMapping(value="/apps/{id}/tasks/{taskid}/publicvote/{uuid}",params="downvote",method=RequestMethod.POST)
    public String publicDownVote(@PathVariable("id") String id, @PathVariable("taskid") long taskid,
    		@PathVariable("uuid") String uuid, @Valid @ModelAttribute AnonymousUser anonymousUser, BindingResult bindingResult, Model model)
    {
    	
    	
    	if (bindingResult.hasErrors()) {
    		
    		Task task = this.taskService.findById(taskid).get();
        	model.addAttribute("task", task);
            return "edemVotePublic";
        }

    	App app = appService.findById(id).get();
    	
    	eDemocracyService.publicVote(uuid, anonymousUser.getNickname(), app, false);

    	model.addAttribute("finish", "finished");
    	
        return "edemVote";
    }
	
	//@GetMapping("/register")
	public String eDemocracyRegister()
	{
		User user = new User();
		user.setFirstName("Fritz");
		user.setLastName("Müller");
		user.setEmail("fritz.mueller@mueller.at");
		
		CensioUserEDemoUserMapper cedum = new CensioUserEDemoUserMapper();
		EDemoUserDto edemoUser = cedum.getEDemoUser(user);
		
		this.eDemocracyService.registerEdemocracyUser(edemoUser);
		
		System.out.println("E-Democracy register test ");
		
		return "edemocracy";
	}
	
	//@GetMapping("/login")
	public String eDemocracyLogin()
	{
		User user = new User();
		user.setFirstName("Fritz");
		user.setLastName("Müller");
		user.setEmail("fritz.mueller@mueller.at");
		
		CensioUserEDemoUserMapper cedum = new CensioUserEDemoUserMapper();
		EDemoUserDto edemoUser = cedum.getEDemoUser(user);
		
		this.eDemocracyService.loginEdemocracyUser(edemoUser);
		
		System.out.println("E-Democracy login test ");
		
		return "edemocracy";
	}
	
	//@GetMapping("/token_test")
	public String eDemocracyTokenTest()
	{
		this.eDemocracyService.tokenTest();
		
		System.out.println("E-Democracy token test ");
		
		return "edemocracy";
	}
	
	//@GetMapping("/createProject")
	public String eDemocracyCreateProject()
	{
		/*
		Task t = new Task();
		t.setTitle("Project A yes no");
		TaskToEdemoProject t2edemp = new TaskToEdemoProject();
		EDemoProjectDto edemoProj = t2edemp.getEDemoProject(t);
		*/
		
		String phaseEnd = "2019-05-29T06:59:46.351Z";
		String phaseCandidates = "2019-05-30T06:59:46.351Z";
		EDemoProjectDto edemoProj = new EDemoProjectDto(0,"A new Project with 2 tickets",phaseEnd, phaseCandidates,null);
		
		//edemoProj.setPhase_end(phaseEnd);
		//edemoProj.setPhase_candidates(phaseCandidates);
		
		this.eDemocracyService.createProject(edemoProj);
		
		System.out.println("E-Democracy create project ");
		
		return "edemocracy";
	}
	
	//@GetMapping("/retrieveProject")
	public String eDemocracyRetrieveProject()
	{

		EDemoProjectDto edemoProj = new EDemoProjectDto(5,"","","",null);
		
		this.eDemocracyService.retrieveProject(edemoProj);
		
		System.out.println("E-Democracy retrieve project ");
		
		return "edemocracy";
	}
	
	//@GetMapping("/createParticipationUser")
	public String eDemocracyCreateParticipationUser()
	{
		String phaseEnd = "2019-05-29T06:59:46.351Z";
		String phaseCandidates = "2019-05-30T06:59:46.351Z";
		EDemoProjectDto edemoProj = new EDemoProjectDto(5,"A Project with 2 tickets",phaseEnd, phaseCandidates,null);
		
		User user = new User();
		user.setFirstName("Fritz");
		user.setLastName("Müller");
		user.setEmail("fritz.mueller@mueller.at");
		
		CensioUserEDemoUserMapper cedum = new CensioUserEDemoUserMapper();
		EDemoUserDto edemoUser = cedum.getEDemoUser(user);
		
		edemoUser.setId(10);
		
		this.eDemocracyService.createParticipation(edemoProj, edemoUser,"user");
		
		System.out.println("E-Democracy create participation ");
		
		return "edemocracy";
	}
	
	//@GetMapping("/createParticipationCandidate")
	public String eDemocracyCreateParticipationCandidate()
	{
		String phaseEnd = "2019-05-29T06:59:46.351Z";
		String phaseCandidates = "2019-05-30T06:59:46.351Z";
		EDemoProjectDto edemoProj = new EDemoProjectDto(5,"A Project with 2 tickets",phaseEnd, phaseCandidates,null);
		
		User user = new User();
		user.setFirstName("Fritz");
		user.setLastName("Müller");
		user.setEmail("fritz.mueller@mueller.at");
		
		CensioUserEDemoUserMapper cedum = new CensioUserEDemoUserMapper();
		EDemoUserDto edemoUser = cedum.getEDemoUser(user);
		
		this.eDemocracyService.createParticipation(edemoProj, edemoUser,"candidate");
		
		System.out.println("E-Democracy create participation ");
		
		return "edemocracy";
	}
	
	//@GetMapping("/createVoting")
	public String eDemocracyCreateVoting()
	{
		EdemoVotingDto voting = new EdemoVotingDto();
		int[] voteArr = new int[1];
		voteArr[0] = 72; // id of the ticket to vote for 
		voting.setVotes(voteArr);
		
		String phaseEnd = "2019-05-29T06:59:46.351Z";
		String phaseCandidates = "2019-05-30T06:59:46.351Z";
		EDemoProjectDto edemoProj = new EDemoProjectDto(5,"A Project with 2 tickets",phaseEnd, phaseCandidates,null);
		
		this.eDemocracyService.createVoting(voting,edemoProj);
		
		System.out.println("E-Democracy create voting ");
		
		return "edemocracy";
	}
}
