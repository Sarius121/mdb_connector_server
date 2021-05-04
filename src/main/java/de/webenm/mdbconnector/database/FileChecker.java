package de.webenm.mdbconnector.database;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileChecker {

	private String directory;
	
	public FileChecker() {
		try {
			FileInputStream inputStream = new FileInputStream("src/main/resources/application.properties");
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = br.readLine()) != null) {
			    String[] property = line.split("=");
			    if(property[0].equals("dbfile.directory")) {
			    	this.directory = property[1];
			    	break;
			    }
			}
			br.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean checkFilename(String file) {
		return !file.contains("/") && !file.contains("\\");
	}
	
	public String getFilePath(String file) {
		return directory + file;
	}
}
