package com.selectionarts.projectcensio.openReqServices.eDemocracy.model.mapper;

import com.selectionarts.projectcensio.model.User;
import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.EDemoUserDto;

public class CensioUserEDemoUserMapper {
	
	public EDemoUserDto getEDemoUser(User u)
	{
		return new EDemoUserDto(0, u.getEmail());
	}

}
