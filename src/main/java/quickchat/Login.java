package quickchat;

/**
 * Stores the registered user's details and checks the username, password and
 * cell phone number. It also handles registration and login messages.
 *
 * @author lwandle
 */
public class Login {

    private final String username;
    private final String password;
    private final String cellPhoneNumber;
    private final String firstName;
    private final String lastName;

    public Login(String username, String password, String cellPhoneNumber,
            String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * The username must contain an underscore and be no more than five
     * characters long.
     */
    public boolean checkUserName() {
        return username != null
                && username.contains("_")
                && username.length() <= 5;
    }

    /**
     * The password must be at least eight characters long and contain a
     * capital letter, a number and a special character.
     */
    public boolean checkPasswordComplexity() {
        return password != null
                && password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[0-9].*")
                && password.matches(".*[^a-zA-Z0-9].*");
    }

    /**
     * Checks the cell phone number by using the shared regular expression
     * method below.
     */
    public boolean checkCellPhoneNumber() {
        return isValidCellNumber(cellPhoneNumber);
    }

    /**
     * A valid number starts with the South African international code (+27)
     * followed by nine digits, for example +27838968976.
     *
     * The regular expression is based on the Pattern class documentation:
     * Oracle (n.d.) Class Pattern, Java Platform, Standard Edition API
     * Specification. Available at:
     * https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/regex/Pattern.html
     * (Accessed: 10 October 2026).
     */
    public static boolean isValidCellNumber(String number) {
        return number != null && number.matches("^\\+27[0-9]{9}$");
    }

    /**
     * Returns the registration message. The first problem found is reported.
     */
    public String registerUser() {
        if (!checkUserName()) {
            return "Username is not correctly formatted; please ensure that "
                    + "your username contains an underscore and is no more "
                    + "than five characters in length.";
        }
        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure that "
                    + "the password contains at least eight characters, a "
                    + "capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber()) {
            return "Cell phone number incorrectly formatted or does not "
                    + "contain international code.";
        }
        return "Username successfully captured.\n"
                + "Password successfully captured.\n"
                + "Cell phone number successfully added.";
    }

    /**
     * Checks the entered login details against the registered details.
     */
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return username.equals(enteredUsername)
                && password.equals(enteredPassword);
    }

    /**
     * Returns the welcome message or the failed login message.
     */
    public String returnLoginStatus(boolean loginSuccessful) {
        if (loginSuccessful) {
            return "Welcome " + firstName + ", " + lastName
                    + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
