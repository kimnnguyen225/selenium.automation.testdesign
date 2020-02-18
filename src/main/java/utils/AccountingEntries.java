package utils;

/***
 * This class contains accounting entries for each generated transaction for a single policy for current cycle.
 */

public class AccountingEntries {
	
	String AccountDesc;
	String AccountType;
	String AccountNum;
	String DebitAmt;
	String CreditAmt;
	
	public String getAccountDesc() {
		return AccountDesc;
	}
	public void setAccountDesc(String accountDesc) {
		AccountDesc = accountDesc;
	}
	public String getAccountType() {
		return AccountType;
	}
	public void setAccountType(String accountType) {
		AccountType = accountType;
	}
	public String getAccountNum() {
		return AccountNum;
	}
	public void setAccountNum(String accountNum) {
		AccountNum = accountNum;
	}
	public String getDebitAmt() {
		return DebitAmt;
	}
	public void setDebitAmt(String debitAmt) {
		DebitAmt = debitAmt;
	}
	public String getCreditAmt() {
		return CreditAmt;
	}
	public void setCreditAmt(String creditAmt) {
		CreditAmt = creditAmt;
	}

}
