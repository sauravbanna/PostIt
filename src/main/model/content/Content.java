package model.content;

import java.util.ArrayList;
import java.util.List;

public abstract class Content {

    // CONSTANTS



    // FIELDS
    private String opName;
    private int likes;
    private int dislikes;

    // METHODS

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

}
