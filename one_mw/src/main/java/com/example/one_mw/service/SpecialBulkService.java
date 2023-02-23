package com.example.one_mw.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.example.one_mw.schema.converters.XmlToMapConverter;


@Service
public class SpecialBulkService {

	//Put all services here like TCS request, File services, Database insert .. etc
	
	public List<Map<String,Object>> processMultipartFile(MultipartFile file) throws IOException{
	
		BufferedReader fileReader = new BufferedReader(newReader(file.getInputStream()));
		
		//CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);
		
		Iterable<CSVRecord> csvRecords = CSVFormat.EXCEL.withHeader().parse(fileReader);//csvParser.getRecords();
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		
		for(CSVRecord csvRecord : csvRecords) {
			
			Map<String,Object> row = new HashMap<>();
			
			row.put("msisdn",csvRecord.get("Account MSISDN"));
			row.put("shortcode",csvRecord.get("Account Template"));
			row.put("flag",1);
			row.put("firstname",checkCsvRecordValue(csvRecord, "Fisrt Name"));
			row.put("secondname",checkCsvRecordValue(csvRecord, "Second Name"));
			row.put("thirdname",checkCsvRecordValue(csvRecord, "Third Name"));
			row.put("lastname",checkCsvRecordValue(csvRecord, "Last Name"));
			row.put("fullname",MessageFormat.format("{0} {1} {2} {3}", row.get("firstname").toString(),
					row.get("secondname").toString(),
					row.get("thirdname").toString(),
					row.get("lastname").toString()));
			row.put("addressline1",checkCsvRecordValue(csvRecord, "Address Line"));
			row.put("addressline2",checkCsvRecordValue(csvRecord, "Address Line"));
			row.put("idnumber",checkCsvRecordValue(csvRecord, "ID Number"));
			row.put("idtype",checkCsvRecordValue(csvRecord, "ID Type"));			
			row.put("country",checkCsvRecordValue(csvRecord, "Country"));
			row.put("province",checkCsvRecordValue(csvRecord, "Province"));
			row.put("city",checkCsvRecordValue(csvRecord, "City"));		
			row.put("dateofbirth",checkCsvRecordValue(csvRecord, "Date of Birth"));
			row.put("occupation",checkCsvRecordValue(csvRecord, "Occupation"));
			row.put("gender",checkCsvRecordValue(csvRecord, "Gender"));
			row.put("idissuancedate",checkCsvRecordValue(csvRecord, "ID Issuance Date"));
			row.put("idexpirydate",checkCsvRecordValue(csvRecord, "ID Expiry Date"));
			row.put("email",checkCsvRecordValue(csvRecord, "Email"));
			row.put("accountname",checkCsvRecordValue(csvRecord, "Account Name"));
			
			row.put("shortname",checkCsvRecordValue(csvRecord, "Short Name"));
			row.put("commercialname",checkCsvRecordValue(csvRecord, "Commercial Name"));
			row.put("assignedtdr",checkCsvRecordValue(csvRecord, "Assigned TDR"));
			row.put("registrationno",checkCsvRecordValue(csvRecord, "Registration No"));//Registration No
			row.put("businesslicense",checkCsvRecordValue(csvRecord, "Business License"));//Business License
			
			row.put("taxnumber",checkCsvRecordValue(csvRecord, "TAX Number"));
			row.put("bankname",checkCsvRecordValue(csvRecord, "Bank Name"));
			row.put("branchname",checkCsvRecordValue(csvRecord, "Branch Name"));
			row.put("accountholder",checkCsvRecordValue(csvRecord, "Account Holder"));
			
			row.put("personincharge",checkCsvRecordValue(csvRecord, "Person In Charge"));
			row.put("personinchargemobile",checkCsvRecordValue(csvRecord, "Person In Charge Mobile"));
			row.put("personinchargeidnumber",checkCsvRecordValue(csvRecord, "Person In Charge ID Number"));
			
			row.put("personinchargeidtype",checkCsvRecordValue(csvRecord, "Person In Charge ID Type"));
			row.put("relationship",checkCsvRecordValue(csvRecord, "Relationship"));
			row.put("accounttype",checkCsvRecordValue(csvRecord, "Account Type"));
			row.put("accountsubtype",checkCsvRecordValue(csvRecord, "Account Sub Type"));
			
			result.add(row);
		}
		
		return result;
	}
	
	private Object checkCsvRecordValue(CSVRecord r,String fieldName) {
		
		try {
			
			return r.get(fieldName);
		}
		catch(Exception e) {
			
			return null;
		}
	}
	
	public String doTcsRequest(String xmlBody) {
		
		try {
			
			System.out.print("XML"+xmlBody);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.TEXT_XML);
			
			HttpEntity<String> request = new HttpEntity<String>(xmlBody, headers);
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters()
			        .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
			
			ResponseEntity<String> response = restTemplate.postForEntity("http://10.200.1.49:6060/telepin", request, String.class);
		    
			return response.getBody();
		}
		catch(Exception e) {
		
			e.printStackTrace();
			
			return null;
		}
	}
	
	public void doSimpleGetRequest(String url) {
		
		try {
			
			
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters()
			        .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
			
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		    
			response.getBody();
		}
		catch(Exception e) {
		
			e.printStackTrace();
			
		}
	}

	public String generateXmlBody(Map<String,Object> record) {
		
		String template="<TCSRequest> " + 
				"    <UserName>{0}</UserName> " + 
				"    <Password>{1}</Password> " + 
				"    <TerminalType>WEB</TerminalType> " + 
				"    <UserId>000</UserId> " + 
				"    <Function name=\"CREATEACCOUNTNOTAC\"> " + 
				"        <param1>{2}</param1>   " + 
				"        <param4>3</param4>   " +
				"        <param10>{3}</param10> " + 
				"        <param12>{4}</param12> " + 
				"        <param103>{5}</param103> " + 
				"        <param119>{6}</param119> " + 
				"        <param104>{7}</param104>  " + 
				"        <param147>{8}</param147> " + 
				"        <param122>{9}</param122> " + 
				"        <param105>{10}</param105> " + 
				"        <param127>{11}</param127> " + 
				"        <param106>{12}</param106> " + 
				"        <param107>{13}</param107> " + 
				"        <param114>{14}</param114> " + 
				"        <param113>{15}</param113> " + 
				"        <param110>{16}</param110> " + 
				"        <param111>{17}</param111> " + 
				"        <param118>{18}</param118> " + 
				"        <param120>{19}</param120> " + 
				"        <param242>{20}</param242> " + 
				"        <param126>{21}</param126> " + 
				"        <param278>{22}</param278> " + 
				"        <param137>{23}</param137> " + 
				"        <param200>7</param200> " + 
				"        <param190>{24}</param190> " + 
				"        <param191>{25}</param191> " + 
				"        <param206>{26}</param206> " + 
				"        <param136>{27}</param136> " + 
				"        <param218>{28}</param218> " + 
				"        <param210>{29}</param210> " + 
				"        <param154>{30}</param154> " + 
				"        <param209>{31}</param209> " + 
				"        <param236>{32}</param236> " + 
				"        <param240>{33}</param240> " + 
				"        <param153>{34}</param153> " + 
				"        <param262>{35}</param262> " + 
				"        <param143>{36}</param143> " + 
				"        <param146>{37}</param146> " + 
				"        <param147>{38}</param147> " + 
				"        <param148>{39}</param148> " + 
				"        <param235>{40}</param235> " + 
				"        <param234>{41}</param234> " + 
				"        <param262>{42}</param262> " + 
				"        <Param131>{43}</Param131> " + 
				"        <Param132>{44}</Param132> " + 
				"        <Param133>{45}</Param133> " + 
				"        <Param134>{46}</Param134> " + 
				"        <Param135>{47}</Param135> " + 
				"    </Function> " + 
				"</TCSRequest> " + 
				"";
		
		
		String formatted = MessageFormat.format(template, 
				record.get("username"),
				record.get("password"),
				record.get("msisdn"),
				record.get("shortcode"),
				record.get("flag"),
				checkXmlBodyStringValue(record, "firstname"),//<param103>{5}</param103>
				checkXmlBodyStringValue(record, "secondname"),//<param119>{6}</param119>
				checkXmlBodyStringValue(record, "lastname"),//<param104>{7}</param104>
				checkXmlBodyStringValue(record, "thirdname"),//<param147>{8}</param147>
				checkXmlBodyStringValue(record, "fullname"),//<param122>{9}</param122>
				checkXmlBodyStringValue(record, "addressline1"),//<param105>{10}</param105>
				checkXmlBodyStringValue(record, "addressline2"),//<param127>{11}</param127>
				checkXmlBodyStringValue(record, "idnumber"),//12
				checkXmlBodyStringValue(record, "idtype"),//13
				checkXmlBodyStringValue(record, "province"),//14
				checkXmlBodyStringValue(record, "city"),//15
				checkXmlBodyStringValue(record, "dateofbirth"),//16
				checkXmlBodyStringValue(record, "occupation"),//17
				checkXmlBodyStringValue(record, "gender"),//18
				checkXmlBodyStringValue(record, "idexpirydate"),//19
				checkXmlBodyStringValue(record, "idissuancedate"),//20
				checkXmlBodyStringValue(record, "email"),//21
				checkXmlBodyStringValue(record, "accountname"),//22
				"254",//23
				"",//24
				checkXmlBodyStringValue(record, "businesslicense"),//25
				checkXmlBodyStringValue(record, "commercialname"),//26
				checkXmlBodyStringValue(record, "shortname"),//27
				"",//28
				checkXmlBodyStringValue(record, "registrationno"),//29
				"",//30
				checkXmlBodyStringValue(record, "taxnumber"),//31
				"",//32
				"",//33
				"",//34
				"",//35
				"",//36
				"",//37
				"",//38
				"",//39
				"",//40
				"",//41
				"",//42
				"",//43
				"",//44
				"",//45
				"",//46
				"");//47
		
		return formatted;
	}
	
	public String generateXmlBody4VerifyAccount(Map<String,Object> record) {
		
		String template="<TCSRequest> " + 
				"    <UserName>{0}</UserName> " + 
				"    <Password>{1}</Password> " + 
				"    <TerminalType>WEB</TerminalType> " + 
				"    <UserId>000</UserId> " + 
				"    <Function name=\"VERIFYACCOUNTLEVEL\"> " + 
				"        <param1>{2}</param1>   " + 
				"        <param100>7</param100> " + 
				"    </Function> " + 
				"</TCSRequest> " + 
				"";
		
		
		String formatted = MessageFormat.format(template, 
				record.get("username"),
				record.get("password"),
				record.get("msisdn"));
		
		return formatted;
	}
	
	private String checkXmlBodyStringValue(Map<String,Object> o,String fieldName) {
		
		if(o.get(fieldName) == null)
			return "";
		else
			return o.get(fieldName).toString();
	}

	
	@SuppressWarnings("unused")
	private String checkXmlBodyDateValue(Map<String,Object> o,String fieldName) {
		
		if(o.get(fieldName) == null)
			return "";
		else {
			
			LocalDate d = LocalDate.parse(o.get(fieldName).toString());
			String telepinPattern = "ddMMyyyy";
			DateTimeFormatter telepinDateFormatter = DateTimeFormatter.ofPattern(telepinPattern);
			
			return telepinDateFormatter.format(d);
		}
			
	}

	public Boolean isSuccessRequest(String xml) throws Exception {
		
		Map<String, Object> result = XmlToMapConverter.convertXmlToFlattenedMap(xml);
		
		return result.get("Result").toString().contentEquals("0");
	}
	
	private InputStreamReader newReader(final InputStream inputStream) {
	    return new InputStreamReader(new BOMInputStream(inputStream), StandardCharsets.UTF_8);
	}
}
