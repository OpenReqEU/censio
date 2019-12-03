package com.selectionarts.projectcensio.openReqServices.eDemocracy.dal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.EDemoParticipationDto;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.EDemoProjectDto;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.EDemoUserDto;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.EdemoVotingDto;
import com.selectionarts.projectcensio.openReqServices.util.GenericRestController;

@Component
public class eDemocracyProvider {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private String eDemoBaseUrl = "http://localhost:9750/api/";
	
	@Autowired
	private GenericRestController<JsonNode> serverCommunication;
	
	public JsonNode performEdemoRegister(EDemoUserDto edemoUserDto) {
		
		String eDemoUrl = eDemoBaseUrl + "users/register";
		
    	serverCommunication.createRequest(JsonNode.class,
    			eDemoUrl,
                HttpMethod.POST,
                edemoUserDto);
                
        return serverCommunication.sendRequest();
	}
	
	public JsonNode performEdemoLogin(EDemoUserDto edemoUserDto) {
		
		String eDemoUrl = eDemoBaseUrl + "users/login";
		
    	serverCommunication.createRequest(JsonNode.class,
    			eDemoUrl,
                HttpMethod.POST,
                edemoUserDto);
                
        return serverCommunication.sendRequest();
	}
	
	public JsonNode performEdemoTokentest(String token) {
		
		String eDemoUrl = eDemoBaseUrl + "users/token_test";
		
    	serverCommunication.createRequest(JsonNode.class,
    			eDemoUrl,
                HttpMethod.GET,
                token);
                
        return serverCommunication.sendRequest(token);
	}
	
	public JsonNode createEdemoProject(EDemoProjectDto edemoProjectDto) {
		
		String eDemoUrl = eDemoBaseUrl + "projects";
		
    	serverCommunication.createRequest(JsonNode.class,
    			eDemoUrl,
                HttpMethod.POST,
                edemoProjectDto);
                
        return serverCommunication.sendRequest();
	}
	
	public JsonNode retrieveEdemoProject(EDemoProjectDto edemoProjectDto) {
		
		String eDemoUrl = eDemoBaseUrl + "projects/"+edemoProjectDto.getId()+"/";
		
    	serverCommunication.createRequest(JsonNode.class,
    			eDemoUrl,
                HttpMethod.GET,
                edemoProjectDto);
                
        return serverCommunication.sendRequest();
	}
	
	public JsonNode retrieveEdemoProjectReport(EDemoProjectDto edemoProjectDto) {
		
		String eDemoUrl = eDemoBaseUrl + "projects/"+edemoProjectDto.getId()+"/report";
		
    	serverCommunication.createRequest(JsonNode.class,
    			eDemoUrl,
                HttpMethod.GET,
                edemoProjectDto);
                
        return serverCommunication.sendRequest();
	}
	
	public JsonNode createEdemoParticipation(EDemoParticipationDto edemoParticipationDto, String token)
	{
		String eDemoUrl = eDemoBaseUrl + "projects/"+edemoParticipationDto.getProject_id()+"/participations/current";
		
    	serverCommunication.createRequest(JsonNode.class,
    			eDemoUrl,
                HttpMethod.PUT,
                edemoParticipationDto);
                
        return serverCommunication.sendRequest(token);
	}
	
	public JsonNode createEdemoVoting(EdemoVotingDto edemoVotingDto, EDemoProjectDto edemoProjectDto, String token)
	{
		String eDemoUrl = eDemoBaseUrl + "projects/"+edemoProjectDto.getId()+"/participations/current/votes";
		
    	serverCommunication.createRequest(JsonNode.class,
    			eDemoUrl,
                HttpMethod.PUT,
                edemoVotingDto);
                
        return serverCommunication.sendRequest(token);
	}
}
