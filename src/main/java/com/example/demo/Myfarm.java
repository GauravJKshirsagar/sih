package com.example.demo;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Myfarm {
	
	public DB db=null;
	 Myfarm(DB d){
	 db =d;
	}
	
	
	public Map<String,String> addfarms(Map<String,Object> payload) throws ParseException{
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
	
	
	
	
	
	
	public Map<String,String> addcrops(Map<String,Object> payload){
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
	
	
	
	
	
	
	public List showfarm(Map<String,Object> payload){
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
//			System.out.println(map1);
			mymap.add(i, map1);
//			System.out.println("mymap="+mymap);
			i++;
			}


		} catch (SQLException e) {
			e.printStackTrace();
		}
	return mymap;
				
	}
	
	
	
	
	
	public List showcrop(Map<String,Object> payload){
		String farmid=(String) payload.get("farmid");

		
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

//			System.out.println(map1);
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
