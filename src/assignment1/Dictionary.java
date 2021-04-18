/* Dictionary representation. */

package assignment1;

// Dependencies.
import java.util.*;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.*;

/*
** Handle operations on input dictionary (a JSON file).
 */
public class Dictionary {

    // All words in the dictionary.
    public static Map<String, ArrayList<String>> words;
    
    // Class constructor.
    public Dictionary(String source) {
        try {
            // Create object mapper instance.
            ObjectMapper mapper = new ObjectMapper();

            // Convert JSON dictionary file to a Map in form of Map<Word, Meanings>.
            words = mapper.readValue(Paths.get(source).toFile(), Map.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Query the meanings of a given word.
    public synchronized String query(String word) {
        word = word.toLowerCase();

        if ( words.containsKey(word) ) {

            // Convert meanings of word (an ArrayList<String>) to String form so we can return.
            ArrayList<String> meanings = words.get(word);
            String reply = "";
            for (Object meaning: meanings) {
                reply += (String) meaning +"\n";
            }
            return reply;
        }
        return("No such word exists.");
    }

    // Add a new word to dictionary.
    public synchronized String add(String word, ArrayList<String> meanings) throws Exception {
        word = word.toLowerCase();

        // Word already exists in dictionary.
        if ( words.containsKey(word) ) {
            return("This word already exists.");
        }

        // No meanings provided.
        if ( meanings == null || meanings.size() == 0 ) {
            return("No meanings provided.");
        }

        words.put(word, meanings);
        return("Success.");
    }

    // Remove a word from dictionary.
    public synchronized String remove(String word) throws Exception {
        word = word.toLowerCase();

        // Word doesn't exist in dictionary.
        if ( !words.containsKey(word) ) {
            return("No such word exists.");
        }

        words.remove(word);
        return("Success.");
    }

    // Update a word's meanings.
    public synchronized String update(String word, ArrayList<String> meanings) throws Exception {
        word = word.toLowerCase();

        // Word doesn't exist in dictionary.
        if ( !words.containsKey(word) ) {
            return("No such word exists.");
        }

        // No new meanings are provided.
        if (meanings == null || meanings.size() == 0) {
            return("No meanings provided.");
        }

        words.put(word, meanings);
        return("Success.");
    }
}
