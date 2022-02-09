package ui;

import model.User;
import model.content.othercontent.Comment;
import model.content.posts.Post;
import model.content.posts.TextPost;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Feed {

    // CONSTANTS 
    
    public final String NEXT_COMMAND = "/next";
    public final String LIKE_COMMAND = "/like";
    public final String DISLIKE_COMMAND = "/dislike";
    public final String COMMENT_COMMAND = "/comment";
    public final String VIEW_COMMENTS_COMMAND = "/vc";
    public final String HELP_COMMAND = "/help";
    public final String BACK_COMMAND = "/back";
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

    public final int NUM_COMMENTS_TO_SHOW = 5;

    // FIELDS
    protected LinkedList<Post> userFeed;
    protected String sort;
    protected Boolean userFeedActive;
    private int feedPosition;
    private Post currentPost;
    private Boolean loggedIn;
    private User currentUser;
    private Boolean liked;

    private Scanner input;
    
    // METHODS
    
    public Feed(LinkedList<Post> postList) {
        userFeed = postList;
        sort = NEW_SORT;
        userFeedActive = true;
        feedPosition = 0;
        input = new Scanner(System.in);
        loggedIn = false;
    }

    public Feed(LinkedList<Post> postList, Boolean loggedIn, User user) {
        userFeed = postList;
        sort = NEW_SORT;
        userFeedActive = true;
        feedPosition = 0;
        input = new Scanner(System.in);
        this.loggedIn = loggedIn;
        this.currentUser = user;
    }

    // MODIFIES: this
    // EFFECTS: starts displaying the feed to the user
    public String start() {
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
                    likePost();
                    break;
                case DISLIKE_COMMAND:
                    dislikePost();
                    break;
                case COMMENT_COMMAND:
                    System.out.println("Please type your comment and press enter when done:");
                    String comment = input.nextLine();
                    currentPost.addComment(new Comment(currentUser.getUserName(), comment));
                    System.out.println("Your comment has been posted!");
                    break;
                case NEXT_COMMAND:
                    liked = null;
                    feedPosition++;
                    currentPost = userFeed.get(feedPosition);
                    showPost(currentPost);
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
                case SORT_COMMAND:
                    sortPosts();
                    break;
                case EXIT_COMMAND:
                case VIEW_COMMUNITY_COMMAND:
                case MAKE_POST_COMMAND:
                case LOGIN_COMMAND:
                case LOGOUT_COMMAND:
                case SEARCH_COMMAND:
                case HOME_COMMAND:
                    return userChoice;
                default:
                    System.out.println("Sorry, I didn't understand you.");
                    break;

            }
        }

        return null;
    }

    // MODIFIES: this
    // EFFECTS: sorts feed according to current sort option
    public void sortPosts() {
        System.out.println("How would you like your posts sorted?");
        String sortChoice = input.nextLine();

        switch (sortChoice) {
            case NEW_SORT:
                break;
            case TOP_SORT:
                break;
            case DISLIKE_SORT:
                break;
            case COMMENT_SORT:
                break;
            default:
                System.out.println("Sorry, I didn't understand that, please sort by " + NEW_SORT + ", " + TOP_SORT
                + ", " + DISLIKE_SORT + ", or " + COMMENT_SORT);
                sortPosts();
                break;
        }
    }

    // EFFECTS: likes the current post (adds one like to its total) if post has not been previously liked
    //          adds post to user's liked posts list
    //          removes one dislike from post if post has been previously disliked
    //          and removes it from user's disliked posts list
    //          otherwise does nothing
    public void likePost() {
        if (currentUser.getLikedPosts().contains(currentPost)) {
            System.out.println("You've already liked this post before!");
        } else {
            System.out.println("Post added to liked posts");
            currentPost.like();
            currentUser.addLikedPost(currentPost);
            if (currentUser.getDislikedPosts().contains(currentPost)) {
                currentPost.undislike();
                currentUser.removeDislikedPost(currentPost);
            }
        }
    }

    // EFFECTS: dislikes the current post (adds one dislike to its total) if post has not been previously disliked
    //          adds post to user's disliked posts list
    //          removes one like from post if post has been previously liked
    //          and removes it from user's liked posts list
    //          otherwise does nothing
    public void dislikePost() {
        if (currentUser.getDislikedPosts().contains(currentPost)) {
            System.out.println("You've already disliked this post before!");
        } else {
            System.out.println("Post added to disliked posts");
            currentPost.dislike();
            currentUser.addDislikedPost(currentPost);
            if (currentUser.getLikedPosts().contains(currentPost)) {
                currentPost.unlike();
                currentUser.removeLikedPost(currentPost);
            }
        }
    }

    // Methods to print things to console

    // EFFECTS: prints out the list of available commands for the feed
    public void showCommands() {
        System.out.println(NEXT_COMMAND + " to view the next post");
        System.out.println(LIKE_COMMAND + " to like a post");
        System.out.println(DISLIKE_COMMAND + " to dislike a post");
        System.out.println(COMMENT_COMMAND + " to comment on a post");
        System.out.println(VIEW_COMMENTS_COMMAND + " to view the comments a post");
        System.out.println(HELP_COMMAND + " to view all the commands");
        System.out.println(BACK_COMMAND + " to go to the previous post");
        System.out.println(EXIT_COMMAND + " to exit the forum");
        System.out.println(VIEW_COMMUNITY_COMMAND + " to view a specific community");
        System.out.println(MAKE_POST_COMMAND + " to make a post");
        System.out.println(LOGIN_COMMAND + " to log in to PostIt");
        System.out.println(LOGOUT_COMMAND + " to log out of PostIt");
        System.out.println(SEARCH_COMMAND + " to search for a community");
        System.out.println(HOME_COMMAND + " to go to the home feed");
        System.out.println(SORT_COMMAND + " to sort the feed");

    }

    // EFFECTS: prints out the given post with its title, body, user who post, and likes / dislikes / comments numbers
    public void showPost(Post p) {
        System.out.println(p.getTitle());
        System.out.println("Posted by: " + p.getOpName());
        if (p.getClass() == TextPost.class) {
            System.out.println((p.getBody()));
        }
        System.out.println("Likes: " + p.getLikes() + ", Dislikes: " + p.getDislikes() + ", Comments: " + p.getCommentCount());
        System.out.println();

    }

    // EFFECTS: prints out the first numCommentsToShow comments from the given list with their user who posted, comment body, and like / dislike number
    public void showComments(List<Comment> commentList) {
        int currentCommentCount = 0;

        while (true) {
            for (int i = currentCommentCount; i < Math.min(currentCommentCount + NUM_COMMENTS_TO_SHOW, commentList.size()); i++) {
                showComment(commentList.get(i));
            }

            currentCommentCount += NUM_COMMENTS_TO_SHOW;

            if (currentCommentCount >= commentList.size()) {
                System.out.println("No more comments, feel free to add one!");
                break;
            }

            System.out.println("Type " + VIEW_COMMENTS_COMMAND + " to view the next " + NUM_COMMENTS_TO_SHOW + " comments.");
            System.out.println("Type Enter to stop reading comments.");
            String userChoice = input.nextLine();
            if (!userChoice.equals(VIEW_COMMENTS_COMMAND)) {
                break;
            }
        }
    }

    // EFFECTS: prints out the given comment
    public void showComment(Comment c) {
        System.out.println(c.getOpName() + " says: ");
        System.out.println(c.getCommentBody());
        System.out.println();
    }

}
