package com.example.demo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/register",  produces = "application/json")
public class Login {
	public String log;
	public boolean admin_log=true;
	public boolean emp_log = false;
	
	@Autowired
	DB db;
	

	
	@GetMapping("/pi")										//method to test
	public Map<String, String> test() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("value", "Hello");
		return map;
	}
	
	@GetMapping("/login")										//method to test
	public Map<String,String> loginmethod(@RequestBody Map<String, Object> payload,DB db){
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
	
	
	
	
	
	
	
	@PostMapping("/farmer")
	public Map<String, String> detailsfarmer(@RequestBody Map<String, Object> payload) {
		String aadharid = (String) payload.get("aadharid");
		String fullname = (String) payload.get("fullname");
		String gender = (String) payload.get("gender");
		String email = (String) payload.get("email");
		String mobile = (String) payload.get("mobile");

		Map<String,String> map= new HashMap<String,String>();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String sql1 = "INSERT INTO public.farmerinfo(aadharid, name, gender, email, mobile) VALUES (?, ?, ?,?,?);";
		
		try {
			PreparedStatement stmt = db.connect().prepareStatement(sql1);
			stmt.setString(1, aadharid);
			stmt.setString(2, fullname);
			stmt.setString(3, gender);
			stmt.setString(4, email);
			stmt.setString(5, mobile);

				
			stmt.executeUpdate();
			System.out.println("done");
			map.put("status","Entry Successful");
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			map.put("status","Not Successful");
			return map;
			}
		
	return map;
	}
	
	
	@PostMapping("/vendor")
	public Map<String, String> detailsvendors(@RequestBody Map<String, Object> payload) {
		String aadharid = (String) payload.get("aadharid");
		String fullname = (String) payload.get("fullname");
		String gender = (String) payload.get("gender");
		String email = (String) payload.get("email");
		String mobile = (String) payload.get("mobile");

		Map<String,String> map= new HashMap<String,String>();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String sql1 = "INSERT INTO public.vendors(aadharid, name, gender, email, mobile) VALUES (?, ?, ?,?,?);";
		
		try {
			PreparedStatement stmt = db.connect().prepareStatement(sql1);
			stmt.setString(1, aadharid);
			stmt.setString(2, fullname);
			stmt.setString(3, gender);
			stmt.setString(4, email);
			stmt.setString(5, mobile);

				
			stmt.executeUpdate();
			System.out.println("done");
			map.put("status","Entry Successful");
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			map.put("status","Not Successful");
			return map;
			}
		
	return map;
	}
	
	
	
	
	
	@PostMapping("/expert")
	public Map<String, String> detailsexpert(@RequestBody Map<String, Object> payload) {
		String aadharid = (String) payload.get("aadharid");
		String fullname = (String) payload.get("fullname");
		String gender = (String) payload.get("gender");
		String email = (String) payload.get("email");
		String mobile = (String) payload.get("mobile");

		Map<String,String> map= new HashMap<String,String>();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String sql1 = "INSERT INTO public.experts(aadharid, name, gender, email, mobile) VALUES (?, ?, ?,?,?);";
		
		try {
			PreparedStatement stmt = db.connect().prepareStatement(sql1);
			stmt.setString(1, aadharid);
			stmt.setString(2, fullname);
			stmt.setString(3, gender);
			stmt.setString(4, email);
			stmt.setString(5, mobile);

				
			stmt.executeUpdate();
			System.out.println("done");
			map.put("status","Entry Successful");
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			map.put("status","Not Successful");
			return map;
			}
		
	return map;
	}
	
	
	
	
	
	
	
}
