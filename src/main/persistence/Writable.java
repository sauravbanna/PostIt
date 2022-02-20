package persistence;

import org.json.JSONObject;

// CITATION
// JsonSerializationDemo from CPSC 210

// Interface for all information that should be written to file
public interface Writable {
    // EFFECTS: returns the object that method is called on as a JSON Object
    JSONObject toJson();
}
