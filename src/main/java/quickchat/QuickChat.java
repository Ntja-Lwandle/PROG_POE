/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package quickchat;

import java.util.Scanner;

/**
 * Main class for the QuickChat console application.
 * 
 * This class collects registration details from the user,
 * creates a Login object, validates the registration details,
 * and allows the user to attempt to log in.
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
      
      
      Login user = new Login(
              username,
              password,
              cellPhoneNumber,
              firstName,
              lastName
              
      );
      
      System.out.println();
      
      System.out.println(user.registerUser());
      
     System.out.println();
     
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
