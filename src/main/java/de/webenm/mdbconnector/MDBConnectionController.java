package de.webenm.mdbconnector;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.webenm.mdbconnector.database.FileChecker;
import de.webenm.mdbconnector.database.MDBConnector;

@Component
@RestController
public class MDBConnectionController {

	Map<Integer, MDBConnector> connections = new HashMap<>();
	Map<String, Integer> connectionFiles = new HashMap<String, Integer>();
	
	@Autowired
	FileChecker fileChecker;
	
	@Value("${mdbconnector.apisecret}")
	private String apisecret;
	
	@PostMapping("/mdbconnect")
	public String connect(@RequestParam(value = "apisecret") String apisecret, @RequestParam(value = "file") String file, @RequestParam(value = "password") String password, @RequestParam(value = "debug", defaultValue = "false") String debugStr) {
		this.checkPermission(apisecret);
		
		boolean debug = Boolean.valueOf(debugStr);
		
		if(debug) {
			System.out.println("mdbconnect");
		}
		
		if(this.fileChecker.checkFilename(file)) {
			file = this.fileChecker.getFilePath(file);
			
			MDBConnector mdb = new MDBConnector(debug);
			
			boolean result = mdb.connect(file, password);
			if(result) {
				Random random = new Random();
				int uniqID;
				do{
					uniqID = 100000 + random.nextInt(899999);
				} while(this.connections.containsKey(uniqID));
				this.connections.put(uniqID, mdb);
				this.connectionFiles.put(file, uniqID);
				return "{ \"success\": true, \"connID\": " + String.valueOf(uniqID) + "}";
			}
		}
		
		return "{ \"success\": false }";
	}
	
	@PostMapping("/mdbexecute")
	public String execute(@RequestParam(value = "apisecret") String apisecret, @RequestParam(value = "connID") String idStr, @RequestParam(value = "method") String method, @RequestParam(value = "sql") String sql,
			@RequestParam(value = "dict", defaultValue = "false") String dictStr) {
		this.checkPermission(apisecret);
		try {
			int id = Integer.parseInt(idStr);
			boolean dict = Boolean.valueOf(dictStr);
			
			if(this.connections.containsKey(id)) {
				if(this.connections.get(id).isDebug()) {
					System.out.println("mdbexecute");
				}
				if(method.equals(MDBConnector.METHOD_QUERY)) {
					String result = this.connections.get(id).query(sql, dict);
					if(result != null) {
						return "{ \"success\": true, \"data\": " + result + " }";
					}
				} else if(method.equals(MDBConnector.METHOD_EXECUTE)) {
					boolean result = this.connections.get(id).execute(sql);
					System.out.println(result);
					return "{ \"success\": " + result + "}";
				}
			}
		} catch(Exception e) {
			
		}
		
		return "{ \"success\": false }";
	}
	
	@PostMapping(value = "/mdbclose", params = "connID")
	public String close(@RequestParam(value = "apisecret") String apisecret, @RequestParam(value = "connID") String idStr) {
		this.checkPermission(apisecret);
		boolean success = false;
		try {
			int id = Integer.parseInt(idStr);
			if(this.connections.containsKey(id)) {
				if(this.connections.get(id).isDebug()) {
					System.out.println("mdbclose");
				}
				success = this.connections.get(id).close();
				if(success) {
					this.connectionFiles.remove(this.connections.get(id).getFile());
					this.connections.remove(id);
				}
			}
		} catch(Exception e) {
			
		}
		System.out.println(success);
		return "{ \"success\": " + success + "}";
	}
	
	@PostMapping(value = "/mdbclose", params = "file")
	public String closeWithoutID(@RequestParam(value = "apisecret") String apisecret, @RequestParam(value = "file") String file) {
		this.checkPermission(apisecret);
		boolean success = false;
		if(this.fileChecker.checkFilename(file)) {
			file = this.fileChecker.getFilePath(file);
			if(this.connectionFiles.containsKey(file)) {
				int id = this.connectionFiles.get(file);
				if(this.connections.get(id).isDebug()) {
					System.out.println("mdbclose");
				}
				try {
					success = this.connections.get(id).close();
					if(success) {
						this.connections.remove(id);
						this.connectionFiles.remove(file);
					}
				} catch(Exception e) {
					
				}
			}
		}
		return "{ \"success\": " + success + "}";
	}
	
	public boolean checkPermission(String apisecret) {
		if(this.apisecret.equals(apisecret)) {
			return true;
		} else {
			throw new ForbiddenException();
		}
	}

}