package assignment1;
import com.fasterxml.jackson.databind.*;

import java.util.*;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        try {
            // JSON string
            String json = "{\"operation\":\"Thinking in Java\",\"word\":\"978-0131872486\"" +
                    ",\"meanings\":[\"Bruce Eckel\", \"CAC\"]}";
            Dictionary dictionary = new Dictionary("dictionary.json");
            //ArrayList meanings = (ArrayList) dictionary.query("socket");

            //System.out.println(Arrays.toString(meanings));

            // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();

            // convert JSON string to Book object
            Request request = mapper.readValue(json, Request.class);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
