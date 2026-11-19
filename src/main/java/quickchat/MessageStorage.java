package quickchat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves the stored messages to a JSON file and reads them back into a list.
 *
 * The Gson library is used to change Message objects to JSON and back.
 * Reference: Google (n.d.) Gson User Guide. Available at:
 * https://github.com/google/gson/blob/main/UserGuide.md
 * (Accessed: 10 October 2026).
 *
 * @author lwandle
 */
public class MessageStorage {

    private final Gson gson;
    private final Path filePath;

    public MessageStorage(Path filePath) {

        this.filePath = filePath;

        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    /**
     * Writes all the stored messages to the JSON file.
     */
    public void saveMessages(List<Message> messages)
            throws IOException {

        String json = gson.toJson(messages);

        Files.writeString(filePath, json);
    }

    /**
     * Reads messages from the JSON file.
     *
     * If the file does not exist, an empty list is returned.
     */
    public List<Message> loadMessages() throws IOException {

        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        String json = Files.readString(filePath);

        if (json.isBlank()) {
            return new ArrayList<>();
        }

        Type messageListType
                = new TypeToken<ArrayList<Message>>() {
                }.getType();

        List<Message> messages
                = gson.fromJson(json, messageListType);

        if (messages == null) {
            return new ArrayList<>();
        }

        return messages;
    }
}
