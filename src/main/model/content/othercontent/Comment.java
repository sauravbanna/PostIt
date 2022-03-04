package model.content.othercontent;

import model.content.Content;
import org.json.JSONObject;

// A comment with a username who posted and a body
public class Comment extends Content {

    // CONSTANTS
    public static final String BODY_KEY = "body";

    // FIELDS
    private String body;

    // METHODS

    // Constructor
    // REQUIRES: given username is a registered user on PostIt
    // EFFECTS: creates a comment with the given username posting and the given body
    //          with 0 likes and dislikes
    public Comment(String opName, String body) {
        super(opName);
        this.body = body;
    }

    // EFFECTS: returns the comment's body
    public String getCommentBody() {
        return this.body;
    }

    @Override
    public JSONObject toJson() {
        JSONObject comment = super.toJson();
        comment.put(BODY_KEY, body);
        return comment;
    }

}
