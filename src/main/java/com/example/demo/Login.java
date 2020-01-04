package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Login {

	public Map<String,String> loginmethod(Map<String, Object> payload,DB db){
	Map<String,String>map=new HashMap<String,String>();
	
	String loginid= (String) payload.get("loginid");
	String password= (String) payload.get("password");
	
	Statement st = null;
	ResultSet rs = null;
	String sql1=null;
	try {
	sql1 = "SELECT password,role FROM public.login where loginid='"+loginid+"';";
	 st = db.connect().createStatement();
	 rs = st.executeQuery(sql1);
	}
	catch(Exception e){
		map.put("Status", "Error");
		return map;
		
	}
	try {
		if(!rs.next()) {
			map.put("Status", "Error");
			return map;
			}
	} catch (SQLException e1) {
		e1.printStackTrace();
	}
	
	
	try {
		if(rs.getString("password").equals(password)) {
		map.put("status", "login successful");
		String rights= rs.getString("role");
		map.put("rights", rights);
		}
		else {
			map.put("status", "login failed");

		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
		
	

	return map;
}
	
	
	
}
