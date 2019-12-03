package com.selectionarts.projectcensio.openReqServices.eDemocracy.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.selectionarts.projectcensio.model.App;
import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.dal.eDemocracyProvider;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.entity.EdemocracyParticipation;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.entity.EdemocracyVoting;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.EDemoParticipationDto;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.EDemoProjectDto;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.EDemoReturnProjectDto;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.EDemoTokenDto;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.EDemoUserDto;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.EdemoVotingDto;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.Ticket;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.VotingResultView;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.report.EDemoReport;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.report.EDemoTickets;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.repository.EdemocracyParticipationRepository;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.repository.EdemocracyVotingRepository;
import com.selectionarts.projectcensio.service.EmailService;


@Service
public class EDemocracyService implements IEDemocracyService {

	@Autowired
	private eDemocracyProvider eDemocracyProvider;
	
	@Autowired
	private EdemocracyVotingRepository edemocracyVotingRepository;
	
	@Autowired
	private EdemocracyParticipationRepository edemocracyParticipationRepository;
	
	@Autowired
    private EmailService emailService;
	
    @Value( "${censio.email-link}" )
    private String emaillink;
	
	private String token = null;
	
	public EdemocracyVoting registerEdemocracyUser(List<String> users, App app, Task task) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		List<EDemoUserDto> eDemoUsers = new ArrayList<EDemoUserDto>();
		for (String s : users) {
			
			List<EdemocracyVoting> evs = this.edemocracyVotingRepository.findByParticipationsUsername(s);
			if (evs == null || evs.isEmpty()) {
				JsonNode output = this.eDemocracyProvider.performEdemoRegister(new EDemoUserDto(0, s));
				System.out.println("output edemo, register user:" + output);
				try {
					EDemoUserDto eud = objectMapper.treeToValue(output, EDemoUserDto.class);
					eDemoUsers.add(eud);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			} else {
				Optional<EdemocracyVoting> oev = evs.stream().filter(e -> e.getTask().getId() == task.getId()).findFirst();
				if (oev != null && oev.isPresent()) {
					System.out.println("user exists in this project already");
				} else {
					Optional<EdemocracyParticipation> oep = evs.get(0).getParticipations().stream().filter(p -> p.getUsername().equalsIgnoreCase(s)).findFirst();
					if (oep != null && oep.isPresent()) {
						eDemoUsers.add(new EDemoUserDto(oep.get().getUsernameId(), oep.get().getUsername()));
						System.out.println("user exists in another project, added to list");
					} else {
						System.err.println("user not found");
					}
				}
			}
		}
		
		List<EdemocracyParticipation> eDemoparts = new ArrayList<EdemocracyParticipation>();
		for (EDemoUserDto eDemoUser : eDemoUsers) {
			eDemoparts.add(new EdemocracyParticipation(eDemoUser.getName(), eDemoUser.getId(), UUID.randomUUID().toString()));
		}
		
		EdemocracyVoting edemVoting = this.edemocracyVotingRepository.findByTaskId(task.getId());
		if (edemVoting == null) {
			
			edemVoting = new EdemocracyVoting();
			edemVoting.setParticipations(eDemoparts);
			edemVoting.setTask(task);
			
			EDemoProjectDto edemoProj = new EDemoProjectDto(0, app.getTitle(), "2020-05-29T06:59:46.351Z", "2020-05-29T06:59:46.351Z", null);
			JsonNode jEdemoProj = createProject(edemoProj);
			try {
				EDemoReturnProjectDto creataedEdemoProj = objectMapper.treeToValue(jEdemoProj, EDemoReturnProjectDto.class);
				edemVoting.setEDemoProjectId(creataedEdemoProj.getId());
				
				Optional<Ticket> oTicketYes = creataedEdemoProj.getTickets().stream().filter(t -> t.getTitle().equalsIgnoreCase("yes")).findFirst();
				if (oTicketYes != null && oTicketYes.isPresent()) {
					edemVoting.setYesTicketId(oTicketYes.get().getId());
				}
				
				Optional<Ticket> oTicketNo = creataedEdemoProj.getTickets().stream().filter(t -> t.getTitle().equalsIgnoreCase("no")).findFirst();
				if (oTicketNo != null && oTicketNo.isPresent()) {
					edemVoting.setNoTicketId(oTicketNo.get().getId());
				}
				
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		} else {
			edemVoting.getParticipations().addAll(eDemoparts);
		}
		
		this.edemocracyParticipationRepository.saveAll(eDemoparts);
		edemocracyVotingRepository.save(edemVoting);
		
		String message = "";
		try {
			
			InputStream input = new ClassPathResource("static/assets/mailTemplate/mailVoting.html").getInputStream();
			message = IOUtils.toString(input);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (EdemocracyParticipation eDemopart : eDemoparts) {
			
			String msgTmp = message;
			
			msgTmp = msgTmp.replace("###PROJECT###", app.getTitle());
			msgTmp = msgTmp.replace("###TASK###", task.getTitle());
			msgTmp = msgTmp.replace("###URL###", emaillink + "eDemocracy/" + eDemopart.getUuid());
			
			emailService.sendEmailAsync(eDemopart.getUsername(), "[Censio] Voting invitation", msgTmp);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return edemVoting;
	}
	
	public EdemocracyVoting createEmptyVoting(Task task) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		EdemocracyVoting edemVoting = new EdemocracyVoting();
		edemVoting.setTask(task);
		
		EDemoProjectDto edemoProj = new EDemoProjectDto(0, task.getApp().getTitle(), "2020-05-29T06:59:46.351Z", "2020-05-29T06:59:46.351Z", null);
		JsonNode jEdemoProj = createProject(edemoProj);

		try {
			EDemoReturnProjectDto creataedEdemoProj = objectMapper.treeToValue(jEdemoProj, EDemoReturnProjectDto.class);
			edemVoting.setEDemoProjectId(creataedEdemoProj.getId());
			
			Optional<Ticket> oTicketYes = creataedEdemoProj.getTickets().stream().filter(t -> t.getTitle().equalsIgnoreCase("yes")).findFirst();
			if (oTicketYes != null && oTicketYes.isPresent()) {
				edemVoting.setYesTicketId(oTicketYes.get().getId());
			}
			
			Optional<Ticket> oTicketNo = creataedEdemoProj.getTickets().stream().filter(t -> t.getTitle().equalsIgnoreCase("no")).findFirst();
			if (oTicketNo != null && oTicketNo.isPresent()) {
				edemVoting.setNoTicketId(oTicketNo.get().getId());
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		edemocracyVotingRepository.save(edemVoting);
		
		return edemVoting;
	}
	
	public void publicVote(String publicUuid, String nickname, App app, Boolean thumbsUp) {
		
		//nickname = nickname + UUID.randomUUID().toString();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss.SSS");  
		LocalDateTime now = LocalDateTime.now();  
		
		ObjectMapper objectMapper = new ObjectMapper(); 
		
		EdemocracyVoting edemocracyVoting = this.edemocracyVotingRepository.findByPublicUuid(publicUuid);
		
		JsonNode output = this.eDemocracyProvider.performEdemoRegister(new EDemoUserDto(0, nickname + " (" + dtf.format(now) + ")"));
		System.out.println("output edemo, register user:" + output);
		EDemoUserDto eud = null;
		try {
			eud = objectMapper.treeToValue(output, EDemoUserDto.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		EdemocracyParticipation edemocracyParticipation = new EdemocracyParticipation(nickname, eud.getId(), UUID.randomUUID().toString());
		this.edemocracyParticipationRepository.save(edemocracyParticipation);
		if (edemocracyVoting.getParticipations() == null) {
			edemocracyVoting.setParticipations(new ArrayList<EdemocracyParticipation>());
		}
		edemocracyVoting.getParticipations().add(edemocracyParticipation);
		this.edemocracyVotingRepository.save(edemocracyVoting);
		
		
		JsonNode loginOutput = this.eDemocracyProvider.performEdemoLogin(new EDemoUserDto(eud.getId(), eud.getName()));
		
		System.out.println("output edemo, login user:" + loginOutput);
		
		EDemoTokenDto eDemoTokenDto = null;
		try {
			eDemoTokenDto = objectMapper.treeToValue(loginOutput, EDemoTokenDto.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		EDemoProjectDto edemoProj = new EDemoProjectDto(edemocracyVoting.getEDemoProjectId(), app.getTitle(), "2020-05-29T06:59:46.351Z", "2020-05-29T06:59:46.351Z", null);
		
		EDemoParticipationDto edemoParticipationDto = new EDemoParticipationDto();
		
		edemoParticipationDto.setProject_id(edemocracyVoting.getEDemoProjectId());
		edemoParticipationDto.setUser_id(eud.getId());
		edemoParticipationDto.setRole("user");
		
		JsonNode outputPart = this.eDemocracyProvider.createEdemoParticipation(edemoParticipationDto, eDemoTokenDto.getToken());
		
		System.out.println("output edemo, createEdemoParticipation:" + outputPart);
		
		edemoParticipationDto.setRole("candidate");
		
		JsonNode outputCand = this.eDemocracyProvider.createEdemoParticipation(edemoParticipationDto, eDemoTokenDto.getToken());
		
		System.out.println("output edemo, createEdemoParticipation:" + outputCand);
		
		int[] voteArr = new int[1];
		voteArr[0] = thumbsUp ? edemocracyVoting.getYesTicketId() : edemocracyVoting.getNoTicketId();
		EdemoVotingDto voting = new EdemoVotingDto(voteArr);
		 
		JsonNode outputCoting = this.eDemocracyProvider.createEdemoVoting(voting, edemoProj, eDemoTokenDto.getToken());
		
		System.out.println("output edemo, createEdemoVoting:" + outputCoting);
		
		JsonNode outputProject = this.eDemocracyProvider.retrieveEdemoProjectReport(edemoProj);
		
		System.out.println("output edemo, retrieve project report:" + outputProject);
	}
	
	public void vote(String uuid, App app, Boolean thumbsUp) {
		
		EdemocracyVoting edemocracyVoting = this.edemocracyVotingRepository.findByParticipationsUuid(uuid);
		EdemocracyParticipation eDemopart = this.edemocracyParticipationRepository.findByUuid(uuid);
		
		JsonNode output = this.eDemocracyProvider.performEdemoLogin(new EDemoUserDto(eDemopart.getUsernameId(), eDemopart.getUsername()));
		
		System.out.println("output edemo, login user:" + output);
		
		ObjectMapper objectMapper = new ObjectMapper();
		EDemoTokenDto eDemoTokenDto = null;
		try {
			eDemoTokenDto = objectMapper.treeToValue(output, EDemoTokenDto.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		EDemoProjectDto edemoProj = new EDemoProjectDto(edemocracyVoting.getEDemoProjectId(), app.getTitle(), "2020-05-29T06:59:46.351Z", "2020-05-29T06:59:46.351Z", null);
		
		EDemoParticipationDto edemoParticipationDto = new EDemoParticipationDto();
		
		edemoParticipationDto.setProject_id(edemocracyVoting.getEDemoProjectId());
		edemoParticipationDto.setUser_id(eDemopart.getUsernameId());
		edemoParticipationDto.setRole("user");
		
		JsonNode outputPart = this.eDemocracyProvider.createEdemoParticipation(edemoParticipationDto, eDemoTokenDto.getToken());
		
		System.out.println("output edemo, createEdemoParticipation:" + outputPart);
		
		edemoParticipationDto.setRole("candidate");
		
		JsonNode outputCand = this.eDemocracyProvider.createEdemoParticipation(edemoParticipationDto, eDemoTokenDto.getToken());
		
		System.out.println("output edemo, createEdemoParticipation:" + outputCand);
		
		int[] voteArr = new int[1];
		voteArr[0] = thumbsUp ? edemocracyVoting.getYesTicketId() : edemocracyVoting.getNoTicketId();
		EdemoVotingDto voting = new EdemoVotingDto(voteArr);
		 
		JsonNode outputCoting = this.eDemocracyProvider.createEdemoVoting(voting, edemoProj, eDemoTokenDto.getToken());
		
		System.out.println("output edemo, createEdemoVoting:" + outputCoting);
		
		JsonNode outputProject = this.eDemocracyProvider.retrieveEdemoProjectReport(edemoProj);
		
		System.out.println("output edemo, retrieve project report:" + outputProject);
	}
	
	public EdemocracyVoting getByUserUUID(String uuid) {
		
		EdemocracyVoting edemocracyVoting = this.edemocracyVotingRepository.findByParticipationsUuid(uuid);
		
		return edemocracyVoting;
	}

	@Override
	public JsonNode registerEdemocracyUser(EDemoUserDto user) {
		
		JsonNode output = this.eDemocracyProvider.performEdemoRegister(user);
		
		System.out.println("output edemo, register user:" + output);
		
		return output;
	}
	
	@Override
	public String loginEdemocracyUser(EDemoUserDto user) {
		
		JsonNode output = this.eDemocracyProvider.performEdemoLogin(user);
		
		System.out.println("output edemo, login user:" + output);
		
		for (Iterator<Map.Entry<String, JsonNode>> iter = output.fields(); iter.hasNext(); ) 
		{
			
			Map.Entry<String, JsonNode> n = iter.next();
			if(n.getKey().equals("token"))
			{
				this.token = n.getValue().asText();
			} else {
				this.token = "error";
			}
			System.out.println("output edemo login key: " + n.getKey());
			System.out.println("output edemo login val: " + n.getValue());
		}
		
		return token;
	}
	
	@Override
	public boolean tokenTest() {
		
		JsonNode output = this.eDemocracyProvider.performEdemoTokentest(this.token);
		
		System.out.println("Token test:" + output);
		
		return false;
	}
	
	public JsonNode createProject(EDemoProjectDto project)
	{
		
		JsonNode output = this.eDemocracyProvider.createEdemoProject(project);
		
		System.out.println("output edemo, create project:" + output);
		
		return output;
	}
	
	public JsonNode retrieveProject(EDemoProjectDto project)
	{
		JsonNode output = this.eDemocracyProvider.retrieveEdemoProject(project);
		
		System.out.println("output edemo, retrieve project:" + output);
		
		return output;
	}
	
	public JsonNode retrieveProjectReport(EDemoProjectDto project)
	{
		JsonNode output = this.eDemocracyProvider.retrieveEdemoProjectReport(project);
		
		System.out.println("output edemo, retrieve project report:" + output);
		
		return output;
	}
	
	public VotingResultView retrieveProjectReport(int projectId)
	{
		EDemoProjectDto edemoProj = new EDemoProjectDto(projectId, "", "2020-05-29T06:59:46.351Z", "2020-05-29T06:59:46.351Z", null);
		
		JsonNode output = this.eDemocracyProvider.retrieveEdemoProjectReport(edemoProj);
		
		System.out.println("output edemo, retrieve project report:" + output);
		
		ObjectMapper objectMapper = new ObjectMapper();
		VotingResultView votingResultView = new VotingResultView(0, 0, 0);
		try {
			EDemoReport eDemoReport = objectMapper.treeToValue(output, EDemoReport.class);
			
			EdemocracyVoting edemocracyVoting = this.edemocracyVotingRepository.findByEDemoProjectId(projectId);
			votingResultView.setNotVoted(edemocracyVoting.getParticipations().size());
			
			for (EDemoTickets et : eDemoReport.getVotes().getTickets()) {
				if (et.getTicket().getId() == edemocracyVoting.getNoTicketId()) {
					votingResultView.setNoVotes(et.getVotes_received());
				}
				
				if (et.getTicket().getId() == edemocracyVoting.getYesTicketId()) {
					votingResultView.setYesVotes(et.getVotes_received());
				}
			}
			int notVoted = edemocracyVoting.getParticipations().size() - (votingResultView.getNoVotes() + votingResultView.getYesVotes());
			votingResultView.setNotVoted(notVoted);
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return votingResultView;
	}
	
	public JsonNode createParticipation(EDemoProjectDto project,EDemoUserDto user, String role)
	{
		EDemoParticipationDto edemoParticipationDto = new EDemoParticipationDto();
		
		edemoParticipationDto.setProject_id(project.getId());
		edemoParticipationDto.setUser_id(user.getId());
		edemoParticipationDto.setRole(role);
		
		JsonNode output = this.eDemocracyProvider.createEdemoParticipation(edemoParticipationDto, this.token);
		
		System.out.println("output edemo, create participation:" + output);
		
		return output;
	}
	
	public JsonNode createVoting(EdemoVotingDto voting,EDemoProjectDto project)
	{
		JsonNode output = this.eDemocracyProvider.createEdemoVoting(voting,project, this.token);
		
		System.out.println("output edemo, create voting:" + output);
		
		return output;
	}
	
}
