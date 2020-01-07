package com.example.demo;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;    
import ch.qos.logback.core.util.SystemInfo;

import java.sql.JDBCType;
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
@RequestMapping(value = "/govt",  produces = "application/json")
public class Govt {
	@Autowired
	DB db;
	
	
	
	@PostMapping("/broadcast")
	public Map<String,String> broadcast(@RequestBody Map<String,Object> payload) throws ParseException{
		 
	String message = (String) payload.get("message");
	String aadharid = (String) payload.get("aadharid");

	String state = (String) payload.get("state");
	String district = (String) payload.get("district");
	String taluka = (String) payload.get("taluka");

	String title = (String) payload.get("title");

	String time  = java.time.LocalDateTime.now().toString().substring(java.time.LocalDate.now().toString().length()+1, java.time.LocalDateTime.now().toString().length() - 1);
	System.out.println(time);
	
	String d = java.time.LocalDate.now().toString();
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	java.util.Date date1 = sdf1.parse(d);
	java.sql.Date date = new java.sql.Date(date1.getTime());
	
	Map<String,String> map= new HashMap<String,String>();
	PreparedStatement st = null;
	ResultSet rs = null;
	
	String sql1 = "INSERT INTO public.govtbroadcast(aadharid,message,state,district,taluka,title,date,time) VALUES (?, ?, ?,?,?,?,?,?);";
	
	try {
		PreparedStatement stmt = db.connect().prepareStatement(sql1);
		stmt.setString(1, aadharid);
		stmt.setString(2, message);
		stmt.setString(3, state);
		stmt.setString(4, district);
		stmt.setString(5, taluka);

		stmt.setString(6, title);
		stmt.setDate(7, date);
		stmt.setString(8, time);

		
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
	
	
	
	

	@PostMapping("/upload/scheme")
	public Map<String,String> uploadscheme(@RequestBody Map<String,Object> payload) throws ParseException{
	String aadharid = (String) payload.get("aadharid");
	String title = (String) payload.get("title");
	String synopsis = (String) payload.get("synopsis");
	String details = (String) payload.get("details");
	
	String uploadtime  = java.time.LocalDateTime.now().toString().substring(java.time.LocalDate.now().toString().length()+1, java.time.LocalDateTime.now().toString().length() - 1);
	System.out.println(uploadtime);
	
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	java.util.Date date1 = sdf1.parse((String) payload.get("expirydate"));
	java.sql.Date expirydate = new java.sql.Date(date1.getTime());
	
	 date1 = sdf1.parse((String) payload.get("startdate"));
	java.sql.Date startdate = new java.sql.Date(date1.getTime());
	
	String d = java.time.LocalDate.now().toString();
	 date1 = sdf1.parse(d);
	java.sql.Date uploaddate = new java.sql.Date(date1.getTime());
	
	
	Map<String,String> map= new HashMap<String,String>();
	PreparedStatement st = null;
	ResultSet rs = null;
	
	String sql1 = "INSERT INTO public.govtschemes(aadharid,title,synopsis,expirydate,startdate,uploaddate,uploadtime,details) VALUES (?, ?, ?,?,?,?,?,?);";
	
	try {
		PreparedStatement stmt = db.connect().prepareStatement(sql1);
		stmt.setString(1, aadharid);
		stmt.setString(2, title);
		stmt.setString(3, synopsis);
		stmt.setDate(4, expirydate);
		stmt.setDate(5, startdate);

		stmt.setDate(6, uploaddate);
		stmt.setString(7, uploadtime);
		stmt.setString(8, details);

		
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
