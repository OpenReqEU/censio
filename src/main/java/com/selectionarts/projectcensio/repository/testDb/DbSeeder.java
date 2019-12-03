package com.selectionarts.projectcensio.repository.testDb;

import com.selectionarts.projectcensio.model.*;
import com.selectionarts.projectcensio.model.enumeration.CommentType;
import com.selectionarts.projectcensio.model.enumeration.NotificationType;
import com.selectionarts.projectcensio.model.mapperViewEntity.TaskCommentMapper;
import com.selectionarts.projectcensio.repository.*;
import com.selectionarts.projectcensio.service.dbserviceimpl.DBAppService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
@ConditionalOnProperty(name = "noteit.db.recreate", havingValue = "true")
public class DbSeeder implements CommandLineRunner {

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private DBAppService appService;

    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private TaskParticipationRepository participationRepository;

    @Autowired
    private AppRegistrationRepository appRegistrationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskCommentRepository commentRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private CustomNotificationRepository customNotificationRepository;

	@Autowired
	private NotificationRepository notificationRepository;

    @Autowired
    private TaskTypesRepository taskTypesRepository;
	
    @Override
    public void run(String... args) throws Exception {

        // Remove all existing entities
        appRepository.deleteAll();
        taskRepository.deleteAll();


        tugTestData();
    }
    
    private void vote(App app, User user) {
    	
    	List<Task> tasks = this.taskRepository.findAllByApp(app);
    	
    	Random random = new Random();
    	 
    	for (Task t : tasks) {
    		TaskParticipation storedtask = new TaskParticipation();
            storedtask.setRatedat(new Date());
            int val = random.nextInt(2);
            storedtask.setRating(val);
            storedtask.setUser(user);
            storedtask.setTask(t);
            this.participationRepository.save(storedtask);
            
            if (val == 1) {
            	comment(app, user, t);
            }
    	}
    }
    
    private void comment(App app, User user, Task task) {
    	
    	Random random = new Random();
    	int rand = random.nextInt(9);
    	
    	TaskComment taskComment = null;
    			
    	switch(rand) {
    	
    		case 0:
	    		taskComment = new TaskComment("I think this task must be implemented", CommentType.PRO,
	                    task, user, new Date());
	    		break;
        	
        	case 1:
        		taskComment = new TaskComment("A really necessary feature", CommentType.PRO,
                        task, user, new Date());
        		break;
            	
        	case 2:
        		taskComment = new TaskComment("This feature is essential.", CommentType.PRO,
                        task, user, new Date());
        		break;
        	case 3:
	    		taskComment = new TaskComment("Is this feature really needed?", CommentType.NEUTRAL,
	                    task, user, new Date());
	    		break;
        	
        	case 4:
        		taskComment = new TaskComment("I'm not sure, if we need this requirement", CommentType.NEUTRAL,
                        task, user, new Date());
        		break;
            	
        	case 5:
        		taskComment = new TaskComment("Maybe there are more important tasks", CommentType.NEUTRAL,
                        task, user, new Date());
        		break;
        	case 6:
	    		taskComment = new TaskComment("I think there are more important requirements", CommentType.CON,
	                    task, user, new Date());
	    		break;
        	
        	case 7:
        		taskComment = new TaskComment("I'm not sure, if we need this requirement", CommentType.CON,
                        task, user, new Date());
        		break;
            	
        	case 8:
        		taskComment = new TaskComment("I think this feature is useless", CommentType.CON,
                        task, user, new Date());
        		break;
        		
    		default:
    			taskComment = new TaskComment("", CommentType.PRO,
                        task, user, new Date());
    			break;
    	}
        
        this.commentRepository.save(taskComment);
    }
    
    private void tugTestData() {

		User userA = new User("admin@selectionarts.com","Admin", "Admin","ADMIN");
		userA.setPassword(bCryptPasswordEncoder.encode("test"));
		this.userRepository.save(userA);
		
		Notification notification = new Notification();
		notification.setUser(userA);
		this.notificationRepository.save(notification);


        App appSportswatch = new App("Sportswatch", "A new type of watch, 200m waterproof");
        appSportswatch.setImgaeLocation("watch.png");
        appSportswatch.setCreator(userA);
        appService.save(appSportswatch);
        
        Types tCost = new Types("Text", "Costs");
        tCost.setApp(appSportswatch);
        this.taskTypesRepository.save(tCost);
        
        Types tTime = new Types("Date", "Endtdate");
        tTime.setApp(appSportswatch);
        this.taskTypesRepository.save(tTime);

		Task task1 = new Task();
		task1.setTitle("Speed Measurement");
		task1.setCreator(userA);
		task1.setDescription(
				"As evaluation after a workout, the average speed must be shown. The following statistics should be displayed: average speed and maximum speed. For measuring the average and maximum speed, time and distance have to be measured, and a storage unit for storing the data is necessary.");
		task1.setApp(appSportswatch);
		task1.setTaskAdditionalTypes(new ArrayList<String>(Arrays.asList(new String[] {"1000", "27.12.2019"})));
		this.taskRepository.save(task1);

		Task task2 = new Task();
		task2.setCreator(userA);
		task2.setTitle("Ideal BMI");
		task2.setDescription(
				"Based on the data on height, weight, body fat, age and gender, the watch should be able to calculate the ideal BMI for a user.");
		task2.setApp(appSportswatch);
		task2.setTaskAdditionalTypes(new ArrayList<String>(Arrays.asList(new String[] {"2000", "27.12.2019"})));
		this.taskRepository.save(task2);

		Task task3 = new Task();
		task3.setCreator(userA);
		task3.setTitle("Infrared");
		task3.setDescription(
				"In order to be able to connect the watch with a computer, WLAN, Bluetooth, and infrared modules must be available.");
		task3.setApp(appSportswatch);
		task3.setTaskAdditionalTypes(new ArrayList<String>(Arrays.asList(new String[] {"500", "27.12.2019"})));
		this.taskRepository.save(task3);

		Task task4 = new Task();
		task4.setCreator(userA);
		task4.setTitle("Data-Storage Function");
		task4.setDescription(
				"For evaluation purposes, the data should be stored in the internal memory. The memory is used for saving the measured information such as the distance, the height, the average heart rate, and the calorie consumption. The stored data in the memory will then be used by the evaluation software.");
		task4.setApp(appSportswatch);
		task4.setTaskAdditionalTypes(new ArrayList<String>(Arrays.asList(new String[] {"3000", "27.12.2019"})));
		this.taskRepository.save(task4);

		Task task5 = new Task();
		task5.setCreator(userA);
		task5.setTitle("Time Measurement");
		task5.setDescription(
				"The clock must have an internal timer which is used for saving the current time and the measured time during a workout. This time information is then stored in memory and used for further evaluation of the data.");
		task5.setApp(appSportswatch);
		this.taskRepository.save(task5);
		
		Task task6 = new Task();
		task6.setCreator(userA);
		task6.setTitle("Evaluation Software");
		task6.setDescription(
				"For evaluating the recorded training data, an evaluation software is required. This software requires the connection and the access to the clock's internal memory. The evaluation should contain measured information regarding the distance, the height, the average heart rate, and the calorie consumption.");
		task6.setApp(appSportswatch);
		this.taskRepository.save(task6);
/*
		Task task7 = new Task();
		task7.setCreator(userA);
		task7.setTitle("Distance Measurement");
		task7.setDescription(
				"For statistical purposes, a distance measurement is necessary which needs data from a GPS sensor. This data is needed for the evaluation software and therefore stored in memory.");
		task7.setApp(appSportswatch);
		this.taskRepository.save(task7);
*/
		Task task8 = new Task();
		task8.setCreator(userA);
		task8.setTitle("GPS");
		task8.setDescription(
				"To capture position data, a GPS sensor should be used. Through the measured position and time information, the speed and the distance can be measured.");
		task8.setApp(appSportswatch);
		this.taskRepository.save(task8);

		
		User userB = new User("test@selectionarts.com","Michael", "Jeran","USER");
		userB.setPassword(bCryptPasswordEncoder.encode("test"));
		this.userRepository.save(userB);

		Notification notificationuserb = new Notification();
		notificationuserb.setUser(userB);
		this.notificationRepository.save(notificationuserb);
		
		for (int i = 0; i < 30; i++) {
			User useri = new User("user" + i + "@selectionarts.com","User " + i, "","USER");
			useri.setPassword(bCryptPasswordEncoder.encode("test"));
			this.userRepository.save(useri);

			Notification notificationuseri = new Notification();
			notificationuseri.setUser(useri);
			this.notificationRepository.save(notificationuseri);
			
			AppRegistration appRsp = new AppRegistration();
			appRsp.setApp(appSportswatch);
			appRsp.setUser(useri);
			appRsp.setRegisteredAt(new Date());
			this.appRegistrationRepository.save(appRsp);
			
			vote(appSportswatch, useri);
			
			
		}
		
        AppRegistration appRegistration = new AppRegistration();
        appRegistration.setApp(appSportswatch);
        appRegistration.setUser(userA);
        appRegistration.setRegisteredAt(new Date());
		this.appRegistrationRepository.save(appRegistration);


		AppRegistration appRegistration2 = new AppRegistration();
		appRegistration2.setApp(appSportswatch);
		appRegistration2.setUser(userB);
		appRegistration2.setRegisteredAt(new Date());
        this.appRegistrationRepository.save(appRegistration2);
        
        
        // APPS 
        App appStudyBattle = new App("StudyBattles", "Gamification-based e-learning");
        appStudyBattle.setImgaeLocation("studybattles.png");
        appStudyBattle.setCreator(userA);
        this.appService.save(appStudyBattle);
        
        App appRadetzky = new App("DG Radetzkystr.", "Dachgeschoßausbau");
        appRadetzky.setImgaeLocation("dach.png");
        appRadetzky.setCreator(userA);
        this.appService.save(appRadetzky);
        
        App appFmatic = new App("F-MATIC", "Facility management software");
        appFmatic.setImgaeLocation("fmatic.png");
        appFmatic.setCreator(userA);
        this.appService.save(appFmatic);
        
        AppRegistration appRegistrationSB = new AppRegistration();
        appRegistrationSB.setApp(appStudyBattle);
        appRegistrationSB.setUser(userA);
        appRegistrationSB.setRegisteredAt(new Date());
		this.appRegistrationRepository.save(appRegistrationSB);


		AppRegistration appRegistrationSB2 = new AppRegistration();
		appRegistrationSB2.setApp(appStudyBattle);
		appRegistrationSB2.setUser(userB);
		appRegistrationSB2.setRegisteredAt(new Date());
        this.appRegistrationRepository.save(appRegistrationSB2);
        
        AppRegistration appRegistrationRadetzky = new AppRegistration();
        appRegistrationRadetzky.setApp(appRadetzky);
        appRegistrationRadetzky.setUser(userA);
        appRegistrationRadetzky.setRegisteredAt(new Date());
		this.appRegistrationRepository.save(appRegistrationRadetzky);


		AppRegistration appRegistrationRadetzky2 = new AppRegistration();
		appRegistrationRadetzky2.setApp(appRadetzky);
		appRegistrationRadetzky2.setUser(userB);
		appRegistrationRadetzky2.setRegisteredAt(new Date());
        this.appRegistrationRepository.save(appRegistrationRadetzky2);
        
        AppRegistration appRegistrationFmatic = new AppRegistration();
        appRegistrationFmatic.setApp(appFmatic);
        appRegistrationFmatic.setUser(userA);
        appRegistrationFmatic.setRegisteredAt(new Date());
		this.appRegistrationRepository.save(appRegistrationFmatic);


		AppRegistration appRegistrationFmatic2 = new AppRegistration();
		appRegistrationFmatic2.setApp(appFmatic);
		appRegistrationFmatic2.setUser(userB);
		appRegistrationFmatic2.setRegisteredAt(new Date());
        this.appRegistrationRepository.save(appRegistrationFmatic2);

        System.out.println("Initialized database");
    }
}
