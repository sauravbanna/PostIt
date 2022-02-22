package model;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public abstract class PostCollections {

    // CONSTANTS
    public static final String POSTS_KEY = "posts";

    // FIELDS
    private List<Integer> posts;

    // METHODS

    public PostCollections() {
        posts = new ArrayList<>();
    }

    public JSONArray postsToJson(List<Integer> posts) {
        JSONArray postsJson = new JSONArray();

        for (Integer id : posts) {
            postsJson.put(id);
        }

        return postsJson;
    }

    public List<Integer> getPosts() {
        return this.posts;
    }

    public void addPosts(int postId) {
        this.posts.add(postId);
    }


}
