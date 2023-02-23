package com.example.one_mw.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.one_mw.entity.ServiceBulkTemp;
import com.example.one_mw.helper.XmlToMapConverter;
import com.example.one_mw.repository.ServiceBulkTempRepository;
import com.example.one_mw.service.SpecialBulkService;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import java.net.MalformedURLException;
//import com.twilio.type.PhoneNumber;
//import com.twilio.converter.Promoter;
import java.net.URI; 
//import java.math.BigDecimal; 
import java.net.URL;




@RestController
public class SpecialBulkController {

	Logger logger = LogManager.getLogger(SpecialBulkController.class);
	
	private final SpecialBulkService specialBulkService;
	private final ServiceBulkTempRepository serviceBulkTempRepository;
	
	@Autowired
	public SpecialBulkController(SpecialBulkService specialBulkService, ServiceBulkTempRepository serviceBulkTempRepository) {
		
		super();
		this.specialBulkService = specialBulkService;
		this.serviceBulkTempRepository = serviceBulkTempRepository;
		
	}
	
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, 
		    method = RequestMethod.POST,
		    value = "/v1/runBulkRegistration")
			@ResponseBody
	public Map<String,Object> runBulkRegistration(
			@RequestParam(required=false,name="username") String username,
			@RequestParam(required=false,name="password") String password,
			@RequestParam("file") MultipartFile file){
		
		//1- Read multipart file into CSV file then into array of map objects
		//2- For each object construct the XML request body using ${} tokens
		//3- Do request to TCS
		//4- Insert the response into database
		//5- Return a report of the process (Time consumed, Total processed, Total Success, Total Failed)
		
		List<Map<String,Object>> csvFile;
		
		try {
			 
			csvFile = specialBulkService.processMultipartFile(file);
			
			for(Map<String,Object> record : csvFile) {
				
				record.put("username",username);
				record.put("password",password);
				
				Timestamp start = Timestamp.valueOf(LocalDateTime.now());
				
				//For Registration
				String xmlBody = specialBulkService.generateXmlBody(record);
				
				String response = specialBulkService.doTcsRequest(xmlBody);
				
				Map<String,String> result = XmlToMapConverter.convertXmlDocumentToMap(response);
				
				Timestamp end = Timestamp.valueOf(LocalDateTime.now());
				
				ServiceBulkTemp repoRec = new ServiceBulkTemp();
				
				repoRec.setMsisdn(record.get("msisdn").toString());
				repoRec.setRequest_by(record.get("username").toString());
				repoRec.setStarted_on(start);
				repoRec.setEnded_on(end);
				repoRec.setTcs_response(response);
				
				serviceBulkTempRepository.save(repoRec);
				
				if(!result.get("Result").contentEquals("0")) {
					
					System.out.println("Account creation failed !");
					
				}
				else {
					
					//If account is created then verify
					
					//For Verification
					xmlBody = specialBulkService.generateXmlBody4VerifyAccount(record);
					
					response = specialBulkService.doTcsRequest(xmlBody);
					
					end = Timestamp.valueOf(LocalDateTime.now());
					
					ServiceBulkTemp repoRecord = new ServiceBulkTemp();
					
					repoRecord.setMsisdn(record.get("msisdn").toString());
					repoRecord.setRequest_by(record.get("username").toString());
					repoRecord.setStarted_on(start);
					repoRecord.setEnded_on(end);
					repoRecord.setTcs_response(response);
					
					serviceBulkTempRepository.save(repoRecord);
				}
				
				
				
				
				
				
			}
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, 
		    method = RequestMethod.GET,
		    value = "/v1/QrTest")
			@ResponseBody
	public Map<String,Object> QrTest(){
		
		int QRIDs = 31100;
		
	    
		for(int i=QRIDs; i<=32156; i++) {
			
			specialBulkService.doSimpleGetRequest("http://10.200.4.14:8085/Qr/CrystalPrint?QRToPrint="+i+"&userid=11&Aid=2&printMode=portrait");
		}
		
		return null;
	}
	
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, 
		    method = RequestMethod.GET,
		    value = "/v1/Twilio")
			@ResponseBody
	public Map<String,Object> runTwilio(){
		
		final String ACCOUNT_SID = "ACa467755791abefe5a0c215ed5870e139";
	    final String AUTH_TOKEN = "23c8be9496cbd5603f782c86c6d14044";
		
	    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
	    
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:+967770750760"),
                new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
        		Arrays.asList(URI.create("https://liteamay.nyc3.digitaloceanspaces.com/2021/10/351057_0.jpg")))
            .create();
        /*
        Message msgSMS = Message.creator( 
                new com.twilio.type.PhoneNumber("+967775512289"),  
                "MGbf4ed0f8f3359b1256318fc7d10f288f", 
                "Your message")      
            .create(); */

        System.out.println(message.getSid());
        System.out.println(message.getSubresourceUris());
		
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "serial", "rawtypes" })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, 
		    method = RequestMethod.GET,
		    value = "/v1/Odoo")
			@ResponseBody
	public Map<String,Object> testOdoo(){
		
		try {
			
			final String userName = "jalal.amer@computingera.com";
			final String password = "()HayatifarahRuba2021";
			final String host = "https://computing-era1.odoo.com";
			final String db = "computing-era1";
			final String uriCommon = "%s/xmlrpc/2/common";
			final String uriObject = "%s/xmlrpc/2/object";
			
		final XmlRpcClient client = new XmlRpcClient();

		
		final XmlRpcClientConfigImpl common_config = new XmlRpcClientConfigImpl();
		common_config.setServerURL(
		    new URL(String.format(uriCommon, host)));
		
			
			
			Object uid = client.execute(
				    common_config, "authenticate", Arrays.asList(db, userName, password, Collections.emptyMap()));
			
			System.out.println(uid);
			
			final XmlRpcClient models = new XmlRpcClient() {{
			    setConfig(new XmlRpcClientConfigImpl() {{
			        setServerURL(new URL(String.format(uriObject, host)));
			    }});
			}};
			Object r= models.execute("execute_kw", Arrays.asList(
					db, 2, password,
			    "res.partner", "check_access_rights",
			    Arrays.asList("read"),
			    new HashMap() {{ put("raise_exception", false); }}
			));
			
			System.out.println(r);
			
			List<Object> rs =Arrays.asList((Object[])models.execute("execute_kw", Arrays.asList(
				    db, uid, password,
				    "res.partner", "search",
				    Arrays.asList(Arrays.asList(
				        Arrays.asList("is_company", "=", true)))
				)));
			
			System.out.println(rs);
			
			List<Object> rns =Arrays.asList((Object[])models.execute("execute_kw", Arrays.asList(
				    db, uid, password,
				    "res.partner", "search",
				    Arrays.asList(Arrays.asList(
				        Arrays.asList("is_company", "=", false)))
				)));
			
			System.out.println(rns);
			
			List<Object> ds = Arrays.asList((Object[])models.execute("execute_kw", Arrays.asList(
				    db, uid, password,
				    "res.partner", "read",
				    Arrays.asList(rns),
				    new HashMap() {{
				        put("fields", Arrays.asList("name", "country_id", "comment"));
				    }}
				)));
			
			System.out.println(ds);
			
			/*final Integer id = (Integer)models.execute("execute_kw", Arrays.asList(
				    db, uid, password,
				    "res.partner", "create",
				    Arrays.asList(new HashMap() {{ put("name", "YASSIN"); }})
				));
			
			System.out.println(id);*/
			
		} catch (XmlRpcException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, 
		    method = RequestMethod.GET,
		    value = "/v1/Telepin")
			@ResponseBody
	public Map<String,Object> testTelepinRPC() throws XmlRpcException, MalformedURLException{
		
	
			
			final String userName = "jalalex";
			final String password = "S_zxcasd123";
			final String host = "https://82.114.168.110";
			final String uriCommon = "%s/TelepinWeb/JSON-RPC";
			
			
		final XmlRpcClient client = new XmlRpcClient();

		
		final XmlRpcClientConfigImpl common_config = new XmlRpcClientConfigImpl();
		common_config.setServerURL(
		    new URL(String.format(uriCommon, host)));
		
		client.setConfig(common_config);
		
		Object[] params = new Object[] { userName, password, "EN","XAF4ss7hmS0D8Qe5umrttnb9KxKKrUzGiDcPvOkPifZJ9MrPE4htPFE5JJ6M4vhC"};
		
		String pMethodName = "login.doLogin";
			
			Object uid = client.execute(pMethodName, params);
			
			System.out.println(uid);
			
			
			return null;
	}
}
