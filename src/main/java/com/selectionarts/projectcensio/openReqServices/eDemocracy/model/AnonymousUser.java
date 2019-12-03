package com.selectionarts.projectcensio.openReqServices.eDemocracy.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class AnonymousUser {

	@NotNull
	@Size(min=2, max=30)
	private String nickname;
}
