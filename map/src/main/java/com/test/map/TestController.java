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

@RestController
public class TestController {
	
	
	@RequestMapping("/map")
	public String map(HttpServletRequest request, HttpServletResponse response,Model model) throws IOException {
		
		return "map";
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
	    
	

}
