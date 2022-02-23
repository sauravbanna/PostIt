package persistence;

import model.PostIt;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class JsonWriter {

    // CONSTANTS

    // FIELDS

    private static final int INDENT_FACTOR = 4;
    private PrintWriter writer;
    private String fileLocation;

    // METHODS

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    public JsonWriter(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    public void openWriter() throws FileNotFoundException {
        writer = new PrintWriter(fileLocation);
    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    public void saveForum(PostIt postIt) {
        JSONObject forum = postIt.toJson();
        writer.print(forum.toString(INDENT_FACTOR));
    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    public void close() {
        writer.close();
    }



}
