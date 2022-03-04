package model;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

// A class that represents a collection of post ids
public abstract class PostCollections {

    // CONSTANTS
    public static final String POSTS_KEY = "posts";

    // FIELDS
    private List<Integer> posts;

    // METHODS

    // Constructor
    // EFFECTS: creates a new collection of post ids that is empty
    public PostCollections() {
        posts = new ArrayList<>();
    }

    // EFFECTS: returns the collection of post ids as a JSONArray
    public JSONArray postsToJson(List<Integer> posts) {
        JSONArray postsJson = new JSONArray();

        for (Integer id : posts) {
            postsJson.put(id);
        }

        return postsJson;
    }

    // EFFECTS: returns the collection of post ids
    public List<Integer> getPosts() {
        return this.posts;
    }

    // REQUIRES: given postId belongs to a post made by a registered user on PostIt
    //           and cannot already be in this collection
    // MODIFIES: this
    // EFFECTS: adds the given post id to the collection of post ids
    public void addPosts(int postId) {
        this.posts.add(postId);
    }

    // REQUIRES: given list of post ids belong to posts made by registered users on PostIt
    //           list cannot contain duplicates
    // MODIFIES: this
    // EFFECTS: sets the collection of post ids to the given list
    public void setPosts(List<Integer> posts) {
        this.posts = posts;
    }


}
