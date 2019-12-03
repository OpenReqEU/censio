package com.selectionarts.projectcensio.openReqServices.similarityDetection.dal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.selectionarts.projectcensio.openReqServices.similarityDetection.model.SimilarityResponseDto;
import com.selectionarts.projectcensio.openReqServices.similarityDetection.model.JsonProjectDto;
import com.selectionarts.projectcensio.openReqServices.similarityDetection.model.RequirementsDto;
import com.selectionarts.projectcensio.openReqServices.util.GenericRestController;

@Component
public class SimilartiyDetectionProvider {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private GenericRestController<SimilarityResponseDto> serverCommunication;
	
	public SimilarityResponseDto addReqs(RequirementsDto requirementsDto, String callbackUrl) {
			
		//String addReqsUrl = "https://api.openreq.eu/similarity-detection/upc/similarity-detection/DB/AddReqs?url=";
		String addReqsUrl = "http://localhost:9404/upc/similarity-detection/AddReqs?url=";
		
		addReqsUrl += callbackUrl;
		addReqsUrl += "&organization=selectionarts";
		
    	serverCommunication.createRequest(SimilarityResponseDto.class,
    			addReqsUrl,
                HttpMethod.POST,
                requirementsDto);
                
        return serverCommunication.sendRequest();
	}
	
	public SimilarityResponseDto getSimilarities(JsonProjectDto j, String callbackUrl, String projectId, String threshold) {

		//String getSimilatiryUrl = "https://api.openreq.eu/similarity-detection/upc/similarity-detection/Project?url=";
		String getSimilatiryUrl = "http://localhost:9404/upc/similarity-detection/Project?url=";
		
		getSimilatiryUrl += callbackUrl;
		
		getSimilatiryUrl += "&project=" + projectId;
		getSimilatiryUrl += "&threshold=" + threshold;
		getSimilatiryUrl += "&organization=selectionarts";

		serverCommunication.createRequest(
				SimilarityResponseDto.class, 
				getSimilatiryUrl, 
				HttpMethod.POST, 
				j);

		return serverCommunication.sendRequest();
	}
}
