package com.selectionarts.projectcensio.openReqServices.prsImprovingRequirementsQuality.dal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.selectionarts.projectcensio.openReqServices.prsImprovingRequirementsQuality.model.dto.RequirementsDto;
import com.selectionarts.projectcensio.openReqServices.util.GenericRestController;

@Component
public class PrsImprovingRequirementsQualityProvider {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private GenericRestController<JsonNode> serverCommunication;
	
	public JsonNode getImprovedRequirements(RequirementsDto requirementsDto) {
			
		//String addReqsUrl = "https://api.openreq.eu/prs-improving-requirements-quality/check-quality";
		String addReqsUrl = "http://localhost:9799/check-quality";
		
    	serverCommunication.createRequest(JsonNode.class,
    			addReqsUrl,
                HttpMethod.POST,
                requirementsDto);
                
        return serverCommunication.sendRequest();
	}
	
	
}
