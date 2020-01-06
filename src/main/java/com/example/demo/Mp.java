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

	@Autowired
	DB db;
	

	
	@GetMapping("/pi")										//method to test
	public Map<String, String> test() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("value", "Hello");
		return map;
	}
	
	
	
	@GetMapping("/pi/predictcrop")
	public List predictcrop(@RequestBody Map<String, Object> payload) {
		String aadharid = (String)payload.get("aadharid");
		Map<String,String>map= new HashMap<String,String>();
		java.util.List<Map<String,String>> mymap = new ArrayList<Map<String, String>>();

		Statement st = null;
		ResultSet rs = null;
		String sql1=null;
		try {
		sql1 = "SELECT farmid,location FROM public.farm where farmerid='"+aadharid+"';";
		 st = db.connect().createStatement();
		 rs = st.executeQuery(sql1);
		 int i=0;
		 while(i<10) {
			 i++;
			 Map<String, String>map1=new HashMap<String,String>();
			 map1.put("crop", "wheat");
			 mymap.add(map1);
		 }
		 return mymap;
		 
		}
		catch(Exception e){
			map.put("Status", "Error");
			mymap.add(map);
			return mymap;
		}
	}
	

	
}
