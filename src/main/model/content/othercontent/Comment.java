package model.content.othercontent;

import model.content.Content;

public class Comment extends Content {

    // CONSTANTS

    // FIELDS
    private String body;

    // METHODS

    // REQUIRES: given username is a registered user
    // EFFECTS: creates a comment with the given username posting and the given body
    public Comment(String opName, String body) {
        super(opName);
        this.body = body;
    }

    // EFFECTS: returns the comment's body
    public String getCommentBody() {
        return this.body;
    }

}
