package de.webenm.mdbconnector.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

public class MDBConnector {
	
	public static final String METHOD_EXECUTE = "execute";
	public static final String METHOD_QUERY = "query";
	
	Connection conn;
	boolean debug = false;
	
	private String file;

	public MDBConnector() {
		this.debug = false;
	}
	
	public MDBConnector(boolean debug) {
		this.debug = debug;
	}
	
	public boolean connect(String file, String password) {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			this.conn = DriverManager.getConnection("jdbc:ucanaccess://" + file + ";jackcessOpener=de.webenm.mdbconnector.database.CryptCodecOpener;singleconnection=true", null, password);
			this.file = file;
			return true;
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println("[2]connection error");
			if(this.debug) {
				e.printStackTrace();
			}
			return false;
		}
	}
	
	/**
	 * 
	 * @param sql
	 * @return json encoded results
	 */
	public String query(String sql, boolean dict) {
		Statement st = null;
		ResultSet result = null;;
		
		try {
			st = conn.createStatement();
			result = st.executeQuery(sql);
			ResultSetMetaData resultMeta = result.getMetaData();
			
			JSONObject jsonDict = null;
			JSONArray json = null;
			
			if(dict) {
				jsonDict = new JSONObject();
			} else {
				json = new JSONArray();
			}
			
			while(result.next()) {
				JSONObject row = new JSONObject();
				for(int i = 1; i <= resultMeta.getColumnCount(); i++) {
					//first column is 1!!!
					row.put(resultMeta.getColumnName(i), result.getObject(i));
				}
				if(dict) {
					jsonDict.put(String.valueOf(result.getObject(1)), row);
				} else {
					json.put(row);
				}
			}
			
			result.close();
			st.close();
			
			if(dict) {
				return jsonDict.toString();
			} else {
				return json.toString();
			}
		} catch (SQLException e) {
			System.out.println("[3]execution error");
			if(this.debug) {
				e.printStackTrace();
			}
		} finally {
			try { result.close(); } catch (Exception e) { /* Ignored */ }
			try { st.close(); } catch (Exception e) { /* Ignored */ }
		}
		return null;
	}
	
	/**
	 * 
	 * @param sql
	 * @return true if succeeded
	 */
	public boolean execute(String sql) {
		Statement st = null;
		try {
			st = conn.createStatement();
			boolean success = st.executeUpdate(sql) > 0;
			st.close();
			return success;
		} catch (SQLException e) {
			System.out.println("[3]execution error");
			if(this.debug) {
				e.printStackTrace();
			}
		} finally {
			try { st.close(); } catch (Exception e) { /* Ignored */ }
		}
		return false;
	}
	
	public boolean close() {
		try {
			this.conn.close();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public String getFile() {
		return file;
	}
	
	public boolean isDebug() {
		return this.debug;
	}
}
