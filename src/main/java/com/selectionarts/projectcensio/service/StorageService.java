package com.selectionarts.projectcensio.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService implements IStorageService {

    @Value( "${censio.uploadDir}" )
    private String uploadDir;
    
	@Override
	public String store(MultipartFile file) {
		
		Path fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

		File directory = new File(fileStorageLocation.toString());
		if (!directory.exists()) {
            directory.mkdir();
        }
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
		Path targetLocation = fileStorageLocation.resolve(fileName);
        
		try {
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return fileName;
	}
	
	@Override
	public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                return null;
            }
        } catch (MalformedURLException ex) {
            return null;
        }
    }
}
