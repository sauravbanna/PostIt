package persistence;

// CITATION
// JsonSerializationDemo from CPSC 210

import model.Community;
import model.PostIt;
import model.User;
import org.json.JSONObject;

import java.io.IOException;

public class JsonReader {

    // CONSTANTS

    // FIELDS
    private String fileLocation;

    // METHODS

    public JsonReader(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public PostIt read() throws IOException {
        String data = readFile(fileLocation);
        JSONObject forum = new JSONObject(data);
        return readPostIt(forum);
    }

    private String readFile(String source) throws IOException {

    }

    private PostIt readPostIt(JSONObject jsonObject) {

    }


    private PostIt addCommunities() {
        return null;
    }

    private PostIt addCommunity() {
        return null;
    }

    private PostIt addUsers() {
        return null;
    }

    private PostIt addUser() {
        return null;
    }

    private PostIt addPosts() {
        return null;
    }

    private PostIt addPost() {
        return null;
    }

}
