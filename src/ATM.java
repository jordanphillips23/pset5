import java.io.IOException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


/**
 * Just like last time, the ATM class is responsible for managing all
 * of the user interaction. This means login procedures, displaying the
 * menu, and responding to menu selections. In the enhanced version, the
 * ATM class will have the added responsibility of interfacing with the
 * Database class to write and read information to and from the database.
 * 
 * Most of the functionality for this class should have already been
 * implemented last time. You can always reference my Github repository
 * for inspiration (https://github.com/rwilson-ucvts/java-sample-atm).
 */

public class ATM {
	private BankAccount currentAccount;
	private Scanner in = new Scanner(System.in);
	private Database data = new Database();
	private DecimalFormat format = new DecimalFormat("###,##0.00");
	
	public ATM() {
		data.readFromFile();
		data.cleanData();
	}
	
	public void firstMenu() {
		String input = "";
		
		String[] menu = {
				"Welcome to the bank ATM. Please select your choice.",
				"",
				"1: Open Account",
				"2: Login",
				"3: Quit"
				};
		
		String[] invalid = {
				"Invalid Input."
		};
		
		while (!input.equals("3")) {
			window(menu);
			
			input = in.nextLine();
			
			switch (input) {
				case "1":
					newAccountMenu();
					break;
				case "2":
					loginMenu();
					updateDatabase(currentAccount);
					break;
				case "3":
					close();
					break;
				default:
					window(invalid);
					break;
			}
		}
		
	}
	
	public void newAccountMenu() {
		ArrayList<String> record = new ArrayList<String>();
		String accountNumber = data.getNewAccountNumber();
		record.add(accountNumber);
		System.out.println("Your Account Number is: " + accountNumber);
		String[] fields = {"AccountNumber","PIN","Balance", "First Name", "Last Name", "Date of Birth", "Phone Number", "Street Address", "City", "State", "Postal Code"};
		String[] constraints = {"", "####", "", "John", "Doe", "YYYYMMDD", "##########", "11 Streetname Dr.", "Citytown", "NJ", "#####"};
		
		for (int i = 1; i < 11; i++) {
			
			if (i == 2) {
				record.add("0.00");
			}
			else {
				String[] prompt = {"Please enter your " + fields[i] + " in the form of " + constraints[i] + " :"};
				String input = "";
				do {
					
					window(prompt);
					input = in.nextLine();
				} while (!validate(i, input));
				record.add(input);
			}
			
		}
		record.add("Y");
		data.createRecord(record);
		
	}
	
	public boolean validate(int index, String input) {
		String[] regexes = {"\\d{9}", "\\d{4}", "\\d{0,12}[.]?\\d{0,2}", "[a-zA-Z]{1,20}", "[a-zA-Z]{1,15}", "\\d{8}", "\\d{10}", ".{1,30}", "[a-zA-Z ]{1,30}", "[A-Z]{2}", "\\d{5}", "[YN]"};
		return input.matches(regexes[index]);
	}
	
	public void loginMenu() {
		String[] accountNumber = {
				"Please enter your Account Number.",
				"Enter in -1 to leave"};
		
		String[] pinDisplay = {"Please Enter your PIN: "};
		
		ArrayList<String> currentRecord = null;
		
		while(currentRecord == null) {
			
			
			window(accountNumber);
			
			
			String input = in.nextLine();
			if (input.equals("-1")) {
				return;
			}
			
			
			currentRecord = data.getRecord(input);
		}
		
		String pin = "";
		
		while(!pin.equals(currentRecord.get(1))) {
			
			window(pinDisplay);
			pin = in.nextLine();
		}
		
		getBankAccountFromRecord(currentRecord);
		loggedInMenu();
		
	}
	
	public void loggedInMenu() {
		String input = "";
		User user = currentAccount.getUser();
		String[] menu = {
				"Hello " + user.getFirstName() + " " + user.getLastName() + "!",
				"",
				"1: Deposit Funds",
				"2: Withdraw Funds",
				"3: Transfer Funds",
				"4: View Balance",
				"5: View Personal Information",
				"6: Update Personal Information",
				"7: Close Account",
				"8: Logout"
		};
		
		while(!input.equals("8") && currentAccount.getOpen().equals("Y")) {
			
			window(menu);
			
			input = in.nextLine();
			
			switch(input) {
				case "1":
					deposit();
					break;
				case "2":
					withdraw();
					break;
				case "3":
					transfer();
					break;
				case "4":
					viewBalance();
					break;
				case "5":
					viewPersonalInformation();
					break;
				case "6":
					updatePersonalInformation();
					break;
				case "7":
					closeAccount();
					break;
				case "8":
					break;
				default:
					break;
					
			}
		}	
	}
	
	public void deposit() {
		String input = "";
		String depositMessage[] = {"How much would you like to deposit?"};
		do {
			window(depositMessage);
			input = in.nextLine();
		} while(!validate(2, input) || !(Double.parseDouble(input) > 0));
		
		if (Double.parseDouble(input) + currentAccount.getBalance() > 999999999999.99) {
			
			String[] message =  {
					"Amount inputted exceeds max limit of account.",
					"Balance set to $999,999,999,999.99",
					"The remaining $" + format.format(Double.parseDouble(input) + currentAccount.getBalance() - 999999999999.99) + " has been returned to you."
			};
			
			window(message);
			
		}
		currentAccount.deposit(Double.parseDouble(input));
		
		viewBalance();
	}
	
	public void withdraw() {
		if (currentAccount.getBalance() <= 0) {
			System.out.println("Invalid. Not enough Money");
		}
		else {
			String input = "";
			String[] withdrawMessage = {"How much would you like to withdraw?"};
			do {
				window(withdrawMessage);
				input = in.nextLine();
			} while(!validate(2, input) || !(Double.parseDouble(input) <= currentAccount.getBalance())|| !(Double.parseDouble(input) > 0));
			currentAccount.withdraw(Double.parseDouble(input));
			
			viewBalance();	
		}
		
	}
	
	public void transfer() {
		if (currentAccount.getBalance() <= 0) {
			System.out.println("Invalid. Not enough Money");
			return;
		}
		ArrayList<String> receivingRecord = null;
		String input = "";
		String[] message1 = {"What is the account number of the recieving account?"};
		while(receivingRecord == null) {
			window(message1);
			input = in.nextLine();
			receivingRecord = data.getRecord(input);
		}
		
		String input2 = "";
		String[] message2 = {"How much would you like to transfer?"};
		do {
			window(message2);
			input2 = in.nextLine();
			
		} while (!validate(2, input2) || !(Double.parseDouble(input2) <= currentAccount.getBalance()) || !(Double.parseDouble(input2) > 0));
		double change = Double.parseDouble(input2);
		
		if (change + Double.parseDouble(receivingRecord.get(2)) > 999999999999.99) {
			
			String[] message =  {
					"Amount inputted exceeds max limit ofreceiving account.",
					"$ " + format.format(999999999999.99 - Double.parseDouble(receivingRecord.get(2))) + " of the funds sent.",
					"The remaining $" + format.format(change + Double.parseDouble(receivingRecord.get(2)) - 999999999999.99) + " remains in your account."
			};
			window(message);
			change = 999999999999.99 - Double.parseDouble(receivingRecord.get(2));
			
		}
		currentAccount.setBalance( currentAccount.getBalance() - change);
		receivingRecord.set(2, Double.toString(Double.parseDouble(receivingRecord.get(2)) + change));
		viewBalance();
	}
	
	public void viewBalance() {
		System.out.println("");
		System.out.println("Your Current Balance is: $" + format.format(currentAccount.getBalance()));
		System.out.println("");
	}
	
	public void viewPersonalInformation() {
		User user = currentAccount.getUser();
		System.out.println("Your Personal Information");
		System.out.format("%-15s: %s \n", "Account Number", currentAccount.getAccountNumber());
		System.out.format("%-15s: %s \n", "Full Name", user.getFirstName() + " " + user.getLastName());
		String DOB = user.getDOB();
		System.out.format("%-15s: %s \n", "Date of Birth", DOB.substring(4, 6) + "-" + DOB.substring(6, 8) + "-" + DOB.substring(0, 4));
		System.out.format("%-15s: %s \n", "Address", user.getAddress() + ", " + user.getCity() + ", " + user.getState() + " " + user.getPostalCode());
		System.out.format("%-15s: %s \n", "Phone Number", user.getTelephoneNumber());
	}
	
	public void updatePersonalInformation() {
		String input = "";
		
		String[] choices = {"Which Would you like to update?", 
				"1: Telephone Number", 
				"2: Street Address", 
				"3: City", 
				"4: State", 
				"5: Postal Code", 
				"6: PIN", 
				"7: Exit"};
		while (!input.equals("7")) {
			
			window(choices);
			
			input = in.nextLine();
			String value = "";
			switch (input) {
				case "1":
					value = updateField(6);
					currentAccount.getUser().setTelephoneNumber(value);
					break;
				case "2":
					value = updateField(7);
					currentAccount.getUser().setAddress(value);
					break;
				case "3":
					value = updateField(8);
					currentAccount.getUser().setCity(value);
					break;
				case "4":
					value = updateField(9);
					currentAccount.getUser().setState(value);
					break;
				case "5":
					value = updateField(10);
					currentAccount.getUser().setPostalCode(value);
					break;
				case "6":
					value = updateField(1);
					currentAccount.getUser().setPIN(value);
					break;
				case "7":
					break;
				default:
					break;
			}
		}
	
		
	}
	
	public String updateField(int field) {
		if (field == 1) {
			String input1 = "";
			String[] prompt = {"Please enter your current PIN: "};
			while (!input1.equals(currentAccount.getUser().getPIN())) {
				window(prompt);
				input1 = in.nextLine();
				if (!input1.equals(currentAccount.getUser().getPIN())) {
					System.out.println("Invalid Input \n");
				}
			}
		}
		
		String[] change = {"Updating What would you like to change it to?"};
		String input2 = "";
		do {
			window(change);
			input2 = in.nextLine();
			if (!validate(field, input2)) {
				System.out.println("Invalid Input \n");
			}
		} while(!validate(field, input2));	
		System.out.println("updated to: " + input2);
		return input2;
	}
	
	
	public void closeAccount() {
		String input = "";
		do {
			String[] message = {"To close your account permanently please enter your pin. Enter -1 to exit"};
			window(message);
			input = in.nextLine();
			if (input.equals("-1")) {
				return;
			}
		} while(!input.equals(currentAccount.getUser().getPIN()));
		currentAccount.setOpen("N");
		
		
	}
	
	public void close() {
		in.close();
		if (currentAccount != null) {
			updateDatabase(currentAccount);
		}
		data.unCleanData();
		
		try {
			data.writeToFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Thank you for using this ATM");
	}
	
	public void getBankAccountFromRecord (ArrayList<String> record) {
		String[] output = new String[11];
		output[0] = record.get(3);
		output[1] = record.get(4);
		output[2] = record.get(1);
		
		for (int i = 3; i < 9; i++) {
			output[i] = record.get(i + 2);
		}
		User user = new User(output);
		String accountNumber = record.get(0);
		String open = record.get(11);
		double balance = Double.parseDouble(record.get(2));
		this.currentAccount = new BankAccount(user, accountNumber, balance, open);
	}
	
	public void updateDatabase(BankAccount currentAccount) {
		ArrayList<String> record = new ArrayList<String>();
		record.add(currentAccount.getAccountNumber());
		record.add(currentAccount.getUser().getPIN());
		record.add(Double.toString(currentAccount.getBalance()));
		record.add(currentAccount.getUser().getFirstName());
		record.add(currentAccount.getUser().getLastName());
		record.add(currentAccount.getUser().getDOB());
		record.add(currentAccount.getUser().getTelephoneNumber());
		record.add(currentAccount.getUser().getAddress());
		record.add(currentAccount.getUser().getCity());
		record.add(currentAccount.getUser().getState());
		record.add(currentAccount.getUser().getPostalCode());
		record.add(currentAccount.getOpen());
		
		data.updateRecord(currentAccount.getAccountNumber(), record);
	}
	
	public void window(String[] toPrint) {
		int neededLines = (12 - toPrint.length);
		ArrayList<String> output = new ArrayList<String>();
		for (int i = 0; i < neededLines / 2 + neededLines % 2; i++) {
			output.add("");
		}
		
		output.addAll(Arrays.asList(toPrint));
		
		for (int i = 0; i < neededLines / 2; i++) {
			output.add("");
		}
		
		for(int j = 0; j < output.size(); j++) {
			System.out.println(output.get(j));
		}
		
	}
}