package com.framework.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * <p>
 * <b> This Class provides the code for Object repository related functionality.</b>
 * 
 * @author mmutukul
 * @Description - This Class provides the code for Object repository functionality.
 * @Date: 6/12/2018
 * @Revision History:
 *                  - Change Date :
 *                  - Change Reason: 
 *                  - Changed Behavior : 
 *                  - Last Changed By: mahesh
 * 
 *         </p>
 */
public class OR {
	
	/**
	 * <p>
	 * <b> This Method provides the code related to Properties file generation for Object Repository..</b>
	 * @author mmutukul
	 * @Description - This Method provides the code related to Properties file generation for Object Repository..
	 * @param Input: Property file
	 * @Output: Properties
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
    public static Properties propertyFile(final String propertyFile) {


        //String workingDir = System.getProperty("user.dir");
        File file = new File(propertyFile);
        //System.out.println("property: "+propertyFile);
        //File file = new OR().getFile(propertyFile);
        
        FileInputStream fileInput = null;
        try {
              fileInput = new FileInputStream(file);
            } catch (FileNotFoundException e) 
            {
              e.printStackTrace();
            }
        Properties prop = new Properties();

        // load properties file
        try {
            prop.load(fileInput);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prop;
        
    }
    
    /**
	 * <p>
	 * <b> This Method provides the code related to Properties file generation for Object Repository..</b>
	 * @author mmutukul
	 * @Description - This Method provides the code related to Properties file generation for Object Repository..
	 * @param Input: Property file
	 * @Output: Properties
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
    public static Properties property(final String propertyFile) {


        File file = new OR().getFile(propertyFile);
        FileInputStream fileInput = null;
        try {
              fileInput = new FileInputStream(file);
            } catch (FileNotFoundException e) 
            {
              e.printStackTrace();
            }
        Properties prop = new Properties();

        // load properties file
        try {
            prop.load(fileInput);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prop;
    }
    
    /*public static Properties propertyResource(final String propertyFile) throws Exception {


    	
        Properties prop = new Properties();

        // load properties file
        try {
            prop.load(new OR().getFileResource(propertyFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prop;
    }*/
    
       
    /**
	 * <p>
	 * <b> This Method provides the code for generating the locator ..</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for generating the locator for the element ....
	 * @param Input: elemnet key
	 * @Output: loctaor value
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
    public static By getLocator(final String ElementName) throws Exception {
    	    	
    	String locator = null;
    	int i = ElementName.indexOf(".");
    	String propFile = ElementName.substring(0, i);
    	//String rest = ElementName.substring(i);
    	//System.out.println(propFile);
    	
    	switch (propFile) {
    	case "login":
			locator = propertyFile(OR.getConfig("locators_Prop")+"generic\\"+propFile+".properties").getProperty(ElementName);
			break;
			
		case "Profile":
			locator = propertyFile(OR.getConfig("locators_Prop")+"crm\\"+propFile+".properties").getProperty(ElementName);
			break;
			
		case "Reservation":
			locator = propertyFile(OR.getConfig("locators_Prop")+"rsv\\"+propFile+".properties").getProperty(ElementName);
			break;
			
		case "config":
			locator = propertyFile(OR.getConfig("locators_Prop")+"config\\"+propFile+".properties").getProperty(ElementName);
			break;
			
		case "Configuration":
			locator = propertyFile(OR.getConfig("locators_Prop")+"config\\"+propFile+".properties").getProperty(ElementName);
			break;
			
		case "Events":
			locator = propertyFile(OR.getConfig("locators_Prop")+"evm\\"+propFile+".properties").getProperty(ElementName);
			break;
			
		case "Exports":
			locator = propertyFile(OR.getConfig("locators_Prop")+"OPERAUIGeneric\\"+propFile+".properties").getProperty(ElementName);
		
			break;
		case "Reports":
			locator = propertyFile(OR.getConfig("locators_Prop")+"OPERAUIGeneric\\"+propFile+".properties").getProperty(ElementName);
			break;
		case "osb":
			locator = propertyFile(OR.getConfig("locators_Prop")+"generic\\"+propFile+".properties").getProperty(ElementName);
			break;
			
		case "weblogic":
			locator = propertyFile(OR.getConfig("locators_Prop")+"generic\\"+propFile+".properties").getProperty(ElementName);
			break;

		case "Housekeeping":
		case "Checkin": 
		case "Checkout": 
		case "EndOfDay": 	
			locator = propertyFile(OR.getConfig("locators_Prop")+"fof\\"+propFile+".properties").getProperty(ElementName);
			break;	
			
		case "Rates": 	
			locator = propertyFile(OR.getConfig("locators_Prop")+"rates\\"+propFile+".properties").getProperty(ElementName);
			break;

		}
    	
    	
    	
        // Read value using the logical name as Key
        //String locator = propertyFile(OR.locators_Prop).getProperty(ElementName);
        //String locator = propertyFile(OR.getConfig("locators_Prop")+propFile+".properties").getProperty(ElementName);
       //System.out.println("Locator: "+locator);
        
        // Split the value which contains locator type and locator value
        String locatorType = locator.split("&")[0];
        String locatorValue = locator.split("&")[1];
        // Return a instance of By class based on type of locator
        if (locatorType.toLowerCase().equals("id")) {
            return By.id(locatorValue);
        } else if (locatorType.toLowerCase().equals("name")) {
            return By.name(locatorValue);
        } else if ((locatorType.toLowerCase().equals("classname")) || (locatorType.toLowerCase().equals("class"))) {
            return By.className(locatorValue);
        } else if ((locatorType.toLowerCase().equals("tagname")) || (locatorType.toLowerCase().equals("tag"))) {
            return By.className(locatorValue);
        } else if ((locatorType.toLowerCase().equals("linktext")) || (locatorType.toLowerCase().equals("link"))) {
            return By.linkText(locatorValue);
        } else if (locatorType.toLowerCase().equals("partiallinktext")) {
            return By.partialLinkText(locatorValue);
        } else if ((locatorType.toLowerCase().equals("cssselector")) || (locatorType.toLowerCase().equals("css"))) {
            return By.cssSelector(locatorValue);
        } else if (locatorType.toLowerCase().equals("xpath")) {
            return By.xpath(locatorValue);
        } else {
            throw new Exception("Locator type '" + locatorType + "' not defined!!");
        }
    }
    
    public static String getORpropvalue(final String ElementName) {
    	String locator = null;
    	int i = ElementName.indexOf(".");
    	String propFile = ElementName.substring(0, i);
    	
    	
    	switch (propFile) {
    	case "login":
			locator = propertyFile(OR.getConfig("locators_Prop")+"generic\\"+propFile+".properties").getProperty(ElementName);
			break;
		case "Profile":
			locator = propertyFile(OR.getConfig("locators_Prop")+"crm\\"+propFile+".properties").getProperty(ElementName);
			break;
		case "Reservation":
			locator = propertyFile(OR.getConfig("locators_Prop")+"rsv\\"+propFile+".properties").getProperty(ElementName);
			break;
		case "Housekeeping": 	
			locator = propertyFile(OR.getConfig("locators_Prop")+"fof\\"+propFile+".properties").getProperty(ElementName);
			break;
		case "config":
			locator = propertyFile(OR.getConfig("locators_Prop")+"config\\"+propFile+".properties").getProperty(ElementName);
			break;
		case "Configuration":
			locator = propertyFile(OR.getConfig("locators_Prop")+"config\\"+propFile+".properties").getProperty(ElementName);
			break;
		case "Events":
			locator = propertyFile(OR.getConfig("locators_Prop")+"evm\\"+propFile+".properties").getProperty(ElementName);
			break;
		case "Exports":
			locator = propertyFile(OR.getConfig("locators_Prop")+"rsv\\"+propFile+".properties").getProperty(ElementName);
			//System.out.println("locator: "+locator);
			break;
		case "osb":
			locator = propertyFile(OR.getConfig("locators_Prop")+"generic\\"+propFile+".properties").getProperty(ElementName);
			break;
		case "weblogic":
			locator = propertyFile(OR.getConfig("locators_Prop")+"generic\\"+propFile+".properties").getProperty(ElementName);
			break;
			
		case "Rates": 	
			locator = propertyFile(OR.getConfig("locators_Prop")+"rates\\"+propFile+".properties").getProperty(ElementName);
			break;

		}
    	

        
        return locator.split("&")[1];
    	
    }
    public static String setLog(String key)
    {
    	 OR.property(key);
		return null;
    }
    
    /**
	 * <p>
	 * <b> This Method provides the code for getting the value from config properties file..</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for generating the locator for the element ....
	 * @param Input: key
	 * @Output: value
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
    public static String getConfig(String key) 
    {
    	String value = OR.propertyFile(OR.config_Prop).getProperty(key);
		
    	return value;
    }
    
    /**
	 * <p>
	 * <b> This Method provides the code for getting the value from TestData properties file..</b>
	 * @author mmutukul
	 * @Description - This Method provides the code for getting the value from TestData properties file..
	 * @param Input: key
	 * @Output: value
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
    public static String getTestData(String key)
    {
    	String value = OR.propertyFile(OR.getConfig("testdata_Prop")).getProperty(key);
    	return value;
    }
    
    
    /**
	 * <p>
	 * <b> This Method provides the code for getting the value from Transaction Data properties file..</b>
	 * @author mmutukul
	 * @Description -  This Method provides the code for getting the value from Transaction Data properties file..
	 * @param Input: key
	 * @Output: value
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
    public static String getTransData(String key)
    {
    	String value = OR.propertyFile(OR.getConfig("trans_Prop")).getProperty(key);
    	return value;
    }
    
    
   
    public File getFile(String resourceFile)
    {
    	//System.out.println("Clsss: "+getClass());
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource(resourceFile).getFile());
    	return file;
    }
    
    /**
   	 * <p>
   	 * <b> This Method provides the code for getting the Images from Images folder from Metadata for interacting with windows based application</b>
   	 * @author swatij
   	 * @Description -  This Method provides the code for getting Images from Images folder from Metadata
   	 * @param Input: Image Name
   	 * @Output: Path of the Image
   	 * @Date: 2/7/2019
   	 * @Revision History:
   	 *                  - Change Date : 
   	 *                  - Change Reason: 
   	 *                  - Changed Behavior : 
   	 *                  - Last Changed By: swati
   	 * 
   	 *</p>
   	 */
    public static String getImage(String filename) 
    {
    	String currentDir = System.getProperty("user.dir");
    	int i = currentDir.lastIndexOf("\\auto");
    	String ImageFile = currentDir.substring(0, i);
    	String locator = ImageFile+OR.getConfig("Path_Images")+"\\"+filename;
	return locator;
    }
  
   /* public InputStream getFileResource(String resourceFile)
    {
    	InputStream in = getClass().getClassLoader().getResourceAsStream("/config.properties");
    	InputStreamReader inputreader = new InputStreamReader(in);
    	//BufferedReader reader =new BufferedReader(inputreader);
    	return in;
    	   	
    }*/
    
   /* public  File getFileResource(String fileName) throws Exception{
		
  	  String result = "";
  		
  	  ClassLoader classLoader = getClass().getClassLoader();
  	 
  		result = IOUtils.toString(classLoader.getResourceAsStream(fileName));
  		File file = new File(result);
  	    return file;
  		
    }*/

  

    // ******************************************************************************************************************************************************************************
    // @@@@@ Common Objects @@@@@
    // ******************************************************************************************************************************************************************************

    public static final String config_Prop = "..//..//testdata\\metadata\\config.properties";


    // ***********************************************************************************************************************************************************
    // @@@@@ ScreenShots Paths & Objects @@@@@
    // *********************************************************************************************************************************************************

    public static final String Path_ScreenShot = getConfig("Path_ScreenShots");//"screenshots\\";

}
