package com.selectionarts.projectcensio.openReqServices.dependencyDetection.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.selectionarts.projectcensio.openReqServices.dependencyDetection.model.dto.RequirementDto;
import com.selectionarts.projectcensio.openReqServices.util.GenericRestController;

@Component
public class DependencyDetectionProvider {

	@Autowired
	private GenericRestController<RequirementDto[]> serverCommunication;
	
	private static final String TUG_SERVICE_PROTOCOL = "http://";
    private static final String TUG_SERVICE_HOST = "localhost"; //"217.172.12.199";
    private static final int TUG_REQUIREMENT_DEPENDENCY_RECOMMENDATION_SERVICE_PORT = 9007;
    
	public RequirementDto[] getDependencies(RequirementDto[] requirementsArray) {
	
    	serverCommunication.createRequest(RequirementDto[].class,
				TUG_SERVICE_PROTOCOL + TUG_SERVICE_HOST + ":" +
				TUG_REQUIREMENT_DEPENDENCY_RECOMMENDATION_SERVICE_PORT + 
				"/v1/recommend",
                HttpMethod.POST,
                requirementsArray);
                
        return serverCommunication.sendRequest();
	}
}
