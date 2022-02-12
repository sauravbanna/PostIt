package model;

import model.content.posts.Post;

import java.util.LinkedList;

public class Community {

    // CONSTANTS

    // FIELDS
    private String communityName;
    private String communityAbout;
    private int subCount;
    private LinkedList<Post> posts;

    // METHODS

    // Constructor
    // EFFECTS: creates a new community with the given name and about section
    //          with 0 posts and no subscribers
    public Community(String name, String about) {
        this.communityName = name;
        this.communityAbout = about;
        this.subCount = 0;
        this.posts = new LinkedList<>();
    }

    // EFFECTS: return the posts from this community
    public LinkedList<Post> getPosts() {
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
    // EFFECTS: adds a post to the community
    public void addPost(Post p) {
        this.posts.add(p);
    }
}
