package persistence;

import model.PostIt;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;


// CITATION
// JsonSerializationDemo from CPSC 210

public class JsonWriter {

    // CONSTANTS

    // FIELDS

    private static final int INDENT_FACTOR = 4;
    private PrintWriter writer;
    private String fileLocation;

    // METHODS

    public JsonWriter(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public void openWriter() throws FileNotFoundException {
        writer = new PrintWriter(fileLocation);
    }

    public void saveForum(PostIt postIt) {
        JSONObject forum = postIt.toJson();
        writer.print(forum.toString(INDENT_FACTOR));
    }

    public void close() {
        writer.close();
    }



}
