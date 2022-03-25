package ui;

import exceptions.EndOfFeedException;
import exceptions.StartOfFeedException;
import model.PostIt;
import model.User;
import model.content.othercontent.Comment;
import model.content.posts.Post;

import javax.swing.*;

import java.awt.*;
import java.util.List;
import java.util.Scanner;


// A Feed of posts that the user can view one by one, like, dislike, or comment on
public class Feed {

    public static final int NUM_COMMENTS_TO_SHOW = 5;

    // FIELDS
    protected List<Integer> userFeed;
    protected Boolean userFeedActive;
    private int feedPosition;
    private Post currentPost;
    private final Boolean loggedIn;
    private final User currentUser;
    private PostIt postIt;
    private JPanel display;
    private int displayWidthPx;
    private int displayHeightPx;

    private Scanner input;


    // METHODS

    // Constructor
    // EFFECTS: creates a new Feed with the given list of posts, whether the user is logged in or not,
    //          the current user (null if not logged in), with feedPosition at 0, userFeedActive set to True,
    //          with the postIt field set the the current forum the feed is displayed on,
    //          and with the Scanner object input instantiated to read user input
    public Feed(List<Integer> postList, PostIt forum) {
        userFeed = postList;
        feedPosition = 0;
        input = new Scanner(System.in);
        this.loggedIn = forum.getLoggedIn();
        this.currentUser = forum.getCurrentUser();
        this.postIt = forum;
    }

    // TODO if time
    private void showCurrentPost() {
        display.removeAll();
        display.add(Box.createHorizontalGlue());
        display.add(new PostDisplay(postIt.getPosts().get(userFeed.get(feedPosition)), postIt));
        display.add(Box.createHorizontalGlue());
        display.revalidate();
        display.repaint();
    }

    private void refreshDisplayDimensions() {
        this.displayHeightPx = this.display.getHeight();
        this.displayWidthPx = this.display.getWidth();
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
    public void next() throws EndOfFeedException {
        if (feedPosition >= userFeed.size() - 1) {
            throw new EndOfFeedException();
        } else {
            feedPosition++;
            showCurrentPost();
        }
    }

    // MODIFIES: this
    // EFFECTS: if feedPosition > 0, decrements it by 1 and shows post at that position in feed
    //          else, tells user they've reached the beginning of the feed
    public void back() throws StartOfFeedException {
        if (feedPosition > 0) {
            feedPosition--;
            currentPost = postIt.getPosts().get(userFeed.get(feedPosition));
            showCurrentPost();
        } else {
            throw new StartOfFeedException();
        }
    }

    // Methods to print things to console


/*
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
    }*/

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

    public int getFeedPosition() {
        return feedPosition;
    }

    public void setDisplay(JPanel display) {
        this.display = display;
        refreshDisplayDimensions();
        showCurrentPost();
    }

}
