package model.content.posts;

public class TextPost extends Post {

    // CONSTANTS

    // FIELDS
    private String postBody;

    // METHODS

    // REQUIRES: given name is a registered user, given community is an existing community on PostIt
    // EFFECTS: creates a new Text Post with given poster name, title and body
    public TextPost(String opName, String title, String body, String community) {
        super(opName, title, community);
        this.postBody = body;
    }

    // EFFECTS: returns the text post's body
    public String getBody() {
        return postBody;
    }

}
