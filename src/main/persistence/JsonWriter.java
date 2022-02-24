package persistence;

import model.PostIt;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Represents a writer that writes JSON representation of the PostIt forum to file
public class JsonWriter {

    // CONSTANTS

    // FIELDS

    private static final int INDENT_FACTOR = 4;
    private PrintWriter writer;
    private String fileLocation;

    // METHODS

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void openWriter() throws FileNotFoundException {
        writer = new PrintWriter(fileLocation);
    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // MODIFIES: this
    // EFFECTS: writes JSON representation of PostIt forum to file
    public void saveForum(PostIt postIt) {
        JSONObject forum = postIt.toJson();
        writer.print(forum.toString(INDENT_FACTOR));
    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }
}
