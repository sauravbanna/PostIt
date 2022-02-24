package model;

import exceptions.EmptyFeedException;
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
    public static final String POSTS_KEY = "posts";
    public static final String ACTIVE_USER_KEY = "user";
    public static final String USERS_KEY = "users";
    public static final String COMMUNITIES_KEY = "communities";
    public static final String LOGGED_IN_KEY = "loggedIn";
    public static final String NULL_USER = "null";

    public static final String VIEW_COMMUNITY_COMMAND = "/visit";
    public static final String MAKE_POST_COMMAND = "/post";
    public static final String LOGOUT_COMMAND = "/logout";
    public static final String LOGIN_COMMAND = "/login";
    public static final String HOME_COMMAND = "/home";
    public static final String VIEW_USER_COMMAND = "/user";
    public static final String REGISTER_COMMAND = "/register";
    public static final String EXIT_COMMAND = "/exit";
    public static final String NEXT_ACTION_COMMAND = "action";
    public static final String SHOW_AVAILABLE_COMMUNITIES = "/show";
    public static final String SUBSCRIBE_TO_COMMUNITY_COMMAND = "/join";
    public static final String CREATE_COMMUNITY_COMMAND = "/create";
    public static final String HELP_COMMAND = "/help";
    public static final String EDIT_PROFILE_COMMAND = "/edit";
    public static final String SELF_PROFILE_COMMAND = "me";
    public static final String VIEW_USER_POSTS = "/userposts";

    public static final int MAX_USERNAME_LENGTH = 20;
    public static final int MIN_PASSWORD_LENGTH = 8;

    public static final List<String> DEFAULT_COMMUNITIES =
            Arrays.asList("funny", "news", "mildlyinteresting", "canada", "sports", "gaming");
    public static final List<String> EXIT_FEED_COMMANDS =
            Arrays.asList(VIEW_COMMUNITY_COMMAND, MAKE_POST_COMMAND, LOGIN_COMMAND, LOGOUT_COMMAND,
                    HOME_COMMAND, VIEW_USER_COMMAND, REGISTER_COMMAND, EXIT_COMMAND, SHOW_AVAILABLE_COMMUNITIES,
                    SUBSCRIBE_TO_COMMUNITY_COMMAND, CREATE_COMMUNITY_COMMAND);

    // FIELDS

    private Boolean loggedIn;
    private User currentlyLoggedInUser;
    private Feed activeFeed;

    private HashMap<String, User> usernamePasswords;
    private HashMap<String, Community> communities;
    private static HashMap<Integer, Post> posts;
    private int maxId;

    // METHODS

    // EFFECTS: creates a new instance of the PostIt forum, with loggedIn being false and no logged-in user
    //          instantiates the usernamePassword and communities as empty hashmaps
    //          instantiates and adds communities from DEFAULT_COMMUNITIES with the default about section
    public PostIt() {
        loggedIn = false;
        usernamePasswords = new HashMap<>();
        communities = new HashMap<>();
        posts = new HashMap<>();
        currentlyLoggedInUser = null;
        maxId = 100000;
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

    // REQUIRES: given user has a valid username and password
    // MODIFIES: this
    // EFFECTS: adds the given username and user to the map of registered users
    public void addUser(String username, User user) {
        this.usernamePasswords.put(username, user);
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
    //          with poster being the current user, and adds it to current user's and
    //          community's posts
    //          returns true if post successfull made, returns false if generated id already exists
    public Boolean makeTextPost(String title, String body, String communityChoice) {
        int randomId = (int)(Math.random() * maxId);
        if (posts.containsKey(randomId)) {
            return false;
        } else {
            Post newPost = new TextPost(currentlyLoggedInUser.getUserName(), title, body, communityChoice, randomId);
            posts.put(randomId, newPost);
            communities.get(communityChoice).addPost(randomId);
            currentlyLoggedInUser.addUserPost(randomId);
            return true;
        }
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

    }

    // MODIFIES: this
    // EFFECTS: returns the user's home feed
    //          if logged in, returns feed with posts from user's subscribed communities
    //          throws EmptyFeedException if feed is empty
    //          if not logged in, returns feed with posts from default communities
    public Feed startHomeFeed() throws EmptyFeedException {
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
        }

        activeFeed = new Feed(currentFeed, loggedIn, currentlyLoggedInUser, this);
        return activeFeed;
    }

    // REQUIRES: given communityName is a name of a registered community on PostIt
    // MODIFIES: this
    // EFFECTS: adds posts from the community with the given name to the active feed
    //          and returns the feed
    public Feed showCommunity(String communityName) {
        activeFeed = new Feed(communities.get(communityName).getPosts(),
                loggedIn, currentlyLoggedInUser, this);
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
        return this.posts;
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

    public void setMaxId(int maxId) {
        this.maxId = maxId;
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
        JSONArray posts = new JSONArray();

        for (Post p : this.posts.values()) {
            posts.put(p.toJson());
        }

        return posts;
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
