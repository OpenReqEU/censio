package com.selectionarts.projectcensio.openReqServices.crossReferenceDetection.dal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.selectionarts.projectcensio.openReqServices.crossReferenceDetection.model.JsonProjectsReqsDepDto;
import com.selectionarts.projectcensio.openReqServices.crossReferenceDetection.model.JsonProjectsReqsDto;
import com.selectionarts.projectcensio.openReqServices.util.GenericRestController;

@Component
public class CrossReferenceDetectionProvider {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private GenericRestController<JsonProjectsReqsDepDto> serverCommunication;
	
	public JsonProjectsReqsDepDto getCrossReferences(JsonProjectsReqsDto j, String projectId) {

		//String url = "https://api.openreq.eu/cross-reference-detection/upc/cross-reference-detection/json/";
		String url = "http://localhost:9401/upc/cross-reference-detection/json/";
		url += projectId;

		serverCommunication.createRequest(
				JsonProjectsReqsDepDto.class, 
				url, 
				HttpMethod.POST, 
				j);

		return serverCommunication.sendRequest();
	}
}
