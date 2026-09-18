/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package quickchat;

import java.util.Scanner;

/**
 * Main class for the QuickChat console application.
 * 
 * Main class for the QuickChat console application.
 * Handles user input, registration, and login.
 *
 * @author lwandle
 */
public class QuickChat {
    
    /**
     * Starts the QuickChat application.
     *
     * @param args command-line arguments
     */  

    public static void main(String[] args) {
        
      Scanner input = new Scanner (System.in);
      
      Login user;

        // Keep asking for registration details until all details are valid
        while (true) { 
      
      System.out.println("==== QuickChat Registration ====");
      
      System.out.print("Enter first name: ");
      String firstName = input.nextLine();
      
      System.out.print("Enter last  name: ");
      String lastName = input.nextLine();
      
      System.out.print("Enter username: ");
      String username = input.nextLine();
      
      System.out.print("Enter password: ");
      String password = input.nextLine();
      
      System.out.print("Enter South African cellphone number: ");
      String cellPhoneNumber = input.nextLine();
      
      // Create Login object using the entered information
       user = new Login(
              username,
              password,
              cellPhoneNumber,
              firstName,
              lastName
              
      );
      
     System.out.println();

            // Check all registration requirements
            boolean validUsername = user.checkUserName();
            boolean validPassword = user.checkPasswordComplexity();
            boolean validCellPhone = user.checkCellPhoneNumber();

            boolean registrationSuccessful =
                    validUsername && validPassword && validCellPhone;

            // Display all errors instead of stopping at the first error
            if (!validUsername) {
                System.out.println(
                        "Username is not correctly formatted; please ensure that "
                        + "your username contains an underscore and is no more than "
                        + "five characters."
                );
            }

            if (!validPassword) {
                System.out.println(
                        "Password is not correctly formatted; please ensure that "
                        + "the password contains at least eight characters, a capital "
                        + "letter, a number, and a special character."
                );
            }

            if (!validCellPhone) {
                System.out.println(
                        "Cellphone number is incorrectly formatted; please ensure "
                        + "that it starts with +27 and contains 9 digits after the "
                        + "country code."
                );
            }

            // If everything is valid, complete registration
            if (registrationSuccessful) {
                System.out.println(user.registerUser());
                break;
            }

            // If something is invalid, ask for the details again
            System.out.println();
            System.out.println("Please enter the correct details.");
            System.out.println();
        }
     
     // Login section only starts after successful registration
     System.out.println("==== Login ====");
     
     System.out.print("Enter username: ");
     String loginUsername = input.nextLine();
     
     System.out.print("Enter password: ");
     String loginPassword = input.nextLine();
     
     boolean loginSuccessful = user.loginUser(loginUsername, loginPassword);
     
     System.out.println(
             user.returnLoginStatus(loginSuccessful)
     );
     input.close();
    }
}
