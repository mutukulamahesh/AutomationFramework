package com.framework.utilities;

import java.util.HashMap;
import java.util.Map;

import com.relevantcodes.extentreports.LogStatus;

/**
 * <p>
 * <b> This Class provides the code for Login functionality.</b>
 * 
 * @author mmutukul
 * Description - This Class provides the code for Login functionality.
 * Input: UserName, Password, Property
 * Output: 
 * Date: 6/12/2018
 * Revision History:
 *                  - Change Date : 19/12/2018
 *                  - Change Reason: for adding validation for login page
 *                  - Changed Behavior : added validation for login page
 *                  - Last Changed By: mahesh
 * 
 *         </p>
 */
public class LoginPage extends Utils {

	/**
	 * <p>
	 * <b> This Method provides the code related to SignIn functionality.</b>
	 * 
	 * @param username
	 *            as Strings.
	 * @param password
	 *            as Strings.
	 * @param validate
	 *            parameter as boolean when validation required
	 *            </p>
	 */
	

	public static void Login() throws Exception {
		
		String Scriptname = Utils.getMethodName();
		Map<String, String> configMap = new HashMap<String, String>();
		configMap = ExcelUtils.getDataByColoumn(OR.getConfig("Path_EnvironmentDetails"), "RunConfig", "KEY");
		configMap = ExcelUtils.getDataByColoumn(OR.getConfig("Path_EnvironmentDetails"), "Configuration", "KEY",configMap.get("Set"));
		System.out.println("Config: " + configMap);
		
		try {
			Utils.takeScreenshot("Login");
			System.out.println("Sign in method started");
			
			System.out.println("Login Title: "+driver.getTitle());
			
			if(driver.getTitle().contains("Login"))
			{
				Utils.implicitWait(30);
				Utils.textBox("login.username", configMap.get("UserName"));
				logger.log(LogStatus.PASS, "Entered Username: ");

				Utils.implicitWait(30);
				Utils.textBox("login.password", configMap.get("Password"));
				logger.log(LogStatus.PASS, "Entered Password ");

				Utils.implicitWait(30);
				Utils.click("login.login");
				logger.log(LogStatus.PASS, "Selected Login Button");
				waitForSpinnerToDisappear(45);
			}
			else
			{
				Utils.gotoPage(configMap.get("URL"));
				
				if(driver.getTitle().contains("Login")){
					LoginFunc();
				}
				else
				{
					Utils.gotoPage(configMap.get("URL"));
					if(driver.getTitle().contains("Login"))
					{
						LoginFunc();
					
				    }
					else
					{
						driver.quit();
					}
			}
			}
			
		} catch (Exception e) {

			Utils.takeScreenshot(driver, Scriptname);
			logger.log(LogStatus.FAIL, "Login :: Failed");
			logger.log(LogStatus.FAIL, "Exception occured in Login due to:" + e.getMessage());
			logger.log(LogStatus.FAIL, extentLogger.addScreenCapture(ReportsClass.getScreenshot(driver, Scriptname)));
			tearDown();
		}

	}
	
	
	/**
	 * <p>
	 * <b> This Method provides the code related to SignIn functionality.</b>
	 * 
	 * @param username
	 *            as Strings.
	 * @param password
	 *            as Strings.
	 * @param validate
	 *            parameter as boolean when validation required
	 *            </p>
	 */
     public static void LoginFunc() throws Exception {
		
		String Scriptname = Utils.getMethodName();
		Map<String, String> configMap = new HashMap<String, String>();
		configMap = ExcelUtils.getDataByColoumn(OR.getConfig("Path_EnvironmentDetails"), "RunConfig", "KEY");
		configMap = ExcelUtils.getDataByColoumn(OR.getConfig("Path_EnvironmentDetails"), "Configuration", "KEY",configMap.get("Set"));
		System.out.println("Config: " + configMap);
		
		try {
			
				Utils.implicitWait(30);
				Utils.textBox("login.username", configMap.get("UserName"));
				logger.log(LogStatus.PASS, "Entered Username: ");

				Utils.implicitWait(30);
				Utils.textBox("login.password", configMap.get("Password"));
				logger.log(LogStatus.PASS, "Entered Password ");

				Utils.implicitWait(30);
				Utils.click("login.login");
				logger.log(LogStatus.PASS, "Selected Login Button");
				waitForSpinnerToDisappear(45);

		} catch (Exception e) {

			Utils.takeScreenshot(driver, Scriptname);
			logger.log(LogStatus.FAIL, "Login :: Failed");
			logger.log(LogStatus.FAIL, "Exception occured in Login due to:" + e.getMessage());
			logger.log(LogStatus.FAIL, extentLogger.addScreenCapture(ReportsClass.getScreenshot(driver, Scriptname)));
			tearDown();
		}

	}

	/**
	 * <p>
	 * <b> This Method provides the code related to Sign Out functionality.</b>
	 * 
	 * @param validate
	 *            parameter as boolean when validation required
	 *            </p>
	 */
	public static void Logout() throws Exception {


		try {

			if (Utils.isExists("login.header")) {
				Utils.implicitWait(30);
				// Utils.WebdriverWait(100, "header", "clickable");
				Utils.click(Utils.element("login.header"), false);
				Utils.jsClick("login.header");
				System.out.println("Clicked on header....");
				
				Utils.WebdriverWait(200, "login.signout", "visible");
				Utils.click(Utils.element("login.signout"), false);
				Utils.takeScreenshot("Logout");
				//waitForSpinnerToDisappear(100);
				waitForPageLoad(100);
				waitForPageLoad(100);
				//Add validation
				Thread.sleep(5000);
				if(driver.getTitle().contains("Login"))
				{
						System.out.println("Logged Out from the Application");
						logger.log(LogStatus.PASS, "Logged Out from the Application");
				}
				else
				{
					waitForPageLoad(100);
					if(driver.getTitle().contains("Login"))
					{
							System.out.println("Logged Out from the Application");
							logger.log(LogStatus.PASS, "Logged Out from the Application");
					}
					
				}
			} else {

			}

		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Logout :: Failed");
			logger.log(LogStatus.FAIL, "Exception occured in Logout due to:" + e.getMessage());
			logger.log(LogStatus.FAIL, extentLogger.addScreenCapture(ReportsClass.getScreenshot(driver, "Logout")));
		}

	}

	/**
	 * <p>
	 * <b> This Method provides the code related to Validation for landing Page.</b>
	 * 
	 * @param greeting text
	 *            as Strings.
	 *            </p>
	 */
	public static void Loginvalidation() throws Exception {

		try {

			if (Utils.isExists("login.session_error")) {
				Utils.WebdriverWait(100, "login.session_error_msg", "presence");
				String error = Utils.getText("login.session_error_msg");
				System.out.println("Error msg: " + error);
				logger.log(LogStatus.FAIL, "Session error-Not Logged In the Application - " + error);
				logger.log(LogStatus.FAIL, "Session error-Not Logged In the Application - " + error);
				logger.log(LogStatus.FAIL, extentLogger.addScreenCapture(ReportsClass.getScreenshot(driver, "login")));
				Utils.tearDown();
				//driver.get("http://OCFUNCTIONAL1:Welcome1234!@cloudfunmw1.us.oracle.com:9003/OPERA9/cloudfun/operacloud/");
			}

			waitForSpinnerToDisappear(200);
			 if (Utils.isExists("login.homepage_greeting")) {
				 System.out.println("in Login Validation");
				Utils.WebdriverWait(30, "login.homepage_greeting", "presence");
				String greeting = Utils.getText("login.homepage_greeting");
				System.out.println(greeting);
				Thread.sleep(1000);
				if (greeting.contains(OR.getTestData("homePage_ValidationText"))) {
					System.out.println("Landed in home page with user: " + greeting);
					Utils.takeScreenshot("Homepage");
					logger.log(LogStatus.PASS, "Logged In the Application");
					logger.log(LogStatus.PASS, "Landed In Home Page with User: " + greeting);
					waitForSpinnerToDisappear(45);

				} else {
					tearDown();
					System.out.println("Not Landed in home page");
					logger.log(LogStatus.FAIL, "Not Logged In the Application ");
				}
			} 
			 else if (Utils.isExists("login.session_error")) {
					Utils.WebdriverWait(100, "login.session_error_msg", "presence");
					String error = Utils.getText("login.session_error_msg");
					System.out.println("Error msg: " + error);
					logger.log(LogStatus.FAIL, "Session error-Not Logged In the Application - " + error);
					logger.log(LogStatus.FAIL, "Session error-Not Logged In the Application - " + error);
					logger.log(LogStatus.FAIL, extentLogger.addScreenCapture(ReportsClass.getScreenshot(driver, "login")));
					Utils.tearDown();
				}
			 else {
				System.out.println("Not Landed in home page");
				logger.log(LogStatus.FAIL, "Not Logged In the Application ");
				tearDown();
			}
			property();
			// Thread.sleep(5000);

		} catch (Exception e) {
			logger.log(LogStatus.FAIL, "Login Validation :: Failed");
			logger.log(LogStatus.FAIL, "Exception occured in LoginValidation due to:" + e.getMessage());
			logger.log(LogStatus.FAIL, extentLogger.addScreenCapture(ExtentReportsClass.getScreenshot(driver, "login")));			
			tearDown();
		}

	}
	
	

	/**
	 * <p>
	 * <b> This Method provides the code related to Property functionality.</b>
	 * 
	 * @param property name
	 *            as Strings.
	 *            </p>
	 */
	public static void property() throws Exception {
		
		Map<String, String> configMap = new HashMap<String, String>();
		configMap = ExcelUtils.getDataByColoumn(OR.getConfig("Path_EnvironmentDetails"), "RunConfig", "KEY");
		configMap = ExcelUtils.getDataByColoumn(OR.getConfig("Path_EnvironmentDetails"), "Configuration", "KEY",configMap.get("Set"));
		System.out.println("Config: " + configMap);
		String property = configMap.get("Property");

		try {
			String greeting = Utils.getText("login.homepage_property");
			System.out.println(greeting);
			Thread.sleep(2000);

			if (greeting.contains(property)) {// OR.getConfig("property")
				System.out.println("Landed in property: " + greeting);
				System.out.println("*******************************************************************");
				Utils.takeScreenshot("Homepage");
				logger.log(LogStatus.PASS, "Landed in property:: " + greeting);
				
			} else {

				// Select header
				Utils.implicitWait(30);
				Utils.WebdriverWait(100, "login.header", "presence");
				Utils.click(Utils.element("login.header"), false);
				System.out.println("Clicked on header....");
				logger.log(LogStatus.PASS, "Clicked on header....");

				Utils.WebdriverWait(100, "login.homepage_selectLocation", "presence");
				Utils.jsClick("login.homepage_selectLocation");
				System.out.println("Location Dropdown selected");
				logger.log(LogStatus.PASS, "Location Dropdown selected");
				waitForSpinnerToDisappear(45);
				
				Utils.WebdriverWait(100, "login.homepage_radbtnSelectproperty", "presence");
				Utils.jsClick("login.homepage_radbtnSelectproperty");
				System.out.println("Property Radio button selected");
				logger.log(LogStatus.PASS, "Property Radio button selected");
				waitForSpinnerToDisappear(45);
				
				
				waitForPageLoad(100);
				Utils.WebdriverWait(100, "login.homepage_SearchTextProperty", "presence");
				Utils.textBox("login.homepage_SearchTextProperty", property);
				//Thread.sleep(2000);
				System.out.println("Provided property in textbox....");
				logger.log(LogStatus.PASS, "Provided desired property in textbox....");

				Utils.WebdriverWait(100, "login.homepage_PropSearchBtn", "clickable");
				Utils.jsClick("login.homepage_PropSearchBtn");
				waitForSpinnerToDisappear(45);
				System.out.println("Selected search btn....");

				Utils.WebdriverWait(100, "login.homepage_PropSelectBtn", "clickable");
				Utils.jsClick("login.homepage_PropSelectBtn");
				System.out.println("Select select btn for property..." + property);
				logger.log(LogStatus.PASS, "Select select btn for property..." + property);
				waitForSpinnerToDisappear(100);

				// Loginvalidation();

			}

		} catch (Exception e) {

			logger.log(LogStatus.FAIL, "Property Selection :: Failed");
			logger.log(LogStatus.FAIL, "Exception occured in Property Selection due to:" + e.getMessage());
			logger.log(LogStatus.FAIL, extentLogger.addScreenCapture(ExtentReportsClass.getScreenshot(driver, "propertySelction")));			
			tearDown();
		}
	}

	/**
	 * <p>
	 * <b> This Method provides the code for selecting home button.</b>
	 * 
	 *            </p>
	 */
	public static void homePage() throws Exception {

		try {

			Utils.WebdriverWait(100, "login.homepage", "clickable");
			Utils.mouseHover("login.homepage");
			Utils.click(Utils.element("login.homepage"));
			Thread.sleep(5000);
			// Loginvalidation();

		} catch (Exception e) {

			logger.log(LogStatus.FAIL, "Home Page Selection :: Failed");
			logger.log(LogStatus.FAIL, "Exception occured in HomePage Selection due to:" + e.getMessage());
			logger.log(LogStatus.FAIL, extentLogger.addScreenCapture(ExtentReportsClass.getScreenshot(driver, "homePageSelction")));			
			tearDown();
		}
	}
	

	/**
	 * <p>
	 * <b> This Method provides the code related to Casier Login functionality.</b>
	 * 
	 * @param username
	 *            as Strings.
	 * @param password
	 *            as Strings.
	 *            </p>
	 */
	public static void cashierLogin() throws Exception {

		try {

			String CashierId = ExcelUtils.getCellData("TestData", "Login", "Login", "CashierId");
			String Password = ExcelUtils.getCellData("TestData", "Login", "Login", "Password");
			System.out.println(Password);

			WebdriverWait(30, "login.CashierID_Textbox", "presence");
			textBox("login.CashierID_Textbox", CashierId);
			Thread.sleep(5000);
			// WebdriverWait(30, "CashierID_Password_textbox", "presence");
			jsTextbox("login.CashierID_Password_textbox", Password);

			WebdriverWait(30, "login.Cashier_Login_Button", "clickable");
			jsClick("login.Cashier_Login_Button");

		} catch (Exception e) {

			logger.log(LogStatus.FAIL, "Cashier Login :: Failed");
			logger.log(LogStatus.FAIL, "Exception occured in Cashier Login due to:" + e.getMessage());
			logger.log(LogStatus.FAIL, extentLogger.addScreenCapture(ExtentReportsClass.getScreenshot(driver, "Cashier Login")));			
			tearDown();
		}
	}

}
