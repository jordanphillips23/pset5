import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class will serve as the intermediary between our ATM program and
 * the database of BankAccounts. It'll be responsible for fetching accounts
 * when users try to login, as well as updating those accounts after any
 * changes are made.
 */

public class Database {
	public int[] recordSizes= {9, 4, 15, 20, 15, 8, 10, 30, 30, 2, 5, 1};
	private ArrayList<ArrayList<String>> database = new ArrayList<ArrayList<String>>();
	
	public void readFromFile() {
		File inputFile = new File("accounts-db.txt");
		Scanner inputScanner = null;
		try {
			inputScanner = new Scanner(inputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while(inputScanner.hasNextLine()) {
			String line = inputScanner.nextLine();
			database.add(lineToData(line));
		}
	}
	
	public ArrayList<String> lineToData(String line) {
		int index = 0;
		ArrayList<String> output= new ArrayList<String>();
		
		for (int i = 0; i < recordSizes.length; i++) {
			output.add(line.substring(index, index + recordSizes[i]));
			index+= recordSizes[i];
		}
		return output;
	}
	
	public void writeToFile() throws IOException {
		File outputFile = new File("accounts-db.txt");
		Files.deleteIfExists(outputFile.toPath());
		BufferedWriter writer = new BufferedWriter(new FileWriter("accounts-db.txt", true));
		for (int i = 0; i < database.size(); i++) {
			for (int j = 0; j < database.get(0).size(); j++) {
				writer.append(database.get(i).get(j));
			}
			writer.newLine();
		}
		writer.close();
	}
	
	public void cleanData() {
		for (int i = 0; i < database.size(); i++) {
			for (int j = 0; j < database.get(0).size(); j++) {
				int index = recordSizes[j] - 1;
				
				while (database.get(i).get(j).charAt(index) == ' ') {
					index --;
				}
				database.get(i).set(j, database.get(i).get(j).substring(0, index + 1));
				
				}
			}
	}
	
	public void unCleanData() {
		for (int i = 0; i < database.size(); i++) {
			for (int j = 0; j < database.get(0).size(); j++) {
					int spacesToAdd = recordSizes[j] - database.get(i).get(j).length();
					for (int k = 0; k < spacesToAdd; k++) {
						database.get(i).set(j, database.get(i).get(j) + " ");
					}
				}
			}
	}
	
	public void createRecord(ArrayList<String> record) {
		database.add(record);
	}
	
	public boolean deleteRecord(String accountNumber) {
		for (int i = 0; i < database.size(); i++) {
			if (database.get(i).get(0).equals(accountNumber)) {
				database.remove(i);
				return true;
			}
			
		}
		return false;
	}
	
	public boolean updateTerm(String accountNumber, int index, String term) {
		for (int i = 0; i < database.size(); i++) {
			if (database.get(i).get(0).equals(accountNumber)) {
				database.get(i).set(index, term);
				return true;
			}
		}
		return false;
	}
	
	public boolean updateRecord(String accountNumber, ArrayList<String> record) {
		for (int i = 0; i < database.size(); i++) {
			if (database.get(i).get(0).equals(accountNumber)) {
				database.set(i, record);
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<String> getRecord(String accountNumber) {
		
		for (int i = 0; i < database.size(); i++) {
			if (database.get(i).get(0).equals(accountNumber) && database.get(i).get(11).equals("Y")) {
				return database.get(i);
			}
		}
		return null;
	}
	
	public String getNewAccountNumber() {
		return "" + (Integer.parseInt(database.get(database.size() - 1).get(0)) + 1);
	}
}