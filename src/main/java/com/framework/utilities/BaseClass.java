package com.framework.utilities;
/**
 * <p>
 * <b> This Class provides the driver object for code .</b>
 * @author mmutukul
 * </p>
 */
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentTest;

/**
 * <p>
 * <b> This Class provides the code for holding webdriver instance for the session.</b>
 * 
 * @author mmutukul
 * @Description - This Class provides the code for holding webdriver instance for the session.
 * @Date: 6/12/2018
 * @Revision History:
 *                  - Change Date :
 *                  - Change Reason: 
 *                  - Changed Behavior : 
 *                  - Last Changed By: mahesh
 * 
 *         </p>
 */
public class BaseClass {
	

    public static WebDriver driver = null;
    public static ExtentTest logger;
    public static boolean bResult;

    /**
	 * <p>
	 * <b> This Method provides the code related to SignIn functionality.</b>
     * @param  driver parameter as WebDriver Object to provide object to the whole project  
	 * </p>
	 */
    public BaseClass(final WebDriver driver, ExtentTest logger) {
    	
        BaseClass.driver = driver;
        BaseClass.logger = logger;
        BaseClass.bResult = true;
        
        System.out.println("In Base Class");
    }
    
}
