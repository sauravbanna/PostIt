package ui;

import exceptions.EndOfFeedException;
import exceptions.StartOfFeedException;
import model.PostIt;
import model.User;
import model.content.posts.Post;

import javax.swing.*;
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


    // METHODS

    // Constructor
    // EFFECTS: creates a new Feed with the given list of posts, whether the user is logged in or not,
    //          the current user (null if not logged in), with feedPosition at 0, userFeedActive set to True,
    //          with the postIt field set the the current forum the feed is displayed on
    public Feed(List<Integer> postList, PostIt forum) {
        userFeed = postList;
        feedPosition = 0;
        this.loggedIn = forum.getLoggedIn();
        this.currentUser = forum.getCurrentUser();
        this.postIt = forum;
    }


    // MODIFIES: this, JPanel
    // EFFECTS: clears the current content on the post display
    //          and sets it to the post content at the current feed position
    private void showCurrentPost() {
        display.removeAll();
        display.add(Box.createHorizontalGlue());
        display.add(new PostDisplay(postIt.getPosts().get(userFeed.get(feedPosition)), postIt));
        display.add(Box.createHorizontalGlue());
        display.revalidate();
        display.repaint();
    }

    // MODIFIES: this
    // EFFECTS: sets the height and width fields to the current height and width of the display
    private void refreshDisplayDimensions() {
        this.displayHeightPx = this.display.getHeight();
        this.displayWidthPx = this.display.getWidth();
    }

    // MODIFIES: this
    // EFFECTS: if feedPosition < size of userFeed - 1, increments it by 1 and shows post at that position in feed
    //          else, throws EndOfFeedException
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
    //          else, throws StartOfFeedException
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

    // MODIFIES: this, JPanel
    // EFFECTS: clears the post display and sets it to the given display
    //          reloads the dimensions and shows the post at current feed position
    public void setDisplay(JPanel display) {
        display.removeAll();
        this.display = display;
        refreshDisplayDimensions();
        showCurrentPost();
    }

}
