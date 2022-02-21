package model;

import model.content.posts.Post;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// A group on PostIt with a community name, about section, subscriber count, and list of Posts
// that user can post to and view the posts of
public class Community implements Writable {

    // CONSTANTS
    public static final String DEFAULT_CREATOR = "PostIt";

    // FIELDS
    private String communityName;
    private String communityAbout;
    private int subCount;
    private List<Integer> posts;
    private String creator;

    // METHODS

    // Constructor
    // EFFECTS: creates a new community with the given name and about section
    //          with 0 posts and no subscribers
    public Community(String name, String about, String creator) {
        this.communityName = name;
        this.communityAbout = about;
        this.subCount = 0;
        this.posts = new ArrayList<>();
        if (creator != null) {
            this.creator = creator;
        } else {
            this.creator = DEFAULT_CREATOR;
        }
    }

    // EFFECTS: return the posts from this community
    public List<Integer> getPosts() {
        return this.posts;
    }

    // EFFECTS: return the name of this community
    public String getCommunityName() {
        return communityName;
    }

    // EFFECTS: return the about section of this community
    public String getCommunityAbout() {
        return communityAbout;
    }

    // EFFECTS: return the subscriber count of this community
    public int getSubCount() {
        return subCount;
    }

    // MODIFIES: this
    // EFFECTS: adds a post's post id to the community
    public void addPost(Integer postId) {
        this.posts.add(postId);
    }

    // MODIFIES: this
    // EFFECTS: adds 1 subscriber to this community's sub count
    public void addSubscriber() {
        this.subCount++;
    }

    // EFFECTS: returns the creator name of the community
    public String getCreator() {
        return this.creator;
    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
