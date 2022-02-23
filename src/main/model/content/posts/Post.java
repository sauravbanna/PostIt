package model.content.posts;

import com.sun.xml.internal.bind.v2.model.core.ID;
import model.content.othercontent.Comment;
import model.content.Content;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



// A subclass of Content with a username who posted, a title, a community that is was posted in, a unique id number
// an abstract body, a list of comments, and number of likes, dislikes, and comments
public abstract class Post extends Content {

    // CONSTANTS
    public static final String TITLE_KEY = "title";
    public static final String COMMENTS_KEY = "comments";
    public static final String COMMENT_COUNT_KEY = "commentCount";
    public static final String COMMUNITY_KEY = "community";
    public static final String ID_KEY = "id";


    // FIELDS
    private String title;
    private List<Comment> comments;
    private int commentCount;
    private int id;

    private String community;

    // METHODS

    // Constructor
    // REQUIRES: given name is a registered user, given community is an existing community on PostIt, given id
    //           is not already assigned to another post on PostIt
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

    // EFFECTS: returns the unique id of this post
    public int getId() {
        return id;
    }

    // EFFECTS: returns the body of the post
    public abstract String getBody();

    @Override
    public JSONObject toJson() {
        JSONObject post = super.toJson();
        post.put(TITLE_KEY, title);
        post.put(COMMENTS_KEY, commentsToJson());
        post.put(COMMENT_COUNT_KEY, commentCount);
        post.put(COMMUNITY_KEY, community);
        post.put(ID_KEY, id);
        return post;
    }

    private JSONArray commentsToJson() {
        JSONArray comments = new JSONArray();

        for (Comment c : this.comments) {
            comments.put(c.toJson());
        }

        return comments;

    }
}
