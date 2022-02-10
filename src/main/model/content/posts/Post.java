package model.content.posts;

import model.content.othercontent.Comment;
import model.content.Content;

import java.util.ArrayList;
import java.util.List;

public abstract class Post extends Content {

    // CONSTANTS

    // FIELDS
    private String title;
    private List<Comment> comments;
    private int commentCount;

    private String community;

    // METHODS

    // REQUIRES: given name is a registered user, given community is a existing community on PostIt
    // EFFECTS: creates a new Post with given poster name and title
    public Post(String opName, String title, String community) {
        super(opName);
        this.title = title;
        this.comments = new ArrayList<>();
        this.commentCount = 0;
        this.community = community;

    }

    // MODIFIES: this
    // EFFECTS: adds given comment to post's list of comments and increase comment count by 1
    public void addComment(Comment c) {
        this.comments.add(c);
        this.commentCount++;
    }

    // EFFECTS: returns number of comments post has
    public int getCommentCount() {
        return commentCount;
    }

    // EFFECTS: returns the title of the post
    public String getTitle() {
        return title;
    }

    // EFFECTS: returns the comments of the post
    public List<Comment> getComments() {
        return comments;
    }

    // EFFECTS: returns the community the post was posted in
    public String getCommunity() {
        return community;
    }

    public abstract String getBody();

}
