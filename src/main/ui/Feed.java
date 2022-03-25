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

    // FIELDS
    protected List<Integer> userFeed;
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
        this.loggedIn = forum.getLoggedIn();
        this.currentUser = forum.getCurrentUser();
        this.postIt = forum;
    }


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
        display.removeAll();
        this.display = display;
        refreshDisplayDimensions();
        showCurrentPost();
    }

}
