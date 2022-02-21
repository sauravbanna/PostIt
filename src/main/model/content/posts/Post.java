package model.content.posts;

import model.content.othercontent.Comment;
import model.content.Content;

import java.util.ArrayList;
import java.util.List;

// A subclass of Content with a username who posted, a title, a community that is was posted in,
// an abstract body, a list of comments, and number of likes, dislikes, and comments
public abstract class Post extends Content {

    // CONSTANTS

    // FIELDS
    private String title;
    private List<Comment> comments;
    private int commentCount;
    private int id;

    private String community;

    // METHODS

    // Constructor
    // REQUIRES: given name is a registered user, given community is an existing community on PostIt
    // EFFECTS: creates a new Post with given poster name and title
    public Post(String opName, String title, String community, int id) {
        super(opName);
        this.title = title;
        this.comments = new ArrayList<>();
        this.commentCount = 0;
        this.community = community;
        this.id = id;

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
