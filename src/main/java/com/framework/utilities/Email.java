package com.framework.utilities;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.zeroturnaround.zip.ZipUtil;

public class Email extends Utils{

	public static int fErrorCount = 0;
	//reportFileName = TestExecutionResultFileName
	public static void execute(String strDate) throws Exception

	{ 
		System.out.println("In Email Class");
		System.out.println("In Email Execute");
		//zipFolder(strDate);
		//String sourceDirPath = OR.getConfig("reportOutputPath")+strDate;
		//String zipFilePath = OR.getConfig("reportOutputPath")+strDate;		
		//pack(sourceDirPath, zipFilePath);		
		//String attachmentPath = OR.getConfig("attachmentPath")+strDate+".zip";		
		//OR.getConfig("attachmentPath");  
		String to=OR.propertyFile(OR.config_Prop).getProperty("to").toString();//.split(",");
		String cc=OR.propertyFile(OR.config_Prop).getProperty("cc").toString();//.split(",");
		String from = OR.propertyFile(OR.config_Prop).getProperty("from").toString();

		//String userName =OR.getConfig("userName");
		//String passWord =OR.getConfig("passWord");
		String host=OR.getConfig("host");
		String port=OR.getConfig("port");
		String subject = OR.getConfig("subject");
		String text = OR.getConfig("text");
		String reportFileName = "";

		Email.SendMail(to,cc,subject,text,from,host,port/*,attachmentPath*/);
	}

	public static String reportDate()
	{
		SimpleDateFormat sdfdate = new SimpleDateFormat("ddMMMyyyyhhmmss"); //15Nov2017113044
		String timeZone = "IST";
		Date date = new Date();
		sdfdate.setTimeZone(TimeZone.getTimeZone(timeZone));
		System.out.println(sdfdate.format(date));
		return sdfdate.format(date);
	}

	public static void zipFolder(String strDate)
	{
		String reportOutputPath = OR.getConfig("reportOutputPath");
		ZipUtil.pack(new File(reportOutputPath+strDate), 
				new File(reportOutputPath+strDate+".zip"));
	}

	public static void pack(String sourceDirPath, String zipFilePathh) throws IOException {
		String zipFilePath = zipFilePathh+".zip";
		Path p = Files.createFile(Paths.get(zipFilePath));
		try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
			Path pp = Paths.get(sourceDirPath);
			Files.walk(pp)
			.filter(path -> !Files.isDirectory(path))
			.forEach(path -> {
				ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
				try {
					zs.putNextEntry(zipEntry);
					Files.copy(path, zs);
					zs.closeEntry();
				} catch (IOException e) {
					System.err.println(e);
				}
			});
		}
	}

	public static String bodyText() throws Exception
	{
		String bodyPath= OR.getConfig("bodyText"); 
		InputStream is = new FileInputStream(bodyPath); 
		BufferedReader buf = new BufferedReader(new InputStreamReader(is)); 
		String line = buf.readLine(); 
		StringBuilder sb = new StringBuilder(); 
		while(line != null)
		{ sb.append(line).append("\n"); 
		line = buf.readLine(); 
		} 
		String fileAsString = sb.toString(); 
		return fileAsString;
	}



	public static void SendMail(String aStrTORecipients, String aStrCCRecipients, String aStrSubject, String aStrMessage,
			String aStrFrom, String SMTP_HOST_NAME, String SMTP_PORT/*,String attachmentPath*/) throws Exception {

		boolean debug = false;
		System.out.println("IN Send Mail");       
		try {


			// Set the host smtp address
			Properties props = System.getProperties();

			props.put(
					"mail.smtp.host",
					SMTP_HOST_NAME);
			props.put(
					"mail.smtp.port",
					SMTP_PORT);

			Session lObjSession = Session.getDefaultInstance(
					props,
					null);

			Message lObjMsg = new MimeMessage(lObjSession);

			// ------------------------------------------------

			// set the from and to address
			InternetAddress lObjInternetAddressFrom = new InternetAddress(aStrFrom);

			lObjMsg.setFrom(lObjInternetAddressFrom);

			// ------------------------------------------------

			InternetAddress[] lObjInternetAddressTo = null;

			if (aStrTORecipients.contains(";")) {

				String[] lArrStrRecipients = aStrTORecipients.split(";");

				if (lArrStrRecipients == null) {
					System.out.println("Please provide recipient address list in this format.  aaa@123.com;bbb@123.com... etc");
					return;
				}

				int lIntToCount = lArrStrRecipients.length;

				lObjInternetAddressTo = new InternetAddress[lIntToCount];

				for (int lIntIndex = 0; lIntIndex < lIntToCount; lIntIndex++) {
					String lStrToMailIDTemp = lArrStrRecipients[lIntIndex].trim();

					if (lStrToMailIDTemp.trim() == "")
						continue;

					if (!ValidateEmail(lStrToMailIDTemp)) {
						System.out.println("Invalid recipient email ID(s).");
						return;
					}

					lObjInternetAddressTo[lIntIndex] = new InternetAddress(lStrToMailIDTemp);
				}

			} else {

				if (!ValidateEmail(aStrTORecipients)) {
					System.out.println("Invalid recipient email ID(s).");
					return;
				}

				lObjInternetAddressTo = new InternetAddress[1];
				lObjInternetAddressTo[0] = new InternetAddress(aStrTORecipients.trim());
			}

			lObjMsg.addRecipients(
					Message.RecipientType.TO,
					lObjInternetAddressTo);

			// ------------------------------------------------

			if (aStrCCRecipients != null) {

				if (aStrCCRecipients.trim() != "") {

					InternetAddress[] lObjInternetAddressCC = null;

					if (aStrCCRecipients.contains(";")) {

						String[] lArrStrRecipients = aStrCCRecipients.split(";");

						if (lArrStrRecipients == null) {
							System.out.println("Please provide recipient address list in this format.  aaa@123.com;bbb@123.com... etc");
							return;
						}

						int lIntCCCount = lArrStrRecipients.length;

						lObjInternetAddressCC = new InternetAddress[lIntCCCount];

						for (int lIntIndex = 0; lIntIndex < lIntCCCount; lIntIndex++) {
							String lStrCCMailIDTemp = lArrStrRecipients[lIntIndex].trim();

							if (lStrCCMailIDTemp.trim() == "")
								continue;

							if (!ValidateEmail(lStrCCMailIDTemp)) {
								System.out.println("Invalid recipient email ID(s).");
								return;
							}

							lObjInternetAddressCC[lIntIndex] = new InternetAddress(lStrCCMailIDTemp);
						}

					} else {

						if (!ValidateEmail(aStrCCRecipients)) {
							System.out.println("Invalid CC recipient email ID(s).");
							return;
						}

						lObjInternetAddressCC = new InternetAddress[1];
						lObjInternetAddressCC[0] = new InternetAddress(aStrCCRecipients.trim());
					}

					lObjMsg.addRecipients(
							Message.RecipientType.CC,
							lObjInternetAddressCC);

				}

			}

			// ------------------------------------------------

			// Setting the Subject and Content Type
			lObjMsg.setSubject(aStrSubject);
			lObjMsg.setContent(
					aStrMessage,
					"text/html");
			lObjMsg.setSentDate(new Date());

			//			String bodyPath= OR.getConfig("bodyPath"); 
			//			InputStream is = new FileInputStream(bodyPath); 
			//			BufferedReader buf = new BufferedReader(new InputStreamReader(is)); 
			//			String line = buf.readLine(); 
			StringBuilder sb = new StringBuilder(); 

			/*if(line != null)
			{ 			
				sb.append(line).append("\n"); 
				line = buf.readLine(); 
				line = line.split("<b align=\"center\">Result Summary Table</b>")[0];//+"</body></html>";
				System.out.println("Line is :: "+line);
				sb.append(line).append("\n"); 		
			} */

			String lStrFinalMessage = "";
			lStrFinalMessage = BuildMessageBody("results");

			String strSummary = CreateSummary();			
			sb.append(strSummary);
			sb.append(lStrFinalMessage);
			String fileAsString = sb.toString(); 
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(fileAsString, "text/html");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			/*if (attachmentPath != null && attachmentPath.length() > 0) {
				MimeBodyPart attachPart = new MimeBodyPart();

				try {
					attachPart.attachFile(attachmentPath);
				} catch (IOException ex) {
					ex.printStackTrace();
				}

				multipart.addBodyPart(attachPart);
			}*/

			/*MimeBodyPart textPart = new MimeBodyPart();
		     textPart.setContent(bodyText(),"text/html");
		     multipart.addBodyPart(textPart);*/


			lObjMsg.setContent(multipart);

			try {

				Transport.send(lObjMsg);
				System.out.println("Mail sent.");

			} catch (Exception e) {
				// TODO: handle exception				
				System.out.println(e.getMessage().toString());
			}

		} catch (Exception e) {			
			System.out.println(e.getMessage());
		}

	}

	public static boolean ValidateEmail(String sEmail) {

		try {

			String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			Pattern pattern;
			Matcher matcher;

			pattern = Pattern.compile(EMAIL_PATTERN);

			matcher = pattern.matcher(sEmail);
			return matcher.matches();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}

	}

	public static String BuildMessageBody(String aStrLogDirFilePath)  {

		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$BuildMessageBody");
		String lStrFinalMessage = "";

		//lStrFinalMessage += "";

		try {

			File lFileLogDir = new File(aStrLogDirFilePath);
			System.out.println("Dir Path : " + aStrLogDirFilePath);
			File[] files = lFileLogDir.listFiles();
			Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
			System.out.println("\nLast Modified Ascending Order (LASTMODIFIED_COMPARATOR)");
			System.out.println("List Of files: "+files);
			for (File lCurrFile : files) {
				String lStrCurrPath = lCurrFile.getAbsolutePath();
				if (lStrCurrPath == null)
					continue;
				if (lStrCurrPath.trim() == "")
					continue;
				System.out.println("Reading file : " + lStrCurrPath);
				if (lStrCurrPath.toLowerCase().endsWith(".xml"))
					lStrFinalMessage = lStrFinalMessage + ReadFileAndCreateRowsData(lStrCurrPath);
			}

			return lStrFinalMessage;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return lStrFinalMessage;

		}
	}

	static String ReadFileAndCreateRowsData(String aStrFilePath) throws Exception {

		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$ ReadFileAndCreateRowsData");
		String lStrFinalRowsData = "";
		String lStrFinalMessge = "";
		Boolean lErrorCounted = false;
		int lFailedCount = 0;
		String lStrTempRowData = "";

		String lStrSNo = "";
		String lStrResult = "";
		String lStrTestStep = "";
		String lStrTestData = "";
		FileInputStream fStream = null;
		DataInputStream dataInput = null;
		BufferedReader breader= null;

		try {

			File file = new File(aStrFilePath);

			if (!file.exists())
				return lStrFinalRowsData;

			System.out.println("Creating mail data");

			fStream = new FileInputStream(aStrFilePath);
			dataInput = new DataInputStream(fStream);
			breader = new BufferedReader(new InputStreamReader(dataInput));

			File fXmlFile;
			if (aStrFilePath.toLowerCase().endsWith(".xml"))
				fXmlFile = new File(aStrFilePath);
			else
				return "";
			System.out.println("Creating mail data");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			String strKey[] = aStrFilePath.replace('\\', ' ').replace('/',' ').trim().split(" ");		
			String strKey1 = strKey[strKey.length-1];	
			strKey1 = strKey1.split("\\.")[0];
			String executeTime = ReadValueFromPropertiesFile(strKey1,"ExecutionTime.properties");
			String lStrScenarioReportHeader = doc.getDocumentElement().getAttribute("scenarioName")+" : "+doc.getDocumentElement().getAttribute("description")+" (Execution Time:: "+executeTime+" Mins)";
			String lStrScenarioModule = doc.getDocumentElement().getAttribute("scenarioModule");
			System.out.println ("lStrScenarioReportHeader: "+lStrScenarioReportHeader);

			NodeList nList = doc.getElementsByTagName("testRow");

			String lTempResult = "";



			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					lStrSNo  = "";
					lStrSNo += "<td align='center'>";
					lStrSNo += temp +1;
					lStrSNo += "</td>";

					lStrTestStep = "";
					lStrTestStep = 	"<td>";
					lStrTestStep += eElement.getElementsByTagName("rowText").item(0).getTextContent();
					lStrTestStep += "</td>";


					lStrTestData += "";
					lStrTestData  = "<td>";
					lStrTestData += eElement.getElementsByTagName("testData").item(0).getTextContent();
					lStrTestData += "</td>";


					lTempResult = eElement.getElementsByTagName("status").item(0).getTextContent();
					System.out.println("Status is :: "+ lTempResult.trim().toUpperCase());
					lStrResult = "";
					if (lTempResult.trim().toUpperCase().contains(
							"PASS")) {
						fHmScenarios.put(lStrScenarioReportHeader, "PASSED");
						lStrResult += "<td align='center' style='color: #FFFFFF;'  bgcolor='#006400'>";
						lStrResult += lTempResult.trim();
						lStrResult += "</td>";

					}

					else if (lTempResult.trim().toUpperCase().contains(
							"INFO")) {

						lStrResult += "<td align='center' style='color: #C1CDCD;'  bgcolor='#F8F8FF'>";
						lStrResult += lTempResult.trim();
						lStrResult += "</td>";

					} else if (lTempResult.trim().toUpperCase().contains(
							"WARNING")) {

						lStrResult += "<td align='center' style='color: #FFFFFF;'  bgcolor='#1874CD'>";
						lStrResult += lTempResult.trim();
						lStrResult += "</td>";

					}

					else {
						fHmScenarios.put(lStrScenarioReportHeader, "FAILED");						
						lStrResult += "<td align='center' style='color: #FFFFFF;'  bgcolor='#CD0000'>";
						lStrResult += lTempResult.trim();
						lStrResult += "</td>";
						lFailedCount++;
					}
					lStrTempRowData = "<tr>";
					lStrTempRowData += lStrSNo + lStrTestStep + lStrTestData + lStrResult;
					lStrTempRowData += "</tr>";

					// *******************************

					if (lStrTempRowData.trim() != "")
						lStrFinalRowsData += lStrTempRowData;					

				}


			}		
			if(lFailedCount > 0){
				fHmScenarios.put(lStrScenarioReportHeader, "FAILED");
				if(!fHmModuleWiseStats.containsKey(lStrScenarioModule+":FAILED"))
				{					
					fHmModuleWiseStats.put(lStrScenarioModule+":FAILED", 1);					
				}
				else{					
					fHmModuleWiseStats.put(lStrScenarioModule+":FAILED", fHmModuleWiseStats.get(lStrScenarioModule+":FAILED")+1);
				}						
			}
			else
			{
				if(!fHmModuleWiseStats.containsKey(lStrScenarioModule+":PASSED"))
				{					
					fHmModuleWiseStats.put(lStrScenarioModule+":PASSED", 1);
				}
				else{					
					fHmModuleWiseStats.put(lStrScenarioModule+":PASSED", fHmModuleWiseStats.get(lStrScenarioModule+":PASSED")+1);
				}
			}

			if(!fHmModuleWiseExecutiontime.containsKey(lStrScenarioModule))	
			{					
				if(executeTime.isEmpty()){
					executeTime = "0";
					System.out.println("Debug:: Execution time for "+lStrScenarioReportHeader+" is not present in ExecutionTime.properties file");
				}				
				fHmModuleWiseExecutiontime.put(lStrScenarioModule, Integer.parseInt(executeTime));
			}
			else{
				
				if(executeTime.isEmpty()){
					executeTime = "0";
					System.out.println("Debug:: Execution time for "+lStrScenarioReportHeader+" is not present in ExecutionTime.properties file");
				}				
				fHmModuleWiseExecutiontime.put(lStrScenarioModule, fHmModuleWiseExecutiontime.get(lStrScenarioModule)+Integer.parseInt(executeTime));
			}

			if (lStrFinalRowsData.trim() != "") {
				lStrFinalMessge = CreateFinalHtmlEmailBodyTable(
						lStrFinalRowsData,
						lStrScenarioReportHeader,
						true);
				breader.close();
				dataInput.close();
				fStream.close();
				return lStrFinalMessge;
			}

			return lStrFinalMessge;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			breader.close();
			dataInput.close();
			fStream.close();
			return lStrFinalMessge;
		}		

	}

	static String CreateFinalHtmlEmailBodyTable(String RowsData, String TableHeaderText, Boolean IsEmailBodyMessagePart)
			throws Exception {

		String lStrFinalMessage = "";

		try {

			String lStrTableBorder = "0";

			if (IsEmailBodyMessagePart) {
				lStrTableBorder = "4";
			}

			lStrFinalMessage = "<html><body>";

			lStrFinalMessage += "<STYLE TYPE='text/css'>TD{ font-size: 11pt;}Th{ font-size: 13.5pt;}</STYLE>";

			lStrFinalMessage += "<Table cellspacing='4' cellpadding='4' border='" + lStrTableBorder
					+ "' style='color: #292421' align='center' WIDTH='75%'  >";

			String lStrTrColor = "#008B8B";

			if (IsEmailBodyMessagePart) {
				lStrTrColor = "#0000FF"; // Brown
			}

			lStrFinalMessage += "<tr align='center' style='color: " + lStrTrColor + "'><TH COLSPAN='4'><H3>"
					+ TableHeaderText + "</H3></TH></tr>";

			if (IsEmailBodyMessagePart) {

				lStrFinalMessage += "<tr align='center'  style='font-weight: bold;color: #DC143C'><TH>S No</TH><TH>Test Step</TH><TH>Test Data</TH><TH>Result</TH></tr>";

				// *************************************************

				lStrFinalMessage += RowsData;

				// *************************************************

				lStrFinalMessage += "</tr>";
			}

			lStrFinalMessage += "</table>";
			lStrFinalMessage += "</body></html>";
			lStrFinalMessage += "<br/>";

			return lStrFinalMessage;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return lStrFinalMessage;
		}
	}

	static String CreateSummary() throws Exception
	{
		String aStrBodyHead="<br />";
		aStrBodyHead += "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">"
				+"<html xmlns=\"http://www.w3.org/1999/xhtml\">"
				+ "<body>";
		aStrBodyHead = aStrBodyHead+"<table cellpadding='4' cellspacing='4' border='4' align='center'>";     
		aStrBodyHead = aStrBodyHead+"<th>Executed By</th>";
		aStrBodyHead = aStrBodyHead+"<th>OPERA Version</th>";
		aStrBodyHead = aStrBodyHead+"<th>OPERA UI GIT Hash</th>";
		aStrBodyHead = aStrBodyHead+"<th>OPERA WS GIT Hash</th>";
		aStrBodyHead = aStrBodyHead+"<th>For any queries, Please contact</th>";
		aStrBodyHead = aStrBodyHead+"<tr>";      
		aStrBodyHead = aStrBodyHead+"<td align='right'> " + getDateAsString() + " " + " | " + getSysInfoAsString() + "</td>";     
		aStrBodyHead = aStrBodyHead+"<td align='right'> " + ReadValueFromPropertiesFile("OPERAVersion","ExecutionTime.properties") + "</td>";     
		aStrBodyHead = aStrBodyHead+"<td align='right'> " + ReadValueFromPropertiesFile("OPERAUIGITHash","ExecutionTime.properties") + "</td>";     
		aStrBodyHead = aStrBodyHead+"<td align='right'> " + ReadValueFromPropertiesFile("OPERAWSGITHash","ExecutionTime.properties") + "</td>";
		aStrBodyHead = aStrBodyHead+"<td align='right'>opera_qa_automation_architects_us_grp@oracle.com </td>";
		aStrBodyHead = aStrBodyHead+"</tr>";
		aStrBodyHead = aStrBodyHead+"</table>";
		aStrBodyHead += "<br />";
		aStrBodyHead = aStrBodyHead+"<table cellspacing='4' cellpadding='4' border='4' style='color: #292421' align='center'>"; 
		aStrBodyHead = aStrBodyHead+"<tr style='font-weight: bold; color: #0000FF; font-size: 13.5pt'><TH COLSPAN='4'>Overall Module Wise Test Execution Summary</TH></tr>";
		aStrBodyHead = aStrBodyHead+"<tr style='font-weight: bold; font-size: 13.5pt'><th style='color: #0000FF'>Module</th>"
				+ "<th style='color: #408B14'># Passed</th>"
				+ "<th style='color: #DC143C'># Failed</th>"
				+ "<th style='color: #0000FF'>Duration</th>"
				+ "</b></tr>";


		HashMap<String, Integer> fHMPassed = new HashMap<String, Integer>();
		HashMap<String, Integer> fHmFailed = new HashMap<String, Integer>();

		for (Map.Entry<String, Integer> HMModule : fHmModuleWiseStats.entrySet()) 
		{			
			if(HMModule.getKey().contains(":P"))		
				fHMPassed.put(HMModule.getKey().split(":P")[0], HMModule.getValue());			

			if(HMModule.getKey().contains(":F"))		
				fHmFailed.put(HMModule.getKey().split(":F")[0], HMModule.getValue());			
		}
		
	    Iterator it = fHmModuleWiseExecutiontime.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();		
			int iPass =0;
			if(fHMPassed.containsKey(pair.getKey()))
				iPass = fHMPassed.get(pair.getKey());
			int iFail = 0;
			int iDuration = 0;			
			if(fHmFailed.containsKey(pair.getKey()))
				iFail =  fHmFailed.get(pair.getKey());
			iDuration =  fHmModuleWiseExecutiontime.get(pair.getKey());			
			String strModule = pair.getKey().toString();
			if(strModule.equalsIgnoreCase("Generic"))
				strModule = "Exports";
			else if (strModule.equalsIgnoreCase("Cashiering"))
				strModule = "checkout";
			aStrBodyHead = aStrBodyHead+"<tr>"
					+ "<td>"+strModule+"</td>"
					+ "<td class=\"num\" align='center'>"+iPass+"</td>"
					+ "<td class=\"num\" align='center'>"+iFail+"</td>"
					+ "<td class=\"num\" align='center'>"+iDuration+" Min(s)</td>"
					+ "</tr>";   

		}
		aStrBodyHead = aStrBodyHead+"<tr style='font-weight: bold; font-size: 13.5pt'><th style='color: #0000FF'>Total</th>"
				+ "<th class=\"num\">"+fHMPassed.values().stream().reduce(0, Integer::sum)+"</th>"
				+ "<th class=\"num\">"+fHmFailed.values().stream().reduce(0, Integer::sum)+"</th>"
				+ "<th class=\"num\">"+fHmModuleWiseExecutiontime.values().stream().reduce(0, Integer::sum)+" Min(s)</th>"
				+ "</b></tr>";
		aStrBodyHead += "</table>";

		aStrBodyHead +="<br />";
		aStrBodyHead = aStrBodyHead+"<table cellspacing='4' cellpadding='4' border='4' style='color: #292421' align='center'>"; 
		aStrBodyHead = aStrBodyHead+"<tr style='font-weight: bold; color: #0000FF; font-size: 13.5pt'><TH COLSPAN='3'>Summary of Test Cases Execution</TH></tr>";
		for (String lTempString : fHmScenarios.values())
		{
			if (lTempString.equals("FAILED"))
				fErrorCount++;
		}
		aStrBodyHead = aStrBodyHead+"<tr style='font-weight: bold; font-size: 13.5pt'><th style='color: #0000FF'>Total Test Cases Executed: "+fHmScenarios.size()+"</th><th style='color: #408B14'>Passed: "+(fHmScenarios.size()-fErrorCount)+"</th><th style='color: #DC143C'>Failed: "+fErrorCount+"</th></b></tr>";

		if(fErrorCount>0)
			aStrBodyHead = aStrBodyHead+"<tr style='font-weight: bold; font-size: 13.5pt; color: #DC143C'><TH COLSPAN='3'>Failed Test Cases</th></b></tr>";

		for (Map.Entry<String, String> HMScenario : fHmScenarios.entrySet()) 
		{
			if(HMScenario.getValue().equals("FAILED"))
			{
				aStrBodyHead = 		aStrBodyHead + "<tr><td COLSPAN='2'>" + HMScenario.getKey() + "</td>";
				aStrBodyHead = 		aStrBodyHead + "<td align='center' style='color: #FFFFFF;'  bgcolor='#CD0000'>" + HMScenario.getValue() + "</td>";
			}

		}

		aStrBodyHead =aStrBodyHead+ "</table><br/><br/>";	
		aStrBodyHead = aStrBodyHead+ "</body></html>";

		return aStrBodyHead;
	}

	protected static String getSysInfoAsString() throws UnknownHostException {

		try {

			String username = System.getProperty("user.name");
			InetAddress addr;
			addr = InetAddress.getLocalHost();
			String hostname = addr.getHostName();
			String SysInfo = username + " @ " + hostname;
			return SysInfo;
		} catch (Exception e) {
			throw (e);
		}
	}   
}
