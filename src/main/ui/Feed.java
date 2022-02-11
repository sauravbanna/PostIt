package ui;

import model.User;
import model.content.othercontent.Comment;
import model.content.posts.Post;
import model.content.posts.TextPost;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static ui.PostIt.*;

public class Feed {

    // CONSTANTS 
    
    public static final String NEXT_COMMAND = "/next";
    public static final String LIKE_COMMAND = "/like";
    public static final String DISLIKE_COMMAND = "/dislike";
    public static final String COMMENT_COMMAND = "/comment";
    public static final String VIEW_COMMENTS_COMMAND = "/vc";
    public static final String BACK_COMMAND = "/back";
    //public static final String SORT_COMMAND = "/sort";

    public static final String NEW_SORT = "NEW";
    public static final String TOP_SORT = "TOP";
    public static final String COMMENT_SORT = "COMMENTS";
    public static final String DISLIKE_SORT = "DISLIKES";

    public static final int NUM_COMMENTS_TO_SHOW = 5;

    // FIELDS
    protected LinkedList<Post> userFeed;
    protected Boolean userFeedActive;
    private int feedPosition;
    private Post currentPost;
    private String currentSort;
    private final Boolean loggedIn;
    private final User currentUser;

    private Scanner input;
    
    // METHODS

    // EFFECTS: creates a new Feed with the given list of posts, whether the user is logged in or not,
    //          the current user (null if not logged in), with feedPosition at 0, userFeedActive set to True,
    //          and with the Scanner object input instantiated to read user input
    public Feed(LinkedList<Post> postList, Boolean loggedIn, User user) {
        userFeed = postList;
        //sortPosts(currentSort);
        userFeedActive = true;
        feedPosition = 0;
        input = new Scanner(System.in);
        this.loggedIn = loggedIn;
        this.currentUser = user;
    }


    // MODIFIES: this
    // EFFECTS: starts displaying the feed to the user
    //SuppressWarnings:
    @SuppressWarnings("methodlength")
    public String start() {
        //System.out.println("Currently sorting by: " + currentSort);

        if (userFeed.isEmpty()) {
            System.out.println("There are no posts to show, what would you like to do?");
            return input.nextLine();
        }

        currentPost = userFeed.get(feedPosition);
        showPost(currentPost);
        String userChoice;

        while (userFeedActive) {
            System.out.println("You can " + LIKE_COMMAND + ", " + DISLIKE_COMMAND + ", " + COMMENT_COMMAND
                    + ", view the " + NEXT_COMMAND + " post, or " + VIEW_COMMENTS_COMMAND + " to see "
                    + NUM_COMMENTS_TO_SHOW + " comments.");
            System.out.println("Type " + HELP_COMMAND + " for a full list of commands.");

            userChoice = input.nextLine();


            switch (userChoice) {
                case LIKE_COMMAND:
                    if (loggedIn) {
                        likePost();
                    }  else {
                        System.out.println("You have to be logged in to do that!");
                    }
                    break;
                case DISLIKE_COMMAND:
                    if (loggedIn) {
                        dislikePost();
                    }  else {
                        System.out.println("You have to be logged in to do that!");
                    }
                    break;
                case COMMENT_COMMAND:
                    if (loggedIn) {
                        System.out.println("Please type your comment and press enter when done:");
                        String comment = input.nextLine();
                        currentPost.addComment(new Comment(currentUser.getUserName(), comment));
                        System.out.println("Your comment has been posted!");
                    }  else {
                        System.out.println("You have to be logged in to do that!");
                    }
                    break;
                case NEXT_COMMAND:
                    if (feedPosition >= userFeed.size() - 1) {
                        System.out.println("You've reached the end!");
                    } else {
                        feedPosition++;
                        currentPost = userFeed.get(feedPosition);
                        showPost(currentPost);
                    }
                    break;
                case VIEW_COMMENTS_COMMAND:
                    showComments(currentPost.getComments());
                    break;
                case BACK_COMMAND:
                    if (feedPosition > 0) {
                        feedPosition--;
                        currentPost = userFeed.get(feedPosition);
                        showPost(currentPost);
                    } else {
                        System.out.println("You've reached the beginning!");
                    }
                    break;
                case HELP_COMMAND:
                    showCommands();
                    break;
                /*case SORT_COMMAND:
                    System.out.println("How would you like your posts sorted?");
                    System.out.println("You can sort by " + NEW_SORT + ", " + TOP_SORT
                            + ", " + DISLIKE_SORT + ", or " + COMMENT_SORT);
                    String sortChoice = input.nextLine();
                    System.out.println("Sorting posts ...");
                    sortPosts(sortChoice);
                    currentPost = null;
                    feedPosition = 0;
                    start();
                    break;*/
                case EXIT_COMMAND:
                case VIEW_COMMUNITY_COMMAND:
                case SHOW_AVAILABLE_COMMUNITIES:
                case MAKE_POST_COMMAND:
                case LOGIN_COMMAND:
                case CREATE_COMMUNITY_COMMAND:
                case LOGOUT_COMMAND:
                case REGISTER_COMMAND:
                case VIEW_USER_COMMAND:
                case SUBSCRIBE_TO_COMMUNITY_COMMAND:
                case HOME_COMMAND:
                    return userChoice;
                default:
                    System.out.println("Sorry, I didn't understand you. You can type " + HELP_COMMAND
                            + " to get a full list of commands.");
                    break;

            }
        }

        return null;
    }

    /*// MODIFIES: this
    // EFFECTS: sorts feed according to current sort option
    public void sortPosts(String sortChoice) {
        switch (sortChoice) {
            case NEW_SORT:
                currentSort = NEW_SORT;

                break;
            case TOP_SORT:
                currentSort = TOP_SORT;
                break;
            case DISLIKE_SORT:
                currentSort = DISLIKE_SORT;
                break;
            case COMMENT_SORT:
                currentSort = COMMENT_SORT;
                break;
            default:
                System.out.println("Sorry, I didn't understand that, please sort by " + NEW_SORT + ", " + TOP_SORT
                        + ", " + DISLIKE_SORT + ", or " + COMMENT_SORT);
                sortChoice = input.nextLine();
                sortPosts(sortChoice);
                break;
        }
    }*/

    // REQUIRES: user is logged in (loggedIn is true)
    // MODIFIES: this
    // EFFECTS: likes the current post (adds one like to its total) if post has not been previously liked
    //          adds post to user's liked posts list
    //          removes one dislike from post if post has been previously disliked
    //          and removes it from user's disliked posts list
    //          otherwise tells user that they have already liked the post
    public void likePost() {
        if (currentUser.getLikedPosts().contains(currentPost)) {
            System.out.println("You've already liked this post before!");
        } else {
            System.out.println("Post added to liked posts");
            if (currentUser.getDislikedPosts().contains(currentPost)) {
                currentPost.unDislike();
                currentUser.removeDislikedPost(currentPost);
            }
            currentPost.like();
            currentUser.addLikedPost(currentPost);
        }
    }

    // REQUIRES: user is logged in (loggedIn is true)
    // MODIFIES: this
    // EFFECTS: dislikes the current post (adds one dislike to its total) if post has not been previously disliked
    //          adds post to user's disliked posts list
    //          removes one like from post if post has been previously liked
    //          and removes it from user's liked posts list
    //          otherwise tells user that they have already disliked the post
    public void dislikePost() {
        if (currentUser.getDislikedPosts().contains(currentPost)) {
            System.out.println("You've already disliked this post before!");
        } else {
            currentUser.addDislikedPost(currentPost);
            if (currentUser.getLikedPosts().contains(currentPost)) {
                currentPost.unLike();
                currentUser.removeLikedPost(currentPost);
            }
            System.out.println("Post added to disliked posts");
            currentPost.dislike();
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

    // EFFECTS: prints out the first numCommentsToShow comments from the given list with their user who posted,
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

}
