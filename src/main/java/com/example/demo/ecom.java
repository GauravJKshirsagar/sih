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
@RequestMapping(value = "/ecom",  produces = "application/json")
public class ecom {

	@Autowired
	DB db;
	
	
	@GetMapping("/pi")										//method to test
	public Map<String, String> test() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("value", "Hello");
		return map;
	}
	
	
	@PostMapping("/product/upload")
	public Map<String,String> produp(@RequestBody Map<String,Object> payload) throws ParseException{
	String user_id = (String) payload.get("user_id");
	String price = (String) payload.get("price");
	String quantity = (String) payload.get("quantity");
	String unit = (String) payload.get("unit");

	String name = (String) payload.get("name");
	String category = (String) payload.get("category");
	
	String image = (String) payload.get("image");
	System.out.println("1");


	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	java.util.Date date1 = sdf1.parse((String)payload.get("date"));
	java.sql.Date date = new java.sql.Date(date1.getTime());
	
	java.util.Date date2 = sdf1.parse((String)payload.get("expiry"));
	java.sql.Date expiry = new java.sql.Date(date2.getTime());
	System.out.println("2");

	
	Map<String,String> map= new HashMap<String,String>();
	
	Statement st = null;
	ResultSet rs = null;
	String sql1=null;
	
	
	
			try {
				
				System.out.println("3");

			    sql1="INSERT INTO public.ecom_product(price,quantity,unit,date,expiry,name,category,image,user_id) VALUES (?,?,?,?,?,?,?,?,?);";
				
				PreparedStatement stmt = db.connect().prepareStatement(sql1);
				
				stmt.setString(1, price);
				stmt.setString(2, quantity);
				stmt.setString(3, unit);
				stmt.setDate(4, date);
				stmt.setDate(5, expiry);
				stmt.setString(6, name);
				stmt.setString(7, category);
				stmt.setString(8, image);
				stmt.setString(9, user_id);



					
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
	
	
	@GetMapping("/product/view")
	public List productview(@RequestBody Map<String,Object> payload){
		String aadharid=(String) payload.get("aadharid");
		String showall=(String) payload.get("showall");
		//System.out.println(payload);
		Map<String,String>map= new HashMap<String,String>();
		java.util.List<Map<String,String>> mymap = new ArrayList<Map<String, String>>();

		Statement st = null;
		ResultSet rs = null;
		String sql1=null;
		try {
			
		if(showall.equals("false"))	
			sql1 = "SELECT * FROM public.ecom_product where user_id='"+aadharid+"';";
		else
			sql1 = "SELECT * FROM public.ecom_product;";
			
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
			
			map1.put("prod_id", rs.getString("prod_id"));
			map1.put("unit",rs.getString("unit"));
			map1.put("date",rs.getString("date"));
			map1.put("expiry",rs.getString("expiry"));
			map1.put("name",rs.getString("name"));
			map1.put("category",rs.getString("category"));
			map1.put("image",rs.getString("image"));
			map1.put("user_id",rs.getString("user_id"));
			map1.put("price",rs.getString("price"));
			map1.put("quantity",rs.getString("quantity"));
			
			 
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
	
	
	@PostMapping("/createcart")
	public Map<String,String> createcart(@RequestBody Map<String,Object> payload) throws ParseException{
	String user_id = (String) payload.get("user_id");
	
	
	Map<String,String> map= new HashMap<String,String>();
	
	Statement st = null;
	ResultSet rs = null;
	String sql1=null;
	
	
	
			try {
				
				

			    sql1="INSERT INTO public.ecom_shoppingcart(user_id) VALUES (?);";
				
				PreparedStatement stmt = db.connect().prepareStatement(sql1);
				
				stmt.setString(1, user_id);
			
					
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
	
	
	public void update_cart_amount(int cart_id,int amt)
	{
		Statement st = null;
		String sql=null;
		
		try {
			
			sql = "UPDATE public.ecom_shoppingcart SET total_amount="+amt+"WHERE cart_id="+cart_id+";";
				
			 st = db.connect().createStatement();
			 st.executeUpdate(sql);
			 
			}
			catch(Exception e){
				System.out.print("insert amount Error");
				e.printStackTrace();
			}
		
	}
	
	
	@PostMapping("/addcartitem")
	public Map<String,String> addcartitem(@RequestBody Map<String,Object> payload) throws ParseException{
	int cart_id = Integer.parseInt((String)payload.get("cart_id"));
	int prod_id = Integer.parseInt((String)payload.get("prod_id"));
	int no_of_items = Integer.parseInt((String)payload.get("no_of_items"));
	
	
	Map<String,String> map= new HashMap<String,String>();
	
	
	String sql1=null;
	
	
	
			try {
				
				
			    sql1="INSERT INTO public.ecom_cartitems(cart_id,prod_id,no_of_items) VALUES (?,?,?);";
				
				PreparedStatement stmt = db.connect().prepareStatement(sql1);
				
				stmt.setInt(1, cart_id);
				stmt.setInt(2, prod_id);
				stmt.setInt(3, no_of_items);
				
					
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
			
	Statement st = null;
	ResultSet rs = null;
	String sql2=null;
	int price=0;
	
	try {
		
		
	    sql2="SELECT price from public.ecom_product where prod_id="+prod_id+";";
		
	    st = db.connect().createStatement();
		rs=st.executeQuery(sql2);
		
			try {
			
				rs.next();
				price=Integer.parseInt(rs.getString("price"));
				System.out.println(price);
			
			
				} catch (SQLException e) {
					e.printStackTrace();
				}
		
		
	    update_cart_amount(cart_id,price*no_of_items);
		
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
	

	
	
	@GetMapping("/showcart")
	public List showcart(@RequestBody Map<String,Object> payload){
		String aadharid=(String) payload.get("aadharid");
		
		//System.out.println(payload);
		Map<String,String>map= new HashMap<String,String>();
		java.util.List<Map<String,String>> mymap = new ArrayList<Map<String, String>>();

		Statement st = null;
		ResultSet rs = null;
		String sql1=null;
		int cart_id=0;
		int prod_id=0;
		Map<String,String>product_details=new HashMap<String,String>();
		
		try {
				
		sql1 = "SELECT cart_id FROM public.ecom_shoppingcart where user_id='"+aadharid+"';";
			
		 st = db.connect().createStatement();
		 rs = st.executeQuery(sql1);
		}
		catch(Exception e){
			map.put("Status", "Error");
			mymap.add(map);
			return mymap;
		}
		
		try {
			
			rs.next();
				
			cart_id=rs.getInt("cart_id");
			 
			System.out.println(cart_id);
			
			

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		try {
			
			sql1 = "SELECT * FROM public.ecom_cartitems where cart_id="+cart_id+";";
				
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
			
			//map1.put("cart_id", Integer.toString(rs.getInt("cart_id")));
			prod_id=rs.getInt("prod_id");
			//map1.put("prod_id",Integer.toString(prod_id));
			
			
			//get details of product
			
			Statement st1 = null;
			ResultSet rs1 = null;
			String sql2=null;
			try {
				
			
				sql2 = "SELECT * FROM public.ecom_product where prod_id="+prod_id+";";
			
			 st1 = db.connect().createStatement();
			 rs1 = st1.executeQuery(sql2);
			}
			catch(Exception e){
				map.put("Status", "Error");
				mymap.add(map);
				return mymap;
			}
			
			try {
				
				rs1.next();
					//Map<String,String>map2=new HashMap<String,String>();
				
				map1.put("prod_id", rs1.getString("prod_id"));
				map1.put("unit",rs1.getString("unit"));
				map1.put("date",rs1.getString("date"));
				map1.put("expiry",rs1.getString("expiry"));
				map1.put("name",rs1.getString("name"));
				map1.put("category",rs1.getString("category"));
				map1.put("image",rs1.getString("image"));
				map1.put("user_id",rs1.getString("user_id"));
				map1.put("price",rs1.getString("price"));
				map1.put("quantity",rs1.getString("quantity"));
				map1.put("no_of_items",Integer.toString(rs.getInt("no_of_items")));

				
//				System.out.println("mymap="+mymap);
				

			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
			
			
//			map1.put("prod_details",product_details.toString());
			
			 
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
