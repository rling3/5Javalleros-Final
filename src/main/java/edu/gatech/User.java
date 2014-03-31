package main.java.edu.gatech;

public class User {
	private static User uInstance = null;
	
	protected static String loggedInEmail;
	
	protected User(String loggedInEmail) {
		setLoggedInEmail(loggedInEmail);
	}
	
	public static synchronized User getInstance(String loggedInEmail) {
		if (uInstance == null) {
			uInstance = new User(loggedInEmail);
		}
		return uInstance;
	}
	
	public static String getLoggedInEmail() {
		return loggedInEmail;
	}

	public static void setLoggedInEmail(String loggedInEmail) {
		User.loggedInEmail = loggedInEmail;
	}
	
	public static void logOut() {
		loggedInEmail = null;
		uInstance = null;
	}
}
