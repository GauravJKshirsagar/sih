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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;    
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
@RequestMapping(value = "/forum",  produces = "application/json")
public class Forums {

	
	@Autowired
	DB db;
	
	
	
	@PostMapping("/post/question")
	public Map<String,String> addcrops(@RequestBody Map<String,Object> payload) throws ParseException{
	String farmerid = (String) payload.get("aadharid");
	String question = (String) payload.get("question");
	String time = (String) payload.get("time");
	String title = (String) payload.get("title");
	

	String ids = payload.get("tags").toString();
	System.out.println(ids);
	
//	tags entry left

//	for (int i = 0; i < ids.size(); i++) {
//	    String id = ids.toString();
//	    System.out.println(id);
//	}
	
	
	Map<String,String> map= new HashMap<String,String>();
	
	Statement st = null;
	ResultSet rs = null;
	String sql1=null;
	try {
	sql1 = "SELECT aadharid FROM public.farmerinfo where aadharid='"+farmerid+"';";
	 st = db.connect().createStatement();
	 rs = st.executeQuery(sql1);
	}
	catch(Exception e){
		map.put("Status", "Error");
		return map;
	}
	try {
		if(!rs.next()) {
			map.put("status", "error");
		}
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	try {
		if(rs.getString("aadharid").equals(farmerid)) {
			String sql2 = "INSERT INTO public.questions( farmerid,question,date,time,title) VALUES (?,?, ?, ?,?);";
			
			try {
				System.out.println(java.time.LocalDate.now());  

			    String d = java.time.LocalDate.now().toString();
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date date1 = sdf1.parse(d);
				java.sql.Date date = new java.sql.Date(date1.getTime());
				
				PreparedStatement stmt = db.connect().prepareStatement(sql2);
				stmt.setString(1, farmerid);
				stmt.setString(2, question);
				stmt.setDate(3, date);
				stmt.setString(4, time);
				stmt.setString(5, title);
				

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
		}
		else {
			map.put("status", "farmerid doesn't exist");

		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
return map;
	}
	
	
	
}