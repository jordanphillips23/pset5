import java.util.Scanner;

/**
 * Just like last time, the User class is responsible for retrieving
 * (i.e., getting), and updating (i.e., setting) user information.
 * This time, though, you'll need to add the ability to update user
 * information and display that information in a formatted manner.
 * 
 * Most of the functionality for this class should have already been
 * implemented last time. You can always reference my Github repository
 * for inspiration (https://github.com/rwilson-ucvts/java-sample-atm).
 */

public class User {
	private String firstName;
	private String lastName;
	private String PIN;
	private String DOB;
	private String telephoneNumber;
	private String address;
	private String city;
	private String state;
	private String postalCode;
	
	public User(String[] fields) {
		firstName = fields[0];
		lastName = fields[1];
		PIN = fields[2];
		DOB = fields[3];
		telephoneNumber = fields[4];
		address = fields[5];
		city = fields[6];
		state = fields[7];
		postalCode = fields[8];
		
		
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getPIN() {
		return PIN;
	}
	
	public String getDOB() {
		return DOB;
	}
	
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getState() {
		return state;
	}
	
	public String getPostalCode() {
		return postalCode;
	}
	
	public void setPIN(String newPIN) {
		this.PIN = newPIN;
	}
	
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	} 
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
}