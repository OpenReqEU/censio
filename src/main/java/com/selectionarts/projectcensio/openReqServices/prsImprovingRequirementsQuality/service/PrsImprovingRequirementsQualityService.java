package com.selectionarts.projectcensio.openReqServices.prsImprovingRequirementsQuality.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.selectionarts.projectcensio.model.Task;
import com.selectionarts.projectcensio.openReqServices.prsImprovingRequirementsQuality.dal.PrsImprovingRequirementsQualityProvider;
import com.selectionarts.projectcensio.openReqServices.prsImprovingRequirementsQuality.model.bo.DescriptionPart;
import com.selectionarts.projectcensio.openReqServices.prsImprovingRequirementsQuality.model.bo.ImprovementTask;
import com.selectionarts.projectcensio.openReqServices.prsImprovingRequirementsQuality.model.dto.Improvement;
import com.selectionarts.projectcensio.openReqServices.prsImprovingRequirementsQuality.model.dto.RequirementsDto;
import com.selectionarts.projectcensio.openReqServices.prsImprovingRequirementsQuality.model.mapper.RequirementTaskMapper;

@Service
public class PrsImprovingRequirementsQualityService implements IPrsImprovingRequirementsQualityService {

	@Autowired
	private PrsImprovingRequirementsQualityProvider prsImprovingRequirementsQualityProvider;
	
	private static String[] colors = {
			"#000000",
			"#FF0000",
			"#800000",
			"#808000",
			"#00FF00",
			"#008000",
			"#00FFFF",
			"#008080",
			"#0000FF",
			"#000080",
			"#FF00FF",
			"#800080"};
	
	@Override
	public List<ImprovementTask> getImprovedRequirements(Set<Task> tasks) {
		
		List<ImprovementTask> returnList = new ArrayList<ImprovementTask>();
		ObjectMapper om = new ObjectMapper();
		
		RequirementsDto requirements = new RequirementsDto();
		requirements.setRequirements(RequirementTaskMapper.getRequirements(tasks));
		
		JsonNode output = this.prsImprovingRequirementsQualityProvider.getImprovedRequirements(requirements);
		
		for (Iterator<Map.Entry<String, JsonNode>> iter = output.fields(); iter.hasNext(); ) {
			
			Map.Entry<String, JsonNode> n = iter.next();
			
			System.out.println("output prs: " + n.getKey());
			
			Optional<Task> optTask = tasks.stream().filter(t -> String.valueOf(t.getId()).equals(n.getKey())).findFirst();
			if (optTask != null && optTask.isPresent()) {
				
				ImprovementTask it = new ImprovementTask(optTask.get());
				returnList.add(it);
				
				List<DescriptionPart> descriptionParts = new ArrayList<DescriptionPart>();
				it.setDescriptionParts(descriptionParts);
				
				
				List<Improvement> improvements = new ArrayList<Improvement>();
				it.setImprovements(improvements);
			
				if (n.getValue().isArray()) {
					int colorindex = 0;
					for (int valIndex = 0; valIndex < n.getValue().size(); valIndex++) {
						JsonNode j = n.getValue().get(valIndex);
						System.out.println("  value: " + j.toString());
						try {
							Improvement i = om.readValue(j.toString(), Improvement.class);
							
							Optional<Improvement> oImp = improvements.stream().filter(im -> im.getIndex_end() >= i.getIndex_start()).findFirst();
							if (oImp == null || !oImp.isPresent()) {
								i.setColor(colors[colorindex]);
								improvements.add(i);
								
								if (valIndex == 0) {
									if (i.getIndex_start() != 0) {
										descriptionParts.add(new DescriptionPart(it.getDescription().substring(0, i.getIndex_start()), "#626262"));
									}
								} else {
									Improvement lastI = om.readValue(n.getValue().get(valIndex -1).toString(), Improvement.class);
									if (lastI.getIndex_end() != i.getIndex_start()) {
										descriptionParts.add(new DescriptionPart(it.getDescription().substring(lastI.getIndex_end(), i.getIndex_start()), "#626262"));
									}
								}
								
								descriptionParts.add(new DescriptionPart(i.getText(), colors[colorindex]));
								
								colorindex = (colorindex + 1 >= colors.length ? 0 : colorindex + 1);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (improvements.size() > 0) {
						descriptionParts.add(new DescriptionPart(it.getDescription().substring(improvements.get(improvements.size()-1).getIndex_end()), "#626262"));
					}
				}
			}
		}
		
		return returnList;
	}
}
