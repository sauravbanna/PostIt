package model.content.posts;

import org.json.JSONObject;

// A Post with a text-based body, and all the other information a Post has such as
// community name, user name, title, number of likes, dislikes, comments, id number, and list of comments
public class TextPost extends Post {

    // CONSTANTS
    public static final String TEXT_BODY_KEY = "textBody";


    // FIELDS
    private String postBody;

    // METHODS

    // Constructor
    // REQUIRES: given name is a registered user, given community is an existing community on PostIt, given id
    //          is not already assigned to another post on PostIt
    // EFFECTS: creates a new Text Post with given poster name, title, id, and body in the given community
    public TextPost(String opName, String title, String body, String community, int id) {
        super(opName, title, community, id);
        this.postBody = body;
    }

    // EFFECTS: returns the text post's body
    public String getBody() {
        return postBody;
    }

    @Override
    public JSONObject toJson() {
        JSONObject textPost = super.toJson();
        textPost.put(TEXT_BODY_KEY, postBody);
        return textPost;
    }

}
