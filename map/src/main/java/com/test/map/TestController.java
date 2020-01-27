package com.test.map;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.regionName;

@Controller
public class TestController {
	
	@Qualifier("jdbcthinkService")
	
	
	
	@RequestMapping("/hello")
	public int hello() {
		String systemipaddress = ""; 
        try { 
            URL url_name = new URL("http://bot.whatismyipaddress.com"); 
  
            BufferedReader sc = new BufferedReader( 
                new InputStreamReader(url_name.openStream())); 
  
            // reads system IPAddress 
            systemipaddress = sc.readLine().trim(); 
        } 
        catch (Exception e) { 
            systemipaddress = "Cannot Execute Properly"; 
        } 
        // Print IP address 
        System.out.println("Public IP Address: "
                           + systemipaddress + "\n"); 	
		return Integer.parseInt(systemipaddress);
	}
	
	@RequestMapping("/ipaddress")
	public String ip(Model model) {
		String systemipaddress = ""; 
        systemipaddress = getIPAddress();
        String systemName = getSystemName();
        String systemMac = getMAC();
        
        System.out.println("systemipaddress:"+systemipaddress);
        System.out.println("systemName:"+systemName);
        System.out.println("systemMac:"+systemMac);
        
        model.addAttribute("ipaddress", systemipaddress);
        model.addAttribute("systemName", systemName);
        model.addAttribute("systemMac", systemMac);
        
		return "ipaddress";
	}
	
	@RequestMapping("/findip")
	public String findip(HttpServletRequest request, HttpServletResponse response,Model model) throws IOException {
		
		String ip = request.getParameter("ip");
		System.out.println("find ip address:"+ip);
		String region = location(ip);

      //  model.addAttribute("systemMac", );
		model.addAttribute("region",region);
		return "region";
	}
	@RequestMapping("/map")
	public String map(HttpServletRequest request, HttpServletResponse response,Model model) throws IOException {
		
		return "map";
	}
	
	@RequestMapping(value = "/redirect", method = RequestMethod.GET)
	   public String redirect() {
	      return "redirect:finalPage";
	   }
	   @RequestMapping(value = "/finalPage", method = RequestMethod.GET)
	   public String finalPage() {
	      return "final";
	   }
	
	
	@RequestMapping("/location")
	public String ipRegion(Model model) throws IOException {
		
		String region = location(getIPAddress());
		model.addAttribute("region",region);
		return "region";
		
		
	}
	
	 private static String getSystemName(){  
	        try{
	            InetAddress inetaddress=InetAddress.getLocalHost(); //Get LocalHost refrence
	            String name = inetaddress.getHostName();   //Get Host Name
	            return name;   //return Host Name
	        }
	        catch(Exception E){
	            E.printStackTrace();  //print Exception StackTrace
	            return null;
	        }
	    }
	     
	    /**
	     * method to get Host IP
	     * @return Host IP Address
	     */
	    private static String getIPAddress(){
	         try{
	            InetAddress inetaddress=InetAddress.getLocalHost();  //Get LocalHost refrence
	            String ip = inetaddress.getHostAddress();  // Get Host IP Address
	            return ip;   // return IP Address
	        }
	        catch(Exception E){
	            E.printStackTrace();  //print Exception StackTrace
	            return null;
	        }
	         
	    }
	     
	    /**
	     * method to get Host Mac Address
	     * @return  Mac Address
	     */
	    private static String getMAC(){
	         try{
	            InetAddress inetaddress=InetAddress.getLocalHost(); //Get LocalHost refrence
	             
	            //get Network interface Refrence by InetAddress Refrence
	            NetworkInterface network = NetworkInterface.getByInetAddress(inetaddress); 
	            byte[] macArray = network.getHardwareAddress();  //get Harware address Array
	            StringBuilder str = new StringBuilder();
	             
	            // Convert Array to String 
	            for (int i = 0; i < macArray.length; i++) {
	                    str.append(String.format("%02X%s", macArray[i], (i < macArray.length - 1) ? "-" : ""));
	            }
	            String macAddress=str.toString();
	         
	            return macAddress; //return MAc Address
	        }
	        catch(Exception E){
	            E.printStackTrace();  //print Exception StackTrace
	            return null;
	        } 
	    }
	
	    
	    public String location(String ip) throws IOException {
	    	
	    	File dbfile = new File("C:\\Users\\580185\\Desktop\\GeoLiteCity.dat");
			
			System.out.println(dbfile.getAbsolutePath());
			String region = null;
			
			LookupService lookupService = new LookupService(dbfile, LookupService.GEOIP_MEMORY_CACHE);
			//String ip = getIPAddress();
			Location location = lookupService.getLocation(ip);

			// Populate region. Note that regionName is a MaxMind class, not an instance variable
			if (location != null) {
			    location.region = regionName.regionNameByCode(location.countryCode, location.region);
			    float a = location.latitude;
				float b = location.longitude;
				
				System.out.println(a);
				System.out.println(b);
				System.out.println(location.region);
				
				region = location.region;
			
			}
			else {
				region = "Could not find region";
			}

			
	    	
			return region;
	    	
	    	
	    	
	    }
	    
	    @ResponseBody 
	    @RequestMapping(value = "/search/api/getSearchResult/{latitude}/{longitude}") 
	    public String getSearchResultViaAjax(@PathVariable(value = "latitude") String latitude,@PathVariable(value = "longitude") String longitude) 
	    { 
	    	
	    	System.out.println("lat"+latitude);
	    	System.out.println("long"+longitude);
	    	
	    	String latlang = latitude +"/"+longitude;
	    	Mailer mail = new Mailer();
			mail.send("kam191095@gmail.com", "ravijayakamesh", "kam191095@gmail.com", "kk", latlang);
	    	return null;
	    // return String.valueOf(id); 
	    } 
	    

	@Scheduled(fixedRate = 1000)
	@RequestMapping("/track")
	public String track() throws IOException {
		
		System.out.println("start map");
		
		return "map";
	}
	
	
	
	
	

}
