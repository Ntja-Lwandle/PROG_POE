package quickchat;

import java.io.IOException;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * One QuickChat message. It stores the message details and checks the
 * recipient, the message length, the message ID and the message hash.
 *
 * @author lwandle
 */
public class Message {

    private static final SecureRandom RANDOM = new SecureRandom();
    // Remembers the IDs already used so that every ID is unique.
    private static final Set<String> USED_IDS = new HashSet<>();

    public static final String INVALID_RECIPIENT
            = "Cell phone number is incorrectly formatted or does not "
            + "contain an international code. Please correct the number "
            + "and try again.";

    private String messageID;
    private int messageNumber;
    private String sender;
    private String recipient;
    private String message;

    public Message(String messageID, int messageNumber, String sender,
            String recipient, String message) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.sender = sender;
        this.recipient = recipient;
        // Removes a space before punctuation, e.g. "time !" becomes "time!".
        this.message = message == null
                ? null
                : message.replaceAll("\\s+([,!.?;:])", "$1");
    }

    /**
     * Creates a message when the sender is not given. The sender is then
     * "QuickChat".
     */
    public Message(String messageID, int messageNumber, String recipient,
            String message) {
        this(messageID, messageNumber, "QuickChat", recipient, message);
    }

    /**
     * Creates a random ten-digit message ID that has not been used before.
     */
    public static String generateMessageID() {
        String id;
        do {
            id = "";
            for (int i = 0; i < 10; i++) {
                id = id + RANDOM.nextInt(10);
            }
        } while (!USED_IDS.add(id));
        return id;
    }

    /**
     * The message ID must be exactly ten digits.
     */
    public boolean checkMessageID() {
        return messageID != null && messageID.matches("[0-9]{10}");
    }

    /**
     * Reuses the cell number check from the Login class (Part 1).
     */
    public static boolean validRecipient(String number) {
        return Login.isValidCellNumber(number);
    }

    public String checkRecipientCell() {
        if (validRecipient(recipient)) {
            return "Cell phone number successfully captured.";
        }
        return INVALID_RECIPIENT;
    }

    /**
     * The message may not be empty and may not be longer than 250 characters.
     */
    public boolean checkMessageLength() {
        return message != null && !message.isBlank() && message.length() <= 250;
    }

    /**
     * Returns the message that tells the user if the text can be sent.
     */
    public static String validateMessage(String text) {
        if (text == null || text.isBlank()) {
            return "Please enter a message.";
        }
        if (text.length() > 250) {
            int extra = text.length() - 250;
            return "Message exceeds 250 characters by " + extra
                    + "; please reduce the size.";
        }
        return "Message ready to send.";
    }

    /**
     * Creates the message hash: first two digits of the ID, the message
     * number, and the first and last word of the message, in capital letters.
     * Example: 00:0:HITONIGHT
     */
    public String createMessageHash() {
        if (!checkMessageID() || !checkMessageLength()) {
            throw new IllegalStateException("Cannot create hash for invalid message.");
        }

        String[] words = message.trim().split("\\s+");
        String firstWord = removePunctuation(words[0]);
        String lastWord = removePunctuation(words[words.length - 1]);

        String hash = messageID.substring(0, 2) + ":" + messageNumber + ":"
                + firstWord + lastWord;
        return hash.toUpperCase();
    }

    /**
     * Removes punctuation from the start and end of a word,
     * e.g. "tonight?" becomes "tonight".
     */
    private String removePunctuation(String word) {
        return word.replaceAll("^[^a-zA-Z0-9]+|[^a-zA-Z0-9]+$", "");
    }

    /**
     * Returns the text for the option the user chose:
     * 1 = send, 2 = disregard, 3 = store.
     */
    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                return "Message successfully stored.";
            default:
                return "Invalid option selected.";
        }
    }

    /**
     * Returns the message details in the required order:
     * Message ID, Message Hash, Recipient, Message.
     */
    public String printMessages() {
        return "Message ID: " + messageID
                + "\nMessage Hash: " + createMessageHash()
                + "\nRecipient: " + recipient
                + "\nMessage: " + message;
    }

    public String printSenderAndRecipient() {
        return "Sender: " + getSender() + "\nRecipient: " + recipient;
    }

    /**
     * Returns the total number of messages in the list of sent messages.
     */
    public static int returnTotalMessagess(List<Message> sent) {
        return sent.size();
    }

    /**
     * Adds this message to the JSON file.
     */
    public void storeMessage(Path path) throws IOException {
        MessageStorage storage = new MessageStorage(path);
        List<Message> messages = storage.loadMessages();
        messages.add(this);
        storage.saveMessages(messages);
    }

    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getSender() {
        // Messages saved before the sender was added have no sender.
        if (sender == null || sender.isBlank()) {
            return "Unknown sender";
        }
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageHash() {
        return createMessageHash();
    }
}
