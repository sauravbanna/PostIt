package persistence;


import model.Community;
import model.PostIt;
import model.User;
import model.content.posts.Post;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

public class JsonReader {

    // CONSTANTS

    // FIELDS
    private String fileLocation;

    // METHODS

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    public JsonReader(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    public PostIt read() throws IOException {
        String data = readFile(fileLocation);
        JSONObject forum = new JSONObject(data);
        return readPostIt(forum);
    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines( Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    private PostIt readPostIt(JSONObject jsonObject) {
        PostIt postIt = new PostIt();

        Boolean loggedIn = jsonObject.getBoolean(postIt.LOGGED_IN_KEY);
        User currentlyActiveUser = addUser(jsonObject.getJSONObject(postIt.ACTIVE_USER_KEY));
        HashMap<Integer, Post> posts = addPosts(jsonObject.getJSONArray(postIt.POSTS_KEY));
        HashMap<String, Community> communities = addCommunities(jsonObject.getJSONArray(postIt.COMMUNITIES_KEY));
        HashMap<String, User> users = addUsers(jsonObject.getJSONArray(postIt.USERS_KEY));

        postIt.setCommunities(communities);
        postIt.setCurrentlyLoggedInUser(currentlyActiveUser);
        postIt.setLoggedIn(loggedIn);
        postIt.setUsernamePasswords(users);
        postIt.setPosts(posts);

        return postIt;
    }


    private HashMap<String, Community> addCommunities(JSONArray communitiesJson) {
        HashMap<String, Community> communities = new HashMap<>();

        for (Object json : communitiesJson) {
            Community community = addCommunity((JSONObject) json);
            communities.put(community.getCommunityName(), community);
        }

        return communities;
    }

    private Community addCommunity(JSONObject communityJson) {
        return null;
    }

    private HashMap<String, User> addUsers(JSONArray usersJson) {
        HashMap<String, Community> communities = new HashMap<>();

        for (Object json : communitiesJson) {
            Community community = addCommunity((JSONObject) json);
            communities.put(community.getCommunityName(), community);
        }

        return communities;
    }

    private User addUser(JSONObject userJson) {
        return null;
    }

    private HashMap<Integer, Post> addPosts(JSONArray postsJson) {
        return null;
    }

    private Post addPost(JSONObject postJson) {
        return null;
    }

}
