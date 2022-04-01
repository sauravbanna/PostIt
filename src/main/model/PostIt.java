package model;

import exceptions.EmptyDefaultCommunities;
import exceptions.EmptyFeedException;
import exceptions.IDAlreadyExistsException;
import model.content.posts.ImagePost;
import model.content.posts.Post;
import model.content.posts.TextPost;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;
import ui.Feed;

import java.util.*;

// An object that contains the PostIt forum's communities, posts, users,
public class PostIt implements Writable {

    // CONSTANTS
    public static final int DEFAULT_MAX_ID = 100000;

    public static final String POSTS_KEY = "posts";
    public static final String ACTIVE_USER_KEY = "user";
    public static final String USERS_KEY = "users";
    public static final String COMMUNITIES_KEY = "communities";
    public static final String LOGGED_IN_KEY = "loggedIn";
    public static final String NULL_USER = "null";

    public static final int MAX_USERNAME_LENGTH = 20;
    public static final int MIN_PASSWORD_LENGTH = 8;

    public static final List<String> DEFAULT_COMMUNITIES =
            Arrays.asList("funny", "news", "mildlyinteresting", "canada", "sports", "gaming");

    // FIELDS

    private Boolean loggedIn;
    private User currentlyLoggedInUser;
    private Feed activeFeed;

    private HashMap<String, User> usernamePasswords;
    private HashMap<String, Community> communities;
    private static HashMap<Integer, Post> posts;
    private final int maxId;

    // METHODS

    // EFFECTS: creates a new instance of the PostIt forum, with loggedIn being false and no logged-in user
    //          instantiates the usernamePasswords, posts, and communities as empty hashmaps
    //          sets maxId to the given value
    public PostIt(int maxId) {
        loggedIn = false;
        usernamePasswords = new HashMap<>();
        communities = new HashMap<>();
        posts = new HashMap<>();
        currentlyLoggedInUser = null;
        this.maxId = maxId;
    }

    // EFFECTS: creates a new instance of the PostIt forum, with loggedIn being false and no logged-in user
    //          instantiates the usernamePasswords, posts, and communities as empty hashmaps
    //          sets maxId to the default value
    public PostIt() {
        this(DEFAULT_MAX_ID);
    }

    // EFFECTS: returns the active feed of the forum
    public Feed getActiveFeed() {
        return this.activeFeed;
    }

    // EFFECTS: returns true if user is logged in, false if not
    public Boolean getLoggedIn() {
        return this.loggedIn;
    }

    // MODIFIES: this
    // EFFECTS: logs the user out of the forum and clears the active feed
    public void logOut() {
        loggedIn = false;
        currentlyLoggedInUser = null;
        activeFeed = null;
    }

    // EFFECTS: returns the registered communities of the forum
    public HashMap<String, Community> getCommunities() {
        return this.communities;
    }

    // REQUIRES: given user has a valid and unique username and valid password
    // MODIFIES: this
    // EFFECTS: adds the given username and user to the map of registered users
    public void addUser(String username, User user) {
        this.usernamePasswords.put(username, user);
        EventLog.getInstance().logEvent(
                new Event("Added user " + username + " to the forum."));
    }

    // EFFECTS: returns the registered users on this forum
    public HashMap<String, User> getUsernamePasswords() {
        return this.usernamePasswords;
    }

    // REQUIRES: given username is a registered user on PostIt
    // MODIFIES: this
    // EFFECTS: logs the user with the given username into the forum and sets active feed to null
    public void login(String username) {
        loggedIn = true;
        currentlyLoggedInUser = usernamePasswords.get(username);
        activeFeed = null;
    }

    // EFFECTS: returns the currently logged in user
    public User getCurrentUser() {
        return this.currentlyLoggedInUser;
    }


    // REQUIRES: currentlyLoggedInUser != null, and loggedIn is True,
    //           and given community exists on PostIt
    // MODIFIES: this
    // EFFECTS: creates a new text post with the given title and body with a unique id in the given community,
    //          with poster being the current user, and adds it to current user's and community's posts
    //          tries to generate a unique id for the post
    //          returns true if post successfully made, returns false if generated id already exists
    public Boolean makeTextPost(String title, String body, String communityChoice) {
        int randomId = 0;
        try {
            randomId = getRandomID();
        } catch (IDAlreadyExistsException ide) {
            return false;
        }
        Post newPost = new TextPost(currentlyLoggedInUser.getUserName(), title, body, communityChoice, randomId);
        posts.put(randomId, newPost);
        communities.get(communityChoice).addPost(randomId);
        currentlyLoggedInUser.addUserPost(randomId);
        EventLog.getInstance().logEvent(
                new Event("Added text post with title \""
                        + title + "\" by " + currentlyLoggedInUser.getUserName()
                        + " to the " + communityChoice + " community."));
        return true;
    }


    // REQUIRES: currentlyLoggedInUser != null, and loggedIn is True,
    //           and given community exists on PostIt
    //           and given id is unique and isn't already assigned to a post on PostIt
    // MODIFIES: this
    // EFFECTS: creates a new image post with the given title and image path with a unique id in the given community,
    //          with poster being the current user, and adds it to current user's and community's posts
    public Boolean makeImagePost(String title, String image, String communityChoice, int id) {
        Post newPost = new ImagePost(currentlyLoggedInUser.getUserName(), title, image, communityChoice, id);
        posts.put(id, newPost);
        communities.get(communityChoice).addPost(id);
        currentlyLoggedInUser.addUserPost(id);
        EventLog.getInstance().logEvent(
                new Event("Added image post with title \""
                        + title + "\" by " + currentlyLoggedInUser.getUserName()
                        + " to the " + communityChoice + "community."));
        return true;
    }

    // EFFECTS: generates a random id from 0 to maxId
    //          throws IDAlreadyExistsException if id is already assigned to a post
    public int getRandomID() throws IDAlreadyExistsException {
        int randomId = (int)(Math.random() * maxId);
        if (posts.containsKey(randomId)) {
            throw new IDAlreadyExistsException();
        }
        return randomId;
    }

    // REQUIRES: currentlyLoggedInUser != null, and loggedIn is True
    // MODIFIES: this
    // EFFECTS: changes the currently logged in user's bio to the new bio
    public void updateBio(String newBio) {
        currentlyLoggedInUser.setBio(newBio);
    }

    // REQUIRES: user is logged in (loggedIn is true) and currentlyLoggedInUser != null
    //           and given community name doesn't already exist on PostIt
    // MODIFIES: this
    // EFFECTS: adds a community with the given name and about section, created by
    //          the currently logged in user to the list of communities on the forum
    public void addCommunity(String name, String about) {
        this.communities.put(name, new Community(name, about, currentlyLoggedInUser.getUserName()));
        EventLog.getInstance().logEvent(
                new Event(("Added the " + name + " community to the forum.")));

    }

    // MODIFIES: this
    // EFFECTS: returns the user's home feed
    //          if logged in, returns feed with posts from user's subscribed communities
    //          throws EmptyFeedException if feed is empty
    //          if not logged in, returns feed with posts from default communities
    //          throws EmptyDefaultCommunities if feed is empty
    public Feed startHomeFeed() throws EmptyFeedException, EmptyDefaultCommunities {
        List<Integer> currentFeed = new ArrayList<>();
        if (loggedIn) {
            for (String community : currentlyLoggedInUser.getSubscribedCommunities()) {
                currentFeed.addAll(communities.get(community).getPosts());
            }

            if (currentFeed.size() == 0) {
                throw new EmptyFeedException();
            }
        } else {
            for (String community : DEFAULT_COMMUNITIES) {
                currentFeed.addAll(communities.get(community).getPosts());
            }

            if (currentFeed.size() == 0) {
                throw new EmptyDefaultCommunities();
            }
        }

        activeFeed = new Feed(currentFeed, this);
        return activeFeed;
    }

    // REQUIRES: given communityName is a name of a registered community on PostIt
    // MODIFIES: this
    // EFFECTS: adds posts from the community with the given name to the active feed
    //          and returns the feed
    //          throws EmptyFeedException if feed is empty
    public Feed startCommunityFeed(String communityName) throws EmptyFeedException {
        activeFeed = new Feed(communities.get(communityName).getPosts(), this);
        if (communities.get(communityName).getPosts().isEmpty()) {
            throw new EmptyFeedException();
        }
        return activeFeed;
    }

    // REQUIRES: given username is a name of a registered user on PostIt
    // MODIFIES: this
    // EFFECTS: adds posts from the user with the given name to the active feed
    //          and returns the feed
    //          throws EmptyFeedException if feed is empty
    public Feed visitUser(String username) throws EmptyFeedException {
        activeFeed = new Feed(usernamePasswords.get(username).getPosts(), this);
        if (usernamePasswords.get(username).getPosts().isEmpty()) {
            throw new EmptyFeedException();
        }
        return activeFeed;
    }

    // MODIFIES: this
    // EFFECTS: checks for every default community name if that community exists on PostIt
    //          if exists, does nothing
    //          if not, adds that community to PostIt with default creator and about section
    public void addDefaultCommunitiesCheck() {
        for (String community : DEFAULT_COMMUNITIES) {
            if (!communities.containsKey(community)) {
                communities.put(community, new Community(community, null, null));
                EventLog.getInstance().logEvent(
                        new Event("Added the " + community + " default community to the forum."));
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: clears the active feed
    public void clearActiveFeed() {
        this.activeFeed = null;
    }


    // EFFECTS: returns the map of all the posts and their ids
    public HashMap<Integer, Post> getPosts() {
        return posts;
    }

    // MODIFIES: this
    // EFFECTS: sets the loggedIn status to the given Boolean
    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    // REQUIRES: given user is a registered user on PostIt
    // MODIFIES: this
    // EFFECTS: sets the current user to the given user
    public void setCurrentlyLoggedInUser(User currentlyLoggedInUser) {
        this.currentlyLoggedInUser = currentlyLoggedInUser;
    }

    // REQUIRES: map contains registered users on PostIt
    //           each username must match with the corresponding user
    // MODIFIES: this
    // EFFECTS: sets the map of usernames and users to the given map
    public void setUsernamePasswords(HashMap<String, User> usernamePasswords) {
        this.usernamePasswords = usernamePasswords;
    }

    // REQUIRES: map contains registered communities on PostIt
    //           each community name must match to the corresponding community
    // MODIFIES: this
    // EFFECTS: sets the map of community names and communities to the given map
    public void setCommunities(HashMap<String, Community> communities) {
        this.communities = communities;
    }

    // REQUIRES: map contains posts made by registered users on PostIt
    //           each id must match to the corresponding post
    // MODIFIES: this
    // EFFECTS: sets the map of usernames and users to the given map
    public void setPosts(HashMap<Integer, Post> posts) {
        PostIt.posts = posts;
    }


    @Override
    public JSONObject toJson() {
        JSONObject forum = new JSONObject();
        forum.put(LOGGED_IN_KEY, loggedIn);
        if (currentlyLoggedInUser != null) {
            forum.put(ACTIVE_USER_KEY, currentlyLoggedInUser.toJson());
        } else {
            forum.put(ACTIVE_USER_KEY, NULL_USER);
        }
        forum.put(USERS_KEY, usersToJson());
        forum.put(COMMUNITIES_KEY, communitiesToJson());
        forum.put(POSTS_KEY, postsToJson());
        return forum;
    }

    // EFFECTS: returns the posts on the forum as a JSONArray
    private JSONArray postsToJson() {
        JSONArray postsJson = new JSONArray();

        for (Post p : posts.values()) {
            postsJson.put(p.toJson());
        }

        return postsJson;
    }

    // EFFECTS: returns the communities on the forum as a JSONArray
    private JSONArray communitiesToJson() {
        JSONArray communities = new JSONArray();

        for (Community c : this.communities.values()) {
            communities.put(c.toJson());
        }

        return communities;
    }

    // EFFECTS: returns the users on the forum as a JSONArray
    private JSONArray usersToJson() {
        JSONArray users = new JSONArray();

        for (User u : this.usernamePasswords.values()) {
            users.put(u.toJson());
        }

        return users;
    }
}
