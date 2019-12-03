package com.selectionarts.projectcensio.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {

	public String store(MultipartFile file);
	
	public Resource loadFileAsResource(String fileName);
}
