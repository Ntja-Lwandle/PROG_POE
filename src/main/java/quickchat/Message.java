/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package quickchat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Represents a QuickChat message.
 * Handles message validation, hashing and storage.
 */
public class Message {

private static final Set<String> usedMessageIDs = new HashSet<>();

    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String message;

    public static final String INVALID_RECIPIENT =
        "Cell phone number is incorrectly formatted or does not "
        + "contain an international code. Please correct the "
        + "number and try again.";

    public Message(String messageID, int messageNumber,
                   String recipient, String message) {

        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
    }

    // Checks that the ID contains exactly 10 digits.
    public boolean checkMessageID() {
        return messageID != null
                && messageID.matches("[0-9]{10}");
    }

    // Validates a South African international phone number.
    public boolean checkRecipientCell() {
        return validRecipient(recipient);
    }

    public static boolean validRecipient(String number) {
        return number != null
                && number.matches("^\\+27[0-9]{9}$");
    }

    public String checkRecipientCellMessage() {
        if (checkRecipientCell()) {
            return "Cell phone number successfully captured.";
        }
        return INVALID_RECIPIENT;
    }

    // Checks the 250-character message limit.
    public boolean checkMessageLength() {
        return message != null
                && !message.isBlank()
                && message.length() <= 250;
    }

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

    // Generates a random 10-digit message ID.
    public static String generateMessageID() {

      SecureRandom random = new SecureRandom();
    String id;

    do {
        StringBuilder number = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            number.append(random.nextInt(10));
        }

        id = number.toString();

    } while (!usedMessageIDs.add(id));

    return id; 
    }

    // Creates the required uppercase message hash.
    public String createMessageHash() {

        String firstTwo = messageID.substring(0, 2);

        String[] words = message.trim().split("\\s+");

        String firstWord = words[0];

        String lastWord = words[words.length - 1]
                .replaceAll("^[^a-zA-Z0-9]+|[^a-zA-Z0-9]+$", "");

        return (firstTwo + ":"
                + messageNumber + ":"
                + firstWord
                + lastWord).toUpperCase();
    }

    // Returns the message action selected by the user.
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

    // Displays details in the required order.
    public String printMessages() {

        return "Message ID: " + messageID
                + "\nMessage Hash: " + createMessageHash()
                + "\nRecipient: " + recipient
                + "\nMessage: " + message;
    }

    public String getMessageID() {
        return messageID;
    }

    // Returns the total number of sent messages.
    public static int returnTotalMessagess(List<Message> sent) {
        return sent.size();
    }

    /**
     * Stores a message in a JSON array.
     * JSON string escaping is based on RFC 8259,
     * Section 7:
     * https://www.rfc-editor.org/rfc/rfc8259#section-7
     */
    public void storeMessage(Path path) throws IOException {

        String item =
                "{\"messageID\":\"" + jsonEscape(messageID)
                + "\",\"messageNumber\":" + messageNumber
                + ",\"recipient\":\"" + jsonEscape(recipient)
                + "\",\"message\":\"" + jsonEscape(message)
                + "\",\"messageHash\":\""
                + jsonEscape(createMessageHash()) + "\"}";

        
        String old = "[]";

        if (Files.exists(path)) {
        old = Files.readString(path).trim();

        // Treat an existing empty file as an empty JSON array.
        if (old.isEmpty()) {
        old = "[]";
    }
}

        String inner = old.substring(1, old.length() - 1).trim();

        String result = "[\n"
                + (inner.isEmpty() ? "" : inner + ",\n")
                + item + "\n]";

        Files.writeString(path, result);
    }

    // Escapes special characters before writing JSON.
    private static String jsonEscape(String input) {

        StringBuilder output = new StringBuilder();

        for (char character : input.toCharArray()) {

            switch (character) {

                case '"':
                    output.append("\\\"");
                    break;

                case '\\':
                    output.append("\\\\");
                    break;

                case '\n':
                    output.append("\\n");
                    break;

                case '\r':
                    output.append("\\r");
                    break;

                case '\t':
                    output.append("\\t");
                    break;

                default:
                    if (character < 0x20) {
                        output.append(
                            String.format("\\u%04x", (int) character)
                        );
                    } else {
                        output.append(character);
                    }
            }
        }

        return output.toString();
    }
}
