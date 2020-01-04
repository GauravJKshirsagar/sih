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
import java.sql.Date;
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
@RequestMapping(value = "/farmer",  produces = "application/json")
public class Myfarm {
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
	
	@PostMapping("/add/crop")
	public Map<String,String> addcrop(@RequestBody Map<String,Object> payload) throws ParseException{
	String aadharid = (String) payload.get("aadharid");
	String location = (String) payload.get("location");
	String name = (String) payload.get("name");
	
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	java.util.Date date1 = sdf1.parse((String)payload.get("sowing"));
	java.sql.Date sowing = new java.sql.Date(date1.getTime());

	String farmid= aadharid+location;
	String cropid= name+sowing.toString();

	Map<String,String>map= new HashMap<String,String>();
	
	Statement st = null;
	ResultSet rs = null;
	String sql1=null;
	try {
	sql1 = "SELECT location FROM public.farm where farmid='"+farmid+"';";
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
		if(!rs.getString("location").equals(null)) {
			String sql2 = "INSERT INTO public.crop(cropid,name,farmid,sowing) VALUES (?, ?, ?,?);";
			
			try {
				PreparedStatement stmt = db.connect().prepareStatement(sql2);
				stmt.setString(1, cropid);
				stmt.setString(2, name);
				stmt.setString(3, farmid);
				stmt.setDate(4, (Date) sowing);

					
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
			map.put("status", "farmid doesn't exist");

		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
return map;
	}
	
	
	
	
	
	@PostMapping("/add/farm")
	public Map<String,String> addcrops(@RequestBody Map<String,Object> payload){
	String aadharid = (String) payload.get("aadharid");
	String location = (String) payload.get("location");
	String farmid= aadharid+location;

	Map<String,String> map= new HashMap<String,String>();
	
	Statement st = null;
	ResultSet rs = null;
	String sql1=null;
	try {
	sql1 = "SELECT farmid FROM public.farmerinfo where farmid='"+farmid+"';";
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
		if(!rs.getString("location").equals(null)) {
			String sql2 = "INSERT INTO public.farm(farmid, farmerid,location) VALUES (?, ?, ?);";
			
			try {
				PreparedStatement stmt = db.connect().prepareStatement(sql2);
				stmt.setString(1, farmid);
				stmt.setString(2, aadharid);
				stmt.setString(3, location);
					
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
	
	
	
	
	
	@GetMapping("/show/farm")
	public List showfarm(@RequestBody Map<String,Object> payload){
		String aadharid=(String) payload.get("aadharid");
		
		Map<String,String>map= new HashMap<String,String>();
		java.util.List<Map<String,String>> mymap = new ArrayList<Map<String, String>>();

		Statement st = null;
		ResultSet rs = null;
		String sql1=null;
		try {
		sql1 = "SELECT farmid,location FROM public.farm where farmerid='"+aadharid+"';";
		 st = db.connect().createStatement();
		 rs = st.executeQuery(sql1);
		}
		catch(Exception e){
			map.put("Status", "Error");
			mymap.add(map);
			return mymap;
		}
		
		try {
			int i=0;
			while(rs.next()) {
				Map<String,String>map1=new HashMap<String,String>();
			map1.put("farmid", rs.getString("farmid"));
			map1.put("location", rs.getString("location"));
			System.out.println(map1);
			mymap.add(i, map1);
//			System.out.println("mymap="+mymap);
			i++;
			}


		} catch (SQLException e) {
			e.printStackTrace();
		}
	return mymap;
				
	}
	
	
	
	
	@GetMapping("/show/crop")
	public List showcrop(@RequestBody Map<String,Object> payload){
		String farmid=(String) payload.get("farmid");

		System.out.println(farmid);
		Map<String,String>map= new HashMap<String,String>();
		java.util.List<Map<String,String>> mymap = new ArrayList<Map<String, String>>();

		Statement st = null;
		ResultSet rs = null;
		String sql1=null;
		try {
		sql1 = "SELECT cropid,name,sowing FROM public.crop where farmid='"+farmid+"';";
		 st = db.connect().createStatement();
		 rs = st.executeQuery(sql1);
		}
		catch(Exception e){
			map.put("Status", "Error");
			mymap.add(map);
			return mymap;
		}
		
		try {
			int i=0;
			while(rs.next()) {
				Map<String,String>map1=new HashMap<String,String>();
			map1.put("cropid", rs.getString("cropid"));
			map1.put("name", rs.getString("name"));
			map1.put("sowing", rs.getDate("sowing").toString());

			System.out.println(map1);
			mymap.add(map1);
//			System.out.println("mymap="+mymap);
			i++;
			}


		} catch (SQLException e) {
			e.printStackTrace();
		}
	return mymap;
				
	}
	
	
	
	@GetMapping("/show/warning")
	public List showwarning(){
//		String farmid=(String) payload.get("farmid");
//		System.out.println(farmid);
		
		Map<String,String>map= new HashMap<String,String>();
		java.util.List<Map<String,String>> mymap = new ArrayList<Map<String, String>>();

		Statement st = null;
		ResultSet rs = null;
		String sql1=null;
		try {
		sql1 = "SELECT * FROM public.pestwarning;";
		 st = db.connect().createStatement();
		 rs = st.executeQuery(sql1);
		}
		catch(Exception e){
			map.put("Status", "Error");
			mymap.add(map);
			return mymap;
		}
		
		try {
			while(rs.next()) {
				Map<String,String>map1=new HashMap<String,String>();
			map1.put("type", "pestattack");
			map1.put("crop", rs.getString("crop"));
			map1.put("info", rs.getString("info"));
			map1.put("date", rs.getDate("date").toString());

			System.out.println(map1);
			mymap.add(map1);
//			System.out.println("mymap="+mymap);
			}

			st.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		 sql1=null;
		try {
		sql1 = "SELECT * FROM public.weatherwarning;";
		 st = db.connect().createStatement();
		 rs = st.executeQuery(sql1);
		}
		catch(Exception e){
			map.put("Status", "Error");
			mymap.add(map);
			return mymap;
		}
		
		try {
			while(rs.next()) {
			Map<String,String>map1=new HashMap<String,String>();
			map1.put("type","weather");
			map1.put("info", rs.getString("info"));
			map1.put("date", rs.getDate("date").toString());

			System.out.println(map1);
			mymap.add(map1);
//			System.out.println("mymap="+mymap);
			}

			st.close();
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		
		 sql1=null;
		try {
		sql1 = "SELECT * FROM public.generalwarning;";
		 st = db.connect().createStatement();
		 rs = st.executeQuery(sql1);
		}
		catch(Exception e){
			map.put("Status", "Error");
			mymap.add(map);
			return mymap;
		}
		
		try {
			while(rs.next()) {
			Map<String,String>map1=new HashMap<String,String>();
			map1.put("type","general");
			map1.put("info", rs.getString("info"));
			map1.put("date", rs.getDate("date").toString());

			System.out.println(map1);
			mymap.add(map1);
//			System.out.println("mymap="+mymap);
			}

			st.close();
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	return mymap;
				
	}
	
	
	
	
	
	
	@GetMapping("/show/cropprices")
	public List cropprices(@RequestBody Map<String,Object> payload){
		String crop=(String) payload.get("crop");
		String state=(String) payload.get("state");
		String district=(String) payload.get("district");
		String taluka=(String) payload.get("taluka");
		String town=(String) payload.get("town");


		
		Map<String,String>map= new HashMap<String,String>();
		java.util.List<Map<String,String>> mymap = new ArrayList<Map<String, String>>();

		Statement st = null;
		ResultSet rs = null;
		String sql1=null;
		try {
		sql1 = "SELECT * FROM public.cropprice;";
		 st = db.connect().createStatement();
		 rs = st.executeQuery(sql1);
		}
		catch(Exception e){
			map.put("Status", "Error");
			mymap.add(map);
			return mymap;
		}
		
		try {
			int i=0;
			while(rs.next()) {
			Map<String,String>map1=new HashMap<String,String>();
			map1.put("crop", rs.getString("crop"));
			map1.put("price", rs.getString("price"));
			map1.put("state", rs.getString("state"));
			map1.put("district", rs.getString("district"));
			map1.put("taluka", rs.getString("taluka"));
			map1.put("town", rs.getString("town"));
			map1.put("vendorsid", rs.getString("vendorsid"));
			map1.put("date", rs.getDate("date").toString());

			System.out.println(map1);
			mymap.add(i, map1);
//			System.out.println("mymap="+mymap);
			i++;
			}


		} catch (SQLException e) {
			e.printStackTrace();
		}
	return mymap;
				
	}
	

	

}
