package persistence;

import org.json.JSONObject;


// Interface for all information that should be written to file
public interface Writable {
    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: returns the object that method is called on as a JSON Object
    JSONObject toJson();
}
