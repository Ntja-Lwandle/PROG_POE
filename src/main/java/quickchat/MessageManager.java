package quickchat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Keeps the sent, disregarded and stored messages. It also builds the arrays
 * of message hashes and message IDs and handles searching, deleting and the
 * report. Stored messages are saved in a JSON file.
 *
 * @author lwandle
 */
public class MessageManager {

    private final List<Message> sentMessages = new ArrayList<>();
    private final List<Message> disregardedMessages = new ArrayList<>();
    private final List<Message> storedMessages = new ArrayList<>();

    private final MessageStorage storage;

    /**
     * Loads any messages that were stored in the JSON file earlier.
     */
    public MessageManager(Path filePath) throws IOException {
        storage = new MessageStorage(filePath);
        storedMessages.addAll(storage.loadMessages());
    }

    public void addSentMessage(Message message) {
        sentMessages.add(message);
    }

    public void addDisregardedMessage(Message message) {
        disregardedMessages.add(message);
    }

    /**
     * Adds the message and saves it to the JSON file. If saving fails, the
     * message is removed again so the list matches the file.
     */
    public void addStoredMessage(Message message) throws IOException {
        storedMessages.add(message);
        try {
            saveStoredMessages();
        } catch (IOException error) {
            storedMessages.remove(message);
            throw error;
        }
    }

    public void saveStoredMessages() throws IOException {
        storage.saveMessages(storedMessages);
    }

    // ----- Arrays required for Part 3 -----

    public Message[] getSentMessagesArray() {
        return sentMessages.toArray(new Message[0]);
    }

    public Message[] getDisregardedMessagesArray() {
        return disregardedMessages.toArray(new Message[0]);
    }

    public Message[] getStoredMessagesArray() {
        return storedMessages.toArray(new Message[0]);
    }

    /**
     * Returns the hashes of every sent, disregarded and stored message.
     */
    public String[] getMessageHashesArray() {
        List<String> hashes = new ArrayList<>();
        for (Message message : allMessages()) {
            String hash = message.createMessageHash();
            if (!hashes.contains(hash)) {
                hashes.add(hash);
            }
        }
        return hashes.toArray(new String[0]);
    }

    /**
     * Returns the IDs of every sent, disregarded and stored message.
     */
    public String[] getMessageIDsArray() {
        List<String> ids = new ArrayList<>();
        for (Message message : allMessages()) {
            if (!ids.contains(message.getMessageID())) {
                ids.add(message.getMessageID());
            }
        }
        return ids.toArray(new String[0]);
    }

    public List<Message> getSentMessages() {
        return new ArrayList<>(sentMessages);
    }

    public List<Message> getDisregardedMessages() {
        return new ArrayList<>(disregardedMessages);
    }

    public List<Message> getStoredMessages() {
        return new ArrayList<>(storedMessages);
    }

    public List<String> getMessageHashes() {
        return List.of(getMessageHashesArray());
    }

    public List<String> getMessageIDs() {
        return List.of(getMessageIDsArray());
    }

    // ----- Stored Messages menu features -----

    /**
     * Option a: shows the sender and recipient of every stored message.
     */
    public String displayStoredSendersAndRecipients() {
        if (storedMessages.isEmpty()) {
            return "No stored messages found.";
        }

        String result = "";
        for (Message message : storedMessages) {
            result = result + message.printSenderAndRecipient()
                    + "\n--------------------\n";
        }
        return result;
    }

    /**
     * Option b: finds the longest stored message (null if there are none).
     */
    public Message findLongestStoredMessage() {
        Message longest = null;
        for (Message message : storedMessages) {
            if (longest == null
                    || message.getMessage().length() > longest.getMessage().length()) {
                longest = message;
            }
        }
        return longest;
    }

    /**
     * Option c: finds a message by its message ID (null if not found).
     *
     * The POE test data searches with a recipient number
     * ("0838884567"), so if no ID matches, the recipient is checked too.
     */
    public Message searchMessageByID(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        for (Message message : allMessages()) {
            if (id.equals(message.getMessageID())) {
                return message;
            }
        }
        for (Message message : allMessages()) {
            if (id.equals(message.getRecipient())) {
                return message;
            }
        }
        return null;
    }

    /**
     * Option d: finds all sent and stored messages for one recipient.
     */
    public List<Message> searchMessagesByRecipient(String recipient) {
        List<Message> results = new ArrayList<>();
        if (recipient == null || recipient.isBlank()) {
            return results;
        }
        for (Message message : sentMessages) {
            if (recipient.equals(message.getRecipient())) {
                results.add(message);
            }
        }
        for (Message message : storedMessages) {
            if (recipient.equals(message.getRecipient())) {
                results.add(message);
            }
        }
        return results;
    }

    /**
     * Finds a stored message by its hash (null if not found).
     */
    public Message findStoredMessageByHash(String hash) {
        for (Message message : storedMessages) {
            if (message.createMessageHash().equals(hash)) {
                return message;
            }
        }
        return null;
    }

    /**
     * Option e: deletes a stored message by its hash and updates the JSON
     * file. Returns false if no stored message has that hash.
     */
    public boolean deleteStoredMessageByHash(String hash) throws IOException {
        Message message = findStoredMessageByHash(hash);
        if (message == null) {
            return false;
        }

        int position = storedMessages.indexOf(message);
        storedMessages.remove(position);
        try {
            saveStoredMessages();
        } catch (IOException error) {
            storedMessages.add(position, message);
            throw error;
        }
        return true;
    }

    /**
     * Option f: report with the full details of the sent and stored messages.
     */
    public String displayStoredMessageReport() {
        if (sentMessages.isEmpty() && storedMessages.isEmpty()) {
            return "No sent or stored messages available for the report.";
        }

        String report = "\n========== SENT MESSAGE REPORT ==========\n"
                + reportSection(sentMessages)
                + "\n========== STORED MESSAGE REPORT =========\n"
                + reportSection(storedMessages)
                + "Total sent messages: " + sentMessages.size()
                + "\nTotal stored messages: " + storedMessages.size();
        return report;
    }

    private String reportSection(List<Message> messages) {
        if (messages.isEmpty()) {
            return "No messages in this section.\n";
        }

        String section = "";
        for (Message message : messages) {
            section = section + "Message ID: " + message.getMessageID()
                    + "\nMessage Hash: " + message.createMessageHash()
                    + "\nSender: " + message.getSender()
                    + "\nRecipient: " + message.getRecipient()
                    + "\nMessage: " + message.getMessage()
                    + "\n------------------------------------------\n";
        }
        return section;
    }

    public int returnTotalMessagesSent() {
        return sentMessages.size();
    }

    /**
     * Combines the three lists so they can be searched together.
     */
    private List<Message> allMessages() {
        List<Message> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(disregardedMessages);
        all.addAll(storedMessages);
        return all;
    }
}
