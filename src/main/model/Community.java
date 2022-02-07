package model;

import model.content.posts.Post;

import java.util.ArrayList;
import java.util.List;

public class Community {

    // CONSTANTS

    // FIELDS
    private String communityName;
    private String communityAbout;
    private int subCount;
    private List<Post> posts;

    // METHODS

    // EFFECTS: creates a new community with the given name and about
    //          with 0 posts and no subscribers
    public Community(String name, String about) {
        this.communityName = name;
        this.communityAbout = about;
        this.subCount = 0;
        this.posts = new ArrayList<Post>();
    }

}
