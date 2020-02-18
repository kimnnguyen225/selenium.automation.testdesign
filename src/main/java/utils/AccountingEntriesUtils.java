package utils;

import java.util.ArrayList;

import utils.AccountingEntries;

public class AccountingEntriesUtils {
	
	/***
	 * Get the row from the supplied list that potentially matches the specified rider
	 * parameters.
	 * 
	 * @return boolean indicating whether there's a matching row
	 */
	public int getMatchingRow(String accountDesc, String creditAmt, ArrayList<AccountingEntries> listToSearch) {
		int matchingRow = -1;					// Initialized to -1 to indicate row not found
		int possibleMatchingRow = -1;			// Initialized to -1 to indicate row not found
		int rowsToCompare = listToSearch.size();
		int numberOfMatchingRowsFound = 0;
		int row;
		
		// First, find the number of matching rows; set the matching row, which will be used
		// if there's only one matching row
		for (row = 0; row < rowsToCompare; row++) {
			if (listToSearch.get(row).getAccountDesc().equals(accountDesc)) {
				numberOfMatchingRowsFound++;
				possibleMatchingRow = row;
			}			
		}

		// If only one matching row, use the possible matching row found above
		if (numberOfMatchingRowsFound == 1) {
			matchingRow = possibleMatchingRow;
		} else {
			// If the number of matching rows is more than one, look for additional parameters to
			// get the matching row
			for (row = 0; row < rowsToCompare; row++) {
				if (listToSearch.get(row).getAccountDesc().equals(accountDesc) && listToSearch.get(row).getCreditAmt().equals(creditAmt)) {
					matchingRow = row;
				}
			}		
		}

		return matchingRow;
	}

}
