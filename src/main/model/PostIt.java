package model;

import model.content.*;
import model.content.posts.Post;

import java.util.List;

public class PostIt {

    // CONSTANTS

    // Commands
    private final String NEXT_COMMAND = "/n";
    private final String LIKE_COMMAND = "/l";
    private final String DISLIKE_COMMAND = "/d";
    private final String COMMENT_COMMAND = "/c";
    private final String VIEW_COMMENTS_COMMAND = "/vc";
    private final String HELP_COMMAND = "/help";
    private final String BACK_COMMAND = "/b";

    // FIELDS


    // METHODS

    // MODIFIES: this
    // EFFECTS: sets feed to posts from default communities if user is not logged in
    //          sets feed to posts from user's communities if logged in
    //          sorted according to current sort option
    public void getPosts() {

    }

    // MODIFIES: this
    // EFFECTS: sorts feed according to current sort option
    public void sortPosts() {

    }

    // EFFECTS: starts the forum for the user
    public void start() {

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
