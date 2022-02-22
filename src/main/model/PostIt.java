package model;

import exceptions.EmptyFeedException;
import model.content.posts.Post;
import model.content.posts.TextPost;
import org.json.JSONObject;
import persistence.Writable;
import ui.Feed;

import java.util.*;

// An object that contains the PostIt forum's communities, posts, users,
public class PostIt implements Writable {

    // CONSTANTS

    public static final int MAX_ID = 1000000;

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

    // METHODS

    // EFFECTS: creates a new instance of the PostIt forum, with loggedIn being false and no logged-in user
    //          instantiates the usernamePassword and communities as empty hashmaps
    //          instantiates and adds communities from DEFAULT_COMMUNITIES with the default about section
    public PostIt() {
        loggedIn = false;
        usernamePasswords = new HashMap<>();
        communities = new HashMap<>();
        posts = new HashMap<>();

        for (String community : DEFAULT_COMMUNITIES) {
            communities.put(community, new Community(community, null, null));
        }

        currentlyLoggedInUser = null;
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

    // REQUIRES: given user is a registered member of PostIt
    // EFFECTS: displays the posts of the given user
    public String viewUserPosts(User u) {
        Feed userPosts = new Feed(u.getUserPosts(), loggedIn, currentlyLoggedInUser, this);
        return userPosts.start();
    }

    // REQUIRES: currentlyLoggedInUser != null, and loggedIn is True
    // MODIFIES: this
    // EFFECTS: creates a new text post with the given title and body in the given community,
    //          with poster being the current user, and adds it to current user's and
    //          community's posts
    public void makeTextPost(String title, String body, String communityChoice) {
        int randomId = 0;
        while (posts.containsKey(randomId)) {
            randomId = (int)(Math.random() * MAX_ID);
        }
        Post newPost = new TextPost(currentlyLoggedInUser.getUserName(), title, body, communityChoice, randomId);
        posts.put(randomId, newPost);
        communities.get(communityChoice).addPost(randomId);
        currentlyLoggedInUser.addUserPost(randomId);
    }

    // REQUIRES: currentlyLoggedInUser != null, and loggedIn is True
    // MODIFIES: this
    // EFFECTS: changes the currently logged in user's bio to the new bio
    public void updateBio(String newBio) {
        currentlyLoggedInUser.setBio(newBio);
    }

    // REQUIRES: user is logged in (loggedIn is true) and currentlyLoggedInUser != null
    // MODIFIES: this
    // EFFECTS: adds a community with the given name and about section, created by
    //          the currently logged in user to the list of communities on the forum
    public void addCommunity(String name, String about) {
        this.communities.put(name, new Community(name, about, currentlyLoggedInUser.getUserName()));

    }

    // EFFECTS: starts the user's home feed
    //          if logged in, shows posts from user's subscribed communities
    //          if not logged in, shows posts from default communities
    //          throws EmptyFeedException if feed is empty
    public String startHomeFeed() throws EmptyFeedException {
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
        return activeFeed.start();
    }

    // MODIFIES: this
    // EFFECTS: adds posts from the community with the given name to the active feed
    //          and starts the feed
    public String showCommunity(String userInput) {
        activeFeed = new Feed(communities.get(userInput).getPosts(),
                loggedIn, currentlyLoggedInUser, this);
        return activeFeed.start();
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


    @Override
    public JSONObject toJson() {
        return null;
    }

    public JSONObject postsToJson() {
        return null;
    }

    public JSONObject communitiesToJson() {
        return null;
    }

    public JSONObject usersToJson() {
        return null;
    }
}
