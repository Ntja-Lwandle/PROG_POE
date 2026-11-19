/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quickchat;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Represents an individual QuickChat message.
 *
 * Stores message information and provides validation, message ID generation and
 * hash generation.
 *
 * @author Lwandle
 */
public class Message {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Set<String> USED_IDS = new HashSet<>();

    private String messageID;
    private int messageNumber;
    private String sender;
    private String recipient;
    private String message;

    public static final String INVALID_RECIPIENT
            = "Cell phone number is incorrectly formatted "
            + "or does not contain an international code. "
            + "Please correct the number and try again.";

    /**
     * Creates a message with sender information.
     */
    public Message(String messageID, int messageNumber,
            String sender, String recipient,
            String message) {

        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.sender = sender;
        this.recipient = recipient;
        this.message = normalizeMessage(message);
    }

    /**
     * Backwards-compatible constructor for existing tests.
     */
    public Message(String messageID, int messageNumber,
            String recipient, String message) {

        this(messageID, messageNumber,
                "QuickChat", recipient, message);
    }

    /**
     * Removes unnecessary spaces before punctuation.
     */
    private static String normalizeMessage(String text) {
        return text == null
                ? null
                : text.replaceAll("\\s+([,!.?;:])", "$1");
    }

    /**
     * Backwards-compatible recipient validation method.
     */
    public String checkRecipientCellMessage() {
        return checkRecipientCell();
    }

    /**
     * Returns the total number of sent messages.
     */
    public static int returnTotalMessagess(List<Message> sent) {
        return sent.size();
    }

    /**
     * Backwards-compatible JSON storage method.
     */
    public void storeMessage(Path path) throws IOException {

        MessageStorage storage = new MessageStorage(path);

        List<Message> messages = storage.loadMessages();

        messages.add(this);

        storage.saveMessages(messages);
    }

    /**
     * Generates a unique ten-digit message ID.
     */
    public static synchronized String generateMessageID() {

        String id;

        do {
            StringBuilder number = new StringBuilder();

            for (int i = 0; i < 10; i++) {
                number.append(RANDOM.nextInt(10));
            }

            id = number.toString();

        } while (!USED_IDS.add(id));

        return id;
    }

    /**
     * Checks that the message ID contains ten digits.
     */
    public boolean checkMessageID() {

        return messageID != null
                && messageID.matches("[0-9]{10}");
    }

    /**
     * Validates a South African international number.
     */
    public static boolean validRecipient(String number) {

        return number != null
                && number.matches("^\\+27[0-9]{9}$");
    }

    public String checkRecipientCell() {

        if (validRecipient(recipient)) {
            return "Cell phone number successfully captured.";
        }

        return INVALID_RECIPIENT;
    }

    /**
     * Checks the message length.
     */
    public boolean checkMessageLength() {

        return message != null
                && !message.isBlank()
                && message.length() <= 250;
    }

    /**
     * Returns an appropriate message validation response.
     */
    public static String validateMessage(String text) {

        if (text == null || text.isBlank()) {
            return "Please enter a message.";
        }

        if (text.length() > 250) {

            int extra = text.length() - 250;

            return "Message exceeds 250 characters by "
                    + extra
                    + "; please reduce the size.";
        }

        return "Message ready to send.";
    }

    /**
     * Removes punctuation around a word.
     */
    private String cleanWord(String word) {

        return word.replaceAll(
                "^[^a-zA-Z0-9]+|[^a-zA-Z0-9]+$", ""
        );
    }

    /**
     * Creates the required uppercase message hash.
     *
     * Format: First two ID digits : message number : first word + last word
     */
    public String createMessageHash() {

        if (!checkMessageID() || !checkMessageLength()) {
            throw new IllegalStateException(
                    "Cannot create hash for invalid message."
            );
        }

        String[] words = message.trim().split("\\s+");

        String firstWord = cleanWord(words[0]);
        String lastWord = cleanWord(words[words.length - 1]);

        return (messageID.substring(0, 2)
                + ":" + messageNumber
                + ":" + firstWord + lastWord).toUpperCase();
    }

    /**
     * Returns the selected message action.
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
     * Displays message details in the required order.
     */
    public String printMessages() {

        return "Message ID: " + messageID
                + "\nMessage Hash: " + createMessageHash()
                + "\nRecipient: " + recipient
                + "\nMessage: " + message;
    }

    /**
     * Displays sender and recipient information.
     */
    public String printSenderAndRecipient() {

        return "Sender: " + getSender()
                + "\nRecipient: " + recipient;
    }

    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getSender() {

        // Older JSON records may not contain sender information.
        return sender == null || sender.isBlank()
                ? "Unknown sender"
                : sender;
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
