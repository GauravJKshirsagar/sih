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

import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;  
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
	String farmid = (String) payload.get("farmid");
//	String farmno = (String) payload.get("location");
	String name = (String) payload.get("crop");
	String area = (String) payload.get("area");

	
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	java.util.Date date1 = sdf1.parse((String)payload.get("sowing"));
	java.sql.Date sowing = new java.sql.Date(date1.getTime());

//	String farmid= aadharid+location;
	String cropid= name+sowing.toString();

	Map<String,String>map= new HashMap<String,String>();
	
	Statement st = null;
	ResultSet rs = null;
	String sql1=null;
	try {
	sql1 = "SELECT farmid FROM public.farm where farmid='"+farmid+"';";
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
		if(rs.getString("farmid").equals(farmid)) {
			String sql2 = "INSERT INTO public.crop(cropid,name,farmid,sowing,area) VALUES (?, ?,?, ?,?);";
			
			try {
				PreparedStatement stmt = db.connect().prepareStatement(sql2);
				stmt.setString(1, cropid);
				stmt.setString(2, name);
				stmt.setString(3, farmid);
				stmt.setDate(4, (Date) sowing);
				stmt.setString(5, area);


					
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
	public Map<String,String> addcrops(@RequestBody Map<String,Object> payload) throws ParseException{
	String aadharid = (String) payload.get("aadharid");
	String farmno = (String) payload.get("farmno");
	String location = (String) payload.get("location");
	String pincode = (String) payload.get("pincode");
	String district = (String) payload.get("district");
	String state = (String) payload.get("state");
	String taluka = (String) payload.get("taluka");
	String town = (String) payload.get("town");
	String area = (String) payload.get("area");



	String farmid= aadharid+farmno;

	
	Map<String,String> map= new HashMap<String,String>();
	
	Statement st = null;
	ResultSet rs = null;
	String sql1=null;
	try {
	sql1 = "SELECT aadharid FROM public.farmerinfo where aadharid='"+aadharid+"';";
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
		if(rs.getString("aadharid").equals(aadharid)) {
			String sql2 = "INSERT INTO public.farm(farmid, farmerid,location,pincode,district,state,taluka,town,date,area) VALUES (?,?, ?, ?,?,?,?,?,?,?);";
			
			try {
				System.out.println(java.time.LocalDate.now());  

			    String d = java.time.LocalDate.now().toString();
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date date1 = sdf1.parse(d);
				java.sql.Date date = new java.sql.Date(date1.getTime());
				
				PreparedStatement stmt = db.connect().prepareStatement(sql2);
				stmt.setString(1, farmid);
				stmt.setString(2, aadharid);
				stmt.setString(3, location);
				stmt.setString(4, pincode);
				stmt.setString(5, district);
				stmt.setString(6, state);
				stmt.setString(7, taluka);
				stmt.setString(8, town);
				stmt.setDate(9, date);
				stmt.setString(10,area);



					
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
		System.out.println(payload);
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
			map1.put("pest", rs.getString("pest"));
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
	
	
	
	
	
	
	
	
	
	@PostMapping("/upload/pestattack")
	public Map<String,String> pestattack(@RequestBody Map<String,Object> payload) throws ParseException{
	String location =null;

	String farmid = (String) payload.get("farmid");
	String pest = (String) payload.get("pest");
	String state = (String) payload.get("state");
	String  district= (String) payload.get("district");
	String taluka = (String) payload.get("taluka");
	String town = (String) payload.get("town");
	String crop = (String) payload.get("crop");
	String info = (String) payload.get("info");

	System.out.println(java.time.LocalDate.now());  

    String d = java.time.LocalDate.now().toString();

   SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	java.util.Date date1 = sdf1.parse((String)d);
	java.sql.Date date = new java.sql.Date(date1.getTime());
	


	Map<String,String> map= new HashMap<String,String>();
//	java.util.List<Map<String,String>> mymap = new ArrayList<Map<String, String>>();
	Integer count=0;
	Statement st = null;
	ResultSet rs = null;
	String sql1=null;
	 sql1 = "SELECT count,pk from public.pestattacktemp WHERE crop='"+crop+"' and pest='"+pest+"' and state='"+state+"'and district='"+district+"'and taluka='"+taluka+"' and town='"+town+"';"; 
	try {
		st = db.connect().createStatement();
	} catch (SQLException e1) {
		e1.printStackTrace();
	}
	 try {
		rs = st.executeQuery(sql1);
	} catch (SQLException e1) {
		e1.printStackTrace();
	}
	 try {
		if(rs.next()) {
			 count=Integer.valueOf(rs.getString("count"));
			 if(count>=5) {
				rs.close();
				st.close();
				sql1=null;
				
				 sql1 = "INSERT INTO public.pestwarning(issuersid, state, district, taluka,town, info,crop,date,pest) VALUES (?,?, ?, ?,?,?,?,?,?);";
				
				try {
					PreparedStatement stmt = db.connect().prepareStatement(sql1);
					stmt.setString(1, farmid);
					stmt.setString(2, state);
					stmt.setString(3, district);
					stmt.setString(4, taluka);
					stmt.setString(5, town);
					stmt.setString(6, info);
					stmt.setString(7, crop);
					stmt.setDate(8, date);
					stmt.setString(9, pest);
		
					stmt.executeUpdate();
					System.out.println("done");
					map.put("status","Entry Successful");
					
					
					sql1="DELETE FROM public.pestattacktemp where farmid='"+farmid+"';";
					st = db.connect().createStatement();
					 st.executeUpdate(sql1);

					
				
					
					return map;
				} 
				catch (SQLException e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
					map.put("status","Not Successful");
					return map;
					}
				 //push this data to pestattack
			 }
			 else {
			 count++;
			 String pk=rs.getString("pk");
			 
			rs.close();
			st.close();
			sql1=null;
			
			
			 sql1="UPDATE public.pestattacktemp SET count=? WHERE pk='"+pk+"';";
			PreparedStatement stmt1 = db.connect().prepareStatement(sql1);
			stmt1.setString(1,count.toString());
			stmt1.executeUpdate();	
			map.put("status", "successful");
			 }
		 }
		
		
		 else {
		try {
		sql1 = "SELECT farmid FROM public.farm where farmid='"+farmid+"';";
		 st = db.connect().createStatement();
		 rs = st.executeQuery(sql1);
		 sql1=null;
		 rs.next();
		 
		 if(farmid.equals(rs.getString("farmid"))) {

			    
			
			 sql1 = "INSERT INTO public.pestattacktemp (farmid, pest, date,state, district, taluka, town, count, crop, info) VALUES (?,?,?,?,?,?,?,?,?,?);";
			
			try {
				PreparedStatement stmt = db.connect().prepareStatement(sql1);
				stmt.setString(1, farmid);
				stmt.setString(2, pest);
				stmt.setDate(3, date);
				stmt.setString(4, state);
				stmt.setString(5, district);
				stmt.setString(6, taluka);
				stmt.setString(7, town);
				stmt.setString(8, count.toString());
				stmt.setString(9, crop);
				stmt.setString(10, info);

				System.out.print("here");

				
				stmt.executeUpdate();
				System.out.println("done");
				map.put("status","Entry Successful");
				
				rs.close();
				st.close();
				
				
			} 
			catch (SQLException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
				map.put("status","Not Successful");
				return map;
				}
			 	 
		 }
		 else {
				map.put("Status", "farmerid doesn't exist");

		 }
		 
		 
		 
		 rs.close();
		 st.close();

		}
		catch(Exception e){
			map.put("Status", "Error");
//		mymap.add(map);
			return map;
		}
		 }
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
return map;
	}
	
	
	
	
	
	
	
	@PostMapping("/upload/finance")
	public Map<String,String> finance(@RequestBody Map<String,Object> payload) throws ParseException{
	String farmerid = (String) payload.get("aadharid");
	String farmid = (String) payload.get("farmid");
	String type = (String) payload.get("type");
	String source = (String) payload.get("source");

	String amount = (String) payload.get("amount");
	String receipt = (String) payload.get("receipt");
	String time = (String) payload.get("time");


	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	java.util.Date date1 = sdf1.parse((String)payload.get("date"));
	java.sql.Date date = new java.sql.Date(date1.getTime());
	
	
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
			String sql2 = "INSERT INTO public.finance(farmid, farmerid,source,type,amount,date,time,receipt,timestamp) VALUES (?,?,?, ?, ?,?,?,?,?);";
			
			try {
				System.out.println(java.time.LocalDate.now());  

			    String timestamp = java.time.LocalDate.now().toString();
			
				
				PreparedStatement stmt = db.connect().prepareStatement(sql2);
				stmt.setString(1, farmid);
				stmt.setString(2, farmerid);
				stmt.setString(3, source);
				stmt.setString(4, type);
				stmt.setString(5, amount);
				stmt.setDate(6, date);
				stmt.setString(7, time);
				stmt.setString(8, receipt);
				stmt.setString(9, timestamp);



					
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
	
	
	
	
	
	
	
	
	@GetMapping("/show/finance")
	public List showfinance(@RequestBody Map<String,Object> payload){
		String farmerid=(String) payload.get("aadharid");
		System.out.println(payload);
		Map<String,String>map= new HashMap<String,String>();
		java.util.List<Map<String,String>> mymap = new ArrayList<Map<String, String>>();

		Statement st = null;
		ResultSet rs = null;
		String sql1=null;
		try {
		sql1 = "SELECT * FROM public.finance where farmerid='"+farmerid+"';";
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
			map1.put("source", rs.getString("source"));
			map1.put("type", rs.getString("type"));
			map1.put("amount", rs.getString("amount"));
			map1.put("date", rs.getDate("date").toString());
			map1.put("time", rs.getString("time"));
			map1.put("receipt", rs.getString("receipt"));
			map1.put("timestamp", rs.getString("timestamp"));
			
			
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
