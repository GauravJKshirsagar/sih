package com.example.demo;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.core.util.SystemInfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date.*;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/api",  produces = "application/json")
public class Mp {
	public String log;
	public boolean admin_log=true;
	public boolean emp_log = false;
	
	@Autowired
	DB db = new DB();
	

	
	@GetMapping("/pi")										//method to test
	public Map<String, String> test() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("value", "Hello");
		return map;
	}
	
	
	@PostMapping("/pi/login")										//login
	public Map<String, String> login(@RequestBody Map<String, Object> payload) {
		Login l=new Login();
	 return l.loginmethod(payload,db);
		
	}
	
	
	
	

	@PostMapping("/pi/details/farmer")
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
	
	
	@PostMapping("/pi/details/vendor")
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
	
	
	
	
	
	@PostMapping("/pi/details/expert")
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
	
	
	@PostMapping("/pi/addfarm")
	public Map<String, String> addfarm(@RequestBody Map<String, Object> payload) {
		Myfarm f= new Myfarm(db);
		try {
			return f.addfarms(payload);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@PostMapping("/pi/addfarm/addcrop")
	public Map<String, String> addcrop(@RequestBody Map<String, Object> payload) {
		Myfarm f= new Myfarm(db);
		return f.addcrops(payload);
	}
	
	
	
	@GetMapping("/pi/showfarm")
	public List showfarm(@RequestBody Map<String, Object> payload) {
		Myfarm f= new Myfarm(db);
		return f.showfarm(payload);
	}
	
	
	
	@GetMapping("/pi/showcrop")
	public List showcrop(@RequestBody Map<String, Object> payload) {
		Myfarm f= new Myfarm(db);
		return f.showcrop(payload);
	}
	
	
	
	
	
	
	
	
	


	
	
	
	
}
