package model;

import model.content.posts.Post;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// A user on PostIt with a registered username and password, an about section, a list of
// subscribed communities, and lists of post ids that they've made, liked and disliked
// A User can like and disliked posts, comment under posts, subscribe to communities and change
// their bio
public class User extends PostCollections implements Writable {

    // CONSTANTS
    public static final String DEFAULT_BIO = "No bio yet...";

    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";
    public static final String BIO_KEY = "bio";
    public static final String SUBSCRIBED_COMMUNITIES_KEY = "subscribedCommunities";
    public static final String LIKED_POSTS_KEY = "likedPosts";
    public static final String DISLIKED_POSTS_KEY = "dislikedPosts";

    // FIELDS
    private final String userName;
    private String password;
    private String bio;
    private List<String> subscribedCommunities;
    private List<Integer> likedPosts;
    private List<Integer> dislikedPosts;


    // METHODS

    // Constructor
    // EFFECTS: creates a user with the given username and password
    //          with a default bio and no subscribed communities
    //          with no liked or disliked posts
    public User(String name, String password) {
        super();
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
    public List<Integer> getLikedPosts() {
        return likedPosts;
    }

    // EFFECTS: returns user's disliked posts
    public List<Integer> getDislikedPosts() {
        return dislikedPosts;
    }

    // MODIFIES: this, Post
    // EFFECTS: adds the given post to user's liked posts if not already in user's liked posts
    //          and adds 1 like to post's like count
    //          if post is in user's disliked posts, removes it from there
    //          and reduces the post's dislikes by 1
    public String addLikedPost(Post p) {
        if (!likedPosts.contains(p.getId())) {
            if (dislikedPosts.contains(p.getId())) {
                dislikedPosts.remove(Integer.valueOf(p.getId()));
                p.unDislike();
            }
            p.like();
            likedPosts.add(p.getId());
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
        if (!dislikedPosts.contains(p.getId())) {
            if (likedPosts.contains(p.getId())) {
                likedPosts.remove(Integer.valueOf(p.getId()));
                p.unLike();
            }
            p.dislike();
            dislikedPosts.add(p.getId());
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
    //           post id belongs to a valid post on PostIt
    // MODIFIES: this
    // EFFECTS: adds given post id to list of user-made posts
    public void addUserPost(Integer postId) {
        super.addPosts(postId);
    }

    // REQUIRES: given list of post ids belong to posts made by registered users on PostIt
    //           list must not have duplicates
    // MODIFIES: this
    // EFFECTS: sets the liked posts of this user to the given list of post ids
    public void setLikedPosts(List<Integer> likedPosts) {
        this.likedPosts = likedPosts;
    }

    // REQUIRES: given list of post ids belong to posts made by registered users on PostIt
    //           list must not have duplicates
    // MODIFIES: this
    // EFFECTS: sets the disliked posts of this user to the given list of post ids
    public void setDislikedPosts(List<Integer> dislikedPosts) {
        this.dislikedPosts = dislikedPosts;
    }

    // REQUIRES: given list of community names are registered communities on PostIt
    //           list must not have duplicates
    //           communities must all have >= 1 subscriber
    // MODIFIES: this
    // EFFECTS: sets the subscribed communities of this user to the given list
    public void setSubscribedCommunities(List<String> subscribedCommunities) {
        this.subscribedCommunities = subscribedCommunities;
    }

    @Override
    public JSONObject toJson() {
        JSONObject user = new JSONObject();
        user.put(USERNAME_KEY, userName);
        user.put(PASSWORD_KEY, password);
        user.put(BIO_KEY, bio);
        user.put(SUBSCRIBED_COMMUNITIES_KEY, communitiesToJson());
        user.put(LIKED_POSTS_KEY, postsToJson(likedPosts));
        user.put(DISLIKED_POSTS_KEY, postsToJson(dislikedPosts));
        user.put(POSTS_KEY, postsToJson(getPosts()));
        return user;
    }

    // EFFECTS: returns the names of the subscribed communities of this user as a JSONArray
    private JSONArray communitiesToJson() {
        JSONArray communities = new JSONArray();

        for (String c : this.subscribedCommunities) {
            communities.put(c);
        }

        return communities;
    }
}
