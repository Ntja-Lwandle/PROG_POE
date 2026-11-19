/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
 * Handles saving and retrieving QuickChat messages using a JSON file
 *
 * Google Gson is used to convert Java Message objects into JSON
 * and to convert JSON data back into Java Message objects.
 * Gson library:
 * https://github.com/google/gson
 *
 *@author lwandle
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
     * Saves the complete stored-message collection.
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
