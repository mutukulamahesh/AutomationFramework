package com.framework.utilities;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
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
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.zeroturnaround.zip.ZipUtil;

import com.itextpdf.text.log.SysoCounter;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.NetworkMode;


/**
 * <p>
 * <b> This Class provides the webdriver instance, selenium libraries and Reusable methods.</b>
 * 
 * @author mmutukul
 * @Description - This Class act as a Driver script and provides the webdriver instance, selenium libraries and Reusable methods.
 * @Date: 6/12/2018
 * @Revision History:
 *                  - Change Date :
 *                  - Change Reason: 
 *                  - Changed Behavior : 
 *                  - Last Changed By: mahesh
 * 
 *         </p>
 */
public class Utils {

	public static WebDriver driver = null;

	protected static int iTestCaseRow;
	protected static int iRowCount;
	static String bExecute;
	String sUsername, sPassword, smodelName, sTestDataPath, customer_id;
	static String sBrowserName;
	static String sEnvironment;
	Boolean signin_val = false, signout_val = false;

	protected static ExtentReports report;
	protected static ExtentTest extentLogger;
	private static final String OUTPUT_FOLDER = "/results";
	private static final String FILE_NAME = "/OperaCloud2.0.html";
	private static final String CONFIG_FILE = "/extent-config.xml";
	public static String strDate = reportDate();
	public static SoftAssert softAssertion= null;
	public static String logFilePath = "";
	public static String strStartTime = "";
	public static String strEndTime = "";	
	public static String lStrScenarioOutputFilePath = "";
	static XMLEventFactory lXmlEventFactory;
	static XMLOutputFactory lOutputFactory;
	static FileOutputStream lFileOutputStream;
	static XMLEventWriter lXmlEvtWriter;
	static EndDocument lEndDocument;
	static StartElement lStartElement;
	static String lRowType;
	static StartElement lTestRow;
	static StartElement lRowText;
	static StartElement lTestData;
	static StartElement lTestStatus;
	static EndElement lRowTextEndEle;
	static EndElement lTestDataEndEle;
	static EndElement lTestStatusEndEle;
	static EndElement lTestRowEndElement;
	public static HashMap <String, String> fHmScenarios = new HashMap<String, String>();
	public static HashMap<String, Integer> fHmModuleWiseStats = new HashMap<String, Integer>();
	public static LinkedHashMap<String, Integer> fHmModuleWiseExecutiontime = new LinkedHashMap<String, Integer>();
	
	
	public static String OPERAVersion = "";
	public static String OPERAUIGITHash = "";
	public static String OPERAWSGITHash = "";

	static String strKey = "";

	/**
	 * <p>
	 * <b> This Method runs Before Suite at Suite execution.</b>
	 * @author mmutukul
	 * @Description - This Method runs Before Suite at Suite execution.This method invokes writing Report for the session
	 * @param Input: Date as Strings
	 * @Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	@BeforeSuite(alwaysRun = true)
	public void startReport() throws Exception {
		// Date date = new Date();
		// SimpleDateFormat formatter = new SimpleDateFormat("ddMMMyyyyHHmmss");
		// String strDate = formatter.format(date);

		String path = OUTPUT_FOLDER + "\\" + strDate;
		report = new ExtentReports(System.getProperty("user.dir") + path + FILE_NAME, NetworkMode.OFFLINE);
		report.loadConfig(new File(System.getProperty("user.dir") + CONFIG_FILE));
		
	}	

	/**
	 * <p>
	 * <b> This Method runs After Suite at Suite execution.</b>
	 * @author mmutukul
	 * @Description - This Method runs After Suite at Suite execution.This method ends writing Report for the session and flushes report instance.
	 * @param Input: 
	 * @Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: 
	 * 
	 *</p>
	 */
	@AfterSuite(alwaysRun = true)
	public void endReport() throws Exception {
		// report.close();
		//Utils.tearDown();
		report.endTest(extentLogger);		
		report.flush();		
		
		try{
			System.out.println("In After Suite");
			Utils.startup(); 		
			LoginPage.Login();
			LoginPage.Loginvalidation();

			// Navigating to Main Menu
			click("Configuration.mainMenu", 100, "clickable");

			// Navigating to Administration
			click("Configuration.AboutOperaCloudMenu", 100, "clickable");

			//Verify if the about opera Poup is displayed
			isDisplayed("Configuration.AboutOperaCloudMenu.AbourOperaPopup", "About OPERA Popup");

			OPERAVersion = getAttributeOfElement("Configuration.AboutOperaCloudMenu.AbourOperaPopup.operaVersion", "data-ocformvalue", 100, "presence");
			OPERAUIGITHash = getAttributeOfElement("Configuration.AboutOperaCloudMenu.AbourOperaPopup.operaUiGITHash", "data-ocformvalue", 100, "presence");
			OPERAWSGITHash = getAttributeOfElement("Configuration.AboutOperaCloudMenu.AbourOperaPopup.operaWSGITHash", "data-ocformvalue", 100, "presence");
			
			writeDataIntoPropertiesFile("OPERAVersion", OPERAVersion, "ExecutionTime.properties");
			writeDataIntoPropertiesFile("OPERAUIGITHash", OPERAUIGITHash, "ExecutionTime.properties");
			writeDataIntoPropertiesFile("OPERAWSGITHash", OPERAWSGITHash, "ExecutionTime.properties");			
			tearDown();
		}
		catch (Exception e)
		{
			System.out.println("failed to read Opera information");
			tearDown();
		}
	}

	/**
	 * <p>
	 * <b> This Method runs Before Test Method at Suite execution.</b>
	 * @author mmutukul
	 * @Description - This Method runs Before Test Method at Suite execution.This method invokes execution by creating webdriver instance and handling browser and login activities.
	 * @param Input: 
	 * @Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Object[] parameters, Method method) throws Exception {
		//DOMConfigurator.configure(OR.setLog("log4j.xml"));
		//Utils.writeConfig();
		softAssertion= new SoftAssert();
		System.out.println("Test Name in Before: "+getClass().getSimpleName());
		System.out.println("Package Name in Before: "+getClass().getName());
		System.out.println("TEsttt :: "+method.getName());
		String[] module = getClass().getName().split("\\W+");  
		//System.out.println("Module Name: "+module[6]);
		String testName = getClass().getSimpleName();
		System.out.println("testName: "+testName);
		String testdataPath = "..//..//"+"testdata\\"+module[6]+"\\"+testName.toLowerCase()+"\\dataBank\\";
		//ExcelUtils.setExcelFile("..//..//"+OR.getConfig("Path_TestData")+testName.toLowerCase()+".xlsx");  
		//ExcelUtils.setExcelFile(testdataPath+"TestData"+".xlsx");
		ExcelUtils.setExcelFile(OR.getConfig("Path_EnvironmentDetails"));
		strStartTime = new SimpleDateFormat("yy/MM/dd HH:mm:ss").format(new Date());
		extentLogger = report.startTest(method.getName());
		InitializeScenarioLog(method.getName());
		LogResultsOutputForMailReport(lStrScenarioOutputFilePath,method.getName(),"",getClass().getSimpleName(),"OPERA Cloud","","",true);

		if (parameters != null && parameters.length >= 1) {
			System.out.println("StrBrowserName in beforeMethod " + parameters[0].toString());
			System.out.println("strURL in beforeMethod " + parameters[1].toString());
			String strBrowserName = parameters[0].toString();
			String strURL = parameters[1].toString();
			Utils.startup(strBrowserName,strURL);           
		}
		else{
			Utils.startup(); 		
			LoginPage.Login();
			LoginPage.Loginvalidation();
		}

	}

	/**
	 * <p>
	 * <b> This Method runs After Test Method at Suite execution.</b>
	 * @author mmutukul
	 * @Description - This Method runs After Method at Suite execution.This method ends writing report.
	 * @param Input: 
	 * @Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	@AfterMethod(alwaysRun = true)
	public void getResult(ITestResult result) throws Exception {

		if (result.getStatus() == ITestResult.FAILURE) {
			// logger.log(LogStatus.ERROR, "Test Case Failed is
			// "+result.getName());
			logger.log(LogStatus.FAIL, "Test Case Failed is " + result.getThrowable());
			String screenshotPath = ExtentReportsClass.getScreenshot(driver, result.getName());
			logger.log(LogStatus.FAIL, extentLogger.addScreenCapture(screenshotPath));
		} else if (result.getStatus() == ITestResult.SKIP) {
			logger.log(LogStatus.SKIP, "Test Case Skipped is " + result.getName());
			// logger.log(LogStatus.SKIP, "Test skipped " +
			// result.getThrowable());
		} else {
			// logger.log(LogStatus.PASS, "Test passed");
		}
		report.endTest(extentLogger);
		//		EndResultsLogging();
		softAssertion= null;

	}

	/**
	 * <p>
	 * <b> This is a startup Method starts the execution.</b>
	 * @author mmutukul
	 * @Description - This is a startup Method invokes execution by creating webdriver instance for handling browser and login activities.
	 * @param Input: Browser Name and Url
	 * @Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void startup() throws Exception {
		String strBrowserName = "";
		String strURL = "";
		startup(strBrowserName,strURL);
	}

	/**
	 * <p>
	 * <b> This is a startup Method starts the execution.</b>
	 * @author mmutukul
	 * @Description - This is a startup Method invokes execution by creating webdriver instance for handling browser and login activities.
	 * @param Input: Browser Name and Url
	 * @Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void startup(String strBrowserName, String strURL) throws Exception {
		try {

			// ******************************************************************************************************************************************************************************

			System.out.println("***********************************************");
			System.out.println("************   START OF TEST      ***************");
			System.out.println("***********************************************");

			System.out.println("==================================================================================");
			System.out.println("Selenium -:A cross browser test automation framework");
			System.out.println("Point of Contact: Opera Cloud Testing Team");
			System.out.println("Version: 1.0");
			System.out.println("==================================================================================");
			System.out.println("Executed by user:" + System.getProperty("user.name") + " on machine "
					+ System.getProperty("os.name") + " [version :" + System.getProperty("os.version")
					+ "] which has JRE " + System.getProperty("java.version") + " installed.");

			// ******************************************************************************************************************************************************************************

			if(strBrowserName.length() > 0 && strURL.length() > 0){
				driver = Utils.OpenBrowser(strBrowserName);					
				Utils.gotoPage(strURL);					
				new BaseClass(driver, extentLogger);
			}
			else{	
				Map<String, String> configMap = new HashMap<String, String>();			
				configMap = ExcelUtils.getDataByColoumn(OR.getConfig("Path_EnvironmentDetails"), "RunConfig", "KEY");
				configMap = ExcelUtils.getDataByColoumn(OR.getConfig("Path_EnvironmentDetails"), "Configuration", "KEY",configMap.get("Set"));
				System.out.println("configMap: "+configMap);
				bExecute = configMap.get("RunMode");
				sBrowserName = configMap.get("Browser");
				sEnvironment = configMap.get("URL");
				//if (bExecute.equalsIgnoreCase("YES")) {
				// Launch browser
				System.out.println("Intiating Browser...");
				logger.log(LogStatus.INFO, "Intiating Browser:: "+sBrowserName);
				driver = Utils.OpenBrowser(sBrowserName);					
				Utils.gotoPage(sEnvironment);					
				new BaseClass(driver, extentLogger);
				//}
			}
		} catch (Exception e) {

			logger.log(LogStatus.FAIL, "Class Utils | Method startu | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
		}

	}

	/**
	 * <p>
	 * <b> This Method used to tear down the browser.</b>
	 * @author mmutukul
	 * @Description - This Method handles the LogOut functionality and closing the Browser.
	 * @param Input: 
	 * @Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void tearDown() throws Exception {
		try {

			System.out.println("Closing Application...");
			LoginPage.Logout();	
			System.out.println("***********************************************");
			System.out.println("************   END OF TEST      ***************");
			System.out.println("***********************************************");
			driver.quit();
			EndResultsLogging();
			softAssertion.assertAll();			
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method tearDown | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * <b> This Method used to check the testdata file existence .</b>
	 * @author mmutukul
	 * @Description - This Method used to check the existence of testdata file .
	 * @param Input: path of the testdata file
	 * @Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void inputIsExists() throws Exception {
		try {
			File file = new File(OR.getConfig("Path_TestData"));
			File Inputfolder = new File(OR.getConfig("Path_TestData"));
			if (!Inputfolder.exists()) {
				JOptionPane.showMessageDialog(null, String.valueOf(OR.getConfig("Path_TestData"))
						+ " doesn't exist, hence cannot proceed ahead! please create the folder and put the input excel sheet inside it");
				System.exit(0);
			} else {
				System.out.println(String.valueOf(OR.getConfig("Path_TestData")) + " : Folder exists..");
			}
			if (!file.exists()) {
				JOptionPane.showMessageDialog(null, String.valueOf(OR.getConfig("Path_TestData") + "TestData" + ".xlsx")
						+ " doesn't exist, hence cannot proceed ahead!");
				System.exit(0);
			} else {
				System.out.println(
						String.valueOf(OR.getConfig("Path_TestData") + "TestData" + ".xlsx") + " : File exists..");
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method inputIsExists | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage());
			e.printStackTrace();
			tearDown();
		}
	}

	/**
	 * <p>
	 * <b> This Method used to open Browser.</b>
	 * @author mmutukul
	 * @Description - This Method handles the Browser functionality and opens the required browser with webdriver instance.
	 * @param Input: Browser Name
	 * @Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static WebDriver OpenBrowser(final String sBrowserName) throws Exception {
		try {
			System.out.println(sBrowserName);
			System.out.println("I am in open browser method");

			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			DesiredCapabilities.internetExplorer().setCapability("ignoreProtectedModeSettings", true);
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			System.out.println("Clearing Cache and Cookies");
			capabilities.setCapability(InternetExplorerDriver.ENABLE_ELEMENT_CACHE_CLEANUP, true);
			capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
			System.out.println("Cleared Cache and Cookies");
			capabilities.setJavascriptEnabled(true);

			if (sBrowserName.equals("IE")) {
				System.setProperty("webdriver.ie.driver", OR.propertyFile(OR.config_Prop).getProperty("IE"));
				Utils.driver = new InternetExplorerDriver(capabilities);
				System.out.println("IE Browser invoked");
				Utils.driver.manage().deleteAllCookies();
				Utils.driver.manage().window().maximize();			
				Utils.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				// Utils.driver.get(OR.ParentURL);

				logger.log(LogStatus.INFO, "Browser Launched :: "+sBrowserName);
				System.out.println("Application Launched");

			} else if (sBrowserName.equals("FF")) {				
				System.out.println("Opening Mozila...");
				// System.setProperty("webdriver.gecko.driver",
				// OR.propertyFile(OR.config_Prop).getProperty("Mozila"));
				// DesiredCapabilities capabilities1 =
				// DesiredCapabilities.firefox();
				// capabilities.setCapability("marionette", true);
				Utils.driver = new FirefoxDriver();
				Utils.driver.manage().window().maximize();
				Utils.driver.manage().deleteAllCookies();
				logger.log(LogStatus.INFO, "Browser Launched :: "+sBrowserName);

			} else if (sBrowserName.equals("Chrome")) {
				DesiredCapabilities cap = DesiredCapabilities.chrome();
				cap.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);				
				System.out.println("Opening Chrome...");
				System.out.println("cHROME: "+ OR.getConfig("Chrome"));
				System.setProperty("webdriver.chrome.driver", OR.getConfig("Chrome"));
				Utils.driver = new ChromeDriver(cap);
				Utils.driver.manage().window().maximize();
				Utils.driver.manage().deleteAllCookies();
				// Utils.driver.get(OR.ParentURL);
				System.out.println("Application Launched");
				logger.log(LogStatus.INFO, "Browser Launched :: "+sBrowserName);
			}
			//eventHandler();

		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method OpenBrowser | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage());
			e.printStackTrace();
			tearDown();
		}
		return Utils.driver;
	}


	/**
	 * <p>
	 * <b> This Method provides the code for navigating to portal.</b>
	 * @author mmutukul
	 * @Description - This Method provides the code for navigating to portal..
	 * @param Input:  URL
	 * @Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void navigatePage(final String URL) throws Exception {
		try {
			Utils.driver.get(URL);
			System.out.println("Application Launched");
			Thread.sleep(3000);

			// verifyTitle(OR.propertyFile(OR.Property_File).getProperty("title"));

		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method navigatePage | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}

	}

	/**
	 * <p>
	 * <b> This Method provides the code for navigating to portal.</b>
	 * @author mmutukul
	 * @Description - This Method provides the code for navigating to portal..
	 * @param Input:  URL
	 * @Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void gotoPage(final String env) throws Exception {
		try {

			Utils.driver.get(env);
			System.out.println("Automation Env Launched");
			logger.log(LogStatus.INFO, "Testing Environment Launched :: "+env);
			Thread.sleep(3000);

		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method gotoPage | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}

	}

	/**
	 * <p>
	 * <b> This Method provides the code for getting current TestCase name.</b>
	 * @author mmutukul
	 * @Description - This Method provides the code for getting current TestCase name.
	 * @param  Input:  element as Strings
	 * @return Output: Test Case Name
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static String getTestCaseName(final String sTestCase) throws Exception {
		String value = sTestCase;
		try {
			int posi = value.indexOf("@");
			value = value.substring(0, posi);
			posi = value.lastIndexOf(".");
			value = value.substring(posi + 1);
			return value;
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method getTestCaseName | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * <p>
	 * <b> This Method provides the code for providing Implicit Time.</b>
	 * @author mmutukul
	 * @Description - This Method provides the code for providing Implicit Time.
	 * @param  Input:  Time as int.
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void implicitWait(final int seconds) throws Exception {
		try {

			Utils.driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method implicitWait | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
		}
	}


	/**
	 * <p>
	 * <b> This Method provides the code for getting the current window.</b>
	 * @author mmutukul
	 * @Description - This Method provides the code for getting the current window.
	 * @param  Input:  
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void parentWindow() throws Exception {
		try {

			Set<String> AllWindows = BaseClass.driver.getWindowHandles();
			System.out.println("Handles present :" + AllWindows);
			String parentWindow = (String) AllWindows.toArray()[0];
			System.out.println("Parent Window Handle code :" + AllWindows.toArray()[0]);
			System.out.println("Parent: " + parentWindow);
			// String childWindow1 = (String) AllWindows.toArray()[1];
			// System.out.println("Child Window handle code : " +
			// AllWindows.toArray()[1]);
			// System.out.println("Child1: " + childWindow1);

			driver.switchTo().window(parentWindow);
			System.out.println(driver.getTitle());
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method parentWindow | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * <b> This Method provides the code for for getting the child windows</b>
	 * @author mmutukul
	 * @Description - This Method provides the code for for getting the child windows.
	 * @param  Input:  
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void childWindow() throws Exception {

		try {
			@SuppressWarnings("rawtypes")
			Set handles = Utils.driver.getWindowHandles();
			System.out.println("Handles:" + handles);
			String firstWinHandle = Utils.driver.getWindowHandle();
			System.out.println("First Handle:" + firstWinHandle);
			System.out.println("First Handle URL: " + Utils.driver.getCurrentUrl());
			System.out.println("First Handle Title: " + Utils.driver.getTitle());
			System.out.println("***********************************************************************************");
			System.out.println("***********************************************************************************");

			handles.remove(firstWinHandle);
			String winHandle = (String) handles.iterator().next();
			System.out.println("window: " + winHandle);
			if (winHandle != firstWinHandle) {
				String secondWinHandle = winHandle;
				System.out.println("Second window: " + secondWinHandle);
				Utils.driver.switchTo().window(secondWinHandle);
				String title1 = Utils.driver.getTitle();
				System.out.println("Second window Title: " + title1);
				// String url = Utils.driver.getCurrentUrl();
				// System.out.println("Second Window url: " + url);

				System.out
				.println("***********************************************************************************");
				System.out
				.println("***********************************************************************************");
			}

		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method childWindow | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * <b> This Method provides the code for taking screenshot.</b>
	 * @author mmutukul
	 * @Description - This Method provides the code for taking screenshot.
	 * @param  Input:  driver and  name
	 * @return Output: Screenshot
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void takeScreenshot(final WebDriver driver, final String sTestCaseName) throws Exception {
		try {
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(OR.Path_ScreenShot + sTestCaseName + ".jpg"));
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method takeScreenshot | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * <b> This Method provides the code for taking screenshot.</b>
	 * 
	 * @param Name
	 *            as Strings.
	 *            </p>
	 */

	/**
	 * <p>
	 * <b> This Method provides the code for taking screenshot.</b>
	 * @author mmutukul
	 * @Description - This Method provides the code for taking screenshot.
	 * @param  Input:  name
	 * @return Output: Screenshot
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void takeScreenshot(final String name) throws Exception {
		try {
			File scrFile = ((TakesScreenshot) Utils.driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(OR.Path_ScreenShot + name + ".jpg"));
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method takeScreenshot | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * <b> This Method provides the code for performing Webdriver Waits.</b>
	 * @author mmutukul
	 * @Description - This Method provides the code for performing Webdriver Waits.
	 * @param  Input:  locator,waitCondition as Strings.
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void WebdriverWait(final int seconds, final String locator, final String waitCondition)
			throws Exception {
		try {

			WebDriverWait waitForDrpDown = new WebDriverWait(BaseClass.driver, seconds);

			switch (waitCondition) {
			case "visible":
				waitForDrpDown.until(ExpectedConditions.visibilityOfElementLocated(OR.getLocator(locator)));
				break;
			case "presence":
				waitForDrpDown.until(ExpectedConditions.presenceOfElementLocated(OR.getLocator(locator)));
				break;
			case "clickable":
				waitForDrpDown.until(ExpectedConditions.elementToBeClickable(OR.getLocator(locator)));
				break;
			case "located":
				waitForDrpDown.until(ExpectedConditions.visibilityOfElementLocated(OR.getLocator(locator)));
				break;
			case "frame":
				waitForDrpDown.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
				break;
			case "stale":
				waitForDrpDown.until(ExpectedConditions.stalenessOf(driver.findElement(OR.getLocator(locator))));
				break;
			}

		} catch (Exception e) {
			logger.log(LogStatus.INFO, "Class Utils | Method WebdriverWait | : Waited for the element to be "+waitCondition);
			//logger.log(LogStatus.ERROR, "Class Utils | Method WebdriverWait | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage());
			e.printStackTrace();
			//tearDown();
		}
	}

	public static void Wait(long time) throws InterruptedException {
		Thread.sleep(time);
	}

	/**
	 * <p>
	 * <b> This Method provides the code for getting List of elements as Array
	 * collection .</b>
	 * 
	 * @param Locator,Object,Data
	 *            as Strings.
	 *            </p>
	 */

	/**
	 * <p>
	 * <b> This Method provides the code for getting List of elements as Array collection .</b>
	 * @author mmutukul
	 * @Description - This Method provides the code for getting List of elements as Array collection .
	 * @param  Input:  locator,object,data as Strings.
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void List(final String locator, final String object, final String data) throws Exception {
		try {

			List<WebElement> drpListItems = Utils.driver.findElements(locatorValue(locator, object));
			// List<WebElement> drpListItems =
			// BaseClass.driver.findElements(By.className(object));
			System.out.println("dropdown items" + drpListItems);
			for (WebElement temp : drpListItems) {
				System.out.println(temp.getText());
				if (temp.getText().trim().contains(String.valueOf(data))) {
					temp.click();
					break;
				}
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method List | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}

	/**
	 * <p>
	 * <b> This Method provides the code for getting List of elements as Array collection .</b>
	 * @author mmutukul
	 * @Description - This Method provides the code for getting List of elements as Array collection .
	 * @param  Input:  locator,data as Strings.
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void List(final String locator, final String data) throws Exception {
		try {

			List<WebElement> drpListItems = Utils.driver.findElements(OR.getLocator(locator));
			// List<WebElement> drpListItems =
			// BaseClass.driver.findElements(By.className(object));
			System.out.println("dropdown items" + drpListItems);
			for (WebElement temp : drpListItems) {
				System.out.println(temp.getText());
				if (temp.getText().trim().contains(String.valueOf(data))) {
					temp.click();
					break;
				}
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method List | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}

	/**
	 * <p>
	 * <b> This Method provides the code for getting List of elements as Array collection .</b>
	 * @author mmutukul
	 * @Description - This Method provides the code for getting List of elements as Array collection .
	 * @param  Input:  locator,object as Strings and data as int.
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void List(final String locator, final String object, final int data) throws Exception {
		try {

			List<WebElement> drpListItems = Utils.driver.findElements(locatorValue(locator, object));
			System.out.println("dropdown items" + drpListItems);
			for (WebElement temp : drpListItems) {
				System.out.println(temp.getText());
				if (temp.getText().trim().equals(String.valueOf(data))) {
					temp.click();
					break;
				}
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method List | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}


	/**
	 * <p>
	 * <b>  This Method provides the code for Validation .</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for Validation using Asset functionality.
	 * @param  Input:  expected data, locator, message as string.
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void Assert(final String expected, final String message, final String locator) throws Exception {

		String actual = BaseClass.driver.findElement(OR.getLocator(locator)).getText();
		// String actual =
		// BaseClass.driver.findElement(By.xpath(object)).getText();
		System.out.println("The popup message is : " + actual);
		try {
			Assert.assertEquals(actual, expected, message);

		} catch (AssertionError e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method Assert | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}

	/**
	 * <p>
	 * <b>  This Method provides the code for getting current Method name.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for getting current Method name.
	 * @param  Input:  
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static String methodName() throws Exception {
		String methodname = "";
		try {
			String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			System.out.println("methodName = " + methodName);
			methodname = methodName;
			return methodName;
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method methodName | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
		}
		return methodname;
	}

	/**
	 * <p>
	 * <b>  This Method provides the code for getting System User name.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for getting System User name.
	 * @param  Input:  
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static String SysUsername() throws Exception {
		String userName = "";
		try {
			String username = System.getProperty("user.name");
			System.out.println("System Username is :" + username);
			userName = username;
			return username;
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method SysUsername | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
		}
		return userName;
	}

	/**
	 * <p>
	 * <b>  This Method provides the code for getting system date.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for getting system date.
	 * @param  Input:  Date format
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static String SysDate(final String format) throws Exception {
		String date = "";
		try {
			// DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
			DateFormat df = new SimpleDateFormat(format);
			String SysDate = df.format(new Date());
			System.out.println("System Date is : " + SysDate);
			date = SysDate;
			return SysDate;
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method SysDate | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
		}
		return date;
	}


	/**
	 * <p>
	 * <b>  This Method provides the code for getting locator value.</b>
	 * @author mmutukul
	 * @Description -   This Method provides the code for getting locator value.
	 * @param  Input:  locator type , value as strings
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static By locatorValue(final String locatorTpye, final String value) {
		By by;
		switch (locatorTpye) {
		case "id":
			by = By.id(value);
			break;
		case "name":
			by = By.name(value);
			break;
		case "xpath":
			by = By.xpath(value);
			break;
		case "css":
			by = By.cssSelector(value);
			break;
		case "linkText":
			by = By.linkText(value);
			break;
		case "partialLinkText":
			by = By.partialLinkText(value);
			break;
		case "classname":
			by = By.className(value);
			break;
		default:
			by = null;
			break;
		}
		return by;
	}

	/**
	 * <p>
	 * <b>  This Method provides the code for getting current Browser name.</b>
	 * @author mmutukul
	 * @Description -   This Method provides the code for getting current Browser name..
	 * @param  Input:  
	 * @return Output: Browser Name
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static String getBrowserName() throws Exception {
		String BrowserName = "";
		try {
			Capabilities cap = ((RemoteWebDriver) Utils.driver).getCapabilities();
			String browserName = cap.getBrowserName().toUpperCase();// .toLowerCase();
			System.out.println(browserName);
			BrowserName = browserName;
			return browserName;
		} catch (Exception e) {
			logger.log(LogStatus.INFO, "Class Utils | Method getBrowserName | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
		}
		return BrowserName;
	}

	/**
	 * <p>
	 * <b>  This Method provides the code for getting current OS name..</b>
	 * @author mmutukul
	 * @Description -   This Method provides the code for getting current OS name.
	 * @param  Input:  
	 * @return Output: OS Name
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static String getOsName() throws Exception {
		String OS = "";
		try {
			Capabilities cap = ((RemoteWebDriver) Utils.driver).getCapabilities();
			String os = cap.getPlatform().toString();
			System.out.println(os);
			String v = cap.getVersion().toString();
			System.out.println(v);
			String osv = os + v;
			System.out.println(osv);
			OS = os;
			return os;
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method getOsName | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
		}
		return OS;
	}

	/**
	 * <p>
	 * <b>  This Method provides the code for getting current Class name.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for getting current Class name.
	 * @param  Input:  
	 * @return Output: Class Name
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static String getClassName() throws Exception {
		String ClassName = "";
		try {
			String className = Thread.currentThread().getStackTrace()[2].getClassName();
			int lastIndex = className.lastIndexOf('.');
			ClassName = className.substring(lastIndex + 1);
			return className.substring(lastIndex + 1);
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method getClassName | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
		}
		return ClassName;
	}

	/**
	 * <p>
	 * <b>  This Method provides the code for getting current Method name.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for getting current Method name.
	 * @param  Input:  
	 * @return Output: Method Name
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static String getMethodName() throws Exception {
		try {
			String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
			int lastIndex = methodName.lastIndexOf('.');
			return methodName.substring(lastIndex + 1);
		} catch (Exception e) {
			throw (e);
		}
	}

	/**
	 * <p>
	 * <b>  This Method provides the code for getting Date as String.</b>
	 * @author mmutukul
	 * @Description -   This Method provides the code for getting Date as String.
	 * @param  Input:  
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static String getDateAsString() {
		SimpleDateFormat sdfdate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		String timeZone = "IST";
		Date date = new Date();
		sdfdate.setTimeZone(TimeZone.getTimeZone(timeZone));
		return sdfdate.format(date);
	}

	/**
	 * <p>
	 * <b>  This Method provides the code for getting System name.</b>
	 * @author mmutukul
	 * @Description -    This Method provides the code for getting System name.
	 * @param  Input:  
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static String getProfileName() throws Exception {
		try {
			// String profileName = "Aut_" + Utils.SysUsername() +
			// Utils.SysDate("yyyyMMddhhmmss");
			String profileName = Utils.SysUsername();
			ExcelUtils.setCellData("Profile", "Name", iTestCaseRow, profileName);
			System.out.println("Profile Name is : " + profileName);
			return profileName;
		} catch (Exception e) {
			throw (e);
		}
	}

	/*public static String getEmail() throws Exception {
		try {
			// String email = "test@"+ Utils.SysDate("yyyyMMddhhmmss")+".com";
			String email = ExcelUtils.getCellData("Profile", "Email", iTestCaseRow);
			System.out.println("Email : " + email);
			return email;
		} catch (Exception e) {
			throw (e);
		}
	}*/

	/**
	 * <p>
	 * <b>  This Method provides the code for encoding Password..</b>
	 * @author mmutukul
	 * @Description -    This Method provides the code for encoding Password..
	 * @param  Input:  password
	 * @return Output: encoded password
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static String encPassword(final String pwd) throws Exception {

		byte[] bytesEncoded = Base64.encodeBase64(pwd.getBytes());
		String encodedpwd = new String(bytesEncoded);
		System.out.println("ecncoded value is " + encodedpwd);
		return encodedpwd;
	}

	/**
	 * <p>
	 * <b>  This Method provides the code for decoding Password.</b>
	 * @author mmutukul
	 * @Description -    This Method provides the code for decoding Password.
	 * @param  Input:  password
	 * @return Output: decoded password
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static String decPassword(final String pwd) throws Exception {

		byte[] valueDecoded = Base64.decodeBase64(pwd);
		String decodedpwd = new String(valueDecoded);
		System.out.println("Decoded value is " + decodedpwd);
		return decodedpwd;
	}


	/**
	 * <p>
	 * <b>  This Method provides the code for Checking Image displayed or not.</b>
	 * @author mmutukul
	 * @Description -   This Method provides the code for Checking Image displayed or not.
	 * @param  Input:  image
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void CheckImage() throws Exception {

		Utils.WebdriverWait(60, "loginpageImage", "visible");
		WebElement ImageFile = Utils.driver.findElement(OR.getLocator("loginpageImage"));

		Boolean ImagePresent = (Boolean) ((JavascriptExecutor) Utils.driver).executeScript(
				"return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0",
				ImageFile);
		if (!ImagePresent) {
			System.out.println("Image not displayed.");
		} else {
			System.out.println("Image displayed.");
		}
	}

	// *********************************************************************************************************************************************************************************************

	/**
	 * <p>
	 * <b>  This Method returns a Webelement by providing a locator</b>
	 * @author mmutukul
	 * @Description -   This Method returns a Webelement by providing a locator.
	 * @param  Input:  locator as string
	 * @return Output: element as Webelement type
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static WebElement element(final String locator) throws IOException, Exception {

		try {
			Utils.implicitWait(30);
			WebElement element = BaseClass.driver.findElement(OR.getLocator(locator));
			return element;

		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method element | Exception Des :"+e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
		return null;
	}

	/**
	 * <p>
	 * <b> This Method provides the code to find the WebElements and returns the List.</b>
	 * @author mmutukul
	 * @Description -   This Method provides the code to find the WebElements and returns the List.
	 * @param  Input:  locator as string
	 * @return Output: List of elements
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static List<WebElement> elements(final String locator) throws IOException, Exception {

		try {
			Utils.implicitWait(30);
			List<WebElement> element = BaseClass.driver.findElements(OR.getLocator(locator));
			return element;

		} catch (Exception e) {
			logger.log(LogStatus.ERROR, "Class Utils | Method List | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())).charAt(25) +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
		return null;
	}

	/**
	 * <p>
	 * <b> This Method provides the code for performing Click operation .</b>
	 * @author mmutukul
	 * @Description -   This Method provides the code for performing Click operation .
	 * @param  Input:  locator as string
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void click(final String element) throws IOException, Exception {

		try {
			Utils.implicitWait(30);
			if(isExists(element))
			{
				Utils.WebdriverWait(30, element, "clickable");
				BaseClass.driver.findElement(OR.getLocator(element)).click();	
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method click | Exception Des :"+ e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())).charAt(25) +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}

	/**
	 * <p>
	 * <b> This Method provides the code for performing Click operation .</b>
	 * @author mmutukul
	 * @Description -   This Method provides the code for performing Click operation .
	 * @param  Input:  locator as Webelement
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void click(final WebElement element, Boolean validate) throws IOException, Exception {

		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", element);

		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method click | Exception Des :" + e.getMessage()+extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())).charAt(25) +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}

	/**
	 * <p>
	 * <b> This Method provides the code for performing Click operation .</b>
	 * @author mmutukul
	 * @Description -   This Method provides the code for performing Click operation with mouse mmovement.
	 * @param  Input:  locator as Webelement
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void click(WebElement element) throws IOException, Exception {

		try {
			Actions actions = new Actions(driver);
			actions.moveToElement(element).click().build().perform();

		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method click | Exception Des :"+ e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//logger.log(LogStatus.ERROR, "Class Utils | Method click | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())).charAt(25) +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}

	/**
	 * <p>
	 * <b> This Method provides the code for performing Click operation .</b>
	 * @author mmutukul
	 * @Description -   This Method provides the code for performing Click operation with using java Scirpt executor.
	 * @param  Input:  locator as String
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void jsClick(final String element) throws IOException, Exception {

		try {
			WebElement webElement = BaseClass.driver.findElement(OR.getLocator(element));
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", webElement);
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method click | Exception Des :"+ e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())).charAt(25) +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}

	/**
	 * <p>
	 * <b> This Method provides the code for performing Clear operation .</b>
	 * @author mmutukul
	 * @Description -   This Method provides the code for performing Clear operation in the text box.
	 * @param  Input:  locator as String
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void clear(final String locator) throws IOException, Exception {

		try {
			Utils.implicitWait(30);
			BaseClass.driver.findElement(OR.getLocator(locator)).clear();

		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method clear | Exception Des :"+ e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}

	/**
	 * <p>
	 * <b> This Method provides the code for performing action on Textbox ..</b>
	 * @author mmutukul
	 * @Description -   This Method provides the code for performing operation in the text box by sending data .
	 * @param  Input:  locator and data as String
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void textBox(final String element, final String data) throws Exception {
		try {
			Utils.implicitWait(30);
			BaseClass.driver.findElement(OR.getLocator(element)).clear();
			BaseClass.driver.findElement(OR.getLocator(element)).sendKeys(String.valueOf(data));
			logger.log(LogStatus.PASS, "Entered " + data + " on " +splitString(element) );
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method textBox | Exception Des :"+ e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}

	/**
	 * <p>
	 * <b> This Method provides the code for performing action on Textbox ..</b>
	 * @author mmutukul
	 * @Description -   This Method provides the code for performing operation in the text box by sending data of int Type .
	 * @param  Input:  locator  as String and data as int
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void textBox(final String element, final int data) throws Exception {		
		try {
			Utils.implicitWait(30);
			BaseClass.driver.findElement(OR.getLocator(element)).clear();
			BaseClass.driver.findElement(OR.getLocator(element)).sendKeys(String.valueOf(data));
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method textBox | Exception Des :"+ e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//logger.log(LogStatus.ERROR, "Class Utils | Method textBox | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}


	/**
	 * <p>
	 * <b> This Method provides the code for getting Text from element.</b>
	 * @author mmutukul
	 * @Description -   This Method provides the code for getting Text from element..
	 * @param  Input:  locator  as String 
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static String getText(final String element) throws IOException, Exception {
		try {
			Utils.implicitWait(30);
			String text = BaseClass.driver.findElement(OR.getLocator(element)).getText();
			//WebElement text = BaseClass.driver.findElement(OR.getLocator(element));

			//JavascriptExecutor jse = (JavascriptExecutor)driver;
			//String value = (String) jse.executeScript("return arguments[0].text", text);

			return text;
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method getText | Exception Des :"+ e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//logger.log(LogStatus.ERROR, "Class Utils | Method getText | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * <p>
	 * <b> This Method provides the code for getting value of an element.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for getting value of an element.
	 * @param  Input:  locator  as String 
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static String getTextValue(final String element) throws IOException, Exception {
		try {
			Utils.implicitWait(30);
			String text = BaseClass.driver.findElement(OR.getLocator(element)).getAttribute("value");
			return text;
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method getText | Exception Des :"+ e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//logger.log(LogStatus.ERROR, "Class Utils | Method textBox | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <p>
	 * <b>  This Method provides the code for handling Popup window..</b>
	 * @author mmutukul
	 * @Description - This Method provides the code for handling Popup window.
	 * @param  Input:  
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void popupWindow() throws IOException, Exception {

		try {
			String parentWindowHandler = BaseClass.driver.getWindowHandle();
			System.out.println(parentWindowHandler);
			String subWindowHandler = null;
			Set<String> handles = BaseClass.driver.getWindowHandles();
			Iterator<String> iterator = handles.iterator();
			while (iterator.hasNext()) {
				subWindowHandler = iterator.next();
			}
			BaseClass.driver.switchTo().window(subWindowHandler);
			System.out.println(subWindowHandler);
			BaseClass.driver.switchTo().frame(1);

		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method popupWindow | Exception Des :"+ e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//logger.log(LogStatus.ERROR, "Class Utils | Method popupWindow | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}

	}
	/**
	 * <p>
	 * <b> This Method provides the code for performing action on dropdown.</b>
	 * @author mmutukul
	 * @Description - This Method provides the code for performing action on dropdown.
	 * @param  Input:   element,selectby,value as Strings.
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void selectBy(final String locator, final String selectBy, final String value)
			throws IOException, Exception {

		try {
			WebElement element = BaseClass.driver.findElement(OR.getLocator(locator));
			Select select = new Select(element);
			Thread.sleep(50);
			switch (selectBy) {
			case "text":
				select.selectByVisibleText(value);
				break;
			case "index":
				select.selectByIndex(Integer.parseInt(value));
				break;
			case "value":
				select.selectByValue(value);
				break;
			}

		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method selectBy | Exception Des :"+ e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())) );
			//logger.log(LogStatus.ERROR, "Class Utils | Method selectBy | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}

	/**
	 * <p>
	 * <b> This Method provides the code for performing action on dropdown.</b>
	 * @author mmutukul
	 * @Description - This Method provides the code for performing action on dropdown and getting list of all options.
	 * @param  Input:   locator as Strings.
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static List<WebElement> selectByAllOptions(final String locator) throws IOException, Exception {

		try {
			// WebElement element =
			// BaseClass.driver.findElement(Utils.locatorValue(locator,
			// object));
			WebElement element = BaseClass.driver.findElement(OR.getLocator(locator));
			Select select = new Select(element);
			List<WebElement> allOptions = select.getOptions();
			for (WebElement option : allOptions) {
				System.out.println("All options in the list : " + option.getText());
			}
			return allOptions;
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method selectByAllOptions | Exception Des :"+ e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
		return null;
	}

	/**
	 * <p>
	 * <b> This Method provides the code for performing Mouse Hover operations.</b>
	 * @author mmutukul
	 * @Description - This Method provides the code for performing Mouse Hover operations.
	 * @param  Input:   hoverElement as Strings.
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void mouseHover(final String hoverElement) throws Exception {

		try {
			Actions builder = new Actions(BaseClass.driver);
			Utils.implicitWait(60);
			WebElement element = BaseClass.driver.findElement(OR.getLocator(hoverElement));
			builder.moveToElement(element).build().perform();
			Thread.sleep(1000);

		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method mouseHover | Exception Des :"+ e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//logger.log(LogStatus.ERROR, "Class Utils | Method mouseHover | Exception Des :" + e.toString() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}

	/**
	 * <p>
	 * <b> This Method provides the code for performing Double Click operations.</b>
	 * @author mmutukul
	 * @Description - This Method provides the code for performing Double Click operations.
	 * @param  Input:   hoverElement as Strings.
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void doubleClick(String hoverElement) throws Exception {

		try {
			Actions builder = new Actions(BaseClass.driver);
			Utils.implicitWait(60);
			WebElement element = BaseClass.driver.findElement(OR.getLocator(hoverElement));
			builder.moveToElement(element).perform();
			builder.doubleClick(element).build().perform();

		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method doubleClick | Exception Des :"+ e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}

	public static String xpath(String ele1, String data, String ele2) throws Exception {
		String xpath = ele1 + data + ele2;

		System.out.println("xpath: " + xpath);
		return xpath;
	}

	/**
	 * <p>
	 * <b> This Method provides the code for performing Keyboard actions.</b>
	 * @author mmutukul
	 * @Description - This Method provides the code for performing Keyboard actions.
	 * @param  Input:   Element as Strings.
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	// For perfoming Enter key from Keyboard
	public static void enterKey(String element) throws Exception {
		try {

			element(element).sendKeys(Keys.RETURN);

		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method enterKey | Exception Des :"+ e.getMessage() );
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}

	/**
	 * <p>
	 * <b> This Method provides the code for performing Keyboard actions.</b>
	 * @author mmutukul
	 * @Description - This Method provides the code for performing Keyboard actions.
	 * @param  Input:   Element as Strings.
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	// For perfoming Tab key from Keyboard
	public static void tabKey(String element) throws Exception {
		try {

			element(element).sendKeys(Keys.TAB);

		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method enterKey | Exception Des :"+ e.getMessage() );
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}

	/**
	 * <p>
	 * <b>  This Method provides the code for performing Scrolling actions.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for performing Scrolling actions.
	 * @param  Input:   scrollBy as Strings.
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void scroll(final String scrollBy) throws Exception {  //, WebElement element

		try {

			JavascriptExecutor js = (JavascriptExecutor) driver;

			switch (scrollBy) {
			case "down":
				js.executeScript("window.scrollBy(0,250)", "");
				break;
			case "up":
				js.executeScript("window.scrollBy(0,-250)", "");
				break;
				/*case "element":
				js.executeScript("arguments[0].scrollIntoView(true);", element);
				break;*/
			}

		} catch (Exception e) {
			System.out.println(e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			tearDown();
		}
	}

	/**
	 * <p>
	 * <b>  This Method provides the code for performing Drag and Drop Operations.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for performing Drag and Drop Operations.
	 * @param  Input:   element as Strings.
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void dragAndDrop(String element) throws Exception {

		try {

			Actions act = new Actions(driver);
			WebElement drag = driver.findElement(OR.getLocator(element));
			WebElement drop = driver.findElement(OR.getLocator(element));
			act.dragAndDrop(drag, drop).build().perform();

		} catch (Exception e) {
			System.out.println(e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			tearDown();
		}
	}

	/**
	 * <p>
	 * <b>  This Method provides the code for entering Text using JavaSript.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for entering Text using JavaSript.
	 * @param  Input:   element and data as Strings.
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void jsTextbox(String element, String data) throws Exception {
		try{
			WebElement webElement = BaseClass.driver.findElement(OR.getLocator(element));
			JavascriptExecutor myExecutor = ((JavascriptExecutor) driver);
			myExecutor.executeScript("arguments[0].value='" + data + "';", webElement);
		} catch (Exception e) {
			System.out.println(e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			tearDown();
		}
	}

	// *******************************************************************************************************************************************************
	// Validations Code
	// *******************************************************************************************************************************************************
	/**
	 * <p>
	 * <b>  This Method provides the code for Validating current page.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for entering Text using JavaSript.
	 * @param  Input:   element and compare data, msg for log as Strings.
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static Boolean verifyCurrentPage(String locatorKey, String expectedValueKey) throws Exception {

		try {
			Utils.implicitWait(30);
			String text = BaseClass.driver.findElement(OR.getLocator(locatorKey)).getText();
			System.out.println("text from method" + text);
			Boolean flag = false;

			System.out.println("text from OR" + OR.getTestData(expectedValueKey));

			if (text.equalsIgnoreCase(OR.getTestData(expectedValueKey))) {
				flag = true;
				System.out.println("Validation Text " + expectedValueKey + " is matched with: " + text);
			} else {

				System.out.println("Validaton text " + expectedValueKey + "is not matched with " + text);
				logger.log(LogStatus.ERROR, "Not landed in expected page" + "Validaton text " + expectedValueKey
						+ "is not matched with " + text);
				tearDown();
			}

			return flag;
		} catch (Exception e) {
			System.out.println("Validaton text " + expectedValueKey + "is not matched with Expected ");
			logger.log(LogStatus.ERROR, "Not landed in expected page" + "Validaton text " + expectedValueKey+ "is not matched with Expected ");
			tearDown();
		}
		return null;
	}

	/**
	 * <p>
	 * <b>  This Method provides the code for Validating current page.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for entering Text using JavaSript.
	 * @param  Input:   element and compare data, msg for log as Strings.
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static Boolean verifyCurrentPageContains(String locatorKey, String expectedValueKey) throws Exception {


		Utils.implicitWait(30);
		String text = BaseClass.driver.findElement(OR.getLocator(locatorKey)).getText();
		System.out.println("Actual Text: " + text);
		Boolean flag=false;

		if(text.contains(OR.getTestData(expectedValueKey))) {
			flag=true;
			System.out.println("Page content " + text + " contains " + expectedValueKey + ". Hence Passed");
		}
		else {
			System.out.println("Page content " + text + " doesn't contain " + expectedValueKey + ". Hence Failed");
		}

		return flag;
	}

	/**
	 * <p>
	 * <b>  This Method provides the code for Validating current page.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for entering Text using JavaSript.
	 * @param  Input:   element and compare data, msg for log as Strings.
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static Boolean verifyCurrentPageContainsValue(String locatorKey, String expectedValue) throws Exception {


		Utils.implicitWait(30);
		String text = BaseClass.driver.findElement(OR.getLocator(locatorKey)).getText();
		System.out.println("Actual Text: " + text);
		Boolean flag=false;

		if(text.contains(expectedValue)) {
			flag=true;
			System.out.println("Page content " + text + " contains " + expectedValue + ". Hence Passed");
		}
		else {
			System.out.println("Page content " + text + " doesn't contain " + expectedValue + ". Hence Failed");
		}

		return flag;
	}

	/**
	 * <p>
	 * <b> This Method provides the code for Validating current page Title.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for Validating current page Title.
	 * @param  Input:   title as Strings.
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static boolean verifyTitle(final String title) throws Exception {
		boolean bReturn = false;
		try {
			// String actualTitle=driver.getTitle();

			if (BaseClass.driver.getTitle().equalsIgnoreCase(title)) {
				bReturn = true;
			} else {

				tearDown();
			}
		} catch (Exception e) {
			System.out.println("Validaton- Title is not Expected ");
			logger.log(LogStatus.FAIL, "Validaton- Title is not Expected  ");
			tearDown();
		}

		return bReturn;
	}

	/**
	 * <p>
	 * <b> This Method provides the code for Validating the element is Displayed.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for Validating the element is Displayed.
	 * @param  Input:   element,data as Strings.
	 * @return Output: flag
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static boolean isDisplayed(final String element, String data) throws Exception {

		Utils.implicitWait(30);
		boolean displayed = false;
		try {
		 displayed = driver.findElement(OR.getLocator(element)).isDisplayed();
		}
		catch(Exception e ) {
			displayed = false;
			
		}
		if (displayed) {
			System.out.println("The " + data + " is displayed");
		} else {
			System.out.println("The " + data + " is not displayed");
		}
		return displayed;

	}

	/**
	 * <p>
	 * <b> This Method provides the code for Validating the element is Enabled.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for Validating the element is Enabled.
	 * @param  Input:   element,data as Strings.
	 * @return Output: flag
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static boolean isEnabled(final String element, String data) throws Exception {

		Utils.implicitWait(30);
		boolean enabled = driver.findElement(OR.getLocator(element)).isEnabled();

		if (enabled) {
			System.out.println("The " + data + " is enabled");
		} else {
			System.out.println("The " + data + " is not enabled");
		}
		return enabled;
	}

	/**
	 * <p>
	 * <b> This Method provides the code for Validating the element is Selected.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for Validating the element is Selected.
	 * @param  Input:   element,data as Strings.
	 * @return Output: flag
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static boolean isSelected(final String element, String data) throws Exception {

		Utils.implicitWait(30);
		boolean selected = driver.findElement(OR.getLocator(element)).isSelected();

		if (selected) {
			System.out.println("The " + data + " is selected");
		} else {
			System.out.println("The " + data + " is not selected");
		}
		return selected;
	}

	/**
	 * <p>
	 * <b> This Method provides the code for Validating the element is not displayed.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for Validating the element is not displayed.
	 * @param  Input:   element,data as Strings.
	 * @return Output: flag
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static boolean isNotDisplayed(final String element, final String data) throws Exception {

		Utils.implicitWait(30);
		boolean displayed = BaseClass.driver.findElement(OR.getLocator(element)).isDisplayed();

		if (displayed) {
			System.out.println("The " + data + " is displayed");

		} else {
			System.out.println("The " + data + " is not displayed");
		}
		return displayed;
	}

	/**
	 * <p>
	 * <b> This Method provides the code for Validating the element is disabled.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for Validating the element is disabled.
	 * @param  Input:   element,data as Strings.
	 * @return Output: flag
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static boolean isDisabled(final String element, final String data) throws Exception {

		Utils.implicitWait(30);
		boolean enabled = BaseClass.driver.findElement(OR.getLocator(element)).isEnabled();

		if (enabled) {
			System.out.println("The " + data + " is not disabled");

		} else {
			System.out.println("The " + data + " is disabled");
		}
		return enabled;
	}

	/**
	 * <p>
	 * <b> This Method provides the code for Validation to compare two Array Lists.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for Validation to compare two Array Lists.
	 * @param  Input:   lists as Strings.
	 * @return Output: flag
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static Boolean compareTwoListValues(List<String> firstSet, List<String> secondSet) throws Exception {
		Boolean flag = false;
		for (int i = 0; i <= firstSet.size() - 1; ++i) {
			if (firstSet.get(i).equals(secondSet.get(i))) {
				flag = true;
				System.out.println(firstSet.get(i) + " " + secondSet.get(i));
				System.out.println("Results are fine");

			} else if (!firstSet.get(i).equals(secondSet.get(i))) {
				System.out.println(firstSet.get(i) + " " + secondSet.get(i));
				System.out.println("Results are not fine");
			}
		}
		return flag;

	}

	/**
	 * <p>
	 * <b> This Method provides the code for Validating selected item in dropdown.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for Validating selected item in dropdown.
	 * @param  Input:   Locator as Strings.
	 * @return Output: selected item
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static String getDropdownValue(String locator) throws Exception {

		WebElement element = element(locator);
		// driver.findElement(By.xpath(".//*[@id='languageControl']"));
		Select select = new Select(element);
		WebElement tmp = select.getFirstSelectedOption();
		System.out.println("Selected Text :" + tmp.getText());
		return tmp.getText();
	}

	public static void addingComma(int value) {
		DecimalFormat df = new DecimalFormat("#,###");
		System.out.println(df.format(value));
	}

	public static void remoteDriver(String element, String data) {
		RemoteWebDriver r = (RemoteWebDriver) driver;
		String s1 = "document.getElementById('" + element + "').value='" + data + "'";
		r.executeScript(s1);
	}

	/*
	 * public static Boolean isExists(String element) throws Exception
	 * 
	 * { Boolean flag=false;
	 * 
	 * if(!driver.findElements(By.xpath(OR.propertyFile(OR.ElementProp_File).
	 * getProperty(element))).isEmpty()) { System.out.println(
	 * "Element is present.."); flag=true; } else { System.out.println(
	 * "No Element is present.."); } return flag; }
	 */

	/**
	 * <p>
	 * <b> This Method provides the code for Validating the element is Exists.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for Validating the element is Exists.
	 * @param  Input:   element as Strings.
	 * @return Output: flag
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static Boolean isExists(String element) throws Exception

	{
		List<WebElement> ele = driver.findElements(OR.getLocator(element));
		Boolean flag = false;

		if (!ele.isEmpty()) {
			System.out.println("Element is present..");
			flag = true;
		} else {
			System.out.println("No Element is present..");
		}
		return flag;
	}

	/**
	 * <p>
	 * <b> This Method provides the code for generating current date for report.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for generating current date for report.
	 * @param  Input:  
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static String reportDate() {
		SimpleDateFormat sdfdate = new SimpleDateFormat("ddMMMyyyyhhmmss"); // 15Nov2017113044
		String timeZone = "IST";
		Date date = new Date();
		sdfdate.setTimeZone(TimeZone.getTimeZone(timeZone));
		System.out.println(sdfdate.format(date));
		return sdfdate.format(date);
	}

	/**
	 * <p>
	 * <b> This Method provides the code for taking the screenshot .</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for generating current date for report.
	 * @param  Input:  driver, name
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static String getScreenshot(WebDriver driver, String screenshotName) throws Exception {
		// below line is just to append the date format with the screenshot name
		// to avoid duplicate names
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		// after execution, you could see a folder "FailedTestsScreenshots"
		// under src folder
		String destination = System.getProperty("user.dir") + OR.getConfig("Path_FailedScreenShots") + screenshotName + dateName + ".png"; //"/FailedTestsScreenshots/"

		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		// Returns the captured file path
		return destination;
	}

	/**
	 * <p>
	 * <b> This Method provides the code for handling alerts.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for handling alerts.
	 * @param  Input:  
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void alertException(String testMethod) throws Exception {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			logger.log(LogStatus.FAIL, "Failed " + alertText);
			logger.log(LogStatus.FAIL, extentLogger.addScreenCapture(Utils.getScreenshot(driver, testMethod)));
			System.out.println("Alert data: " + alertText);
			alert.accept();
		} catch (NoAlertPresentException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * <p>
	 * <b> This Method provides the code for waiting till page loads.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for waiting till page loads.
	 * @param  Input: 
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void waitForPageLoad(int timeout) {

		JavascriptExecutor js = (JavascriptExecutor)driver;
		try {
			Thread.sleep(1000);
		}catch (InterruptedException e) {}
		for( int i=0; i<timeout; i++) {

			if(js.executeScript("return document.readyState").toString().equals("complete")){
				System.out.println("Page loaded successfully");
				break;
			}
		}

	}

	/**
	 * <p>
	 * <b> This Method provides the code for handling webdriver events.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for handling webdriver events.
	 * @param  Input:  driver
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static WebDriver eventHandler(WebDriver driver2) {
		// TODO Auto-generated method stub

		System.out.println("In event Handler");
		EventFiringWebDriver eventDriver = new EventFiringWebDriver(driver2);
		EventHandler handler = new EventHandler();
		eventDriver.register(handler);
		return eventDriver;

	}

	/**
	 * <p>
	 * <b> This Method provides the code to trigger email.</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code to trigger email for every execution.
	 * @param  Input:  
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void emailtrigger() throws Exception
	{
		Map<String, String> configMap = new HashMap<String, String>();
		configMap = ExcelUtils.getDataByColoumn(OR.getConfig("Path_EnvironmentDetails"), "RunConfig", "KEY");
		configMap = ExcelUtils.getDataByColoumn(OR.getConfig("Path_EnvironmentDetails"), "Configuration", "KEY",configMap.get("Set"));
		String email = configMap.get("Email");
		if (email.equalsIgnoreCase("YES")) {
			System.out.println("Email trigred");			
			Email.execute(strDate);
		}
	}

	/**
	 * <p>
	 * <b> This Method provides the code to write transactional data to prop file.</b>
	 * @author mmutukul
	 * @Description - This Method provides the code to write transactional data to prop file
	 * @param  Input:  
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */

	public static boolean writeDataPropFile(String key, String value) throws Exception
	{
		return writeDataPropFile(key,value,"");
	}
	public static boolean writeDataPropFile(String key, String value, String fileName) throws Exception
	{
		try{
			String strPropertiesFilePath = "";
			String strGlobalDataSheetPath = OR.getConfig("Trans_DataPath");
			strPropertiesFilePath = strGlobalDataSheetPath+"\\"+fileName+".properties";


			File file = new File(strPropertiesFilePath);
			Properties prop = new Properties();

			FileInputStream fIn = new FileInputStream(file);
			prop.load(fIn);
			fIn.close(); 

			FileOutputStream fOut = new FileOutputStream(file);
			if(value == "")
				prop.remove(key);         
			else
				prop.setProperty(key,value);
			prop.store(fOut,"Properties");
			fOut.close();       
			return true;
		}
		catch(Exception e)
		{
			System.out.println("Exception occured :: "+ e.getMessage());
			tearDown();
			return false;
		}
	}


	/**
	 * <p>
	 * <b> This Method provides the code to write log messages .</b>
	 * @author mmutukul
	 * @Description - This Method provides the code to write log messages .
	 * @param  Input:  message , log status
	 * @return Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */

	public static void log(String message,LogStatus logStatus)
	{
		System.out.println(message);
		logger.log(logStatus, message);
		Reporter.log(message);
	}




	//**************************************************************************************************************************************************************************************************

	/**
	 * <p>
	 * <b> This Method provides the code toExecute email.</b>
	 * @author mmutukul
	 * @Description - This Method provides the code toExecute email and send after every execution .
	 * @param  Input:  date
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void executeEmail(String strDate) throws Exception

	{
		System.out.println("In Email Execute");
		// zipFolder(strDate);
		String sourceDirPath = OR.getConfig("reportOutputPath") + strDate;
		String zipFilePath = OR.getConfig("reportOutputPath") + strDate;
		pack(sourceDirPath, zipFilePath);

		String attachmentPath = OR.getConfig("attachmentPath") + strDate + ".zip";
		// OR.getConfig("attachmentPath");
		// //D:/OperaAutomation/trunk/OperaCloud/R2.0.0.0/OperaCloud
		// 2.0/Results/14Nov2017174418.zip //<Report file path>; D:/Data/
		String[] to = OR.getConfig("to").toString().split(",");//OR.propertyFile(OR.config_Prop).getProperty("to").toString().split(",");// {"mahesh.mutukula@oracle.com","shiva.duppalli@oracle.com"};
		String[] cc = OR.getConfig("cc").toString().split(",");//OR.propertyFile(OR.config_Prop).getProperty("cc").toString().split(",");// OR.propertyFile(OR.config_Prop).getProperty("cc").toString().split(",");
		String[] bcc = {};

		String userName = OR.getConfig("userName");
		String passWord = OR.getConfig("passWord");
		String host = OR.getConfig("host");
		String port = OR.getConfig("port");
		String subject = OR.getConfig("subject");
		String text = OR.getConfig("text");
		String reportFileName = "";

		sendMail(userName, passWord, host, port, "true", "true", true, "javax.net.ssl.SSLSocketFactory", "false",
				to, cc, bcc, subject, text, attachmentPath, reportFileName);

	}

	public static boolean sendMail(String userName, String passWord, String host, String port, String starttls,
			String auth, boolean debug, String socketFactoryClass, String fallback, String[] to, String[] cc,
			String[] bcc, String subject, String text, String attachmentPath, String attachmentName) {

		// Object Instantiation of a properties file.
		Properties props = new Properties();

		props.put("mail.smtp.user", userName);

		props.put("mail.smtp.host", host);

		if (!"".equals(port)) {
			props.put("mail.smtp.port", port);
		}

		if (!"".equals(starttls)) {
			props.put("mail.smtp.starttls.enable", starttls);
			props.put("mail.smtp.auth", auth);
		}

		if (debug) {

			props.put("mail.smtp.debug", "true");

		} else {

			props.put("mail.smtp.debug", "false");

		}

		if (!"".equals(port)) {
			props.put("mail.smtp.socketFactory.port", port);
		}
		if (!"".equals(socketFactoryClass)) {
			props.put("mail.smtp.socketFactory.class", socketFactoryClass);
		}
		if (!"".equals(fallback)) {
			props.put("mail.smtp.socketFactory.fallback", fallback);
		}

		try {

			Session session = Session.getDefaultInstance(props, null);

			session.setDebug(debug);

			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(userName));

			msg.setText(text);

			msg.setSubject(subject);

			String bodyPath = OR.getConfig("bodyPath");
			InputStream is = new FileInputStream(bodyPath);
			BufferedReader buf = new BufferedReader(new InputStreamReader(is));
			String line = buf.readLine();
			StringBuilder sb = new StringBuilder();
			while (line != null) {
				sb.append(line).append("\n");
				line = buf.readLine();
			}
			String fileAsString = sb.toString();

			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(fileAsString, "text/html");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			if (attachmentPath != null && attachmentPath.length() > 0) {
				MimeBodyPart attachPart = new MimeBodyPart();

				try {
					attachPart.attachFile(attachmentPath);
				} catch (IOException ex) {
					ex.printStackTrace();
				}

				multipart.addBodyPart(attachPart);
			}

			/*
			 * MimeBodyPart textPart = new MimeBodyPart();
			 * textPart.setContent(bodyText(),"text/html");
			 * multipart.addBodyPart(textPart);
			 */

			msg.setContent(multipart);
			// msg.setText(text);

			for (int i = 0; i < to.length; i++) {
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
			}

			for (int i = 0; i < cc.length; i++) {
				msg.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
			}

			for (int i = 0; i < bcc.length; i++) {
				msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc[i]));
			}

			msg.saveChanges();

			Transport transport = session.getTransport("smtp");

			transport.connect(host, userName, passWord);

			transport.sendMessage(msg, msg.getAllRecipients());

			transport.close();

			return true;

		} catch (Exception mex) {
			mex.printStackTrace();
			return false;
		}
	}

	public static String reporttDate() {
		SimpleDateFormat sdfdate = new SimpleDateFormat("ddMMMyyyyhhmmss"); // 15Nov2017113044
		String timeZone = "IST";
		Date date = new Date();
		sdfdate.setTimeZone(TimeZone.getTimeZone(timeZone));
		System.out.println(sdfdate.format(date));
		return sdfdate.format(date);
	}

	public static void zipFolder(String strDate) {
		String reportOutputPath = OR.getConfig("reportOutputPath");
		ZipUtil.pack(new File(reportOutputPath + strDate), new File(reportOutputPath + strDate + ".zip"));
	}

	public static void pack(String sourceDirPath, String zipFilePathh) throws IOException {
		String zipFilePath = zipFilePathh + ".zip";
		Path p = Files.createFile(Paths.get(zipFilePath));
		try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
			Path pp = Paths.get(sourceDirPath);
			Files.walk(pp).filter(path -> !Files.isDirectory(path)).forEach(path -> {
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

	public static String bodyText() throws Exception {
		String bodyPath = OR.getConfig("bodyText");
		InputStream is = new FileInputStream(bodyPath);
		BufferedReader buf = new BufferedReader(new InputStreamReader(is));
		String line = buf.readLine();
		StringBuilder sb = new StringBuilder();
		while (line != null) {
			sb.append(line).append("\n");
			line = buf.readLine();
		}
		String fileAsString = sb.toString();
		return fileAsString;
	}


	/**
	 * <p>
	 * <b> This Method provides the code for selecting a value from a LOV list for a webelement.</b>
	 * @author mmutukul
	 * @Description - This Method provides the code for selecting a value from a LOV list for a webelement..
	 * @param  Input:  element, text
	 * @throws Exception 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void selectFromLOV(String LOVxPath ,String text , String tablexpath ,String search , String select, String value) throws Exception {


		try {
			System.out.println("value" +  value);
			Utils.Wait(1000);
			System.out.println("LOVxPath" +  LOVxPath);
			Utils.WebdriverWait(5000, LOVxPath, "presence");
			Utils.mouseHover(LOVxPath);
			Utils.click(Utils.element(LOVxPath));
			//logger.log(LogStatus.PASS, "Selected market code LOV");
			Utils.Wait(1000);

			System.out.println("text" +  text);
			Utils.WebdriverWait(10000, text, "presence");
			Utils.mouseHover(text);
			Utils.textBox(text, value);
			//logger.log(LogStatus.PASS, "Selected Post Charges code");
			Utils.Wait(1000);

			//Clicking on Post code Search button
			System.out.println("search" +  search);
			Utils.WebdriverWait(5000, search, "presence");
			Utils.mouseHover(search);
			Utils.click(Utils.element(search));
			//logger.log(LogStatus.PASS, "Selected Post Charges search button");
			Utils.Wait(10000);

			//Clicking on the required Post code
			List ele = new ArrayList();
			System.out.println("tablexpath" +  tablexpath);
			ele = Utils.elements(tablexpath);

			String xpath = OR.getLocator(tablexpath).toString();
			String nXpath = xpath.split("By.xpath:")[1];

			System.out.println("rows"+ ele.size());
			System.out.println("nXpath"+ nXpath);

			for(int i =0; i<ele.size();i++) {

				String test = nXpath+"["+(i+1)+"]"+"/td[1]//span[contains(@data-ocid,'LOV')]";	
				System.out.println(test+"test");


				if(driver.findElement(By.xpath(test)).getText().equals(value)) {
					driver.findElement(By.xpath(test)).click();
					//Utils.click(test);	
					break;
				}
			}
			System.out.println("value" +  value);
			System.out.println("select" +  select);
			//Selecting on the post code 
			Utils.WebdriverWait(5000, select, "presence");
			Utils.mouseHover(select);
			Utils.click(Utils.element(select));
			//logger.log(LogStatus.PASS, "Selected Post Charges select");
		}	
		catch(Exception e){
			tearDown();

		}

		// TODO Auto-generated method stub

	}

	//*************************************************************************************************************************************************************************************************
	/**
	 * @description This method splits the String and returns the last name in the array
	 * @author cpsinha
	 * @return element Name after splitting
	 * @param element
	 */
	public static String splitString(String element){
		try{
			if (element.equals("")){
				Assert.fail(" The element is null hence failing the test case");
			}
			if (element.contains(".")){
				String[] splitElementName= element.split("\\.");
				String elementName= splitElementName[splitElementName.length-1];
				return elementName;
			}else{
				return element;
			}
		}catch(Exception e){
			return element;
		}
	}

	/**
	 * @author cpsinha
	 * @Description This method clicks on a particular object after waiting for specified time
	 * @param webElement
	 * @param timeout in seconds
	 * @param option - for waiting (visible , presence, clickable , located, frame)
	 * @throws Exception*/

	public static void click(String webElement, int timeout, String option) throws IOException, Exception {
		try {
			if(timeout>0){
				WebdriverWait(timeout, webElement, option);
			}
			Actions actions = new Actions(driver);
			actions.moveToElement(Utils.element(webElement)).click().build().perform();
			System.out.println("***** Clicked on "+ splitString(webElement) + "*****");
			logger.log(LogStatus.PASS, "Clicked on " + splitString(webElement));

		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method click | Exception Des :"+ e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//logger.log(LogStatus.ERROR, "Class Utils | Method click | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())).charAt(25) +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));

			e.printStackTrace();
			tearDown();
		}
	}

	/**
	 * @author cpsinha
	 * @Description This method fills a field with particular data after waiting for specified time
	 * @param webElement
	 * @param data
	 * @param timeout in seconds
	 * @param option - for waiting (visible , presence, clickable , located, frame)
	 * @throws Exception
	 */
	public static void textBox(final String element, final String data,int timeout, String option ) throws Exception {
		try {
			if (timeout>0){
				WebdriverWait(timeout, element, option);	
			}
			Utils.implicitWait(30);
			BaseClass.driver.findElement(OR.getLocator(element)).clear();
			BaseClass.driver.findElement(OR.getLocator(element)).sendKeys(String.valueOf(data));
			System.out.println("***** Set the value of  "+ splitString(element) + " as " + data+ "*****");
			logger.log(LogStatus.PASS, "Set the value of  "+ splitString(element) + "  as " + data);
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method textBox | Exception Des :"+e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//Assert.fail("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}

	/**
	 * @author cpsinha
	 * @Description This method clicks on a particular object after waiting for specified time
	 * @param webElement
	 * @param timeout in seconds
	 * @param option - for waiting (visible , presence, clickable , located, frame)
	 * @throws Exception
	 */
	public static void jsClick(final String element,int timeout, String option ) throws IOException, Exception {

		try {
			if (timeout>0){
				WebdriverWait(timeout, element, option);	
			}
			WebElement webElement = BaseClass.driver.findElement(OR.getLocator(element));
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", webElement);
			System.out.println("*****Clicked on "+ splitString(element) + " using javascript executor *****");
			logger.log(LogStatus.PASS, "Clicked on " + splitString(element) +" using javascript executor");

		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method click | Exception Des :"+e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//logger.log(LogStatus.ERROR, "Class Utils | Method JSclick | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception: " + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())).charAt(25) +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//Assert.fail("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())).charAt(25) +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}

	/**
	 * @author cpsinha
	 * @Description This method hovers over an object after waiting for a specified time
	 * @param webElement
	 * @param timeout in seconds
	 * @param option - for waiting (visible , presence, clickable , located, frame)
	 * @throws Exception
	 */
	public static void mouseHover(final String hoverElement, int timeout, String option) throws Exception {

		try {
			if (timeout>0){
				WebdriverWait(timeout, hoverElement, option);	
			}
			Actions builder = new Actions(BaseClass.driver);
			Utils.implicitWait(60);
			WebElement element = BaseClass.driver.findElement(OR.getLocator(hoverElement));
			builder.moveToElement(element).build().perform();
			Thread.sleep(1000);
			System.out.println("***** Hovered over element "+ splitString(hoverElement) + "*****");
			logger.log(LogStatus.PASS, "Hovered over element "+ splitString(hoverElement));

		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method mouseHover | Exception Des :"+e.getMessage()+extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())) );
			//logger.log(LogStatus.ERROR, "Class Utils | Method mouseHover | Exception Des :" + e.toString() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//Assert.fail("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}

	/**
	 * @author cpsinha
	 * @Description This method gets text of an object after waiting for a specified time
	 * @param element
	 * @param timeout in seconds
	 * @param option - for waiting (visible , presence, clickable , located, frame)
	 * @return text of element
	 * @throws Exception
	 */
	public static String getText(final String element, int timeout, String option) throws IOException, Exception {
		try {
			if (timeout>0){
				WebdriverWait(timeout, element, option);	
			}
			Utils.implicitWait(30);
			String text = BaseClass.driver.findElement(OR.getLocator(element)).getText();
			System.out.println("***** Returned the text of element  "+ splitString(element) + " as  " + text + "*****");
			logger.log(LogStatus.PASS, "Returned the text of element  "+ splitString(element) + " as  " + text);
			return text;
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method getText | Exception Des :"+e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//logger.log(LogStatus.ERROR, "Class Utils | Method getText | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//Assert.fail("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
		return null;
	}

	/**
	 * @author cpsinha
	 * @Description This method gets text of an object after waiting for a specified time
	 * @param element
	 * @param timeout in seconds
	 * @param option - for waiting (visible , presence, clickable , located, frame)
	 * @return text of element
	 * @throws Exception
	 */
	public static String getAttributeOfElement(final String element, String attribute, int timeout, String option) throws IOException, Exception {
		try {
			if (timeout>0){
				WebdriverWait(timeout, element, option);	
			}
			Utils.implicitWait(30);
			String valueofAttribute = BaseClass.driver.findElement(OR.getLocator(element)).getAttribute(attribute);
			System.out.println("***** Returned the value of element  "+ splitString(element) + " as  " + valueofAttribute + "*****");
			logger.log(LogStatus.PASS, "Returned the value of element  "+ splitString(element) + " as  " + valueofAttribute);
			return valueofAttribute;
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method getText | Exception Des :"+e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//logger.log(LogStatus.ERROR, "Class Utils | Method getText | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//Assert.fail("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
		return null;
	}

	/**
	 * @author cpsinha
	 * @Description This method verifies if the element exist
	 * @param element
	 * @param timeout in seconds
	 * @param option - for waiting (visible , presence, clickable , located, frame)
	 * @throws Exception
	 */
	public static Boolean isExists(String element, int timeout, String option) throws Exception{
		try {
			List<WebElement> ele = driver.findElements(OR.getLocator(element));
			Boolean flag = false;
			if (!ele.isEmpty()) {
				System.out.println("***** Element "+ splitString(element) + " is present on the screen "+ "*****");
				logger.log(LogStatus.PASS, "Element "+ splitString(element) + " is present on the screen ");
				flag = true;
			} else {
				System.out.println("***** Element "+ splitString(element) + " is NOT present on the screen "+ "*****");
				logger.log(LogStatus.FAIL, "Element "+ splitString(element) + " is NOT present on the screen ");
			}
			return flag;
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method getText | Exception Des :"+e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())) );
			//logger.log(LogStatus.ERROR, "Class Utils | Method getText | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//Assert.fail("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
		return null;
	}
	
	/**
	 * @author pgadepal
	 * @Description This method verifies if the element exist from the list of elements based on the text it contains
	 * @param locator
	 * @param text
	 * @param attribute
	 * @throws Exception
	 */
	public static Boolean isExists(String locator, String text, String attribute) throws Exception{
		try 
		{
			List<WebElement> elements = driver.findElements(OR.getLocator(locator));
			Boolean flag=false;
			if(!elements.isEmpty())
			{
				for(WebElement e:elements)
				{
					if(e.getAttribute(attribute).contains(text))
					{
						System.out.println("***** Element with the text '"+ text + "' is present on the screen "+ "*****");
						logger.log(LogStatus.PASS, "Element with the text '"+ text + "' is present on the screen");
						flag=true;
						break;
					}
				}
				if(!flag)
				{
					logger.log(LogStatus.INFO, "Element with the text '"+ text + "' is not present on the screen");
				}
			}
			else
			{
				logger.log(LogStatus.FAIL, "No elements found with the given locator" + locator);
			}
			return flag;
		} 
		catch (Exception e) 
		{
			logger.log(LogStatus.FAIL, "Class Utils | Method getText | Exception Des :"+e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())) );
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
		return null;
	}

	/****************************************************************************************************************
	- Description: This method can be used to validate if a value is present in a table in a particular column
	- Input: Column's x-path in which the value needs to be searched
	- Output: Boolean value is returned , True is the value is present else false
	- Author: swatij
	- Date: 11/29/2018
	- Revision History:
	                - Change Date
	                - Change Reason
	                - Changed Behavior
	                - Last Changed By
	 * @throws Exception 
	 ****************************************************************************************************************/

	public static Boolean ValidateGridData(String columnxPath , String value) throws Exception {
        //Fetch Search Grid Column Data and validate
        System.out.println(columnxPath);
        Boolean flag = true;
        try {
               List <WebElement> colData = Utils.elements(columnxPath);
               System.out.println("No of rows are : " + colData.size());
               //flag = false;
               try {
                     while(flag) {
                            Thread.sleep(10000);
                            flag = driver.findElement(By.xpath("//a[contains(@data-ocid,'MORERESULTS')]")).isDisplayed();
                            driver.findElement(By.xpath("//a[contains(@data-ocid,'MORERESULTS')]")).click();
                            waitForSpinnerToDisappear(200);     
                     }
                     }
                     catch(Exception e){
                            flag = false;
                     }
               if(colData.size() > 0)
               {
                     for(int i=0;i<=colData.size()-1;i++)
                     {
                            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", colData.get(i));
                            if(colData.get(i).getText().equalsIgnoreCase(value))
                            {
                                   flag = true;
                                   logger.log(LogStatus.PASS, "Data "+colData.get(i).getText()+"  is already existing");
                                   break;
                            }
                            else
                            {
                                   System.out.println("Data is not present in the system:: "+colData.get(i).getText());                                       
                            }
                     }
               }

        }
        catch(Exception e) {
               e.printStackTrace();
               tearDown();
        }
        return flag;
 }


	/**
	 * @Description This method returns the element for any value in any table
	 * @author Cpsinha
	 * @param elementName
	 * @param column
	 * @param value
	 * @return
	 * @throws Exception 
	 */
	public static WebElement fetchTableValues (String elementName, int column, String value) throws Exception{
		try{
			Thread.sleep(3000);
			WebElement elementToBeFound=null;
			boolean flag=false;
			List<WebElement> element= driver.findElements(OR.getLocator(elementName));
			if (element.size()==0){
				Assert.fail("No Records are found in the Table");
			}
			for (WebElement eachElement:element){
				if (eachElement.getText().equalsIgnoreCase(value)){
					System.out.println("Webelement found for the value"+value );
					elementToBeFound=eachElement;
					flag=true;
					break;
				}
			}
			if (!flag){
				Assert.fail("There are no records with the specified value");
			}
			return elementToBeFound;
		}catch(Exception e){
			logger.log(LogStatus.FAIL, "Class Utils | Method getText | Exception Des :"+e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//logger.log(LogStatus.ERROR, "Class Utils | Method getText | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//Assert.fail("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
		return null;
	}

	/**
	 * @Description This method verifies the expected and actual value based on attribute of the element
	 * @author cpsinha
	 * @param expected
	 * @param message
	 * @param locator
	 * @param attribute
	 * @throws Exception
	 */
	public static void AssertEquals(final String expected, final String message, final String locator,String attribute) throws Exception {
		String actual= null;
		if(attribute.equalsIgnoreCase("text")){
			actual = BaseClass.driver.findElement(OR.getLocator(locator)).getText();
		}else{
			actual = BaseClass.driver.findElement(OR.getLocator(locator)).getAttribute(attribute);
		}
		System.out.println("The popup message is : " + actual);
		try {
			Assert.assertEquals(actual, expected, message);

		} catch (AssertionError e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method Assert | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//Assert.fail("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}

	/**
	 * @Description This method verifies the expected and actual value based on attribute of the element
	 * @author cpsinha
	 * @param expected
	 * @param message
	 * @param locator
	 * @param attribute
	 * @throws Exception
	 */
	public static void AssertContains(final String expected, final String message, final String locator,String attribute) throws Exception {

		String actual = BaseClass.driver.findElement(OR.getLocator(locator)).getAttribute(attribute);
		System.out.println("The popup message is : " + actual);
		try {
			Assert.assertTrue(actual.contains(expected), message);
		} catch (AssertionError e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method Assert | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//Assert.fail("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}

	/**
	 * @Description This method verifies the expected and actual value based on attribute of the element
	 * @author cpsinha
	 * @param expected
	 * @param message
	 * @param locator
	 * @param attribute
	 * @throws Exception
	 */
	public static void AssertEqualsIgnoreCase(final String expected, final String message, final String locator,String attribute) throws Exception {

		String actual = BaseClass.driver.findElement(OR.getLocator(locator)).getAttribute(attribute);
		System.out.println("The popup message is : " + actual);
		try {
			Assert.assertTrue(actual.equalsIgnoreCase(expected), message);
		} catch (AssertionError e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method Assert | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//Assert.fail("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}

	/**
	 * This method returns the proper format of time based on input
	 * @param time
	 * @return
	 */
	public static String verifyTimeFormat(String time){
		String validateTime=time;
		if (time.equals("")){
			Assert.fail("No value found for the time");
		}
		String[] splitTime=time.split(":");
		Integer hour=Integer.parseInt(splitTime[0]);
		if (hour>=12){
			hour=hour;//-12;
			String newHour=null;
			if (hour<=9){
				newHour="0"+String.valueOf(hour);
			}else{
				newHour=String.valueOf(hour);
			}
			validateTime=newHour+":"+splitTime[1];//+" "+"PM";
		}else{
			validateTime=time;//+" "+"AM";
		}
		return validateTime;
	}
	/**
	 * This method wait for the text to be visible for a specified time
	 * @param seconds
	 * @param locator
	 * @param value
	 * @throws Exception
	 */
	public static void waitForTextToBeVisible(final int seconds, final String locator, String value) throws Exception{
		try {

			WebDriverWait waitForDrpDown = new WebDriverWait(BaseClass.driver, seconds);
			waitForDrpDown.until(ExpectedConditions.textToBePresentInElementValue(driver.findElement(OR.getLocator(locator)), value));

		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method WebdriverWait | Exception Des :"+e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//logger.log(LogStatus.ERROR, "Class Utils | Method WebdriverWait | Exception Des :" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}
	public static void scrolltoElement(String locator) throws Exception {
		try {
			WebElement ele= driver.findElement(OR.getLocator(locator));
			JavascriptExecutor   exe=  (JavascriptExecutor)  driver;
			exe.executeScript("arguments[0].scrollIntoView(true);", ele);
			logger.log(LogStatus.INFO, "Navigated to element");

		}
		catch(ElementNotVisibleException e) {
			logger.log(LogStatus.FAIL, "Class Utils | Method scrolltoElement | Exception Des :" + e.getMessage()+extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//logger.log(LogStatus.ERROR, "Class Utils | Method WebdriverWait | Exception Des :" + e.getMessage() +logger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
		}
	}

	/**
	 * @Description This method clicks on an element based on text passed
	 * @param elementName
	 * @param value
	 * @throws Exception 
	 */
	public static void clickOnElementBasedOnText(String elementName, String value) throws Exception{
		try{
			boolean flag=false;
			List<WebElement> element= driver.findElements(OR.getLocator(elementName));
			if (element.size()==0){
				Assert.fail("No Records are found for the locator");
			}
			for (WebElement eachElement:element){
				if (eachElement.getText().equalsIgnoreCase(value)){
					eachElement.click();
					flag=true;
					break;
				}
			}
			if (!flag){
				Assert.fail("No record was found with the text"+value+" specified");
			}
		}catch(Exception e){
			logger.log(LogStatus.FAIL, "Class Utils | Method getText | Exception Des :"+e.getMessage()+extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())) );
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//Assert.fail("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}

	/**
	 * <p>
	 * <b> This method waits till the spinner disappears.</b>
	 * @author mmutukul
	 * @Description - This method waits till the spinner disappears.
	 * @param Input: time
	 * @Output: 
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static void waitForSpinnerToDisappear(int seconds) throws Exception
	{
		try {

			WebDriverWait waitForSpinner = new WebDriverWait(BaseClass.driver, seconds);
			waitForSpinner.until(ExpectedConditions.attributeContains(driver.findElement(By.xpath("//*[@class = 'x13d p_AFMaximized']")),"style","cursor: auto;"));
		}
		catch(TimeoutException e){

		}
		catch(Exception e){
			logger.log(LogStatus.FAIL, "Class Utils | Method waitForSpinnerToDisappear | Exception Des :"+e.getMessage()+extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));			
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
		}
	}
	/**
	 * @Description This method clicks on an element based on text passed
	 * @param elementName
	 * @param value
	 * @throws Exception 
	 */
	public static void clickOnElementBasedOnTextContains(String elementName, String value,String attribute) throws Exception{
		try{
			boolean flag=false;
			List<WebElement> element= driver.findElements(OR.getLocator(elementName));
			if (element.size()==0){
				Assert.fail("No Records are found for the locator");
			}
			for (WebElement eachElement:element){
				if (eachElement.getAttribute(attribute).contains(value)){
					eachElement.click();
					flag=true;
					break;
				}
			}
			if (!flag){
				Assert.fail("No record was found with the text"+value+" specified");
			}
		}catch(Exception e){
			logger.log(LogStatus.FAIL, "Class Utils | Method getText | Exception Des :"+e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			System.out.println("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			//Assert.fail("Browser Exception:" + e.getMessage() +extentLogger.addScreenCapture(Utils.getScreenshot(driver,Utils.getMethodName())));
			e.printStackTrace();
			tearDown();
		}
	}

	/**
	 * <p>
	 * <b> This Method provides the code for handling alerts.</b>
	 * @author cpsinha
	 * @Description -  This Method provides the code for handling alerts and returning boolean value based on verifiation of text
	 * @param  Input:  exceptedText
	 * @return Output: boolean- verifyAlerttext
	 * @Date: 01/08/2019
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: Chittranjan
	 * 
	 *</p>
	 */
	public static boolean accpetAlert(String exceptedText) throws Exception {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			System.out.println("Alert data: " + alertText);
			boolean verifyAlerttext=exceptedText.equalsIgnoreCase(alertText);
			alert.accept();
			return verifyAlerttext;
		} catch (NoAlertPresentException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public void InitializeScenarioLog(String testName) throws Exception
	{	
		String lStrScenarioOutputFilePath = "results"+ "\\"+testName+".xml";
		System.out.println("lStrScenarioOutputFilePath "+lStrScenarioOutputFilePath);
		logFilePath = lStrScenarioOutputFilePath;

		try{						
			lXmlEventFactory = XMLEventFactory.newInstance();			
			lOutputFactory = XMLOutputFactory.newInstance();	       
			lFileOutputStream=new FileOutputStream(lStrScenarioOutputFilePath);
			lXmlEvtWriter = lOutputFactory.createXMLEventWriter(lFileOutputStream);
			StartDocument startDocument = lXmlEventFactory.createStartDocument("UTF-8", "1.0");
			lXmlEvtWriter.add(startDocument);
		}
		catch(Exception e)
		{}

	}

	public static void LogResultsOutputForMailReport(String aStrFilePath, String aStrDescription, String aStrValueIfAny,
			String aStrTestPassStatus, boolean aBlnIsReportHeader) throws Exception {

		LogResultsOutputForMailReport(aStrFilePath, "", aStrDescription, "", "",  aStrValueIfAny, aStrTestPassStatus, false);
	}

	public static void LogResultsOutputForMailReport(String aStrFilePath, String aScenarioName, String aStrDescription, String aScenarioModule, String aTestEnvironment, String aStrValueIfAny,
			String aStrTestPassStatus, boolean aBlnIsReportHeader) throws Exception {		

		try{

			if (aStrTestPassStatus.trim() == "")
			{
				lRowType="System.out.println";
				aStrTestPassStatus = "System.out.println";
				aStrTestPassStatus =aStrTestPassStatus.toLowerCase();
				String s2 = aStrTestPassStatus.substring(0, 1).toUpperCase();
				aStrTestPassStatus = s2 + aStrTestPassStatus.substring(1);
			}		

			else{
				aStrTestPassStatus =aStrTestPassStatus.toLowerCase();			
				String s1 = aStrTestPassStatus.substring(0, 1).toUpperCase();
				aStrTestPassStatus = s1 + aStrTestPassStatus.substring(1);
				lRowType="ASSERT";
			}


			if (aBlnIsReportHeader) {

				System.out.println("%%%%%%%%%%%%%%%% lScenarioName" +aScenarioName);
				System.out.println("%%%%%%%%%%%%%%%% lScenarioDesc" +aStrDescription);
				System.out.println("%%%%%%%%%%%%%%%% lScenarioModule" +aScenarioModule);
				System.out.println("%%%%%%%%%%%%%%%% testEnvironment" +aTestEnvironment);

				Attribute lScenarioName = lXmlEventFactory.createAttribute("scenarioName",aScenarioName);
				Attribute lScenarioDesc = lXmlEventFactory.createAttribute("description",aStrDescription);
				Attribute lScenarioModule = lXmlEventFactory.createAttribute("scenarioModule",aScenarioModule);
				Attribute lTestEnvironment = lXmlEventFactory.createAttribute("testEnvironment",aTestEnvironment);


				List lAttributeList = Arrays.asList(lScenarioName,lScenarioDesc, lScenarioModule, lTestEnvironment);
				List lNameSpaceList=Arrays.asList();
				lStartElement = lXmlEventFactory.createStartElement("", "","testScenario",lAttributeList.iterator(),lNameSpaceList.iterator());

				lXmlEvtWriter.add(lStartElement);				 
			}
			else {
				Attribute lAttrRowType = lXmlEventFactory.createAttribute("RowType",lRowType);
				List lAttributeList = Arrays.asList(lAttrRowType);
				List lNameSpaceList=Arrays.asList();

				lTestRow = lXmlEventFactory.createStartElement("", "", "testRow",lAttributeList.iterator(),lNameSpaceList.iterator());
				lXmlEvtWriter.add(lTestRow);

				lRowText = lXmlEventFactory.createStartElement("", "", "rowText");
				lXmlEvtWriter.add(lRowText);

				Characters lRowTextChars = lXmlEventFactory.createCharacters(aStrDescription);
				lXmlEvtWriter.add(lRowTextChars);

				lRowTextEndEle= lXmlEventFactory.createEndElement("", "","rowText");
				lXmlEvtWriter.add(lRowTextEndEle);

				lTestData = lXmlEventFactory.createStartElement("", "", "testData");
				lXmlEvtWriter.add(lTestData);

				Characters ltestDataChars = lXmlEventFactory.createCharacters(aStrValueIfAny);
				lXmlEvtWriter.add(ltestDataChars);

				lTestDataEndEle= lXmlEventFactory.createEndElement("", "","testData");
				lXmlEvtWriter.add(lTestDataEndEle);

				lTestStatus = lXmlEventFactory.createStartElement("", "", "status");
				lXmlEvtWriter.add(lTestStatus);

				Characters lStatusChars = lXmlEventFactory.createCharacters(aStrTestPassStatus.trim());
				lXmlEvtWriter.add(lStatusChars);

				lTestStatusEndEle= lXmlEventFactory.createEndElement("", "","status");
				lXmlEvtWriter.add(lTestStatusEndEle);

				lTestRowEndElement = lXmlEventFactory.createEndElement("", "","testRow");
				lXmlEvtWriter.add(lTestRowEndElement);

			}

		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}		
	}

	public static void EndResultsLogging(){
		try{	

			System.out.println ("lXmlEvtWriter.toString(): "+lXmlEvtWriter.toString());
			lEndDocument = lXmlEventFactory.createEndDocument();
			lXmlEvtWriter.add(lEndDocument);
			lXmlEvtWriter.flush();
			lXmlEvtWriter.close();
			lFileOutputStream.close();

			BufferedReader br= new BufferedReader(new FileReader(logFilePath));			
			br.close();
			SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
			Date endTime = new Date();
			strEndTime = format.format(endTime);		

			Date d1 = null;
			Date d2 = null;
			System.out.println("Start Time :: " + strStartTime);
			System.out.println("End Time ::: "+ strEndTime);
			try {
				d1 = format.parse(strStartTime);
				d2 = format.parse(strEndTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}			
			long diff = d2.getTime() - d1.getTime();		    
			long diffMinutes = diff / (60 * 1000) % 60;	

			System.out.println("LogFile Path:: "+logFilePath);
			strKey= logFilePath.split("results")[1];
			System.out.println("strKey :: "+ strKey);
			strKey = strKey.replace('\\',' ').trim().split("\\.")[0];
			System.out.println("strKey :: "+ strKey);
			System.out.println("diffMinutes :: "+ diffMinutes);
			writeDataIntoPropertiesFile(strKey,String.valueOf(diffMinutes),"ExecutionTime.properties");
		}
		catch(Exception e)
		{}
	}

	public static boolean writeDataIntoPropertiesFile(String key, String value, String fileName) throws Exception
	{
		try{
			String strPropertiesFilePath = "";		 
			strPropertiesFilePath = OR.getConfig("Path_ExecutionTime")+"\\"+fileName;


			File file = new File(strPropertiesFilePath);
			Properties prop = new Properties();

			FileInputStream fIn = new FileInputStream(file);
			prop.load(fIn);
			fIn.close(); 

			FileOutputStream fOut = new FileOutputStream(file);        
			prop.setProperty(key,value);
			prop.store(fOut,"Properties");
			fOut.close();       

			Enumeration e = prop.propertyNames();
			while (e.hasMoreElements())
			{
				key = (String)e.nextElement();
				//getVariables().set(key, prop.getProperty(key));				         
			}
			return true;
		}
		catch(Exception e)
		{
			System.out.println("Exception occured :: "+ e.getMessage());
			return false;
		}
	}

	public static String ReadValueFromPropertiesFile(String strKey, String fileName) throws Exception
	{
		try{
			String strPropertiesFilePath = OR.getConfig("Path_ExecutionTime")+"\\"+fileName;				 
			Properties propertiesFile = new Properties();
			propertiesFile.load(new FileInputStream(strPropertiesFilePath));            
			Enumeration e = propertiesFile.propertyNames();
			String lkey;	
			while (e.hasMoreElements())			
			{
				lkey = (String)e.nextElement();				
				if(lkey.trim().equalsIgnoreCase(strKey.trim())){					
					return propertiesFile.getProperty(lkey);
				}				        
			}
			return "";
		}
		catch(Exception e)
		{
			System.out.println("Exception occured :: "+ e.getMessage());
			return "";
		}
	}

	/**
	 * This method returns table value based on column name
	 * @author cpsinha
	 */

	public static String getValueFromTableBasedOnColumnName(String columnName)throws Exception{
		String columnNumber= driver.findElement(By.xpath("//span[text()='"+columnName+"']/ancestor::th")).getAttribute("data-ocid");
		String columnValue=driver.findElements(By.xpath("//td[contains(@data-ocid,'"+columnNumber+"')]")).get(0).getAttribute("data-occellvalue");
		return columnValue;		
	}

	/**
	 * This method returns all table values based on column name 
	 * @author cpsinha
	 */

//	public static List<String> getAllValuesFromTableBasedOnColumnName(String columnName)throws Exception{
//		String columnNumber= driver.findElement(By.xpath("//span[text()='"+columnName+"']/ancestor::th")).getAttribute("data-ocid");
//		List<String> columnValues= new ArrayList<String>();
//		List<WebElement> allElements=driver.findElements(By.xpath("//td[contains(@data-ocid,'"+columnNumber+"')]"));
//		for (WebElement eachElement:allElements){
//			columnValues.add(eachElement.getAttribute("data-occellvalue"));
//		}
//		return columnValues;		
//	}
	
	public static List<String> getAllValuesFromTableBasedOnColumnName(String columnName)throws Exception{
		Boolean flag = true;
		try {
			while(flag) {
				flag = driver.findElement(By.xpath("//a[contains(@data-ocid,'MORERESULTS')]")).isDisplayed();
				driver.findElement(By.xpath("//a[contains(@data-ocid,'MORERESULTS')]")).click();
				waitForPageLoad(120);	
			}
			}
			catch(Exception e){
				flag = false;
			}
		String columnNumber= driver.findElement(By.xpath("//span[text()='"+columnName+"']/ancestor::th")).getAttribute("data-ocid");
		List<String> columnValues= new ArrayList<String>();
		List<WebElement> allElements=driver.findElements(By.xpath("//td[contains(@data-ocid,'"+columnNumber+"')]"));
		for (WebElement eachElement:allElements){
			columnValues.add(eachElement.getAttribute("data-occellvalue"));
		}
		return columnValues;		
	}
	
	/**
	 * @Description This method returns date as String in the specified Format
	 * @return formatted Date
	 */
	public static String getCurrentDateforSpecifiedFormat(String option) throws Exception {
		SimpleDateFormat sdfdate = new SimpleDateFormat(option);
		String timeZone = "IST";
		Date date = new Date();
		sdfdate.setTimeZone(TimeZone.getTimeZone(timeZone));
		return sdfdate.format(date);
	}
	/**
	 * Description This method returns a particular date in required format
	 * @return formatted Date
	 * @throws ParseException, Exception 
	 */
	public static String convertDateInReqiuredFormat(String currentFormat, String newFormat, String dateToBeFormatted) throws ParseException, Exception{
		DateFormat originalFormat = new SimpleDateFormat(currentFormat, Locale.ENGLISH);
		DateFormat targetFormat = new SimpleDateFormat(newFormat);
		Date date = originalFormat.parse(dateToBeFormatted);
		String formattedDate = targetFormat.format(date);
		System.out.println(formattedDate);
		return formattedDate;
	}
	
	/**
	 * Description This method returns business date of the application of the property logged in	 
	 * @throws ParseException, Exception 
	 */
	
	public static void getBusinessDate() throws Exception
	{
		Map<String, String> configMap = new HashMap<String, String>();
		configMap = ExcelUtils.getDataByColoumn(OR.getConfig("Path_EnvironmentDetails"), "RunConfig", "KEY");
		String date = driver.findElement(OR.getLocator("login.BusinessDate")).getText();
		date = convertDateInReqiuredFormat("E, MMM dd, yyyy", "MM-dd-yyyy", date);
		ExcelUtils.setDataByRow(OR.getConfig("Path_EnvironmentDetails"), "Configuration", "BusinessDate",
				configMap.get("Set"), date);
		System.out.println("BusinessDate is:"+ date);
	}

	public static String AdddaysToBusinessdate(int noOfdays) throws Exception{
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		Calendar c = Calendar.getInstance();
		
		Utils.getBusinessDate();
		
		Map<String,String> envMap = new HashMap<String,String>();
		envMap = ExcelUtils.getDataByRow(OR.getConfig("Path_EnvironmentDetails"),
		  "Configuration","BusinessDate");
		
		String envname = ExcelUtils.getCellData(OR.getConfig("Path_EnvironmentDetails"), "RunConfig", "Set", "VALUE");
		
		String businessDate = ExcelUtils.getCellData(OR.getConfig("Path_EnvironmentDetails"), "Configuration", "BusinessDate",envname);
		
		//String businessDates =  	envMap.get("VAB");
	
		Date newbusinessDate=new SimpleDateFormat("MM-dd-yyyy").parse(businessDate);
		c.setTime(newbusinessDate); // Now use today date.
		c.add(Calendar.DATE, noOfdays ); // Adding number of days
		String output = sdf.format(c.getTime());
		System.out.println(output);
		return output;
		
		
	}
	
	public static String addYearsToBusinessDate(int noOfYears) throws Exception {
	
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		Calendar c = Calendar.getInstance();
		
		Utils.getBusinessDate();
		
		Map<String,String> envMap = new HashMap<String,String>();
		envMap = ExcelUtils.getDataByRow(OR.getConfig("Path_EnvironmentDetails"), "Configuration", "BusinessDate");
		
		String envname = ExcelUtils.getCellData(OR.getConfig("Path_EnvironmentDetails"), "RunConfig", "Set", "VALUE");
		
		String businessDate = ExcelUtils.getCellData(OR.getConfig("Path_EnvironmentDetails"), "Configuration", "BusinessDate",envname);
		
		Date newbusinessDate=new SimpleDateFormat("MM-dd-yyyy").parse(businessDate);
		c.setTime(newbusinessDate);
		c.add(Calendar.YEAR, noOfYears ); // Adding number of years
		String output = sdf.format(c.getTime());
		System.out.println(output);
		return output;
	}
	
	public static void verifyPDFContent(String strURL, String reqTextInPDF) {

		try {
			/*URL url = new URL(strURL);
			BufferedInputStream file = new BufferedInputStream(url.openStream());
			PDFParser parser = new PDFParser(file);*/
			
			File file = new File(strURL);
			PDDocument doc = PDDocument.load(file);
			String text = new PDFTextStripper().getText(doc);
			doc.close();
			System.out.println(text);
			if(text.contains(reqTextInPDF))
			{
				System.out.println("Requested text "+reqTextInPDF+" is available in pdf : Validated");
			}
			else
			{
				System.out.println("Requested text is not available");
			}
			
			Assert.assertTrue(text.contains(reqTextInPDF));
			
		
		} catch (IOException e) {
		

		}
	}


}
