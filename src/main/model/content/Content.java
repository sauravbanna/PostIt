package model.content;

import java.util.ArrayList;
import java.util.List;

public abstract class Content {

    // CONSTANTS



    // FIELDS
    private String opName;
    private int likes;
    private int dislikes;
    private int commentCount;

    // METHODS

    // REQUIRES: given name is a registered user
    // EFFECTS: creates a new Post with given poster name
    //          with 0 likes, dislikes and comments
    public Content(String opName) {
        this.opName = opName;
        this.likes = 0;
        this.dislikes = 0;
        this.commentCount = 0;

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

    // EFFECTS: returns number of comments post has
    public int getCommentCount() {
        return commentCount;
    }

    // MODIFIES: this
    // EFFECTS: increase the number of likes on this content by 1
    public void like() {

    }

    // MODIFIES: this
    // EFFECTS: increase the number of dislikes on this content by 1
    public void dislike() {

    }
}
