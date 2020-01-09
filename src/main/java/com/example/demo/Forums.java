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
@RequestMapping(value = "/forum",  produces = "application/json")
public class Forums {

	
	@Autowired
	DB db;
	
	
	
	@PostMapping("/post/question")
	public Map<String,String> addcrops(@RequestBody Map<String,Object> payload) throws ParseException{
	String farmerid = (String) payload.get("aadharid");
	String question = (String) payload.get("question");
	String title = (String) payload.get("title");
	int questionid;
	String time  = java.time.LocalDateTime.now().toString().substring(java.time.LocalDate.now().toString().length()+1, java.time.LocalDateTime.now().toString().length() - 1);
	System.out.println(time);
	

	ArrayList ids = (ArrayList) payload.get("tags");
//	System.out.println(ids.get(0));
	
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
			String sql2 = "INSERT INTO public.questions( farmerid,question,date,time,title) VALUES (?,?, ?, ?,?) returning *;";
			
			try {
				

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
				
				ResultSet rs1=stmt.executeQuery();
				rs1.next();
				System.out.println(rs1.getInt("questionid"));
			
				 questionid = rs1.getInt("questionid");
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
			
			
			
			for(int i=0;i<ids.size();i++) {
				
 sql2 = "INSERT INTO public.questiontags(questionid,tag) VALUES (?,?) ;";
			
			try {
				PreparedStatement stmt = db.connect().prepareStatement(sql2);

				stmt.setInt(1, questionid);
				stmt.setString(2, ids.get(i).toString());
				
				
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
		}
		else {
			map.put("status", "farmerid doesn't exist");

		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
return map;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	@PostMapping("/post/answer")
	public Map<String,String> postanswer(@RequestBody Map<String,Object> payload) throws ParseException {
		String expertid=(String)payload.get("aadharid");
		String answer=(String)payload.get("answer");
		int questionid=Integer.valueOf((String) payload.get("questionid"));

		String a  = java.time.LocalDateTime.now().toString().substring(java.time.LocalDate.now().toString().length()+1, java.time.LocalDateTime.now().toString().length() - 1);
		System.out.println(a);
		
		String d = java.time.LocalDate.now().toString();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date1 = sdf1.parse((String)d);
		java.sql.Date date = new java.sql.Date(date1.getTime());
		
		
		Map<String,String> map= new HashMap<String,String>();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String sql1 = "INSERT INTO public.answers(expertid, answer, questionid, date,time) VALUES (?,?, ?, ?,?);";
		
		try {
			PreparedStatement stmt = db.connect().prepareStatement(sql1);
			stmt.setString(1, expertid);
			stmt.setString(2, answer);
			stmt.setInt(3, questionid);
			stmt.setDate(4, date);
			stmt.setString(5, a);
	




				
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
	
	
	
	@PostMapping("/post/comment")
	public Map<String,String> postcomment(@RequestBody Map<String,Object> payload) throws ParseException {
		String userid=(String)payload.get("aadharid");
		String role=(String)payload.get("role");
		String comment=(String)payload.get("comment");


		int answerid=Integer.valueOf((String) payload.get("answerid"));

		String time  = java.time.LocalDateTime.now().toString().substring(java.time.LocalDate.now().toString().length()+1, java.time.LocalDateTime.now().toString().length() - 1);
		System.out.println(time);
		
		String d = java.time.LocalDate.now().toString();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date1 = sdf1.parse((String)d);
		java.sql.Date date = new java.sql.Date(date1.getTime());
		
		
		Map<String,String> map= new HashMap<String,String>();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String sql1 = "INSERT INTO public.comments(answerid, userid, role, comment,date,time) VALUES (?,?, ?, ?,?,?);";
		
		try {
			PreparedStatement stmt = db.connect().prepareStatement(sql1);
			stmt.setInt(1, answerid);
			stmt.setString(2, userid);
			stmt.setString(3, role);

			stmt.setString(4, comment);
			stmt.setDate(5, date);
			stmt.setString(6, time);
	
				
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
	
	
	
	
	
	

	@GetMapping("/show/question")
	public List showquestion(@RequestParam("aadharid") String aadharid, @RequestParam("type") String type) throws ParseException {

		
		Map<String,String> map= new HashMap<String,String>();
		List<Map<String,String>>l=new ArrayList<Map<String,String>>();
		Statement st = null;
		ResultSet rs = null;
		String sql1=null;
		
		if(type.equals("")) {
		 sql1 = "SELECT * FROM public.questions;";
		}
		else if(type.equals("my")) {
			System.out.println("here");
			 sql1 = "SELECT * FROM public.questions WHERE farmerid='"+aadharid+"';";
		}
		
		Statement st2 = null;
		ResultSet rs2 = null;
		String sql2=null;
		
		List<String>tags=new ArrayList<String>();
		try {
		 st = db.connect().createStatement();
		 rs = st.executeQuery(sql1);
			Map<String,String> map1= new HashMap<String,String>();
			while(rs.next()) {
				 sql2="SELECT * FROM public.questiontags WHERE questionid='"+rs.getString("questionid")+"';";
				 st2 = db.connect().createStatement();
				 rs2= st2.executeQuery(sql2);
				 while(rs2.next()) {
					 tags.add(rs2.getString("tag"));	 
				 }			
				map1.put("questionid",rs.getString("questionid"));
				map1.put("question",rs.getString("question"));
				map1.put("date",rs.getString("date"));
				map1.put("time",rs.getString("time"));
				map1.put("title",rs.getString("title"));
				map1.put("upvotes",rs.getString("upvotes"));
				map1.put("downvotes",rs.getString("downvotes"));
				map1.put("tags",tags.toString());

				l.add(map1);

			}
		}
		catch(Exception e){
			map.put("Status", "Error");
			l.add(map);
			return l;
		}
		
	return l;
		
	}
	
	
	
	
	@GetMapping("/show/answer")
	public List showanswer(@RequestBody Map<String,Object> payload) throws ParseException {
		String questionid=null;
		questionid=(String)payload.get("questionid");

		
		Map<String,String> map= new HashMap<String,String>();
		List<Map<String,String>>l=new ArrayList<Map<String,String>>();
		Statement st = null;
		ResultSet rs = null;
		String sql1=null;
		
		
			System.out.println("here");
			 sql1 = "SELECT * FROM public.answers WHERE questionid='"+questionid+"';";
		
		
		List<String>tags=new ArrayList<String>();
		try {
		 st = db.connect().createStatement();
		 rs = st.executeQuery(sql1);
			Map<String,String> map1= new HashMap<String,String>();
			while(rs.next()) {
				
				map1.put("answerid",rs.getString("answerid"));
				map1.put("expertid",rs.getString("expertid"));
				map1.put("answer",rs.getString("answer"));
				map1.put("date",rs.getString("date"));
				map1.put("time",rs.getString("time"));

				l.add(map1);

			}
		}
		catch(Exception e){
			map.put("Status", "Error");
			l.add(map);
			return l;
		}
		
	return l;
		
	}
	
	
	
	
	
	
}