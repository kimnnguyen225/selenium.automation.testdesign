package utils;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import utils.AccountingEntries;
import utils.DatabaseConnection;

import com.relevantcodes.extentreports.LogStatus;
import reports.Reports;

public class TestData {
	static DatabaseConnection conn = new DatabaseConnection();
	static Statement stmt = null;
	static ResultSet rs = null;
	
	static String currentCycleDate;
	static String cycleDateALIPScreen;
	static String currentProcDate;
	static ArrayList<String> policiesList;
	static String policy;
	static Object[] contractInformationDetailsBoxFromDB;
	static ArrayList<Integer> transNumberList;
	static Integer transNumber = null;
	ArrayList<AccountingEntries> acctgEntriesOfOneTrans;
	static Integer totalAccountingRows;
	
	// ================ ALIP WEBSITE'S TRANSACTIONS' GENERAL LEDGER/ACCOUNING ENTRIES VS STAGING DATABASE VALIDATION ==================//
		
	/**
	 * get current cycle date for current release
	 * @return - currentCycleDate - String
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public String getCurrentCycleDate() throws Exception {
		 String queryCurrentCycleDate = 
					"SELECT TOP 1 PO_CYCLE_DATE\r\n" + 
					"FROM [" + BaseTest.getWebsite() + "].STAGING.T_STPO_POLICY PO\r\n" + 
					"LEFT JOIN [" + BaseTest.getWebsite() + "].STAGING.T_STTR_TRANSACTION TR " +
					"ON PO.PO_POL_NUM = TR.TR_POL_NUM\r\n" + 
					"WHERE PO.CURRENT_FLAG = 1\r\n" + 
					"AND TR.CURRENT_FLAG = 1\r\n" + 
					"ORDER BY PO.PO_CYCLE_DATE DESC;";
		 
		 conn.createSQLServerConn();
		 try {
			 currentCycleDate=conn.fetchCurrentCycleDateFromDB(queryCurrentCycleDate);
		 } catch(Exception e) {
			Reports.logAMessage(LogStatus.ERROR, "Exception: " + e.getMessage());
			// System.out.println(e.getMessage());
			e.printStackTrace();
		 }
		 return currentCycleDate;
	}
	
	/**
	 * get current cycle date in format mm/dd/yyyy to compare with transaction processed date on ALIP screen
	 * @return - currentCycleDate - String
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public String getCycleDateForALIPScreen() throws Exception {
		 String queryCycleDate = 
					"SELECT TOP 1 FORMAT(CAST(PO_CYCLE_DATE as date), 'MM/dd/yyyy')\r\n" + 
					"FROM [" + BaseTest.getWebsite() + "].STAGING.T_STPO_POLICY PO\r\n" + 
					"LEFT JOIN [" + BaseTest.getWebsite() + "].STAGING.T_STTR_TRANSACTION TR " +
					"ON PO.PO_POL_NUM = TR.TR_POL_NUM\r\n" + 
					"WHERE PO.CURRENT_FLAG = 1\r\n" + 
					"AND TR.CURRENT_FLAG = 1\r\n" + 
					"ORDER BY PO.PO_CYCLE_DATE DESC;";
		 
		 conn.createSQLServerConn();
		 try {
			 cycleDateALIPScreen=conn.fetchCurrentCycleDateFromDB(queryCycleDate);
		 } catch(Exception e) {
			Reports.logAMessage(LogStatus.ERROR, "Exception: " + e.getMessage());
			// System.out.println(e.getMessage());
			e.printStackTrace();
		 }
		 return cycleDateALIPScreen;
	}
	
	/***
	 * get current processed date
	 * @return currentProcDate - String
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public String getCurrentProcDate() throws Exception {
		
		String queryCurrentProcDate = 
				"SELECT TOP 1 DI_PROC_DATE\r\n" + 
				"FROM [" + BaseTest.getWebsite() + "].STAGING.T_STDI_DISBURSEMENTS\r\n" + 
				"ORDER BY DI_PROC_DATE DESC;";
		
		conn.createSQLServerConn();
		try {
			currentProcDate=conn.fetchCurrentProcessDateFromDB(queryCurrentProcDate);
		} catch(Exception e) {
			Reports.logAMessage(LogStatus.ERROR, "Exception: " + e.getMessage());
			//System.out.println("Exception error: " + e.getMessage());
			e.printStackTrace();
		}
		return currentProcDate;
	}
	
	/***
	 * get all policies from current cycle for current release from database (SQL Server)
	 * @return - policiesList - ArrayList<String> of policies from Staging
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public ArrayList<String> getAllPoliciesFromDB () throws Exception {
		String queryGetAllPoliciesFromCurrentCycle = 
				"SELECT DISTINCT PO_CONT\r\n" + 
				"FROM [" + BaseTest.getWebsite() + "].STAGING.T_STPO_POLICY PO\r\n" + 
				"LEFT JOIN [" + BaseTest.getWebsite() + "].STAGING.T_STTR_TRANSACTION TR ON PO.PO_POL_NUM = TR.TR_POL_NUM\r\n" + 
				"WHERE PO.CURRENT_FLAG = 1\r\n" + 
				"AND TR.CURRENT_FLAG = 1\r\n" + 
				"AND PO.PO_CYCLE_DATE = '"+currentCycleDate+"';";
		
		conn.createSQLServerConn();
		try {
			policiesList=conn.fetchPoliciesFromDB (queryGetAllPoliciesFromCurrentCycle);
			Reports.logAMessage(LogStatus.PASS, "Successfully retrieved " + policiesList.size() + " policies to test");
			Reports.logAMessage(LogStatus.INFO, "Policies: \r\n" + policiesList);
			System.out.println("Successfully retrieved " + policiesList.size() + " policies to test \r\n"
					+ "Policies: \r\n" + policiesList);
		} catch (NullPointerException e) {
			Reports.logAMessage(LogStatus.ERROR, "Exception: Null pointer");
		} catch(Exception e) {
			System.err.println("Execption: " + e.getMessage());
			Reports.logAMessage(LogStatus.ERROR, "Exception: " + e.getMessage());
			e.printStackTrace();
		}
		
		if (policiesList.size() < 1) {
			System.out.println("FAIL: There are no policies in the database to test against");
			Reports.logAMessage(LogStatus.FAIL, "There are no policies in the database to test against");
		} else {
			if (policiesList.size() > 1) {
				// do nothing
			}
		}
		return policiesList;
	}
	
	/**
	 * get contract information details from DB (contract number and product name)
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public Object[] getContractInformationBoxDetailsFromDB(String policy) throws Exception {
		String queryContractDetails = 
				"SELECT PO.PO_CONT, PO.PO_PLN_CODE\r\n" +
				"FROM STAGING.T_STPO_POLICY PO\r\n" + 
				"WHERE PO.PO_CONT = '" + policy + "'\r\n" + 
				"AND PO.CURRENT_FLAG = 1;";
		
		conn.createSQLServerConn();
		try {
				contractInformationDetailsBoxFromDB = conn.fetchContractInformationDetailsBoxFromDB(queryContractDetails);
//				Reports.logAMessage(LogStatus.INFO, "Contract Info from DB: " + contractInformationDetailsBoxFromDB);			// dbg This isn't sending data that makes sense to the person reading the report; needs to be fixed or removed 
			} catch (Exception e) {
				System.out.println("Exception: " + e.getMessage());
				Reports.logAMessage(LogStatus.ERROR, "Exception: " + e.getMessage());
				e.printStackTrace();
		}
		return contractInformationDetailsBoxFromDB;
	}
	
	/***
	 * get all transaction numbers for a single policy from Staging
	 * @return transNumberList - ArrayList of Integer of a single policy's trasaction numbers
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public ArrayList<Integer> getTransNumbersOfEachPolicyFromDB (String policy) {
		String queryTransIdListForEachPol =
				"SELECT DISTINCT TR.TR_TXN_ID,\r\n" +
				"TR.TR_AMT,\r\n" +
				"TR_TXN_DATE\r\n" + //transaction date (DB) - known as Requested Date on ALIP website
				"FROM [" + BaseTest.getWebsite() + "].STAGING.T_STGL_GENERAL_LEDGER GL\r\n" + 
				"LEFT JOIN [" + BaseTest.getWebsite() + "].STAGING.T_STTR_TRANSACTION TR ON TR.TR_GL_NUM = GL.GL_GL_NUM \r\n" + 
				"LEFT JOIN [" + BaseTest.getWebsite() + "].STAGING.T_STPO_POLICY PO ON PO.PO_POL_NUM = TR.TR_POL_NUM\r\n" + 
				"WHERE PO.PO_CONT = '"+ policy + "'\r\n" +
				"AND PO.PO_CYCLE_DATE = '"+ currentCycleDate + "'\r\n" + 
				"AND GL.GL_PRCS_DATE = '"+ currentProcDate + "'\r\n" +
				"AND GL_REV_LEDGER_ACC_CODE IS NOT NULL\r\n" + 
				"AND GL_REV_GL_COMPOSIT_ACC_CODE IS NOT NULL\r\n" +
				"AND TR.TR_TXN_TYPE_DESC NOT IN ('Calendar Processing Date', 'In Force Rider Change','Calendar Anniversary', 'Dollar Cost Average', 'Rider Issue', 'Rate Renewal', 'Rider Termination')\r\n" +
				"ORDER BY TR.TR_TXN_DATE DESC,\r\n" +
				"TR.TR_AMT,\r\n" +
				"TR.TR_TXN_ID DESC;";
		
		conn.createSQLServerConn();
		try {
			// Get a policy's all of transaction numbers
			transNumberList=conn.fetchTransNumbersFromDB(queryTransIdListForEachPol);  //only stores 1st column of trans number from ResultSet
		} catch(Exception e) {
			// System.err.println("Exception: " + e.getMessage());
			Reports.logAMessage(LogStatus.ERROR, "Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return transNumberList;
	}
	
	/***
	 * get Accounting Entries for a single transaction
	 * @return acctgEntriesOfOneTrans - ArrayList<AccountingEntries>
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public ArrayList<AccountingEntries> getAcctgEntriesForOneTransFromDB(String policy, Integer transNumber) throws Exception {
		
		String queryTXNAcctgEntries =
				"SELECT \r\n" + 
				"GL.GL_ACC_DESC AS 'ACCOUNT DESC',\r\n" + 
				"GL.GL_ACC_TYPE,\r\n" + 
				"GL.GL_GL_COMPOSIT_ACC_CODE AS 'GL COMPOSIT ACC CODE',\r\n" + 
				"FORMAT(SUM(CASE GL.GL_DBT_CDT_FLG WHEN 'D' THEN GL.GL_LEDGER_AMT ELSE 0 END), 'C', 'en-us') AS 'GL DEBIT',\r\n" + 
				"FORMAT(SUM(CASE GL.GL_DBT_CDT_FLG WHEN 'C' THEN GL.GL_LEDGER_AMT ELSE 0 END), 'C', 'en-us') AS 'GL CREDIT'\r\n" + 
				"FROM STAGING.T_STGL_GENERAL_LEDGER GL\r\n" + 
				"LEFT JOIN [" + BaseTest.getWebsite() + "].STAGING.T_STTR_TRANSACTION TR ON TR.TR_GL_NUM = GL.GL_GL_NUM \r\n" + 
				"LEFT JOIN [" + BaseTest.getWebsite() + "].STAGING.T_STPO_POLICY PO ON PO.PO_POL_NUM = TR.TR_POL_NUM\r\n" + 
				"WHERE GL.CURRENT_FLAG = 1 AND PO.CURRENT_FLAG = 1 AND TR.CURRENT_FLAG = 1\r\n" + 
				"AND PO.PO_CONT = '"+ policy +"'\r\n" + 
				"AND TR.TR_TXN_ID = '" + transNumber + "'\r\n" + // ex: 39999
				"AND PO.PO_CYCLE_DATE = '"+currentCycleDate+"'\r\n" + //ex: 2021-12-31 20:00:00.000
				"GROUP BY\r\n" + 
				"GROUPING SETS((),(\r\n" +
				"GL.GL_GL_COMPOSIT_ACC_CODE, \r\n" + 
				"GL.GL_ACC_TYPE,\r\n" + 
				"GL.GL_ACC_DESC,\r\n" + 
				"GL.GL_LEDGER_AMT,\r\n" + 
				"GL.GL_DBT_CDT_FLG))\r\n" + 
				"ORDER BY \r\n" + 
				"IIF(GL.GL_ACC_DESC IS NULL, 1, 0),\r\n" + 
				"GL.GL_ACC_DESC ASC, \r\n" + 
				"IIF(GL.GL_DBT_CDT_FLG IS NULL, 1, 0),\r\n" + 
				"GL.GL_DBT_CDT_FLG DESC,\r\n" +
				"GL.GL_LEDGER_AMT DESC;";

		// loop through TXNId ArrayList for current policy, then clear all elements before assign a different 
		// policy's new transaction ID (new elements)
		
		conn.createSQLServerConn();
		
		try {
			// Get a policy's transaction's general ledger (accounting entries) from the DB
			acctgEntriesOfOneTrans=conn.fetchAcctgEntriesOfOneTransFromDB(queryTXNAcctgEntries);
		} catch(Exception e) {
			System.err.println("Exception: " + e.getMessage());
			//Reports.logAMessage(LogStatus.ERROR, "Execption: " + e.getMessage());
			e.printStackTrace();
		}
		return acctgEntriesOfOneTrans;
	}
	
	//==================================================BI REPORT VS STAGING DATABASE VALIDATION==============================//
	
	/**
	 * get total transaction accounting and suspense accounting rows from DB
	 */
	@SuppressWarnings("static-access")
	public Integer getTotalAccountingRowsFromDB () {
		String queryGetTotalAccountingRows =
				"SELECT\r\n" + 
				"(SELECT COUNT(*) FROM STAGING.T_STGL_GENERAL_LEDGER GL \r\n" + 
				"WHERE GL.CURRENT_FLAG = 1 AND \r\n" + 
				"GL.GL_CYCLE_DATE = '"+ currentCycleDate +"'\r\n" + //ex: 2021-12-31 20:00:00.000
				"GL.GL_PRCS_DATE = '"+ currentProcDate + "' AND \r\n" +  //ex: 2021-12-31 00:00:00.000
				"GL.GL_GL_SRC = 'T') AS 'TRANSACTION ACCOUNTING ROWS',\r\n" + 
				"'+' AS ' ',\r\n" + 
				"\r\n" + 
				"(SELECT COUNT(*) FROM STAGING.T_STGL_GENERAL_LEDGER GL \r\n" + 
				"WHERE GL.CURRENT_FLAG = 1 \r\n" + 
				"AND GL.GL_CYCLE_DATE = '"+ currentCycleDate +"' \r\n" + 
				"AND GL.GL_GL_SRC = 'S') AS 'SUSPENSE ACCOUNTING ROWS',\r\n" + 
				"'=' AS ' ',\r\n" + 
				"\r\n" + 
				"(SELECT COUNT(*) FROM STAGING.T_STGL_GENERAL_LEDGER GL \r\n" + 
				"WHERE GL.CURRENT_FLAG = 1 \r\n" + 
				"GL.GL_CYCLE_DATE = '"+ currentCycleDate +"'\r\n" + //ex: 2021-12-31 20:00:00.000
				"GL.GL_PRCS_DATE = '"+ currentProcDate + "' AND \r\n" +  //ex: 2021-12-31 00:00:00.000
				"AND GL.GL_GL_SRC = 'T')\r\n" + 
				"+\r\n" + 
				"(SELECT COUNT(*) FROM STAGING.T_STGL_GENERAL_LEDGER GL \r\n" + 
				"WHERE GL.CURRENT_FLAG = 1 \r\n" + 
				"AND GL.GL_CYCLE_DATE = '"+ currentCycleDate +"' \r\n" + 
				"AND GL.GL_GL_SRC = 'S')\r\n" + 
				"AS 'TOTAL ACCOUNTING ROWS';\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"SELECT * FROM STAGING.T_STGL_GENERAL_LEDGER GL \r\n" + 
				"WHERE GL.CURRENT_FLAG = 1 AND \r\n" + 
				"GL.GL_CYCLE_DATE = '"+ currentCycleDate +"'\r\n" + //ex: 2021-12-31 20:00:00.000
				"GL.GL_PRCS_DATE = '"+ currentProcDate + "' AND \r\n" +  //ex: 2021-12-31 00:00:00.000 
				"GL.GL_GL_SRC = 'T'\r\n" + 
				"ORDER BY GL.GL_CYCLE_DATE DESC\r\n" + 
				"---------------------------------------------------\r\n" + 
				"SELECT * FROM STAGING.T_STGL_GENERAL_LEDGER GL \r\n" + 
				"WHERE GL.CURRENT_FLAG = 1 AND \r\n" + 
				"AND GL.GL_CYCLE_DATE = '"+ currentCycleDate +"' \r\n" + 
				"GL.GL_GL_SRC = 'S'\r\n" + 
				"ORDER BY GL.GL_CYCLE_DATE DESC\r\n" + 
				"";
		
		conn.createSQLServerConn();
		try {
			totalAccountingRows=conn.fetchTotalAccountingRowsFromDB(queryGetTotalAccountingRows);  //only stores 1st column of trans number from ResultSet
		} catch(Exception e) {
			Reports.logAMessage(LogStatus.ERROR, "Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return totalAccountingRows;
	}
}
