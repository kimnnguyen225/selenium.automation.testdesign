package test;

import org.junit.Test;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import com.relevantcodes.extentreports.LogStatus;
import reports.Reports;

import bireport.pages.BiHomePage3;
import bireport.pages.BiLoginPage2;

import utils.BaseTest;
import utils.TestData;
import utils.WebDriverUtils;

public class VerifyBiReport extends BaseTest {
	// credential for T4 region
	 String system = "QA-BOESVR.CORP.ONFS.COM:7400";
	 String username = "testqa";
	 String password = "Testqa123";
	 
	 //date
	 String currentCycleDate;
	 String currentProcDate;
	 
	 @Test
	 public void VerifyBiReport() throws Exception {

		 TestData testData = new TestData(); //queries to get data from DB
//		 currentCycleDate = testData.getCurrentCycleDate(); // 8 pm
//		 currentProcDate = testData.getCurrentProcDate(); // 12 am
		 Reports.logAMessage(LogStatus.INFO, "Database connected successfully");
		 System.out.println("Database connected successfully");
		 
		//Login
		System.out.println("*** Log into BI Report website ***");
		Reports.logAMessage(LogStatus.INFO, "*** Log into BI Report website ***");
		BiLoginPage2 biLoginPage = new BiLoginPage2();
		BiHomePage3 biHomePage = biLoginPage.procedureLogin(system, username, password);
		System.out.println("*** Logged-in Successfully ***");
		Reports.logAMessage(LogStatus.PASS, "*** Logged-in Successfully ***");
		biHomePage.downloadBIReport();
	 }
}
