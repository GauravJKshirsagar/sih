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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date.*;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/vendor",  produces = "application/json")
public class Vendor {
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
	
	
	
	@PostMapping("/price/upload")
	public Map<String,String> cropprice(@RequestBody Map<String,Object> payload) throws ParseException{
		String vendorsid=(String)payload.get("aadharid");
		String crop=(String)payload.get("crop");
		String price=(String)payload.get("price");

		String state=(String)payload.get("state");
		String district=(String)payload.get("district");
		String taluka=(String)payload.get("taluka");
		String town=(String)payload.get("town");
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date1 = sdf1.parse((String)payload.get("date"));
		java.sql.Date date = new java.sql.Date(date1.getTime());
		
		
		Map<String,String> map= new HashMap<String,String>();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String sql1 = "INSERT INTO public.cropprice(crop,price,vendorsid, state, district, taluka,town ,date) VALUES ( ?, ?,?,?,?,?,?,?);";
		
		try {
			PreparedStatement stmt = db.connect().prepareStatement(sql1);
			stmt.setString(1, crop);
			stmt.setString(2, price);

			stmt.setString(3, vendorsid);
			stmt.setString(4, state);
			stmt.setString(5, district);
			stmt.setString(6, taluka);
			stmt.setString(7, town);
			stmt.setDate(8, date);

				
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