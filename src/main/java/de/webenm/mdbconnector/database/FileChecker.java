package de.webenm.mdbconnector.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileChecker {

	@Value("${dbfile.directory}")
	private String directory;
	
	public boolean checkFilename(String file) {
		return !file.contains("/") && !file.contains("\\");
	}
	
	public String getFilePath(String file) {
		return directory + file;
	}
}
