package quickchat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for the QuickChat console application.
 * The user registers, logs in and then uses the message menus.
 *
 * @author lwandle
 */
public class QuickChat {

    // The JSON file where stored messages are saved.
    private static final Path STORAGE_FILE = Path.of("stored-messages.json");

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        Login user = registerUser(input);

        // Only a user who logs in successfully may use the messaging menu.
        if (loginUser(input, user)) {
            runMessaging(input, user);
        }

        input.close();
    }

    /**
     * Asks for the registration details until they are all valid.
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

            Login user = new Login(username, password, cellphone,
                    firstName, lastName);

            System.out.println(user.registerUser());

            if (user.checkUserName() && user.checkPasswordComplexity()
                    && user.checkCellPhoneNumber()) {
                return user;
            }
            System.out.println("Please try registering again.");
        }
    }

    /**
     * Asks for the username and password and shows the login message.
     */
    private static boolean loginUser(Scanner input, Login user) {
        System.out.println("\n==== Login ====");

        System.out.print("Enter username: ");
        String username = input.nextLine();
        System.out.print("Enter password: ");
        String password = input.nextLine();

        boolean successful = user.loginUser(username, password);
        System.out.println(user.returnLoginStatus(successful));
        return successful;
    }

    /**
     * Shows the welcome message and runs the main menu until the user quits.
     */
    private static void runMessaging(Scanner input, Login user) {
        MessageManager manager;
        try {
            manager = new MessageManager(STORAGE_FILE);
        } catch (IOException error) {
            System.out.println("Unable to load stored messages: "
                    + error.getMessage());
            return;
        }

        System.out.println("\nWelcome to QuickChat.");

        // The user decides how many messages may be entered.
        int messageLimit = readPositiveInt(input,
                "How many messages would you like to enter? ");
        int processedMessages = 0;

        boolean quit = false;

        // While loop: keeps the application running until the user quits.
        while (!quit) {
            displayMainMenu();
            int option = readInt(input, "Choose an option: ");

            switch (option) {
                case 1:
                    if (processedMessages >= messageLimit) {
                        System.out.println("Message limit reached.");
                        break;
                    }

                    // For loop: only lets the user enter the set number of
                    // messages. The counter is also the message number.
                    for (; processedMessages < messageLimit; processedMessages++) {
                        System.out.println("\nMessage " + (processedMessages + 1)
                                + " of " + messageLimit);
                        processMessage(input, manager, user, processedMessages);
                    }

                    System.out.println("\nTotal messages sent: "
                            + manager.returnTotalMessagesSent());
                    break;

                case 2:
                    System.out.println("Coming Soon.");
                    break;

                case 3:
                    quit = true;
                    break;

                case 4:
                    storedMessagesMenu(input, manager);
                    break;

                default:
                    System.out.println("Invalid option. Choose 1, 2, 3 or 4.");
            }
        }

        System.out.println("Goodbye!");
    }

    private static void displayMainMenu() {
        System.out.println("\n========== QUICKCHAT MENU ==========");
        System.out.println("1) Send Messages");
        System.out.println("2) Show recently sent messages");
        System.out.println("3) Quit");
        System.out.println("4) Stored Messages");
        System.out.println("====================================");
    }

    /**
     * Reads one message (recipient and text), creates its ID and hash, and
     * lets the user send, disregard or store it.
     */
    private static void processMessage(Scanner input, MessageManager manager,
            Login user, int messageNumber) {

        // Ask for the recipient until the number is valid.
        String recipient;
        do {
            System.out.print("Recipient (+27 followed by 9 digits): ");
            recipient = input.nextLine().trim();
            if (!Message.validRecipient(recipient)) {
                System.out.println(Message.INVALID_RECIPIENT);
            }
        } while (!Message.validRecipient(recipient));

        // Ask for the message until it is not empty and not too long.
        String text;
        do {
            System.out.print("Enter message: ");
            text = input.nextLine();
            System.out.println(Message.validateMessage(text));
        } while (text.isBlank() || text.length() > 250);

        Message message = new Message(Message.generateMessageID(),
                messageNumber, user.getFullName(), recipient, text);

        System.out.println("Message ID generated: " + message.getMessageID());
        System.out.println("Message Hash: " + message.createMessageHash());

        int action = readMessageAction(input);

        try {
            switch (action) {
                case 1:
                    manager.addSentMessage(message);
                    break;
                case 2:
                    manager.addDisregardedMessage(message);
                    break;
                default:
                    manager.addStoredMessage(message);
            }

            System.out.println(message.SentMessage(action));

            if (action == 2) {
                readInt(input, "Enter 0 to confirm deletion: ");
                System.out.println("Message disregarded.");
            }

            // Show the full details after the message has been handled.
            System.out.println("\n========== MESSAGE DETAILS ==========");
            System.out.println(message.printMessages());
            System.out.println("======================================");

        } catch (IOException error) {
            System.out.println("Message storage error: " + error.getMessage());
        }
    }

    /**
     * Asks the user to send, disregard or store the message (1, 2 or 3).
     */
    private static int readMessageAction(Scanner input) {
        System.out.println("\nChoose message action:");
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");

        int action = readInt(input, "Select action: ");
        while (action < 1 || action > 3) {
            System.out.println("Please select 1, 2 or 3.");
            action = readInt(input, "Select action: ");
        }
        return action;
    }

    /**
     * The fourth main menu option: works with the stored messages.
     */
    private static void storedMessagesMenu(Scanner input, MessageManager manager) {
        boolean back = false;

        while (!back) {
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
                    System.out.println(manager.displayStoredSendersAndRecipients());
                    break;

                case 2:
                    Message longest = manager.findLongestStoredMessage();
                    if (longest == null) {
                        System.out.println("No stored messages found.");
                    } else {
                        System.out.println("Longest stored message:");
                        System.out.println(longest.printMessages());
                    }
                    break;

                case 3:
                    System.out.print("Enter message ID: ");
                    Message found = manager.searchMessageByID(input.nextLine().trim());
                    if (found == null) {
                        System.out.println("Message ID not found.");
                    } else {
                        System.out.println("Recipient: " + found.getRecipient());
                        System.out.println("Message: " + found.getMessage());
                    }
                    break;

                case 4:
                    System.out.print("Enter recipient number: ");
                    List<Message> results = manager.searchMessagesByRecipient(
                            input.nextLine().trim());
                    if (results.isEmpty()) {
                        System.out.println("No messages found for this recipient.");
                    }
                    for (Message message : results) {
                        System.out.println(message.printMessages());
                        System.out.println("--------------------");
                    }
                    break;

                case 5:
                    deleteByHash(input, manager);
                    break;

                case 6:
                    System.out.println(manager.displayStoredMessageReport());
                    break;

                case 7:
                    back = true;
                    break;

                default:
                    System.out.println("Please select a valid option.");
            }
        }
    }

    /**
     * Deletes a stored message after the user enters its hash.
     */
    private static void deleteByHash(Scanner input, MessageManager manager) {
        System.out.print("Enter the message hash: ");
        String hash = input.nextLine().trim();

        try {
            Message messageToDelete = manager.findStoredMessageByHash(hash);

            if (manager.deleteStoredMessageByHash(hash)) {
                System.out.println("Message: \"" + messageToDelete.getMessage()
                        + "\" successfully deleted.");
            } else {
                System.out.println("Message hash not found.");
            }
        } catch (IOException error) {
            System.out.println("Unable to update JSON file: " + error.getMessage());
        }
    }

    /**
     * Reads a whole number and asks again if the user types something else.
     */
    private static int readInt(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException error) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Reads a whole number that is greater than zero.
     */
    private static int readPositiveInt(Scanner input, String prompt) {
        int value = readInt(input, prompt);
        while (value <= 0) {
            System.out.println("Enter a number greater than zero.");
            value = readInt(input, prompt);
        }
        return value;
    }
}
