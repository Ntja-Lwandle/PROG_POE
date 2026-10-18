/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quickchat;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 *JUint tests for QuickChat Part 2.
 * 
 * @author lwandle
 */
public class MessageTest {
     @Test
    public void testMessageLengthSuccess() {

        String message =
                "Hi Mike, can you join us for dinner tonight?";

        assertEquals(
                "Message ready to send.",
                Message.validateMessage(message)
        );
    }

    @Test
    public void testMessageLengthFailure() {

        String message = "A".repeat(251);

        assertEquals(
                "Message exceeds 250 characters by 1; "
                + "please reduce the size.",
                Message.validateMessage(message)
        );
    }

    @Test
    public void testMessageLengthBoundary() {

        assertEquals(
                "Message ready to send.",
                Message.validateMessage("A".repeat(250))
        );
    }

    @Test
    public void testValidRecipient() {

        Message message = new Message(
                "0012345678",
                0,
                "+27718693002",
                "Hi Mike"
        );

        assertEquals(
                "Cell phone number successfully captured.",
                message.checkRecipientCellMessage()
        );
    }

    @Test
    public void testInvalidRecipient() {

        Message message = new Message(
                "0012345678",
                1,
                "08575975889",
                "Hi Keegan"
        );

        assertEquals(
                Message.INVALID_RECIPIENT,
                message.checkRecipientCellMessage()
        );
    }

    @Test
    public void testMessageHash() {

        Message message = new Message(
                "0012345678",
                0,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertEquals(
                "00:0:HITONIGHT",
                message.createMessageHash()
        );
    }

    @Test
    public void testMessageHashesUsingLoop() {

        String[] messages = {
            "Hi Mike, can you join us for dinner tonight?",
            "Hi Keegan, did you receive the payment?"
        };

        String[] expectedHashes = {
            "00:0:HITONIGHT",
            "00:1:HIPAYMENT"
        };

        for (int i = 0; i < messages.length; i++) {

            Message message = new Message(
                    "0012345678",
                    i,
                    "+27718693002",
                    messages[i]
            );

            assertEquals(
                    expectedHashes[i],
                    message.createMessageHash()
            );
        }
    }

    @Test
    public void testMessageIDGeneration() {

        for (int i = 0; i < 20; i++) {

            String id = Message.generateMessageID();

            assertTrue(id.matches("[0-9]{10}"));
        }
    }

    @Test
    public void testMessageIDValidation() {

        Message valid = new Message(
                "0012345678", 0, "+27718693002", "Hi"
        );

        Message invalid = new Message(
                "12345678901", 0, "+27718693002", "Hi"
        );

        assertTrue(valid.checkMessageID());
        assertFalse(invalid.checkMessageID());
    }

    @Test
    public void testSendMessage() {

        Message message = new Message(
                "0012345678", 0, "+27718693002", "Hi"
        );

        assertEquals(
                "Message successfully sent.",
                message.SentMessage(1)
        );
    }

    @Test
    public void testDisregardMessage() {

        Message message = new Message(
                "0012345678", 0, "+27718693002", "Hi"
        );

        assertEquals(
                "Press 0 to delete the message.",
                message.SentMessage(2)
        );
    }

    @Test
    public void testStoreMessageAction() {

        Message message = new Message(
                "0012345678", 0, "+27718693002", "Hi"
        );

        assertEquals(
                "Message successfully stored.",
                message.SentMessage(3)
        );
    }

    @Test
    public void testTotalMessagesSent() {

        Message message = new Message(
                "0012345678", 0, "+27718693002", "Hi"
        );

        assertEquals(
                1,
                Message.returnTotalMessagess(List.of(message))
        );
    }

    @Test
    public void testPrintMessages() {

        Message message = new Message(
                "0012345678",
                0,
                "+27718693002",
                "Hi"
        );

        String expected =
                "Message ID: 0012345678\n"
                + "Message Hash: 00:0:HIHI\n"
                + "Recipient: +27718693002\n"
                + "Message: Hi";

        assertEquals(expected, message.printMessages());
    }

    @Test
    public void testJsonStorage() throws Exception {

        Path file = Files.createTempFile(
                "quickchat-test", ".json"
        );

        try {

            Message first = new Message(
                    "0012345678",
                    0,
                    "+27718693002",
                    "Hi \"Mike\""
            );

            Message second = new Message(
                    "1112345678",
                    1,
                    "+27718693002",
                    "Thanks"
            );

            first.storeMessage(file);
            second.storeMessage(file);

            String json = Files.readString(file);

            assertTrue(json.startsWith("["));
            assertTrue(json.endsWith("]"));
            assertTrue(json.contains("Hi \\\"Mike\\\""));
            assertTrue(json.contains("1112345678"));

        } finally {

            Files.deleteIfExists(file);
        }
        
    }
@Test
public void testCheckRecipientCellReturnsRequiredMessage() {

    Message valid = new Message(
            "0012345678",
            0,
            "+27718693002",
            "Hi Mike"
    );

    Message invalid = new Message(
            "0012345678",
            1,
            "08575975889",
            "Hi Keegan"
    );

    assertEquals(
            "Cell phone number successfully captured.",
            valid.checkRecipientCell()
    );

    assertEquals(
            Message.INVALID_RECIPIENT,
            invalid.checkRecipientCell()
    );
}   
}
