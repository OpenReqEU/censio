package com.selectionarts.projectcensio.openReqServices.similarityDetection.restController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selectionarts.projectcensio.model.App;
import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.model.User;
import com.selectionarts.projectcensio.openReqServices.dependencyDetection.model.bo.DependencyTask;
import com.selectionarts.projectcensio.openReqServices.similarityDetection.model.DependenciesDto;
import com.selectionarts.projectcensio.openReqServices.similarityDetection.model.DependencyDto;
import com.selectionarts.projectcensio.openReqServices.similarityDetection.service.ISimilarityDetectionService;
import com.selectionarts.projectcensio.openReqServices.similarityDetection.util.OfflineSpringWebContext;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBAppService;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBUserService;
import com.selectionarts.projectcensio.service.securityserviceimpl.UserDetailServiceImpl;

@RestController
@RequestMapping("/similarityDetectionCallbacks")
public class SimilarityDetectionCallbacksController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    @Autowired
    private TemplateEngine templateEngine;
    
    @Autowired
    private ISimilarityDetectionService iSimilarityDetectionService;
    
    @Autowired
    private DBAppService appService;
    
    @Autowired
    private DBUserService userService;

    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private ServletContext servletContext; 
    
    @RequestMapping(value="/AddReqsCallback/{appId}/{taskId}/{userId}", method=RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseEntity<String> similartiyDetectionAddReqsCallback(
    		@PathVariable("appId") final String appId,
    		@PathVariable("taskId") final String taskId,
    		@PathVariable("userId") final String userId,
    		@RequestPart("info") String info, 
    		@RequestPart("result") MultipartFile file,
    		Model model) {
	    
    	System.out.println("similartiyDetectionAddReqsCallback: " + info);
    	
    	byte[] bytes;
		try {
			bytes = file.getBytes();
			String completeData = new String(bytes);
			
			System.out.println("similartiyDetectionAddReqsCallback stream: " + completeData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			JSONObject o = (JSONObject)new JSONParser().parse(info);
			App app = appService.findById(appId).get();
			
			String similarityId = this.iSimilarityDetectionService.getSimilarities((String)o.get("id"), app.getTasks(), appId, taskId, userId);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
    	return new ResponseEntity<String>("", HttpStatus.OK);
    }
    
    @RequestMapping(value="/GetSimilarityCallback/{appId}/{taskId}/{userId}", method=RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseEntity<String> similartiyDetectionGetReqsCallback(
    		@PathVariable("appId") final String appId,
    		@PathVariable("taskId") final String taskId,
    		@PathVariable("userId") final String userId,
    		@RequestPart("info") String info, 
    		@RequestPart("result") MultipartFile file,
    		HttpSession httpSession,
    		HttpServletResponse httpServletResponse,
    		HttpServletRequest httpServletRequest) {
	    
    	System.out.println("similartiyDetectionGetReqsCallback: " + info);
    	//JSONObject jo = (JSONObject)new JSONParser().parse(info);
    	
    	byte[] bytes;
		try {
			bytes = file.getBytes();
			String completeData = new String(bytes);
			
			System.out.println("similartiyDetectionGetReqsCallback stream: " + completeData);
			
			DependenciesDto dependencies = new ObjectMapper().readValue(completeData, DependenciesDto.class);
			
			App app = appService.findById(appId).get();
			
			List<DependencyTask> returnTasks = app.getTasks().stream().map(t -> new DependencyTask(t) ).collect(Collectors.toList());
			
			boolean createdActualTask = false;
			DependencyTask zeroTask = null;
			for (DependencyDto d : dependencies.getDependencies()) {

				if (d.getFromid().equals("0") || d.getToid().equals("0")) {
					
					if (!createdActualTask) {
						zeroTask = new DependencyTask();
						zeroTask.setId(0);
						returnTasks.add(zeroTask);
						
						createdActualTask = true;
					}
				}
				
				Optional<DependencyTask> odt = returnTasks.stream().filter(t -> String.valueOf(t.getId()).equals(d.getFromid())).findFirst();
				if (odt != null && odt.isPresent()) {
					Optional<DependencyTask> odt2 = returnTasks.stream().filter(t -> String.valueOf(t.getId()).equals(d.getToid())).findFirst();
					if (odt2 != null && odt2.isPresent()) {
						if (odt.get().getPredictions() == null) {
							odt.get().setPredictions(new ArrayList<Task>());
							odt.get().getPredictions().add(odt2.get());
						} else {
							Optional<Task> odttmp = odt.get().getPredictions().stream().filter(t -> t.getId() == odt2.get().getId()).findFirst();
							if (odttmp == null || !odttmp.isPresent()) {
								odt.get().getPredictions().add(odt2.get());
							}
						}
					}
				}
				
				Optional<DependencyTask> odt3 = returnTasks.stream().filter(t -> String.valueOf(t.getId()).equals(d.getToid())).findFirst();
				if (odt3 != null && odt3.isPresent()) {
					Optional<DependencyTask> odt2 = returnTasks.stream().filter(t -> String.valueOf(t.getId()).equals(d.getFromid())).findFirst();
					if (odt2 != null && odt2.isPresent()) {
						if (odt3.get().getPredictions() == null) {
							odt3.get().setPredictions(new ArrayList<Task>());
							odt3.get().getPredictions().add(odt2.get());
						} else {
							Optional<Task> odttmp = odt3.get().getPredictions().stream().filter(t -> t.getId() == odt2.get().getId()).findFirst();
							if (odttmp == null || !odttmp.isPresent()) {
								odt3.get().getPredictions().add(odt2.get());
							}
						}
					}
				}
			}
			
			final OfflineSpringWebContext ctx = new OfflineSpringWebContext(servletContext, httpSession, httpServletResponse, httpServletRequest);
	        ctx.setVariable("tasks", returnTasks);
	        ctx.setVariable("appId", appId);
	        ctx.setVariable("taskId", taskId);
			
	        final String htmlContent = this.templateEngine.process("fragments/similarityDetectionFragment.html", ctx);
        
	        User user = userService.findUserById(Long.parseLong(userId)).get();
	        
	        simpMessagingTemplate.convertAndSendToUser(user.getEmail(), "/test1", htmlContent);
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    	return new ResponseEntity<String>(info, HttpStatus.OK);
    }
}
