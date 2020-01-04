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
import java.sql.Date;
import ch.qos.logback.core.util.SystemInfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date.*;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/expert",  produces = "application/json")
public class Expert {
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
	
	
	
	@PostMapping("/warning/issue/weather")
	public Map<String,String> issueweatherwarn(@RequestBody Map<String,Object> payload) throws ParseException{
		String issuersid=(String)payload.get("aadharid");
		String state=(String)payload.get("state");
		String district=(String)payload.get("district");
		String taluka=(String)payload.get("taluka");
		String info=(String)payload.get("info");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date1 = sdf1.parse((String)payload.get("date"));
		java.sql.Date date = new java.sql.Date(date1.getTime());
		
		
		Map<String,String> map= new HashMap<String,String>();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String sql1 = "INSERT INTO public.weatherwarning(issuersid, state, district, taluka, info,date) VALUES (?, ?, ?,?,?,?);";
		
		try {
			PreparedStatement stmt = db.connect().prepareStatement(sql1);
			stmt.setString(1, issuersid);
			stmt.setString(2, state);
			stmt.setString(3, district);
			stmt.setString(4, taluka);
			stmt.setString(5, info);
			stmt.setDate(6, date);


				
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
	
	
	@PostMapping("/warning/issue/pest")
	public Map<String,String> issuepestwarn(@RequestBody Map<String,Object> payload) throws ParseException{
		String issuersid=(String)payload.get("aadharid");
		String state=(String)payload.get("state");
		String district=(String)payload.get("district");
		String taluka=(String)payload.get("taluka");
		String info=(String)payload.get("info");
		String crop=(String)payload.get("crop");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date1 = sdf1.parse((String)payload.get("date"));
		java.sql.Date date = new java.sql.Date(date1.getTime());

		
		
		Map<String,String> map= new HashMap<String,String>();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String sql1 = "INSERT INTO public.pestwarning(issuersid, state, district, taluka, info,crop,date) VALUES (?, ?, ?,?,?,?,?);";
		
		try {
			PreparedStatement stmt = db.connect().prepareStatement(sql1);
			stmt.setString(1, issuersid);
			stmt.setString(2, state);
			stmt.setString(3, district);
			stmt.setString(4, taluka);
			stmt.setString(5, info);
			stmt.setString(6, crop);
			stmt.setDate(7, date);



				
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
	
	
	
	
	
	@PostMapping("/warning/issue/general")
	public Map<String,String> issuegeneralwarn(@RequestBody Map<String,Object> payload) throws ParseException{
		String issuersid=(String)payload.get("aadharid");
		String type=(String)payload.get("type");
		String place=(String)payload.get("place");
		String affectedcrop=(String)payload.get("affectedcrop");
		String info=(String)payload.get("info");
		String crop=(String)payload.get("crop");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date1 = sdf1.parse((String)payload.get("date"));
		java.sql.Date date = new java.sql.Date(date1.getTime());

		
		
		Map<String,String> map= new HashMap<String,String>();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String sql1 = "INSERT INTO public.generalwarning(type, issuersid, info, affectedcrop, place,date) VALUES (?, ?, ?,?,?,?);";
		
		try {
			PreparedStatement stmt = db.connect().prepareStatement(sql1);
			stmt.setString(1, type);
			stmt.setString(2, issuersid);
			stmt.setString(3, info);
			stmt.setString(4, affectedcrop);
			stmt.setString(5, place);
			stmt.setDate(6, date);



				
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