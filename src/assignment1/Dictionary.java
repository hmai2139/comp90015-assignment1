/* Dictionary representation. */

package assignment1;

// Dependencies.
import java.util.*;
import java.nio.file.Paths;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;

/*
** Handle operations on input dictionary (a JSON file).
 */
public class Dictionary {

    // All words in the dictionary.
    public static Map<String, String[]> words;
    
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

    // Get all words of Dictionary.
    public synchronized Map<String, String[]> allWords () {
        return words;
    }

    // Query the meanings of a given word.
    public synchronized Object query(String word) {
        if (words.containsKey(word)) {
            String[] meanings = words.get(word);
            return meanings;
        }
        return("No such word exists.");
    }

    // Query the meanings of a given word.
    public synchronized Object meaningsOf(String word) throws Exception {

        // Word doesn't exist in dictionary.
        if ( !words.containsKey(word) ) {
            throw new Exception("No such word exists.");
        }
        return words.get(word);
    }

    // Add a new word to dictionary.
    public synchronized String add(String word, String[] meanings) throws Exception {

        // Word already exists in dictionary.
        if ( words.containsKey(word) ) {
            return("This word already exists.");
        }

        // No meanings provided.
        if ( meanings.length == 0 || meanings == null) {
            return("No meanings provided.");
        }

        words.put(word, meanings);
        return("Success.");
    }

    // Remove a word from dictionary.
    public synchronized String remove(String word) throws Exception {

        // Word doesn't exist in dictionary.
        if ( !words.containsKey(word) ) {
            return("No such word exists.");
        }

        words.remove(word);
        return("Success.");
    }

    // Update a word's meanings.
    public synchronized String update(String word, String[] meanings) throws Exception {

        // Word doesn't exist in dictionary.
        if ( !words.containsKey(word) ) {
            return("No such word exists.");
        }

        // No new meanings are provided.
        if (meanings.length == 0 || meanings == null) {
            return("No meanings provided.");
        }

        words.put(word, meanings);
        return("Success.");
    }
}
