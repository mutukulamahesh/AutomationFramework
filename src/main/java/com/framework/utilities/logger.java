package com.framework.utilities;

import org.testng.Reporter;

import com.relevantcodes.extentreports.LogStatus;
/**
 * <p>
 * <b> This Class provides the code for Logs.</b>
 * @author mmutukul
 * </p>
 */
public class logger extends Utils{


	public static void log(LogStatus logStatus, String strMessage){

		extentLogger.log(logStatus, strMessage);
		String strStatus = logStatus.toString();

		if(strStatus.equalsIgnoreCase("pass")){
			try{
				Reporter.log(strMessage);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		else if (strStatus.equalsIgnoreCase("fail")){
			try{
				Reporter.log(strMessage);				
				softAssertion.assertTrue(false);								
			}
			catch(AssertionError e){
				e.printStackTrace();
			}
		}		
		
		try {			 
			LogResultsOutputForMailReport(lStrScenarioOutputFilePath,strMessage,"",strStatus,false);
		} catch (Exception e) {			
			e.printStackTrace();
		}	
	}
	//public static Logger Logg = Logger.getLogger(Log.class.getName());

	/* public static void startTestCase(final String sTestCaseName)

    {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        Date date = new Date();

        Log.Logg.info("************************************************************************************************");
        Log.Logg.info("************************************************************************************************");
        Log.Logg.info("     $$$$$$$$$$$$$$$$$$    " + sTestCaseName + " @ " + dateFormat.format(date) + "   $$$$$$$$$$$$$$$$$   ");
        Log.Logg.info("************************************************************************************************");
        Log.Logg.info("************************************************************************************************");

    }

    public static void endTestCaseName(final String sTestCaseName) {

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        Date date = new Date();

        Log.Logg.info("************************************************************************************************");
        Log.Logg.info("************************************************************************************************");
        Log.Logg.info("     $$$$$$$$$$  " + "  E-N-D of " + sTestCaseName + " @ " + dateFormat.format(date) + " $$$$$$$$$$$$     ");
        Log.Logg.info("************************************************************************************************");
        Log.Logg.info("************************************************************************************************");
        Log.Logg.info(" ");
        Log.Logg.info(" ");
        Log.Logg.info(" ");
    }

    public static void info(final String message) {
        Log.Logg.info(message);
    }

    public static void warn(final String message) {
        Log.Logg.warn(message);
    }

    public static void error(final String message) {
        Log.Logg.error(message);
    }

    public static void fatal(final String message) {
        Log.Logg.fatal(message);
    }

    public static void debug(final String message) {
        Log.Logg.debug(message);
    }
	 */
}
