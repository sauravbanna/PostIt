package model;

import model.content.posts.Post;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Community {

    // CONSTANTS

    // FIELDS
    private String communityName;
    private String communityAbout;
    private int subCount;
    private LinkedList<Post> posts;

    // METHODS

    // EFFECTS: creates a new community with the given name and about
    //          with 0 posts and no subscribers
    public Community(String name, String about) {
        this.communityName = name;
        this.communityAbout = about;
        this.subCount = 0;
        this.posts = new LinkedList<Post>();
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




}
