package model;

import model.content.posts.Post;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// A user on PostIt with a registered username and password, an about section, a list of
// subscribed communities, and lists of posts they've liked and disliked
// A User can like and disliked posts, comment under posts, subscribe to communities and change
// their bio
public class User implements Writable {

    // CONSTANTS
    public static final String DEFAULT_BIO = "No bio yet...";

    // FIELDS
    private String userName;
    private String password;
    private String bio;
    private List<String> subscribedCommunities;
    private List<Post> likedPosts;
    private List<Post> dislikedPosts;
    private List<Integer> userPosts;

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
        this.userPosts = new LinkedList<>();
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
    // MODIFIES: this, Community
    // EFFECTS: adds the given community to user's subscribed communities
    public void subscribeToCommunity(Community c) {
        this.subscribedCommunities.add(c.getCommunityName());
        c.addSubscriber();
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

    // REQUIRES: user who posted the post is this user
    // MODIFIES: this
    // EFFECTS: adds given post id to list of user-made posts
    public void addUserPost(Integer postId) {
        this.userPosts.add(postId);
    }

    // EFFECTS: returns a list of post ids of posts that this user has made
    public List<Integer> getUserPosts() {
        return this.userPosts;
    }

    @Override
    // EFFECTS: returns this user's information as a JSON Object
    public JSONObject toJson() {
        return null;
    }
}
