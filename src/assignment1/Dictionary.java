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
    private static Map<String, String[]> words;
    
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

    // Check if a given word already exists in the Dictionary.
    public synchronized boolean contains(String word) {
        return words.containsKey(word);
    }

    // Query the meanings of a given word.
    public synchronized String[] meaningsOf(String word) throws Exception {

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
            throw new Exception("This word already exists.");
        }

        words.put(word, meanings);
        return ("Success.");
    }

    // Remove a word from dictionary.
    public synchronized String remove(String word) throws Exception {

        // Word doesn't exist in dictionary.
        if ( !words.containsKey(word) ) {
            throw new Exception("No such word exists.");
        }

        words.remove(word);
        return ("Success.");
    }

    // Update a word's meanings.
    public synchronized String remove(String word, String[] meanings) throws Exception {

        // Word doesn't exist in dictionary.
        if ( !words.containsKey(word) ) {
            throw new Exception("No such word exists.");
        }

        // No new meanings are provided.
        if (meanings.length == 0 || meanings == null) {
            throw new Exception("No meanings provided.");
        }

        words.put(word, meanings);
        return ("Success.");
    }
}

/*
** Representation of a word in dictionary.
 */
class Word {

    // Word and its meanings.
    private String word;
    private String[] meanings;

    // Class constructor.
    public Word (String word, String[] meanings) {
        this.word = word;
        this.meanings = meanings;
    }

    // Default constructor.
    public Word() {
    }

    // Get word.
    public String getWord() {
        return this.word;
    }

    // Get meanings of word.
    public String[] getMeanings() {
        return this.meanings;
    }

    // Set meanings of word.
    public void setMeanings(String[] newMeanings) {
        this.meanings = newMeanings;
    }
}