/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quickchat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages sent, disregarded and stored messages. Also maintains the arrays of
 * message hashes and IDs.
 *
 * @author lwandle
 */
public class MessageManager {

    private final List<Message> sentMessages = new ArrayList<>();
    private final List<Message> disregardedMessages = new ArrayList<>();
    private final List<Message> storedMessages = new ArrayList<>();

    private final List<String> messageHashes = new ArrayList<>();
    private final List<String> messageIDs = new ArrayList<>();

    private Message[] sentMessageArray = new Message[0];
    private Message[] disregardedMessageArray = new Message[0];
    private Message[] storedMessageArray = new Message[0];
    private String[] messageHashArray = new String[0];
    private String[] messageIDArray = new String[0];

    private final MessageStorage storage;

    public MessageManager(Path filePath) throws IOException {
        storage = new MessageStorage(filePath);
        storedMessages.addAll(storage.loadMessages());
        refreshTrackingInformation();
    }

    public void addSentMessage(Message message) {
        sentMessages.add(message);
        refreshTrackingInformation();
    }

    public void addDisregardedMessage(Message message) {
        disregardedMessages.add(message);
        refreshTrackingInformation();
    }

    public void addStoredMessage(Message message) throws IOException {
        storedMessages.add(message);

        try {
            saveStoredMessages();
            refreshTrackingInformation();
        } catch (IOException ex) {
            storedMessages.remove(storedMessages.size() - 1);
            refreshTrackingInformation();
            throw ex;
        }
    }

    /**
     * Updates all five arrays after a message is added or deleted.
     */
    private void refreshTrackingInformation() {

        messageHashes.clear();
        messageIDs.clear();

        addTrackingFor(sentMessages);
        addTrackingFor(disregardedMessages);
        addTrackingFor(storedMessages);

        sentMessageArray
                = sentMessages.toArray(new Message[0]);

        disregardedMessageArray
                = disregardedMessages.toArray(new Message[0]);

        storedMessageArray
                = storedMessages.toArray(new Message[0]);

        messageHashArray
                = messageHashes.toArray(new String[0]);

        messageIDArray
                = messageIDs.toArray(new String[0]);
    }

    private void addTrackingFor(List<Message> messages) {

        for (Message message : messages) {

            if (!messageIDs.contains(message.getMessageID())) {
                messageIDs.add(message.getMessageID());
            }

            String hash = message.createMessageHash();

            if (!messageHashes.contains(hash)) {
                messageHashes.add(hash);
            }
        }
    }

    public void saveStoredMessages() throws IOException {
        storage.saveMessages(storedMessages);
    }

    public List<Message> getSentMessages() {
        return Collections.unmodifiableList(sentMessages);
    }

    public List<Message> getDisregardedMessages() {
        return Collections.unmodifiableList(disregardedMessages);
    }

    public List<Message> getStoredMessages() {
        return Collections.unmodifiableList(storedMessages);
    }

    public List<String> getMessageHashes() {
        return Collections.unmodifiableList(messageHashes);
    }

    public List<String> getMessageIDs() {
        return Collections.unmodifiableList(messageIDs);
    }

    // Return copies so callers cannot directly change the stored arrays.
    public Message[] getSentMessagesArray() {
        return sentMessageArray.clone();
    }

    public Message[] getDisregardedMessagesArray() {
        return disregardedMessageArray.clone();
    }

    public Message[] getStoredMessagesArray() {
        return storedMessageArray.clone();
    }

    public String[] getMessageHashesArray() {
        return messageHashArray.clone();
    }

    public String[] getMessageIDsArray() {
        return messageIDArray.clone();
    }

    /**
     * Displays the sender and recipient of every stored message.
     *
     * @return the sender and recipient details
     */
    public String displayStoredSendersAndRecipients() {

        if (storedMessageArray.length == 0) {
            return "No stored messages found.";
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < storedMessageArray.length; i++) {

            result.append(
                    storedMessageArray[i].printSenderAndRecipient()
            );

            result.append("\n--------------------\n");
        }

        return result.toString();
    }

    /**
     * Finds the longest message in the stored-message array.
     *
     * @return the longest stored message, or null if the array is empty
     */
    public Message findLongestStoredMessage() {

        Message longestMessage = null;

        for (int i = 0; i < storedMessageArray.length; i++) {

            if (longestMessage == null
                    || storedMessageArray[i].getMessage().length()
                    > longestMessage.getMessage().length()) {

                longestMessage = storedMessageArray[i];
            }
        }

        return longestMessage;
    }

    public Message findLongestMessage() {

        List<Message> all = new ArrayList<>();

        all.addAll(sentMessages);
        all.addAll(disregardedMessages);
        all.addAll(storedMessages);

        return findLongest(all);
    }

    private Message findLongest(List<Message> messages) {

        Message longest = null;

        for (Message message : messages) {

            if (longest == null
                    || message.getMessage().length()
                    > longest.getMessage().length()) {

                longest = message;
            }
        }

        return longest;
    }

    /**
     * Searches stored messages using a message ID.
     *
     * @param id the ID of the message to search for
     * @return the matching stored message, or null if not found
     */
    public Message searchStoredMessageByID(String id) {

        if (id == null || id.isBlank()) {
            return null;
        }

        for (int i = 0; i < storedMessageArray.length; i++) {

            if (id.equals(storedMessageArray[i].getMessageID())) {
                return storedMessageArray[i];
            }
        }

        return null;
    }

    /**
     * Searches all messages using a message ID. If no ID matches, searches
     * using the recipient number.
     *
     * @param id the message ID or recipient number
     * @return the matching message, or null if not found
     */
    public Message searchMessageByID(String id) {

        if (id == null || id.isBlank()) {
            return null;
        }

        // Search all messages using the actual message ID.
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
     * Searches sent and stored messages for a particular recipient.
     */
    public List<Message> searchMessagesByRecipient(String recipient) {

        List<Message> results = new ArrayList<>();

        if (recipient == null || recipient.isBlank()) {
            return results;
        }

        // Search sent messages.
        for (Message message : sentMessageArray) {

            if (recipient.equals(message.getRecipient())) {
                results.add(message);
            }
        }

        // Search stored messages.
        for (Message message : storedMessageArray) {

            if (recipient.equals(message.getRecipient())) {
                results.add(message);
            }
        }

        return results;
    }

    /**
     * Finds a stored message using its message hash.
     *
     * @param hash the hash to search for
     * @return the matching message, or null if not found
     */
    public Message findStoredMessageByHash(String hash) {

        if (hash == null || hash.isBlank()) {
            return null;
        }

        for (int i = 0; i < storedMessageArray.length; i++) {

            if (storedMessageArray[i]
                    .createMessageHash().equals(hash)) {

                return storedMessageArray[i];
            }
        }

        return null;
    }

    /**
     * Deletes a stored message using its message hash.
     */
    public boolean deleteStoredMessageByHash(String hash)
            throws IOException {

        for (int i = 0; i < storedMessages.size(); i++) {

            Message message = storedMessages.get(i);

            if (message.createMessageHash().equals(hash)) {

                storedMessages.remove(i);

                try {
                    saveStoredMessages();
                } catch (IOException ex) {
                    storedMessages.add(i, message);
                    throw ex;
                }

                refreshTrackingInformation();
                return true;
            }
        }

        return false;
    }

    /**
     * Displays sent and stored reports.
     */
    public String displayStoredMessageReport() {

        StringBuilder report = new StringBuilder();

        report.append("\n========== SENT MESSAGE REPORT ==========\n");
        appendReportSection(report, sentMessages);

        report.append("\n========== STORED MESSAGE REPORT =========\n");
        appendReportSection(report, storedMessages);

        report.append("Total sent messages: ")
                .append(sentMessages.size());

        report.append("\nTotal stored messages: ")
                .append(storedMessages.size());

        if (sentMessages.isEmpty() && storedMessages.isEmpty()) {
            return "No sent or stored messages available for the report.";
        }

        return report.toString();
    }

    private void appendReportSection(
            StringBuilder report,
            List<Message> messages) {

        if (messages.isEmpty()) {
            report.append("No messages in this section.\n");
            return;
        }

        for (Message message : messages) {

            report.append("Message ID: ")
                    .append(message.getMessageID());

            report.append("\nMessage Hash: ")
                    .append(message.createMessageHash());

            report.append("\nSender: ")
                    .append(message.getSender());

            report.append("\nRecipient: ")
                    .append(message.getRecipient());

            report.append("\nMessage: ")
                    .append(message.getMessage());

            report.append("\n------------------------------------------\n");
        }
    }

    public int returnTotalMessagesSent() {
        return sentMessages.size();
    }

    private List<Message> allMessages() {

        List<Message> all = new ArrayList<>();

        all.addAll(sentMessages);
        all.addAll(disregardedMessages);
        all.addAll(storedMessages);

        return all;
    }
}
