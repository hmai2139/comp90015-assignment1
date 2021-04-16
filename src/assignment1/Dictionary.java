/**
 * Dictionary representation.
 */

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

    // List of words in the dictionary.
    private static List<Word> words;

    // Main class.
    public static void main(String[] args) {

        // Get dictionary from STDIN.
        final String source = args[0];

        // Convert JSON array in dictionary file to a List of Word objects.
        try {
            // Create object mapper instance.
            ObjectMapper mapper = new ObjectMapper();

            // Convert JSON array in dictionary file to a List of Word objects.
            List<Word> words = Arrays.asList(mapper.readValue(Paths.get("dictionary.json").toFile(), Word[].class));

            // Print words
            for (Word word: words) {
                String currentWord = word.getWord();
                String[] meanings = word.getMeanings();
                System.out.println(currentWord + ":\n" + Arrays.toString(meanings));
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Class constructor.
    public Dictionary(String source) {
        try {
            // Create object mapper instance.
            ObjectMapper mapper = new ObjectMapper();

            // Convert JSON array in dictionary file to a List of Word objects.
            List<Word> words = Arrays.asList(mapper.readValue(Paths.get("dictionary.json").toFile(), Word[].class));

            // Print words
            for (Word word: words) {
                String currentWord = word.getWord();
                String[] meanings = word.getMeanings();
                System.out.println(currentWord + ":\n" + Arrays.toString(meanings));
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
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

    // Dummy constructor.
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