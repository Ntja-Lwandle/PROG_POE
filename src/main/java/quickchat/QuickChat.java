/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package quickchat;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;
import java.io.IOException;

/**
 * Main class for the QuickChat console application.
 * Handles registration, login and messaging
 * @author lwandle
 */
public class QuickChat {
    
    /**
     * Starts the QuickChat application.
     *
     * @param args command-line arguments
     */  

  public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        Login user;

        // PART 1: REGISTRATION
        while (true) {

            System.out.println("==== QuickChat Registration ====");

            System.out.print("Enter first name: ");
            String firstName = input.nextLine();

            System.out.print("Enter last name: ");
            String lastName = input.nextLine();

            System.out.print("Enter username: ");
            String username = input.nextLine();

            System.out.print("Enter password: ");
            String password = input.nextLine();

            System.out.print("Enter South African cellphone number: ");
            String cellphone = input.nextLine();

            user = new Login(
                    username,
                    password,
                    cellphone,
                    firstName,
                    lastName
            );

            boolean validUsername = user.checkUserName();
            boolean validPassword = user.checkPasswordComplexity();
            boolean validCell = user.checkCellPhoneNumber();

            if (validUsername && validPassword && validCell) {
                System.out.println(user.registerUser());
                break;
            }

            System.out.println(user.registerUser());

            if (validUsername && !validPassword) {
                System.out.println("Please correct your password.");
            }

            if (validUsername && validPassword && !validCell) {
                System.out.println("Please correct your cellphone number.");
            }

            System.out.println("Please try registering again.\n");
        }

        // PART 1: LOGIN
        System.out.println("\n==== Login ====");

        System.out.print("Enter username: ");
        String loginUsername = input.nextLine();

        System.out.print("Enter password: ");
        String loginPassword = input.nextLine();

        boolean loggedIn = user.loginUser(
                loginUsername, loginPassword
        );

        System.out.println(user.returnLoginStatus(loggedIn));

        // PART 2: ONLY AUTHENTICATED USERS CAN SEND MESSAGES
        if (loggedIn) {
            runMessaging(input);
        }

        input.close();
    }

    // Runs the Part 2 messaging menu.
    private static void runMessaging(Scanner input) {

        System.out.println("\nWelcome to QuickChat.");

        int limit = readPositiveInt(
                input,
                "How many messages would you like to enter? "
        );

        List<Message> sentMessages = new ArrayList<>();

        int processed = 0;
        boolean quit = false;

        // WHILE LOOP: Keep displaying the menu until Quit.
        while (!quit) {

            System.out.println("\n===== QUICKCHAT MENU =====");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");

            int option = readInt(input, "Choose an option: ");

            switch (option) {

                case 1:

                    if (processed >= limit) {
                        System.out.println("Message limit reached.");
                        break;
                    }

                    // FOR LOOP: Restrict the number of messages.
                    for (; processed < limit; processed++) {

                        System.out.println("\nMessage " + (processed + 1) + " of " + limit);

                        String recipient;

                        // Validate the recipient number.
                        do {

                            System.out.print("Recipient (+27 followed by 9 digits): ");

                            recipient = input.nextLine().trim();

                            if (!Message.validRecipient(recipient)) {
                                System.out.println(Message.INVALID_RECIPIENT);
                            }

                        } while (!Message.validRecipient(recipient));

                        String body;

                        // Validate the message length.
                        do {

                            System.out.print("Enter message: ");
                            body = input.nextLine();

                            System.out.println( Message.validateMessage(body));

                        } while (body.isBlank() || body.length() > 250);

                        // Create the message object.
                        Message message = new Message(
                                Message.generateMessageID(),
                                processed,
                                recipient,
                                body
                        );

                        System.out.println(
                                "Message ID generated: "
                                + message.getMessageID()
                        );

                        // MESSAGE ACTION MENU
                        System.out.println("\nChoose message action:");
                        System.out.println("1) Send Message");
                        System.out.println("2) Disregard Message");
                        System.out.println("3) Store Message to send later");

                        int action;

                        do {
                            action = readInt(input, "Select action: ");

                            if (action < 1 || action > 3) {
                                System.out.println("Please select 1, 2 or 3." );
                            }

                        } while (action < 1 || action > 3);

                        
                        if (action == 1) {

                            // Send the message.
                            sentMessages.add(message);

                            // Display message details.
                            System.out.println(message.SentMessage(1));
                            System.out.println(message.printMessages());
                            

                        } else if (action == 2) {

                          System.out.println(message.SentMessage(2));

                          int confirm = readInt(input,"Enter 0 to confirm deletion: ");

                         while (confirm != 0) {
                                confirm = readInt(input, "Please press 0 to delete: ");
    }

    System.out.println("Message disregarded.");

} else if (action == 3) {

    try {

        message.storeMessage(
                Path.of("stored-messages.json")
        );

        System.out.println(message.SentMessage(3));

    } catch (IOException error) {

        System.out.println(
                "Could not store message: "
                + error.getMessage()
        );
    }
}
}                       
                    System.out.println(
                            "Total messages sent: "
                            + Message.returnTotalMessagess(sentMessages)
                    );

                    break;

                case 2:

                    System.out.println("Coming Soon.");
                    break;

                case 3:

                    quit = true;
                    break;

                default:

                    System.out.println("Invalid option. Choose 1, 2 or 3.");
            }
        }

        System.out.println("Goodbye!");
    }

    // Reads an integer and handles invalid input.
    private static int readInt(Scanner input, String prompt) {

        while (true) {

            System.out.print(prompt);

            try {
                return Integer.parseInt(
                        input.nextLine().trim()
                );

            } catch (NumberFormatException error) {

                System.out.println( "Please enter a valid number.");
            }
        }
    }

    // Ensures the number of messages is positive.
    private static int readPositiveInt(
            Scanner input, String prompt) {

        int value;

        do {

            value = readInt(input, prompt);

            if (value <= 0) {
                System.out.println("Enter a number greater than zero.");
            }

        } while (value <= 0);

        return value;
    }  
}
