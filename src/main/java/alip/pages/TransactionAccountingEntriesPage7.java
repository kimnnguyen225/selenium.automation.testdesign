package alip.pages;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utils.AccountingEntries;
import utils.BasePage1;

import com.relevantcodes.extentreports.LogStatus;
import reports.Reports;

/**
 * This page is used to validate accounting entries for each transaction that gets generated for current cycle for a single policy
 * @author Kim Nguyen
 */
public class TransactionAccountingEntriesPage7 extends BasePage1 {
	
//	//accounting entries from screen
//	ArrayList<AccountingEntries> accountingEntriesArr = new ArrayList<AccountingEntries>();
//	boolean acctgEntryTablePresent = false;
//	By accountingEntriesTBody = By.xpath("//*[@id='LedgerTable']/tbody");
//	List<WebElement> totalRows;
//	WebElement accountingEntriestBody = driver.findElement(accountingEntriesTBody);
//	WebElement spanTag;
//	String spanTagStr;
//	
//	/***
//	 * Determine if accounting entries table is available.
//	 * 
//	 * @return boolean indicating whether there's an accounting entries table present
//	 */
//	public boolean isAccountingEntryTableAvailable() {
//	acctgEntryTablePresent = isElementPresent(accountingEntriesTBody);
//		return acctgEntryTablePresent;
//	}
//
//	/***
//	 * Determine whether accounting entries are shown on the screen or not.
//	 * 
//	 * @return true - if accounting entries are shown
//	 */
//	public boolean AccountingEntriesAreShownOnScreen() {
//		boolean acctgEntriesAreShown = true;			// Assume accounting entries are shown
//		
//		try {
//			driver.findElement(accountingEntriesTBody);
//		} catch (Exception e) {
//			acctgEntriesAreShown = false;
//		}
//		return acctgEntriesAreShown;
//	}
//	
//	/**
//	 * Get accounting entries from screen
//	 * 
//	 * @return
//	 */
//	public ArrayList<AccountingEntries> getAccountingEntriesFromScreen() {
//		if (!AccountingEntriesAreShownOnScreen()) {
//			System.out.println("No accounting entries to validate");
//			Reports.logAMessage(LogStatus.ERROR, "There isn't any accounting entry on screen to validate");
//		} else {
//			totalRows = accountingEntriestBody.findElements(By.tagName("tr"));
//			spanTag = driver.findElement(By.tagName("span"));
//			
//			// dynamic span tag injection
//			 if(accountingEntriestBody.findElements(By.tagName("td")).contains(spanTag)) {
//			         spanTagStr = "/span";
//			    } else {
//			    	spanTagStr = "";
//			    }
//			
//			AccountingEntries accountingEntries;
//			
//			for (int row = 1; row < totalRows.size(); row++) {
//				accountingEntries = new AccountingEntries();
//				
//				accountingEntries.setAccountDesc(driver.findElement(By.xpath("//*[@id='LedgerTable']/tbody/tr["+row+"]/td[1]/span")).getText().trim());
//				accountingEntries.setAccountType(driver.findElement(By.xpath("//*[@id='LedgerTable']/tbody/tr["+row+"]/td[2]/span")).getText().trim());
//				accountingEntries.setAccountNum(driver.findElement(By.xpath("//*[@id='LedgerTable']/tbody/tr["+row+"]/td[3]/span")).getText().trim());
//				accountingEntries.setDebitAmt(driver.findElement(By.xpath("//*[@id='LedgerTable']/tbody/tr["+row+"]/td[4]"+spanTagStr+"")).getText().trim());
//				accountingEntries.setCreditAmt(driver.findElement(By.xpath("//*[@id='LedgerTable']/tbody/tr["+row+"]/td[5]"+spanTagStr+"")).getText().trim());
//				accountingEntriesArr.add(accountingEntries);
//			}
//		}
//		return accountingEntriesArr;
//	}

	/**
	 * Compare accounting entries shown on screen against what're shown in the database
	 * 
	 * @return true - if errors exist
	 */
//	public boolean validateAccountingEntries(ArrayList<AccountingEntries> accountingEntriesFromDB) {
//		boolean errorsExist = false;
//		
//		ArrayList<AccountingEntries> accountingEntriesFromScreen = getAccountingEntriesFromScreen();
//		
//		// Verify that the number of rows from the DB matches the number of rows from the screen
//		try {
//			assertEquals(accountingEntriesFromDB.size(), accountingEntriesFromScreen.size());
//			System.out.println("number of rows from the database = " + 
//					accountingEntriesFromDB.size() + " and number of rows from the screen = " + 
//					accountingEntriesFromScreen.size());
//			Reports.logAMessage(LogStatus.INFO, "number of rows from the database = " + 
//					accountingEntriesFromDB.size() + " and number of rows from the screen = " + 
//					accountingEntriesFromScreen.size());
//		} catch (Error e) {
//			errorsExist = true;
//			Reports.logAMessage(LogStatus.ERROR, "***** MISMATCH in number of rows for accounting entries\r\n number of rows from the database = " 
//			+ accountingEntriesFromDB.size() + " & number of rows from the screen = " + accountingEntriesFromScreen.size());
//			System.out.println("***** MISMATCH in number of rows for accounting entries\r\n number of rows from the database = " + 
//								accountingEntriesFromDB.size() + " & number of rows from the screen = " + 
//								accountingEntriesFromScreen.size());
//		}
//		
//		// Check accounting entries on the screen against what's in the database
//		for (int dbRow = 1, screenRow = 1; dbRow < accountingEntriesFromDB.size() && screenRow < accountingEntriesFromScreen.size(); dbRow++, screenRow++) {
//
//			if (screenRow == -1 || dbRow == -1) { // -1 means row not found
//				errorsExist = true;
//				System.out.println("No accounting entries for this transaction to validate");
//				Reports.logAMessage(LogStatus.ERROR, "No accounting entries for this transaction to validate");
//			} else {
//				AccountingEntries aERowfromDB = accountingEntriesFromDB.get(dbRow);
//				AccountingEntries aERowfromScreen = accountingEntriesFromScreen.get(screenRow);
//				//===============================ACCOUNT DESCRIPTION===============================//
//				try {
//					assertEquals(aERowfromDB.getAccountDesc().trim(), aERowfromScreen.getAccountDesc().trim());
//					System.out.println("*****MATCHED Account Desc: "
//							+ "\r\n Account desc from DB: " + aERowfromDB.getAccountDesc()
//							+ "\r\n Account desc on Screen: " + aERowfromScreen.getAccountDesc());
//					Reports.logAMessage(LogStatus.INFO, "*****MATCHED Account Desc: "
//							+ "\r\n Account desc from DB: " + aERowfromDB.getAccountDesc()
//							+ "\r\n Account desc on Screen: " + aERowfromScreen.getAccountDesc());
//				} catch (Error e) {
//					errorsExist = true;
//					System.out.println("*****MISMATCH with Account Desc: "
//							+ "\r\n Expected account desc from DB: " + aERowfromDB.getAccountDesc()
//							+ "\r\n Actual account desc on Screen: " + aERowfromScreen.getAccountDesc());
//					Reports.logAMessage(LogStatus.ERROR, "*****MISMATCH-1***** in accounting entries: "
//							+ aERowfromScreen.getAccountDesc() 
//							+ "\r\n Expected: " + aERowfromDB.getAccountDesc()
//							+ "\r\n Actual: " + aERowfromScreen.getAccountDesc());
//				}
//				//===============================ACCOUNT TYPE: G====================================//
//				try {
//					assertEquals(aERowfromDB.getAccountType().trim(), aERowfromScreen.getAccountType().trim());
//					System.out.println("*****MATCHED Account Type: "
//							+ "\r\n Account type from DB: " + aERowfromDB.getAccountType()
//							+ "\r\n Account type on Screen: " + aERowfromScreen.getAccountType());
//					Reports.logAMessage(LogStatus.INFO, "*****MATCHED Account Type: "
//							+ "\r\n Account type from DB: " + aERowfromDB.getAccountType()
//							+ "\r\n Account type on Screen: " + aERowfromScreen.getAccountType());
//				} catch (Error e) {
//					errorsExist = true;
//					System.out.println("*****MISMATCH with Account Type: "
//							+ "\r\n Expected account type from DB: " + aERowfromDB.getAccountType()
//							+ "\r\n Actual account type on Screen: " + aERowfromScreen.getAccountType());
//					Reports.logAMessage(LogStatus.ERROR, "*****MISMATCH***** in accounting entries: "
//							+ aERowfromScreen.getAccountDesc() 
//							+ "\r\n Expected: " + aERowfromDB.getAccountType()
//							+ "\r\n Actual: " + aERowfromScreen.getAccountType());
//				}
//				//========================ACCOUNT NUMBER: COMPOSITE ACCOUNT CODES=====================//
//				try {
//					assertEquals(aERowfromDB.getAccountNum().trim(), aERowfromScreen.getAccountNum().trim());
//					System.out.println("*****MATCHED Account Number: "
//							+ "\r\n Account number from DB: " + aERowfromDB.getAccountNum()
//							+ "\r\n Account number on Screen: " + aERowfromScreen.getAccountNum());
//					Reports.logAMessage(LogStatus.INFO, "*****MATCHED Account Number: "
//							+ "\r\n Account number from DB: " + aERowfromDB.getAccountNum()
//							+ "\r\n Account number on Screen: " + aERowfromScreen.getAccountNum());
//				} catch (Error e) {
//					errorsExist = true;
//					System.out.println("*****MISMATCH with Account Number: "
//							+ "\r\n Expected account number from DB: " + aERowfromDB.getAccountNum()
//							+ "\r\n Actual account number on Screen: " + aERowfromScreen.getAccountNum());
//					Reports.logAMessage(LogStatus.ERROR, "*****MISMATCH***** in accounting entries: "
//							+ aERowfromScreen.getAccountDesc() 
//							+ "\r\n Expected: " + aERowfromDB.getAccountNum()
//							+ "\r\n Actual: " + aERowfromScreen.getAccountNum());
//				}
//				//===============================DEBIT AMOUNT====================================//
//				try {
//						String debitAmtFromDB = aERowfromDB.getDebitAmt().trim();
//					if (debitAmtFromDB.equals("$0.00")) {
//						debitAmtFromDB = "";
//					}
//					assertEquals(debitAmtFromDB, aERowfromScreen.getDebitAmt().trim());
//					System.out.println("*****MATCHED Debit Amount: "
//							+ "\r\n Debit amount from DB: " + aERowfromDB.getDebitAmt()
//							+ "\r\n Debit amount on Screen: " + aERowfromScreen.getDebitAmt());
//					Reports.logAMessage(LogStatus.INFO, "*****MATCHED Debit Amount: "
//							+ "\r\n Debit amount from DB: " + aERowfromDB.getDebitAmt()
//							+ "\r\n Debit amount on Screen: " + aERowfromScreen.getDebitAmt());
//				} catch (Error e) {
//					errorsExist = true;
//					System.out.println("*****MISMATCH with Debit Amount: "
//							+ "\r\n Expected Debit amount from DB: " + aERowfromDB.getDebitAmt()
//							+ "\r\n Actual Debit amount on Screen: " + aERowfromScreen.getDebitAmt());
//					Reports.logAMessage(LogStatus.ERROR, "*****MISMATCH***** in accounting entries: "
//							+ aERowfromScreen.getAccountDesc() 
//							+ "\r\n Expected: " + aERowfromDB.getDebitAmt()
//							+ "\r\n Actual: " + aERowfromScreen.getDebitAmt());
//				}
//				//===============================CREDIT AMOUNT====================================//
//				try {
//					String creditAmtFromDB = aERowfromDB.getCreditAmt().trim();
//					if (creditAmtFromDB.equals("$0.00")) {
//						creditAmtFromDB = ""; //change $0.00 to empty string to match with what on screen if any
//					} 
//					assertEquals(creditAmtFromDB, aERowfromScreen.getCreditAmt().trim());
//					System.out.println("*****MATCHED credit amount: "
//							+ "\r\n Credit amount from DB: " + aERowfromDB.getCreditAmt()
//							+ "\r\n Credit amount on Screen: " + aERowfromScreen.getCreditAmt());
//					Reports.logAMessage(LogStatus.INFO, "*****MATCHED credit amount: "
//							+ "\r\n Credit amount from DB: " + aERowfromDB.getCreditAmt()
//							+ "\r\n Credit amount on Screen: " + aERowfromScreen.getCreditAmt());
//				} catch (Error e) {
//					errorsExist = true;
//					System.out.println("*****MISMATCH with credit amount: "
//							+ "\r\n Expected credit amount from DB: " + aERowfromDB.getCreditAmt()
//							+ "\r\n Actual credit amount on Screen: " + aERowfromScreen.getCreditAmt());
//					Reports.logAMessage(LogStatus.ERROR, "*****MISMATCH***** in accounting entries: "
//							+ aERowfromScreen.getAccountDesc() 
//							+ "\r\n Expected: " + aERowfromDB.getCreditAmt()
//							+ "\r\n Actual: " + aERowfromScreen.getCreditAmt());
//				}
//			}
//		} return errorsExist;
//	}
}
