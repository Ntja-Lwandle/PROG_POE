/**
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package quickchat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for the QuickChat application.
 *
 * Handles registration, login and the messaging menu.
 *
 * @author Lwandle
 */
public class QuickChat {

    private static final Path STORAGE_FILE
            = Path.of("stored-messages.json");

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        Login user = registerUser(input);

        if (user == null) {
            input.close();
            return;
        }

        boolean loggedIn = loginUser(input, user);

        if (loggedIn) {
            runMessaging(input, user);
        }

        input.close();
    }

    /**
     * Handles user registration.
     */
    private static Login registerUser(Scanner input) {

        while (true) {

            System.out.println("\n==== QuickChat Registration ====");

            System.out.print("Enter first name: ");
            String firstName = input.nextLine();

            System.out.print("Enter last name: ");
            String lastName = input.nextLine();

            System.out.print("Enter username: ");
            String username = input.nextLine();

            System.out.print("Enter password: ");
            String password = input.nextLine();

            System.out.print("Enter cellphone number: ");
            String cellphone = input.nextLine();

            Login user = new Login(
                    username,
                    password,
                    cellphone,
                    firstName,
                    lastName
            );

            if (user.checkUserName()
                    && user.checkPasswordComplexity()
                    && user.checkCellPhoneNumber()) {

                System.out.println(user.registerUser());

                return user;
            }

            System.out.println(user.registerUser());
            System.out.println("Please try registering again.\n");
        }
    }

    /**
     * Authenticates the registered user.
     */
    private static boolean loginUser(
            Scanner input, Login user) {

        System.out.println("\n==== Login ====");

        System.out.print("Enter username: ");
        String username = input.nextLine();

        System.out.print("Enter password: ");
        String password = input.nextLine();

        boolean successful
                = user.loginUser(username, password);

        System.out.println(
                user.returnLoginStatus(successful)
        );

        return successful;
    }

    /**
     * Runs the main QuickChat application.
     */
    private static void runMessaging(
            Scanner input, Login user) {

        MessageManager manager;

        try {

            manager = new MessageManager(STORAGE_FILE);

        } catch (IOException error) {

            System.out.println(
                    "Unable to load stored messages: "
                    + error.getMessage()
            );

            return;
        }

        System.out.println("\nWelcome to QuickChat.");

        int messageLimit = readPositiveInt(
                input,
                "How many messages would you like to enter? "
        );

        int processedMessages = 0;

        boolean quit = false;

        // Main application loop.
        while (!quit) {

            displayMainMenu();

            int option = readInt(input, "Choose an option: ");

            switch (option) {

                case 1:

                    if (processedMessages >= messageLimit) {

                        System.out.println(
                                "Message limit reached."
                        );

                        break;
                    }

                    // Controls the number of messages entered.
                    for (; processedMessages < messageLimit;
                            processedMessages++) {

                        System.out.println(
                                "\nMessage "
                                + (processedMessages + 1)
                                + " of "
                                + messageLimit
                        );

                        processMessage(
                                input,
                                manager,
                                user,
                                processedMessages
                        );
                    }

                    System.out.println(
                            "\nTotal messages sent: "
                            + manager.returnTotalMessagesSent()
                    );

                    break;

                case 2:

                    System.out.println("Coming Soon.");

                    break;

                case 3:

                    quit = true;

                    break;

                case 4:

                    displayStoredMessagesMenu(
                            input, manager
                    );

                    break;

                default:

                    System.out.println(
                            "Invalid option. Choose 1, 2, 3 or 4."
                    );
            }
        }

        System.out.println("Goodbye!");
    }

    /**
     * Displays the main application menu.
     */
    private static void displayMainMenu() {

        System.out.println("\n========== QUICKCHAT MENU ==========");
        System.out.println("1) Send Messages");
        System.out.println("2) Show recently sent messages");
        System.out.println("3) Quit");
        System.out.println("4) Stored Messages");
        System.out.println("====================================");
    }

    /**
     * Accepts, validates and processes an individual message.
     */
    private static void processMessage(
            Scanner input,
            MessageManager manager,
            Login user,
            int messageNumber) {

        String recipient;

        // Validate the recipient number.
        do {
            System.out.print(
                    "Recipient (+27 followed by 9 digits): "
            );

            recipient = input.nextLine().trim();

            if (!Message.validRecipient(recipient)) {
                System.out.println(Message.INVALID_RECIPIENT);
            }

        } while (!Message.validRecipient(recipient));

        // Accept and validate the message.
        String body;

        do {
            System.out.print("Enter message: ");

            body = input.nextLine()
                    .replaceAll("\\s+([,!.?;:])", "$1");

            System.out.println(Message.validateMessage(body));

        } while (body.isBlank() || body.length() > 250);

        // Create the message.
        Message message = new Message(
                Message.generateMessageID(),
                messageNumber,
                user.getFullName(),
                recipient,
                body
        );

        System.out.println(
                "Message ID generated: "
                + message.getMessageID()
        );

        System.out.println(
                "Message Hash: "
                + message.createMessageHash()
        );

        // Ask the user what to do with the message.
        int action = readMessageAction(input);

        boolean processedSuccessfully = false;

        try {

            switch (action) {

                case 1:

                    manager.addSentMessage(message);

                    System.out.println(
                            message.SentMessage(1)
                    );

                    processedSuccessfully = true;

                    break;

                case 2:

                    manager.addDisregardedMessage(message);

                    System.out.println(
                            message.SentMessage(2)
                    );

                    int confirmation = readInt(
                            input,
                            "Enter 0 to confirm deletion: "
                    );

                    if (confirmation == 0) {
                        System.out.println(
                                "Message disregarded."
                        );
                    } else {
                        System.out.println(
                                "Message was retained as disregarded."
                        );
                    }

                    processedSuccessfully = true;

                    break;

                case 3:

                    manager.addStoredMessage(message);

                    System.out.println(
                            message.SentMessage(3)
                    );

                    processedSuccessfully = true;

                    break;

                default:

                    System.out.println("Invalid action.");

                    break;
            }

            // Display the message details after a successful action.
            if (processedSuccessfully) {

                System.out.println(
                        "\n========== MESSAGE DETAILS =========="
                );

                System.out.println(message.printMessages());

                System.out.println(
                        "======================================"
                );
            }

        } catch (IOException error) {

            System.out.println(
                    "Message storage error: "
                    + error.getMessage()
            );
        }
    }

    /**
     * Displays the message action options.
     */
    private static int readMessageAction(Scanner input) {

        System.out.println("\nChoose message action:");
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");

        int action;

        do {
            action = readInt(input, "Select action: ");

            if (action < 1 || action > 3) {

                System.out.println(
                        "Please select 1, 2 or 3."
                );
            }

        } while (action < 1 || action > 3);

        return action;
    }

    /**
     * Displays stored-message menu.
     */
    private static void displayStoredMessagesMenu(
            Scanner input,
            MessageManager manager) {

        boolean returnToMain = false;

        while (!returnToMain) {

            System.out.println("\n======= STORED MESSAGES =======");
            System.out.println("1) Display sender and recipient");
            System.out.println("2) Display longest stored message");
            System.out.println("3) Search message using ID");
            System.out.println("4) Search messages by recipient");
            System.out.println("5) Delete message using hash");
            System.out.println("6) Display stored message report");
            System.out.println("7) Return to main menu");
            System.out.println("==============================");

            int option = readInt(input, "Choose an option: ");

            switch (option) {

                case 1:

                    System.out.println(
                            manager.displayStoredSendersAndRecipients()
                    );

                    break;

                case 2:

                    Message longest
                            = manager.findLongestStoredMessage();

                    if (longest == null) {

                        System.out.println(
                                "No stored messages found."
                        );

                    } else {

                        System.out.println(
                                "Longest stored message:"
                        );

                        System.out.println(
                                longest.printMessages()
                        );
                    }

                    break;

                case 3:

                    System.out.print("Enter message ID: ");

                    String id = input.nextLine().trim();

                    Message found
                            = manager.searchMessageByID(id);

                    if (found == null) {

                        System.out.println(
                                "Message ID not found."
                        );

                    } else {

                        System.out.println(
                                "Recipient: "
                                + found.getRecipient()
                        );

                        System.out.println(
                                "Message: "
                                + found.getMessage()
                        );
                    }

                    break;

                case 4:

                    System.out.print(
                            "Enter recipient number: "
                    );

                    String recipient
                            = input.nextLine().trim();

                    List<Message> results
                            = manager.searchMessagesByRecipient(
                                    recipient
                            );

                    if (results.isEmpty()) {

                        System.out.println(
                                "No messages found for this recipient."
                        );

                    } else {

                        for (Message message : results) {

                            System.out.println(
                                    message.printMessages()
                            );

                            System.out.println(
                                    "--------------------"
                            );
                        }
                    }

                    break;

                case 5:

                    System.out.print(
                            "Enter the message hash: "
                    );

                    String hash = input.nextLine().trim();

                    try {

                        Message messageToDelete
                                = manager.findStoredMessageByHash(hash);

                        boolean deleted
                                = manager.deleteStoredMessageByHash(hash);

                        if (deleted) {

                            System.out.println(
                                    "Message: \""
                                    + messageToDelete.getMessage()
                                    + "\" successfully deleted."
                            );

                        } else {

                            System.out.println("Message hash not found.");
                        }

                    } catch (IOException error) {

                        System.out.println(
                                "Unable to update JSON file: "
                                + error.getMessage()
                        );
                    }

                    break;

                case 6:

                    System.out.println(
                            manager.displayStoredMessageReport()
                    );

                    break;

                case 7:

                    returnToMain = true;

                    break;

                default:

                    System.out.println(
                            "Please select a valid option."
                    );
            }
        }
    }

    /**
     * Reads an integer and handles invalid input.
     */
    private static int readInt(
            Scanner input, String prompt) {

        while (true) {

            System.out.print(prompt);

            try {

                return Integer.parseInt(
                        input.nextLine().trim()
                );

            } catch (NumberFormatException error) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }

    /**
     * Ensures the requested number is positive.
     */
    private static int readPositiveInt(
            Scanner input, String prompt) {

        int value;

        do {

            value = readInt(input, prompt);

            if (value <= 0) {

                System.out.println(
                        "Enter a number greater than zero."
                );
            }

        } while (value <= 0);

        return value;
    }
}
