package ui;

import model.content.othercontent.Comment;
import model.content.posts.Post;

import java.util.LinkedList;
import java.util.List;

public abstract class Feed {

    // CONSTANTS 
    
    public final String NEXT_COMMAND = "/n";
    public final String LIKE_COMMAND = "/l";
    public final String DISLIKE_COMMAND = "/d";
    public final String COMMENT_COMMAND = "/c";
    public final String VIEW_COMMENTS_COMMAND = "/vc";
    public final String HELP_COMMAND = "/help";
    public final String BACK_COMMAND = "/b";
    public final String EXIT_COMMAND = "/exit";
    public final String VIEW_COMMUNITY_COMMAND = "/view";
    public final String MAKE_POST_COMMAND = "/post";
    public final String LOGOUT_COMMAND = "/logout";
    public final String SEARCH_COMMAND = "/search";
    public final String LOGIN_COMMAND = "/login";
    public final String HOME_COMMAND = "/home";
    public final String SORT_COMMAND = "/sort";

    public final String NEW_SORT = "NEW";
    public final String TOP_SORT = "TOP";
    public final String COMMENT_SORT = "COMMENT";
    public final String DISLIKE_SORT = "DISLIKE";

    // FIELDS
    protected LinkedList<Post> userFeed;
    protected String sort;
    protected Boolean userFeedActive;
    
    // METHODS
    
    public Feed(LinkedList<Post> postList) {
        userFeed = postList;
        sort = NEW_SORT;
        userFeedActive = true;
    }

    public abstract void getPosts();

    // EFFECTS: starts displaying the feed to the user
    public String start() {
        return null;
    }

    // MODIFIES: this
    // EFFECTS: sorts feed according to current sort option
    public void sortPosts() {

    }

    // Methods to print things to console

    // EFFECTS: prints out the list of available commands based on where in the forum the user is
    public void showCommands() {

    }

    // EFFECTS: prints out the given post with its title, body, user who post, and likes / dislikes / comments numbers
    public void showPost(Post p) {

    }

    // EFFECTS: prints out the first 5 comments from the given list with their user who posted, comment body, and like / dislike number
    public void showComments(List<Comment> commentList) {

    }

}
