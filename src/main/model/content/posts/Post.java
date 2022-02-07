package model.content.posts;

import model.content.Comment;
import model.content.Content;

import java.util.ArrayList;
import java.util.List;

public class Post extends Content {

    // CONSTANTS

    // FIELDS
    private String title;
    private List<Comment> comments;

    // METHODS

    // REQUIRES: given name is a registered user
    // EFFECTS: creates a new Post with given poster name and title
    public Post(String opName, String title) {
        super(opName);
        this.title = title;
        this.comments = new ArrayList<Comment>();

    }

    // MODIFIES: this
    // EFFECTS: adds given comment to post's list of comments
    public void addComment(Comment c) {

    }

}
