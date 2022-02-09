package model.content.posts;

public class TextPost extends Post {

    // CONSTANTS

    // FIELDS
    private String postBody;

    // METHODS

    // REQUIRES: given name is a registered user
    // EFFECTS: creates a new Text Post with given poster name, title and body
    public TextPost(String opName, String title, String body) {
        super(opName, title);
        this.postBody = body;
    }

    // EFFECTS: returns the post's body
    public String getBody() {
        return postBody;
    }

}
