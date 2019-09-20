package com.framework.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.relevantcodes.extentreports.LogStatus;

/**
 * <p>
 * <b> This Class provides the Connection activities with DB.</b>
 * 
 * @author TAGG
 * @Description - This Class provides the methods to connect with DB and perform query operations
 * @Date: 6/12/2018
 * @Revision History:
 *                  - Change Date :
 *                  - Change Reason: 
 *                  - Changed Behavior : 
 *                  - Last Changed By: mahesh
 * 
 *         </p>
 */
public class DatabaseUtil {

	public static Connection connection = null;

	/**
	 * <p>
	 * <b> This method establishes the DB Connection using the given connection string parameters.</b>
	 * @author TAGG
	 * @Description - This method establishes the DB Connection using the given connection string parameters
	 * @param Input: Connection details as Strings
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
	public static void setDBConnection(String host, String port, String sid, String uname, String pwd) throws Exception {
		String conString = "jdbc:oracle:thin:@" + host + ":" + port + ":" + sid;
		logger.log(LogStatus.INFO, "Connection String: "+conString +" , "+"User/Password: "+uname+"/"+pwd);

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(conString, uname, pwd);
		}
		catch (SQLException e) {
			System.out.println("Connection Failed! "+conString);
			logger.log(LogStatus.INFO, "DB Connection failed!! **>> Method: setDBConnection");
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * <b> This method executes the given SQL Query and returns the result set.</b>
	 * @author TAGG
	 * @Description - This method executes the given SQL Query and returns the result set
	 * @param Input: Query
	 * @Output: result Set
	 * @Date: 6/12/2018
	 * @Revision History:
	 *                  - Change Date : 
	 *                  - Change Reason: 
	 *                  - Changed Behavior : 
	 *                  - Last Changed By: mahesh
	 * 
	 *</p>
	 */
	public static ResultSet executeQuery(String queryString, Connection con) throws Exception {
		String queryHeader = "";
		ResultSet rs = null;
		Statement stmt;
		try {
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(queryHeader + queryString);
		}
		catch (SQLException e) {
			System.out.println("Error occurred while executing the query "+queryString + " **>> Method: executeQuery");
			logger.log(LogStatus.INFO, "Error occurred while executing the query "+queryString + " **>> Method: executeQuery");
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * <p>
	 * <b> This method closes the Database connection.</b>
	 * @author TAGG
	 * @Description - This method closes the Database connection
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
	public static void closeDatabase() throws Exception {
		connection.close();
		try {
			connection.close();
			System.out.println("Connection Closed");
			logger.log(LogStatus.INFO, "Connection Closed");
		}
		catch(Exception e) {
			System.out.println("Error occurred while attempting to close the connection. **>> Method: closeDatabase");
			logger.log(LogStatus.INFO, "Error occurred while attempting to close the connection **>> Method: closeDatabase");
		}
	}

	/**
	 * <p>
	 * <b> This method returns the record that is resulted upon executing the given SQL..</b>
	 * @author TAGG
	 * @Description - This method returns the record that is resulted upon executing the given SQL..
	 * @param Input: Query
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
	public synchronized static LinkedHashMap<String, String> getDBRow(String query) throws Exception {
		LinkedHashMap<String, String> dbResult = new LinkedHashMap<String, String>();
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		try {
			String queryHeader ="";
			stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(queryHeader + query);
			rsmd = rs.getMetaData();
			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					if (rs.getString(i) != null)
						dbResult.put(rsmd.getColumnName(i), rs.getString(i));
					else
						dbResult.put(rsmd.getColumnName(i), "");
				}
				break;
			}
			logger.log(LogStatus.ERROR, "*******************************************************************************");
			logger.log(LogStatus.ERROR, "Query: "+query);
			logger.log(LogStatus.ERROR, "Resulted records from DB: "+dbResult);
			logger.log(LogStatus.ERROR, "*******************************************************************************");
		}
		catch(Exception e) {
			logger.log(LogStatus.ERROR, "Error while retrieving the data from DB. **>> Method: getDBRow");
			System.out.println("Error while retrieving the data from DB. **>> Method: getDBRow");
		}
		finally {
			rs.close();
			stmt.close();
		}
		return dbResult;
	}

	/**
	 * <p>
	 * <b> This method returns multiple records that are resulted upon executing the given SQL..</b>
	 * @author TAGG
	 * @Description - This method returns multiple records that are resulted upon executing the given SQL..
	 * @param Input: Query
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
	public synchronized static ArrayList<LinkedHashMap<String, String>> getDBRows(String query) throws Exception {
		ArrayList<LinkedHashMap<String, String>> dbResult = new ArrayList<LinkedHashMap<String, String>>();
		Statement stmt=null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		String queryHeader = "";
		try {
			stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(queryHeader + query);
			rsmd = rs.getMetaData();
			int record = 1;
			while (rs.next()) {
				LinkedHashMap<String, String> tmpMap = new LinkedHashMap<String, String>();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					if (rs.getString(i) != null)
						tmpMap.put(rsmd.getColumnName(i), rs.getString(i));
					else
						tmpMap.put(rsmd.getColumnName(i), "");
				}
				if (tmpMap.size() > 0)
					dbResult.add(tmpMap);
				record = record + 1;
			}
			logger.log(LogStatus.ERROR, "*******************************************************************************");
			logger.log(LogStatus.ERROR, "Query: "+query);
			logger.log(LogStatus.ERROR, "Resulted records from DB: "+dbResult);
			logger.log(LogStatus.ERROR, "*******************************************************************************");
		}
		catch(Exception e) {
			logger.log(LogStatus.ERROR, "Error while retrieving the data from DB. **>> Method: getDBRow");
			System.out.println("Error while retrieving the data from DB. **>> Method: getDBRows");
		}
		finally {
			rs.close();
			stmt.close();
		}

		return dbResult;
	}
	
}
