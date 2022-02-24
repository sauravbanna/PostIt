package model.content;

import org.json.JSONObject;
import persistence.Writable;

// The most basic piece of content on PostIt, with just a username, number of likes and dislikes
// This Content can be liked or disliked
public abstract class Content implements Writable {

    // CONSTANTS
    public static final String OP_NAME_KEY = "opName";
    public static final String LIKES_KEY = "likes";
    public static final String DISLIKES_KEY = "dislikes";


    // FIELDS
    private String opName;
    private int likes;
    private int dislikes;

    // METHODS

    // Constructor
    // REQUIRES: given name is a registered user on PostIt
    // EFFECTS: creates a new Post with given poster name
    //          with 0 likes, dislikes and comments
    public Content(String opName) {
        this.opName = opName;
        this.likes = 0;
        this.dislikes = 0;

    }

    // EFFECTS: returns name of poster
    public String getOpName() {
        return opName;
    }

    // EFFECTS: returns number of likes post has
    public int getLikes() {
        return likes;
    }

    // EFFECTS: returns number of dislikes post has
    public int getDislikes() {
        return dislikes;
    }

    // MODIFIES: this
    // EFFECTS: increase the number of likes on this content by 1
    public void like() {
        this.likes++;
    }

    // MODIFIES: this
    // EFFECTS: increase the number of dislikes on this content by 1
    public void dislike() {
        this.dislikes++;
    }

    // REQUIRES: dislikes > 0
    // MODIFIES: this
    // EFFECTS: decrease the number of dislikes on this content by 1
    public void unDislike() {
        this.dislikes--;
    }

    // REQUIRES: likes > 0
    // MODIFIES: this
    // EFFECTS: decrease the number of likes on this content by 1
    public void unLike() {
        this.likes--;
    }

    // REQUIRES: given name is a registered username on PostIt
    // MODIFIES: this
    // EFFECTS: sets the poster name of this content to the given name
    public void setOpName(String opName) {
        this.opName = opName;
    }

    // REQUIRES: given likes >= 0
    // MODIFIES: this
    // EFFECTS: sets the content's likes to the given number
    public void setLikes(int likes) {
        this.likes = likes;
    }

    // REQUIRES: given dislikes >= 0
    // MODIFIES: this
    // EFFECTS: sets the content's dislikes to the given number
    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    @Override
    public JSONObject toJson() {
        JSONObject content = new JSONObject();
        content.put(OP_NAME_KEY, opName);
        content.put(LIKES_KEY, likes);
        content.put(DISLIKES_KEY, dislikes);
        return content;
    }

}
