/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quickchat;

/**
 *
 * The class stores the user's registration details and provides
 * methods to validate the username, password and cellphone number,
 * register the user and authenticate login details.
 *
 * @author lwandle
 */
public class Login {
    private final String username;
    private final String password;
    private final String cellPhoneNumber;
    private final String firstName;
    private final String lastName;
    
    public Login(String username, String password,
                 String cellPhoneNumber,
                 String firstName,
                 String lastName){
        
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
 /**
 * Validates the username according to the project requirements.
 *
 * The username must contain an underscore and must not exceed
 * five characters.
 *
 * @return true if the username is valid, otherwise false
 */
    public boolean checkUserName() {
        return username != null
              && username.contains("_")
              && username.length() <= 5;

}
    
 /**
 * Validates the password according to the project requirements.
 *
 * The password must contain at least eight characters, one
 * uppercase letter, one number and one special character.
 *
 * @return true if the password meets the requirements
 */
  public boolean checkPasswordComplexity() {
   return password != null
                && password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[0-9].*")
                && password.matches(".*[^a-zA-Z0-9].*");
    }

    public boolean checkCellPhoneNumber() {

        return cellPhoneNumber != null
                && cellPhoneNumber.matches("^\\+27[0-9]{9}$");
    }
 
/**
 * Registers the user after validating the username, password
 * and cellphone number.
 *
 * @return a message indicating whether registration was successful
 */    
    public String registerUser() {
        if(!checkUserName()) {
            return "Username is not correctly formatted; "
                    + "please ensure that your username contains "
                    + " an underscore and is no more than five "
                    + "characters in length. ";
        }
        if (!checkPasswordComplexity()){
            return "Password is not correctly formatted; please ensure " +
                    "that the password contains at least eight characters, " +
                    "a capital letter, a number, and a special character. ";
        }
        if (!checkCellPhoneNumber()) {
            return "Cell phone number incorrectly formatted or does not " +
                    "contain international code. ";
        }
        return 
               "Username successfully captured.\n"
               + "Password successfully captured.\n"
               + "Cell phone number successfully added.";      
        }
    
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        
        return username.equals(enteredUsername) 
                && password.equals(enteredPassword);
    }
    
    public String returnLoginStatus (boolean loginSuccessful) {
        
        if(loginSuccessful) {
            return "Welcome " + firstName + ", " 
                    + lastName 
                    + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    
    }
}
