package persistence;


import model.Community;
import model.PostIt;
import model.User;
import model.content.othercontent.Comment;
import model.content.posts.ImagePost;
import model.content.posts.Post;
import model.content.posts.TextPost;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

// Represents a reader that reads a PostIt forum from JSON data stored in file
public class JsonReader {

    // CONSTANTS

    // FIELDS
    private String fileLocation;

    // METHODS

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: constructs reader to read from source file
    public JsonReader(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public PostIt read() throws IOException {
        String data = readFile(fileLocation);
        JSONObject forum = new JSONObject(data);
        return readPostIt(forum);
    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: reads PostIt from JSON object and returns it
    private PostIt readPostIt(JSONObject jsonObject) {
        PostIt postIt = new PostIt();

        Boolean loggedIn = jsonObject.getBoolean(PostIt.LOGGED_IN_KEY);
        User currentlyActiveUser = null;
        if (loggedIn) {
            currentlyActiveUser = jsonToUser(jsonObject.getJSONObject(PostIt.ACTIVE_USER_KEY));
        }
        HashMap<Integer, Post> posts = jsonToPosts(jsonObject.getJSONArray(PostIt.POSTS_KEY));
        HashMap<String, Community> communities = jsonToCommunities(jsonObject.getJSONArray(PostIt.COMMUNITIES_KEY));
        HashMap<String, User> users = jsonToUsers(jsonObject.getJSONArray(PostIt.USERS_KEY));

        postIt.setCommunities(communities);
        postIt.setCurrentlyLoggedInUser(currentlyActiveUser);
        postIt.setLoggedIn(loggedIn);
        postIt.setUsernamePasswords(users);
        postIt.setPosts(posts);

        return postIt;
    }

    // EFFECTS: reads a map of community names and communities from a JSONArray and returns it
    private HashMap<String, Community> jsonToCommunities(JSONArray communitiesJson) {
        HashMap<String, Community> communities = new HashMap<>();

        for (Object json : communitiesJson) {
            Community community = jsonToCommunity((JSONObject) json);
            communities.put(community.getCommunityName(), community);
        }

        return communities;
    }

    // EFFECTS: reads a community from a JSONObject and returns it
    private Community jsonToCommunity(JSONObject communityJson) {
        String communityName = communityJson.getString(Community.NAME_KEY);
        String communityAbout = communityJson.getString(Community.ABOUT_KEY);
        String communityCreator = communityJson.getString(Community.CREATOR_KEY);
        int subCount = communityJson.getInt(Community.SUBCOUNT_KEY);
        List<Integer> posts = jsonToPostIds((JSONArray) communityJson.get(Community.POSTS_KEY));

        Community community = new Community(communityName, communityAbout, communityCreator);
        community.setSubCount(subCount);
        community.setPosts(posts);

        return community;
    }

    // EFFECTS: reads a list of post ids from a JSONArray and returns it
    private List<Integer> jsonToPostIds(JSONArray postsJson) {
        List<Integer> posts = new ArrayList<>();
        for (Object json : postsJson) {
            posts.add((Integer) json);
        }

        return posts;
    }

    // EFFECTS: reads a map of usernames and users from a JSONArray and returns it
    private HashMap<String, User> jsonToUsers(JSONArray usersJson) {
        HashMap<String, User> users = new HashMap<>();

        for (Object json : usersJson) {
            User user = jsonToUser((JSONObject) json);
            users.put(user.getUserName(), user);
        }

        return users;
    }

    // EFFECTS: reads a user from a JSONObject and returns it
    private User jsonToUser(JSONObject userJson) {
        String username = userJson.getString(User.USERNAME_KEY);
        String password = userJson.getString(User.PASSWORD_KEY);
        String about = userJson.getString(User.BIO_KEY);
        List<Integer> posts = jsonToPostIds(userJson.getJSONArray(User.POSTS_KEY));
        List<Integer> likedPosts = jsonToPostIds(userJson.getJSONArray(User.LIKED_POSTS_KEY));
        List<Integer> dislikedPosts = jsonToPostIds(userJson.getJSONArray(User.DISLIKED_POSTS_KEY));
        List<String> subscribedCommunities =
                jsonToCommunityNames(userJson.getJSONArray(User.SUBSCRIBED_COMMUNITIES_KEY));

        User user = new User(username, password);
        user.setBio(about);
        user.setPosts(posts);
        user.setLikedPosts(likedPosts);
        user.setDislikedPosts(dislikedPosts);
        user.setSubscribedCommunities(subscribedCommunities);

        return user;
    }

    // EFFECTS: reads a list of community names from a JSONArray and returns it
    private List<String> jsonToCommunityNames(JSONArray communityNames) {
        List<String> communities = new ArrayList<>();

        for (Object json : communityNames) {
            communities.add((String) json);
        }

        return communities;
    }

    // EFFECTS: reads a map of post ids and posts from a JSONArray and returns it
    private HashMap<Integer, Post> jsonToPosts(JSONArray postsJson) {
        HashMap<Integer, Post> posts = new HashMap<>();

        for (Object json : postsJson) {
            Post post = jsonToPost((JSONObject) json);
            posts.put(post.getId(), post);
        }

        return posts;
    }

    // EFFECTS: reads a text post from a JSONObject and returns it
    private Post jsonToPost(JSONObject postJson) {
        String postTitle = postJson.getString(Post.TITLE_KEY);
        int postLikes = postJson.getInt(Post.LIKES_KEY);
        int postDislikes = postJson.getInt(Post.DISLIKES_KEY);
        int postCommentCount = postJson.getInt(Post.COMMENT_COUNT_KEY);
        List<Comment> postComments = jsonToComments(postJson.getJSONArray(Post.COMMENTS_KEY));
        String postCommunity = postJson.getString(Post.COMMUNITY_KEY);
        int postId = postJson.getInt(Post.ID_KEY);
        String postOpName = postJson.getString(Post.OP_NAME_KEY);

        Post post = null;

        if (postJson.has(TextPost.TEXT_BODY_KEY)) {
            String postBody = postJson.getString(TextPost.TEXT_BODY_KEY);
            post = new TextPost(postOpName, postTitle, postBody, postCommunity, postId);
        } else if (postJson.has(ImagePost.IMAGE_LOCATION_KEY)) {
            String image = postJson.getString(ImagePost.IMAGE_LOCATION_KEY);
            post = new ImagePost(postOpName, postTitle, image, postCommunity, postId);
        }

        post.setDislikes(postDislikes);
        post.setLikes(postLikes);
        post.setCommentCount(postCommentCount);
        post.setComments(postComments);

        return post;
    }

    // EFFECTS: reads a list of comments from a JSONArray and returns it
    private List<Comment> jsonToComments(JSONArray commentsJson) {
        List<Comment> comments = new ArrayList<>();

        for (Object json : commentsJson) {
            comments.add(jsonToComment((JSONObject) json));
        }

        return comments;
    }

    // EFFECTS: reads a comment from a JSONObject and returns it
    private Comment jsonToComment(JSONObject json) {
        String commentOpName = json.getString(Comment.OP_NAME_KEY);
        String commentBody = json.getString(Comment.BODY_KEY);
        int commentLikes = json.getInt(Comment.LIKES_KEY);
        int commentDislikes = json.getInt(Comment.DISLIKES_KEY);

        Comment comment = new Comment(commentOpName, commentBody);
        comment.setLikes(commentLikes);
        comment.setDislikes(commentDislikes);

        return comment;
    }



}
