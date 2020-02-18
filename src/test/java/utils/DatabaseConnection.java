package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.relevantcodes.extentreports.LogStatus;

import reports.Reports;

//import com.ibm.db2.jcc.am.re;

import utils.AccountingEntries;

/***
 * Database connection class for accessing the database and retrieving data.
 */

public class DatabaseConnection {
	static Statement stmt = null;
	static Connection conn = null;
	static ResultSet rs = null;
	protected static String url = null;
	protected static String userName = null;
	protected static String pWord = null;
	static String jdbcDB2ClassName="com.ibm.db2.jcc.DB2Driver";
	static String jdbcSQLClassName="com.microsoft.sqlserver.jdbc.SQLServerDriver"; 
	
	String currentCycleDate;
	String currentProcessDate;
	ArrayList<AccountingEntries> transAcctgEntryDetails = new ArrayList<AccountingEntries>(); // ArrayList of TXN Accounting Entries "Class"
	ArrayList<String> policiesList = new ArrayList<String>();
	Object[] contractInformationDetailsBoxFromDB = new Object[3];
	ArrayList<Integer> transNumberList = new ArrayList<Integer>();
	
	Integer totalAccountingRows;
	
	// DB2 - DBVisualizer Downstream Database Connection
	public static void createDB2Conn() {
        try {
        	Class.forName(jdbcDB2ClassName);
        	url = "jdbc:db2://SMFLPP01:4020/DB2DSNY;DatabaseName=Test"; //databaseserver:port:database
        	userName = "T#R53";
        	pWord = "LONG1002";
        	
			conn = DriverManager.getConnection(url, userName, pWord);
			stmt = conn.createStatement();
			
		} catch (SQLException e) {
			Reports.logAMessage(LogStatus.ERROR, "DB2 Connection exception: " + e.getMessage());
			//System.err.println("DB2 Connection exception: " + e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Reports.logAMessage(LogStatus.ERROR, "Class Not Found Exception: " + e.getMessage());
			e.printStackTrace();
	    }
    }
    
	// SQL Server Staging Database Connection
	public static void createSQLServerConn() {
		url = "jdbc:sqlserver://SPN1ALPSQLT0101:1433;DatabaseName=" + BaseTest.getWebsite();
		userName = "DEVR53SQL";
		pWord = "R53Pass19!";
		 
		try {
			// Load the SQL Server driver using the current class loader
			Class.forName(jdbcSQLClassName);
			
			// Create a DB connection
			conn = DriverManager.getConnection(url, userName, pWord);
			stmt = conn.createStatement();
		} catch(SQLException e) {
			//System.err.println("SQL Server Connection exception: " + e.getMessage());
			Reports.logAMessage(LogStatus.ERROR, "SQL Server Connection exception: " + e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			//System.err.println("Class Not Found Exception: " + e.getMessage());
			Reports.logAMessage(LogStatus.ERROR, "Class Not Found Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Close database connection
	 */
	public static void closeConnection() {
    	try {
			conn.close();
		} catch (SQLException e) {
			Reports.logAMessage(LogStatus.ERROR, "SQL Exception - CloseConnection: " + e.getMessage());
			e.printStackTrace();
		}
    }
	
	/**
	 * get current cycle date from database
	 * @param query
	 * @return current cycle date from database for current cycle of current release ex: 2021-12-31 20:00:00.000
	 * @throws Exception
	 */
	public String fetchCurrentCycleDateFromDB(String query) throws Exception {
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			currentCycleDate = rs.getString(1).trim(); //8 pm
		}
		return currentCycleDate;
	}
	
	/**
	 * get current process date from database
	 * @param query
	 * @return current process date from database for current cycle of current release ex: 2021-12-31 00:00:00.000
	 * @throws Exception
	 */
	public String fetchCurrentProcessDateFromDB(String query) throws Exception {
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			currentProcessDate = rs.getString(1).trim(); //12 am
		}
		return currentProcessDate;
	}
	
	/***
	 * Fetch data (policies/contracts) to be tested from the database, and add to a list of PolNum (policy numbers)
	 * 
	 * @param query - query to execute
	 * @return - list of policy numbers to be tested
	 * @throws Exception
	 */
	public ArrayList<String> fetchPoliciesFromDB(String query) throws Exception {
		rs = stmt.executeQuery(query);
		
		while(rs.next()) {
			policiesList.add(rs.getString(1).trim());
		}
		return policiesList;
	}
	
	/**
	 * Fetch contract info (policy number and product name) from DB
	 * @param query
	 * @return contract info - Object[]
	 * @throws Exception
	 */
	public Object[] fetchContractInformationDetailsBoxFromDB(String query) throws Exception {
		rs = stmt.executeQuery(query);
		
		while(rs.next()) {
			contractInformationDetailsBoxFromDB[0] = rs.getString(1).trim();
			contractInformationDetailsBoxFromDB[1] = rs.getString(2).trim();
		}
		return contractInformationDetailsBoxFromDB;
	}
	
	/***
	 * Fetch data (Transaction ID/Processed Number) to be tested from the database, and add to a list of TXNId (Transaction ID)
	 * 
	 * @param query - query to execute
	 * @return - list of policy numbers to be tested
	 * @throws Exception
	 */
	public ArrayList<Integer> fetchTransNumbersFromDB(String query) throws Exception {
		rs = stmt.executeQuery(query);
		
		while(rs.next()) {
			transNumberList.add(rs.getInt(1));
		}
		return transNumberList;
	}
    
	/***
	 * Fetch a policy's transaction's accounting entries from DB
	 * 
	 * @param query
	 * @return a single transaction's accounting entries
	 * @throws Exception
	 */
	public ArrayList<AccountingEntries> fetchAcctgEntriesOfOneTransFromDB(String query) throws Exception {
		ResultSet rs = stmt.executeQuery(query);
		   
		while (rs.next()) {
			AccountingEntries acctgEntry = new AccountingEntries(); 
			String accountDesc = rs.getString(1);
			if (rs.wasNull()) {
				accountDesc = "Total";
			}
			acctgEntry.setAccountDesc(accountDesc);
			String accountType = rs.getString(2);
			if (rs.wasNull()) {
				accountType = "";
			}
			acctgEntry.setAccountType(accountType);
			String accountNum = rs.getString(3);
			if (rs.wasNull()) {
				accountNum = "";
			}
			acctgEntry.setAccountNum(accountNum.trim());
			acctgEntry.setDebitAmt(rs.getString(4).trim());
			acctgEntry.setCreditAmt(rs.getString(5).trim());
			
			transAcctgEntryDetails.add(acctgEntry);
		}
		return transAcctgEntryDetails;
	}
	
	/***
	 * Fetch data (Transaction ID/Processed Number) to be tested from the database, and add to a list of TXNId (Transaction ID)
	 * 
	 * @param query - query to execute
	 * @return - list of policy numbers to be tested
	 * @throws Exception
	 */
	public Integer fetchTotalAccountingRowsFromDB(String query) throws Exception {
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			totalAccountingRows = rs.getInt(5); // transaction accounting rows + suspense accounting rows = total accounting rows
		}
		return totalAccountingRows;
	}
}
