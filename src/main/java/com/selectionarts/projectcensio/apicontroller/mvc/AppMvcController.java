package com.selectionarts.projectcensio.apicontroller.mvc;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.selectionarts.projectcensio.apicontroller.viewmodel.AppViewModel;
import com.selectionarts.projectcensio.apicontroller.viewmodel.UserViewModel;
import com.selectionarts.projectcensio.model.*;
import com.selectionarts.projectcensio.model.mapperViewEntity.AppMapper;
import com.selectionarts.projectcensio.model.mapperViewEntity.UserMapper;
import com.selectionarts.projectcensio.repository.AppRegistrationRepository;
import com.selectionarts.projectcensio.repository.NotificationRepository;
import com.selectionarts.projectcensio.repository.TaskTypesRepository;
import com.selectionarts.projectcensio.repository.UserRepository;
import com.selectionarts.projectcensio.service.EmailService;
import com.selectionarts.projectcensio.service.IStorageService;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBAppService;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBTaskService;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBUserService;
import com.selectionarts.projectcensio.util.generator.PDFGenerator;
import com.selectionarts.projectcensio.util.generator.PasswordGenerator;
import lombok.var;
import net.bytebuddy.asm.Advice;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import java.io.*;
import java.net.MalformedURLException;
import java.sql.SQLOutput;
import java.util.*;
import java.util.List;

@Controller
@Transactional
@RequestMapping("/apps")
@SessionAttributes("createApp")
public class AppMvcController {

    @Autowired
    private DBAppService appService;

    @Autowired
    private DBUserService userService;


    @Autowired
    private DBTaskService taskService;

    @Autowired
    private AppRegistrationRepository appRegistrationRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private static String UPLOADED_FOLDER = "src/main/resources/public/";

    @ModelAttribute("createApp")
    public AppViewModel createApp() {
        return new AppViewModel();
    }

    @Value( "${censio.email-link}" )
    private String emaillink;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private IStorageService storageService;


    @GetMapping("/{id}")
    public String getApp(@PathVariable("id") String id, 
    		@RequestParam("page") Optional<Integer> page, 
    	    @RequestParam("size") Optional<Integer> size,
    	    @RequestParam("sort") Optional<String> sort,
    	    @RequestParam("direction") Optional<String> direction,
    		Model model, Authentication authentication)
    {
        App app = appService.findById(id).get();

        long userid = ((UserDetailsDbo)authentication.getPrincipal()).getId();
        User user = userService.findUserById(userid).get();
        
        AppViewModel appViewModel = null;
        if (page.isPresent() && size.isPresent()) {
        	Sort s = Sort.by("title").ascending();
        	if (sort.isPresent()) {
        		if (direction.isPresent()) {
        			if (direction.get().equalsIgnoreCase("asc")) {
        				s = Sort.by(sort.get()).ascending();
            		} else {
            			s = Sort.by(sort.get()).descending();
            		}
        		} else {
        			s = Sort.by(sort.get()).ascending();
        		}
        	}
        	List<Task> tasks = taskService.findAllByApp(app, PageRequest.of(page.get(), size.get(), s));
        	appViewModel = AppMapper.convertToAppViewModel(app, tasks, user);
        	
        	long numberOfTasks = taskService.getNumberOfTasks(app);
        	model.addAttribute("count", numberOfTasks);
        	model.addAttribute("page", page.get());
        	model.addAttribute("size", size.get());
        } else {
        	Set<Task> tasks = taskService.findAllByApp(app);
        	appViewModel = AppMapper.convertToAppViewModel(app, tasks, user);
        }
        
        model.addAttribute("app", appViewModel);
        return "adminview";
    }
    
    @GetMapping("/create")
    public String createApp( Model model)
    {
        AppViewModel appViewModel = new AppViewModel();
        appViewModel.setUpdate(false);
        model.addAttribute("createApp", appViewModel);

        return "create";
    }


    @GetMapping("/update")
    public String update( @RequestParam(name="id") String id, Model model)
    {

        App app = appService.findById(id).get();
        AppViewModel appViewModel = AppMapper.convertToAppViewModel(app, new TreeSet<>());
        appViewModel.setUpdate(true);
        model.addAttribute("createApp", appViewModel);
        return "create";
    }

    @PostMapping("/create")
    public String submitNewApp(@ModelAttribute("createApp") AppViewModel appViewModel, 
    		@RequestParam("file") MultipartFile file,
    		Authentication authentication)
    {

        App app = AppMapper.convertToAppEntity(appViewModel);

        if (authentication.getPrincipal() instanceof UserDetailsDbo) {


            long id = ((UserDetailsDbo)authentication.getPrincipal()).getId();
            User user = userService.findUserById(id).get();
            app.setCreator(user);
            
            if (file != null && !file.isEmpty()) {
            	String filepath = this.storageService.store(file);
            	app.setImgaeLocation(filepath);
            }

            if(Long.valueOf(appViewModel.getId()) != 0)
            {
                appService.updateApp(app, appViewModel);
            }
            else
            {
                appService.createApp(app, appViewModel);
                AppRegistration appRegistration = new AppRegistration();
                appRegistration.setApp(app);
                appRegistration.setUser(user);
                appRegistration.setRegisteredAt(new Date());
                this.appRegistrationRepository.save(appRegistration);

            }
        } else {
            return "redirect:/login";
        }


        return "redirect:/apps/" + app.getId();
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = this.storageService.loadFileAsResource(filename);
        
        if (file != null) {
        	return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        } else {
        	return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"\"").body(file);
        }
        
        
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public String addUserToLearningApplication(@SessionAttribute("createApp") AppViewModel
                                                       createApp, @RequestParam("user")
                                                       String user, Model model)
    {
        if(user != null && !user.isEmpty())
        {
            createApp.getUserViewModelSet().add( UserViewModel.builder()
                    .email(user)
                    .firstName("")
                    .lastName("")
                    .newuser(true)
                    .build());
        }

        model.addAttribute("createApp", createApp);

        return "fragments/userlistfragment :: userlist";
    }


    @RequestMapping(value = "/addTask", method = RequestMethod.POST)
    public String addTaskRow(@SessionAttribute("createApp") AppViewModel createApp, 
    		@RequestParam("information") String information, 
    		Model model) {

        System.out.println("Jumped here");
        if(information != null && !information.isEmpty())
        {
            createApp.getTaskAdditionalTypes().add(new Types("Text",information));
        }

        model.addAttribute("createApp", createApp);

        return "fragments/userlistfragment :: tasklist";
    }


    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String handleDelete(@RequestParam(name="id")String id) {

        App app = appService.findById(id).get();
        app.setEnabled(false);

        appService.save(app);

        return "redirect:/dashboard";
    }


    @RequestMapping(value = "/removeUser", method = RequestMethod.POST)
    public String removeUserFromLearningApplication( @RequestParam("user") String name, @SessionAttribute("createApp") AppViewModel
            createApp, Model model  )
    {

        System.out.println("Called Method in remove USer");

        User usertodelete = userService.findByEmail(name);

        if(usertodelete != null && appService.findById(Long.toString(createApp.getId())).isPresent())
        {
            App app = appService.findById(Long.toString(createApp.getId())).get();
            try {
                appRegistrationRepository.deleteAppRegistrationByAppAndUser(app, usertodelete);
            }catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        if(createApp != null)
        {
            if(name != null && !name.isEmpty())
            {
                createApp.getUserViewModelSet().removeIf(p->p.getEmail().equals(name));
            }
            model.addAttribute("createApp", createApp);
        }
        return "fragments/userlistfragment :: userlist";
    }

    @RequestMapping(value = "/resendEmail", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void sendInventation( @RequestParam("user") String name, @SessionAttribute("createApp") AppViewModel
            createApp, Model model   )
    {
        User usertosend = userService.findByEmail(name);
        App app = appService.findById(Long.toString(createApp.getId())).get();


        if(usertosend != null && app != null )
        {
            String message = "";
            try {

                InputStream input = new ClassPathResource("static/assets/mailTemplate/mailUserResend.html").getInputStream();
                message = IOUtils.toString(input);

                message = message.replace("###PROJECT###", app.getTitle());
                message = message.replace("###URL###", emaillink + "apps/"+app.getId());

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            emailService.sendEmailAsync(usertosend.getEmail(),"[Censio] Added to "+app.getTitle(), message);

        }
    }


    @ResponseBody
    @RequestMapping(value = "/pdfreport", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> appReport(@RequestParam("id") String appid, Authentication authentication) {

        App app = appService.findById(appid).get();

        long userid = ((UserDetailsDbo)authentication.getPrincipal()).getId();
        User user = userService.findUserById(userid).get();

        AppViewModel appViewModel = null;
        Set<Task> tasks = taskService.findAllByApp(app);

        appViewModel = AppMapper.convertToAppViewModel(app, tasks, user);


        ByteArrayInputStream bis = PDFGenerator.Report(appViewModel);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=appreport.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }



}
