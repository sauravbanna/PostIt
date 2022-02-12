package model;

import javafx.geometry.Pos;
import model.content.posts.Post;

import java.util.ArrayList;
import java.util.List;

public class User {

    // CONSTANTS
    public static final String DEFAULT_BIO = "No bio yet...";

    // FIELDS
    private String userName;
    private String password;
    private String bio;
    private List<String> subscribedCommunities;
    private List<Post> likedPosts;
    private List<Post> dislikedPosts;

    // METHODS

    // Constructor
    // EFFECTS: creates a user with the given username and password
    //          with a default bio and no subscribed communities
    public User(String name, String password) {
        this.userName = name;
        this.password = password;
        this.bio = DEFAULT_BIO;
        this.subscribedCommunities = new ArrayList<>();
        this.likedPosts = new ArrayList<>();
        this.dislikedPosts = new ArrayList<>();
    }


    // EFFECTS: returns user's username
    public String getUserName() {
        return userName;
    }

    // EFFECTS: returns user's password
    public String getPassword() {
        return password;
    }

    // EFFECTS: returns user's bio
    public String getBio() {
        return bio;
    }

    // EFFECTS: returns user's subscribed communities
    public List<String> getSubscribedCommunities() {
        return subscribedCommunities;
    }

    // EFFECTS: returns user's liked posts
    public List<Post> getLikedPosts() {
        return likedPosts;
    }

    // EFFECTS: returns user's disliked posts
    public List<Post> getDislikedPosts() {
        return dislikedPosts;
    }

    // MODIFIES: this, Post
    // EFFECTS: adds the given post to user's liked posts if not already in user's liked posts
    //          and adds 1 like to post's like count
    //          if post is in user's disliked posts, removes it from there
    //          and reduces the post's dislikes by 1
    public String addLikedPost(Post p) {
        if (!likedPosts.contains(p)) {
            if (dislikedPosts.contains(p)) {
                dislikedPosts.remove(p);
                p.unDislike();
            }
            p.like();
            likedPosts.add(p);
            return "Post added to liked posts";
        } else {
            return "You've already liked this post before!";
        }
    }

    // MODIFIES: this, Post
    // EFFECTS: adds the given post to user's disliked posts if not already in user's disliked posts
    //          and adds 1 like to post's dislike count
    //          if post is in user's liked posts, removes it from there
    //          and reduces the post's likes by 1
    public String addDislikedPost(Post p) {
        if (!dislikedPosts.contains(p)) {
            if (likedPosts.contains(p)) {
                likedPosts.remove(p);
                p.unLike();
            }
            p.dislike();
            dislikedPosts.add(p);
            return "Post added to disliked posts";
        } else {
            return "You've already disliked this post before!";
        }
    }

    // REQUIRES: community is one that is already registered on the forum,
    //           and community is not already in subscribedCommunities
    // EFFECTS: adds the given community to user's subscribed communities
    public void subscribeToCommunity(Community c) {
        this.subscribedCommunities.add(c.getCommunityName());
    }

    // REQUIRES: password is at least ui.PostIt.MIN_PASSWORD_LENGTH characters long
    // MODIFIES: this
    // EFFECTS: sets user's password to given string
    public void setPassword(String password) {
        this.password = password;
    }

    // MODIFIES: this
    // EFFECTS: sets user's bio to given string
    public void setBio(String bio) {
        this.bio = bio;
    }




}
