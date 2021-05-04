package de.webenm.mdbconnector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class MdbconnectorApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(MdbconnectorApplication.class, args);
	}

}
