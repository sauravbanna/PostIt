package ui;

import model.PostIt;
import model.User;
import model.content.othercontent.Comment;
import model.content.posts.Post;
import model.content.posts.TextPost;
import static model.PostIt.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static ui.PostItApp.*;


// A Feed of posts that the user can view one by one, like, dislike, or comment on
public class Feed {

    // CONSTANTS 
    
    public static final String NEXT_COMMAND = "/next";
    public static final String LIKE_COMMAND = "/like";
    public static final String DISLIKE_COMMAND = "/dislike";
    public static final String COMMENT_COMMAND = "/comment";
    public static final String VIEW_COMMENTS_COMMAND = "/vc";
    public static final String BACK_COMMAND = "/back";

    public static final int NUM_COMMENTS_TO_SHOW = 5;

    // FIELDS
    protected List<Integer> userFeed;
    protected Boolean userFeedActive;
    private int feedPosition;
    private Post currentPost;
    private final Boolean loggedIn;
    private final User currentUser;
    private PostIt postIt;

    private Scanner input;
    
    // METHODS

    // Constructor
    // EFFECTS: creates a new Feed with the given list of posts, whether the user is logged in or not,
    //          the current user (null if not logged in), with feedPosition at 0, userFeedActive set to True,
    //          with the postIt field set the the current forum the feed is displayed on,
    //          and with the Scanner object input instantiated to read user input
    public Feed(List<Integer> postList, Boolean loggedIn, User user, PostIt forum) {
        userFeed = postList;
        userFeedActive = true;
        feedPosition = 0;
        input = new Scanner(System.in);
        this.loggedIn = loggedIn;
        this.currentUser = user;
        this.postIt = forum;
    }


    // MODIFIES: this
    // EFFECTS: starts displaying the feed to the user
    @SuppressWarnings("methodlength")
    public String start() {
        if (userFeed.isEmpty()) {
            System.out.println("There are no posts to show, what would you like to do?");
            return input.nextLine();
        }
        currentPost = postIt.getPosts().get(userFeed.get(feedPosition));
        showPost(currentPost);
        String userChoice;
        while (userFeedActive) {
            System.out.println("Type " + LIKE_COMMAND + " to like, " + DISLIKE_COMMAND + " to dislike, "
                    + COMMENT_COMMAND + " to view the comments, " + NEXT_COMMAND + " to see the next post, or "
                    + VIEW_COMMENTS_COMMAND  + " to see " + NUM_COMMENTS_TO_SHOW + " comments.");
            System.out.println("Type " + HELP_COMMAND + " for a full list of commands.");

            userChoice = input.nextLine();

            switch (userChoice) {
                case LIKE_COMMAND:
                    like();
                    break;
                case DISLIKE_COMMAND:
                    dislike();
                    break;
                case COMMENT_COMMAND:
                    comment();
                    break;
                case NEXT_COMMAND:
                    next();
                    break;
                case VIEW_COMMENTS_COMMAND:
                    showComments(currentPost.getComments());
                    break;
                case BACK_COMMAND:
                    back();
                    break;
                case HELP_COMMAND:
                    showCommands();
                    break;
                default:
                    return defaultInput(userChoice);
            }
        }
        return NEXT_ACTION_COMMAND;
    }

    // EFFECTS: checks 1. if input is either a command that requires exiting the feed
    //          or 2. the input is invalid
    //          returns the corresponding command if case 1, returns NEXT_ACTION_COMMAND if case 2
    public String defaultInput(String input) {
        if (EXIT_FEED_COMMANDS.contains(input)) {
            return input;
        } else {
            System.out.println("Sorry, I didn't understand you. You can type " + HELP_COMMAND
                    + " to get a full list of commands.");
            return NEXT_ACTION_COMMAND;
        }
    }

    // MODIFIES: this, User
    // EFFECTS: if user is logged in, adds current post to user's liked post
    //          otherwise, tells user to log in
    public void like() {
        if (loggedIn) {
            System.out.println(currentUser.addLikedPost(currentPost));
        }  else {
            System.out.println("You have to be logged in to do that!");
        }
    }

    // MODIFIES: this, User
    // EFFECTS: if user is logged in, adds current post to user's disliked post
    //          otherwise, tells user to log in
    public void dislike() {
        if (loggedIn) {
            System.out.println(currentUser.addDislikedPost(currentPost));
        }  else {
            System.out.println("You have to be logged in to do that!");
        }
    }

    // MODIFIES: this, Post
    // EFFECTS: if user is logged in, prompts user to enter a comment
    //          adds comment user entered to current post's comments
    //          otherwise, tells user to log in
    public void comment() {
        if (loggedIn) {
            System.out.println("Please type your comment and press enter when done:");
            String comment = input.nextLine();
            currentPost.addComment(new Comment(currentUser.getUserName(), comment));
            System.out.println("Your comment has been posted!");
        }  else {
            System.out.println("You have to be logged in to do that!");
        }
    }

    // MODIFIES: this
    // EFFECTS: if feedPosition < size of userFeed - 1, increments it by 1 and shows post at that position in feed
    //          else, tells user they've reached the end of the feed
    public void next() {
        if (feedPosition >= userFeed.size() - 1) {
            System.out.println("You've reached the end!");
        } else {
            feedPosition++;
            currentPost = postIt.getPosts().get(userFeed.get(feedPosition));
            showPost(currentPost);
        }
    }

    // MODIFIES: this
    // EFFECTS: if feedPosition > 0, decrements it by 1 and shows post at that position in feed
    //          else, tells user they've reached the beginning of the feed
    public void back() {
        if (feedPosition > 0) {
            feedPosition--;
            currentPost = postIt.getPosts().get(userFeed.get(feedPosition));
            showPost(currentPost);
        } else {
            System.out.println("You've reached the beginning!");
        }
    }

    // Methods to print things to console

    // EFFECTS: prints out the list of available commands for the feed
    public static void showCommands() {
        System.out.println("Here are all the commands available: ");
        System.out.println(NEXT_COMMAND + " to view the next post");
        System.out.println(LIKE_COMMAND + " to like a post");
        System.out.println(DISLIKE_COMMAND + " to dislike a post");
        System.out.println(COMMENT_COMMAND + " to comment on a post");
        System.out.println(VIEW_COMMENTS_COMMAND + " to view the comments a post");
        System.out.println(HELP_COMMAND + " to view all the commands");
        System.out.println(BACK_COMMAND + " to go to the previous post");
        System.out.println(EXIT_COMMAND + " to exit the forum");
        System.out.println(VIEW_COMMUNITY_COMMAND + " to view a specific community");
        System.out.println(CREATE_COMMUNITY_COMMAND + " to create a new community");
        System.out.println(MAKE_POST_COMMAND + " to make a post");
        System.out.println(SUBSCRIBE_TO_COMMUNITY_COMMAND + " to subscribe to a community");
        System.out.println(VIEW_USER_COMMAND + " to view a specific user's profile");
        System.out.println(LOGIN_COMMAND + " to log in to PostIt");
        System.out.println(LOGOUT_COMMAND + " to log out of PostIt");
        System.out.println(REGISTER_COMMAND + " to register an account");
        System.out.println(HOME_COMMAND + " to go to the home feed");
        System.out.println(SHOW_AVAILABLE_COMMUNITIES + " to see all existing communities on PostIt");
        //System.out.println(SORT_COMMAND + " to sort the feed");

    }

    // EFFECTS: prints out the given post with its title, body, user who posted it, community it was posted in
    //          and number of likes / dislikes / comments
    public void showPost(Post p) {
        System.out.println(p.getTitle());
        System.out.println("Posted by: " + p.getOpName());
        System.out.println("Posted in: " + p.getCommunity());
        if (p.getClass() == TextPost.class) {
            System.out.println((p.getBody()));
        }
        System.out.println("Likes: " + p.getLikes() + ", Dislikes: "
                + p.getDislikes() + ", Comments: " + p.getCommentCount());
        System.out.println();

    }

    // EFFECTS: prints out the first NUM_COMMENTS_TO_SHOW comments from the given list with their user who posted,
    //          comment body, and like / dislike numbers
    public void showComments(List<Comment> commentList) {
        int currentCommentCount = 0;

        while (true) {
            for (int i = currentCommentCount; i < Math.min(currentCommentCount
                    + NUM_COMMENTS_TO_SHOW, commentList.size()); i++) {
                showComment(commentList.get(i));
            }

            currentCommentCount += NUM_COMMENTS_TO_SHOW;

            if (currentCommentCount >= commentList.size()) {
                System.out.println("No more comments, feel free to add one!");
                break;
            }

            System.out.println("Type " + VIEW_COMMENTS_COMMAND + " to view the next "
                    + NUM_COMMENTS_TO_SHOW + " comments.");
            System.out.println("Type Enter to stop reading comments.");
            String userChoice = input.nextLine();
            if (!userChoice.equals(VIEW_COMMENTS_COMMAND)) {
                break;
            }
        }
    }

    // EFFECTS: prints out the given comment with user who posted it and comment body
    public void showComment(Comment c) {
        System.out.println(c.getOpName() + " says: ");
        System.out.println(c.getCommentBody());
        System.out.println();
    }

    // EFFECTS: returns the current feed
    public List<Integer> getUserFeed() {
        return userFeed;
    }

    // EFFECTS: returns the current logged in status
    public Boolean getLoggedIn() {
        return loggedIn;
    }

    // EFFECTS: returns the current user
    public User getCurrentUser() {
        return currentUser;
    }

    // EFFECTS: returns the current PostIt forum that feed is displaying in
    public PostIt getPostIt() {
        return postIt;
    }

}
