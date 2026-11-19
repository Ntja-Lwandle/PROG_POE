package quickchat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for QuickChat part 3
 *
 * Tests message arrays, searching, deletion, JSON storage and message
 * reporting.
 *
 * @author lwandle
 */
public class MessageManagerTest {

    @TempDir
    Path temporaryDirectory;

    private MessageManager manager;

    private Message message1;
    private Message message2;
    private Message message3;
    private Message message4;
    private Message message5;

    @BeforeEach
    public void setUp() throws IOException {

        Path jsonFile
                = temporaryDirectory.resolve("stored-messages.json");

        manager = new MessageManager(jsonFile);

        // Message 1: Sent
        message1 = new Message(
                "1000000001",
                0,
                "Lwandle Ntja",
                "+27834557896",
                "Did you get the cake?"
        );

        // Message 2: Stored
        message2 = new Message(
                "2000000002",
                1,
                "Lwandle Ntja",
                "+27838884567",
                "Where are you? You are late! "
                + "I have asked you to be on time."
        );

        // Message 3: Disregarded
        message3 = new Message(
                "3000000003",
                2,
                "Lwandle Ntja",
                "+27834484567",
                "Yohoooo, I am at your gate."
        );

        // Message 4: Sent
        message4 = new Message(
                "4000000004",
                3,
                "Lwandle Ntja",
                "0838884567",
                "It is dinner time !"
        );

        // Message 5: Stored
        message5 = new Message(
                "5000000005",
                4,
                "Lwandle Ntja",
                "+27838884567",
                "Ok, I am leaving without you."
        );

        manager.addSentMessage(message1);
        manager.addStoredMessage(message2);
        manager.addDisregardedMessage(message3);
        manager.addSentMessage(message4);
        manager.addStoredMessage(message5);
    }

    /**
     * Tests whether the sent array contains the correct messages.
     */
    @Test
    public void testSentMessagesArray() {

        assertEquals(2, manager.getSentMessages().size());

        assertEquals(
                "Did you get the cake?",
                manager.getSentMessages().get(0).getMessage()
        );

        assertEquals(
                "It is dinner time!",
                manager.getSentMessages().get(1).getMessage()
        );
    }

    /**
     * Tests whether disregarded messages are stored in the correct collection.
     */
    @Test
    public void testDisregardedMessagesArray() {

        assertEquals(
                1,
                manager.getDisregardedMessages().size()
        );

        assertEquals(
                "Yohoooo, I am at your gate.",
                manager.getDisregardedMessages()
                        .get(0).getMessage()
        );
    }

    /**
     * Tests whether the stored array is populated.
     */
    @Test
    public void testStoredMessagesArray() {

        assertEquals(2, manager.getStoredMessages().size());

        assertEquals(
                "Where are you? You are late! "
                + "I have asked you to be on time.",
                manager.getStoredMessages().get(0).getMessage()
        );
    }

    /**
     * Tests whether message IDs are recorded.
     */
    @Test
    public void testMessageIDArray() {

        assertEquals(5, manager.getMessageIDs().size());

        assertTrue(
                manager.getMessageIDs().contains("2000000002")
        );
    }

    /**
     * Tests whether message hashes are recorded.
     */
    @Test
    public void testMessageHashArray() {

        assertEquals(5, manager.getMessageHashes().size());

        assertTrue(
                manager.getMessageHashes().contains(
                        message2.createMessageHash()
                )
        );
    }

    /**
     * Tests the longest-message search.
     */
    @Test
    public void testDisplayLongestMessage() {

        Message longest = manager.findLongestStoredMessage();

        assertNotNull(longest);

        assertEquals(
                message2.getMessage(),
                longest.getMessage()
        );
    }

    /**
     * Tests the longest stored message.
     */
    @Test
    public void testLongestStoredMessage() {

        Message longest
                = manager.findLongestStoredMessage();

        assertNotNull(longest);

        assertEquals(
                message2.getMessage(),
                longest.getMessage()
        );
    }

    /**
     * Tests searching for a message using its ID.
     */
    @Test
    public void testSearchMessageByID() {

        Message found
                = manager.searchMessageByID(
                        "2000000002"
                );

        assertNotNull(found);

        assertEquals(
                "+27838884567",
                found.getRecipient()
        );

        assertEquals(
                message2.getMessage(),
                found.getMessage()
        );
    }

    /**
     * Tests searching messages using a recipient number.
     */
    @Test
    public void testSearchMessagesByRecipient() {

        List<Message> results
                = manager.searchMessagesByRecipient(
                        "+27838884567"
                );

        assertEquals(2, results.size());

        assertEquals(
                "Where are you? You are late! "
                + "I have asked you to be on time.",
                results.get(0).getMessage()
        );

        assertEquals(
                "Ok, I am leaving without you.",
                results.get(1).getMessage()
        );
    }

    /**
     * Tests that a message can be deleted using its message hash.
     */
    @Test
    public void testDeleteMessageByHash()
            throws IOException {

        String hash = message2.createMessageHash();

        boolean deleted
                = manager.deleteStoredMessageByHash(hash);

        assertTrue(deleted);

        // Confirm that Message 2 has been removed.
        assertEquals(1, manager.getStoredMessagesArray().length);

        assertNull(
                manager.searchMessageByID("2000000002")
        );

        // Confirm that the message hash has also been removed.
        assertFalse(
                List.of(manager.getMessageHashesArray()).contains(hash)
        );

        // Confirm that the JSON file was updated.
        MessageManager reloadedManager
                = new MessageManager(
                        temporaryDirectory.resolve("stored-messages.json")
                );

        assertNull(
                reloadedManager.searchMessageByID("2000000002")
        );

        assertEquals(
                1,
                reloadedManager.getStoredMessagesArray().length
        );
    }

    /**
     * Tests that an incorrect hash does not delete a message.
     */
    @Test
    public void testDeleteInvalidHash()
            throws IOException {

        boolean deleted
                = manager.deleteStoredMessageByHash(
                        "INVALID:HASH"
                );

        assertFalse(deleted);

        assertEquals(2, manager.getStoredMessages().size());
    }

    /**
     * Tests the stored-message report.
     */
    @Test
    public void testDisplayMessageReport() {

        String report
                = manager.displayStoredMessageReport();

        assertTrue(report.contains("Message ID:"));
        assertTrue(report.contains("Message Hash:"));
        assertTrue(report.contains("Recipient:"));
        assertTrue(report.contains("Message:"));
        assertTrue(report.contains("Sender:"));
        assertTrue(report.contains("Total stored messages: 2"));
    }

    /**
     * Tests the sender and recipient display.
     */
    @Test
    public void testDisplaySendersAndRecipients() {

        String result
                = manager.displayStoredSendersAndRecipients();

        assertTrue(result.contains("Lwandle Ntja"));
        assertTrue(result.contains("+27838884567"));
    }

    /**
     * Tests the total number of sent messages.
     */
    @Test
    public void testTotalMessagesSent() {

        assertEquals(
                2,
                manager.returnTotalMessagesSent()
        );
    }

    /**
     * Tests that stored messages can be retrieved from the JSON file.
     */
    @Test
    public void testReadJSONFile() throws IOException {

        Path jsonFile
                = temporaryDirectory.resolve("stored-messages.json");

        MessageManager loadedManager
                = new MessageManager(jsonFile);

        assertEquals(
                2,
                loadedManager.getStoredMessages().size()
        );

        assertEquals(
                message2.getMessage(),
                loadedManager.getStoredMessages()
                        .get(0).getMessage()
        );
    }

    @Test
    public void testPOEMessageFourSearchValue() {

        Message found
                = manager.searchMessageByID("0838884567");

        assertNotNull(found);

        assertEquals(
                "It is dinner time!",
                found.getMessage()
        );
    }

    @Test
    public void testActualMessageIDSearchAcrossCollections() {

        Message found
                = manager.searchMessageByID(
                        message4.getMessageID()
                );

        assertNotNull(found);

        assertEquals(
                "It is dinner time!",
                found.getMessage()
        );
    }

    /**
     * Tests that all five required arrays are correctly populated.
     */
    @Test
    public void testRequiredArraysArePopulated() {

        // Sent Messages array: Messages 1 and 4.
        Message[] sentArray = manager.getSentMessagesArray();

        assertEquals(2, sentArray.length);

        assertEquals(
                "Did you get the cake?",
                sentArray[0].getMessage()
        );

        assertEquals(
                "It is dinner time!",
                sentArray[1].getMessage()
        );

        // Disregarded Messages array: Message 3.
        Message[] disregardedArray
                = manager.getDisregardedMessagesArray();

        assertEquals(1, disregardedArray.length);

        assertEquals(
                "Yohoooo, I am at your gate.",
                disregardedArray[0].getMessage()
        );

        // Stored Messages array: Messages 2 and 5.
        Message[] storedArray = manager.getStoredMessagesArray();

        assertEquals(2, storedArray.length);

        assertEquals(
                message2.getMessage(),
                storedArray[0].getMessage()
        );

        assertEquals(
                message5.getMessage(),
                storedArray[1].getMessage()
        );

        // Message Hash array.
        String[] hashArray = manager.getMessageHashesArray();

        assertEquals(5, hashArray.length);

        assertTrue(
                List.of(hashArray).contains(
                        message1.createMessageHash()
                )
        );

        assertTrue(
                List.of(hashArray).contains(
                        message2.createMessageHash()
                )
        );

        assertTrue(
                List.of(hashArray).contains(
                        message3.createMessageHash()
                )
        );

        assertTrue(
                List.of(hashArray).contains(
                        message4.createMessageHash()
                )
        );

        assertTrue(
                List.of(hashArray).contains(
                        message5.createMessageHash()
                )
        );

        // Message ID array.
        String[] idArray = manager.getMessageIDsArray();

        assertEquals(5, idArray.length);

        assertTrue(
                List.of(idArray).contains(message1.getMessageID())
        );

        assertTrue(
                List.of(idArray).contains(message2.getMessageID())
        );

        assertTrue(
                List.of(idArray).contains(message3.getMessageID())
        );

        assertTrue(
                List.of(idArray).contains(message4.getMessageID())
        );

        assertTrue(
                List.of(idArray).contains(message5.getMessageID())
        );
    }

    @Test
    public void testReportIncludesSentAndStoredMessageDetails() {

        String report
                = manager.displayStoredMessageReport();

        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("It is dinner time!"));
        assertTrue(report.contains("Where are you? You are late!"));

        assertTrue(report.contains("Message Hash:"));
        assertTrue(report.contains("Recipient:"));
    }

    /**
     * Tests that the arrays update when a new message is added.
     */
    @Test
    public void testArraysUpdateAfterAddingMessage() {

        Message additionalMessage = new Message(
                "6000000006",
                5,
                "Lwandle Ntja",
                "+27831112222",
                "This is an additional test message."
        );

        manager.addSentMessage(additionalMessage);

        assertEquals(
                3,
                manager.getSentMessagesArray().length
        );

        assertEquals(
                6,
                manager.getMessageIDsArray().length
        );

        assertEquals(
                6,
                manager.getMessageHashesArray().length
        );

        assertTrue(
                List.of(manager.getMessageIDsArray()).contains(
                        "6000000006"
                )
        );
    }

}
