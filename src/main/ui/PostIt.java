package ui;

import model.Community;
import model.User;

import java.util.HashMap;
import java.util.LinkedList;

public class PostIt {

    // CONSTANTS
    public final String VIEW_COMMUNITY_COMMAND = "/viewcommunity";
    public final String MAKE_POST_COMMAND = "/post";
    public final String LOGOUT_COMMAND = "/logout";
    public final String SEARCH_COMMAND = "/search";
    public final String LOGIN_COMMAND = "/login";
    public final String HOME_COMMAND = "/home";

    public final String USER_PROFILE_STATE = "user";

    // FIELDS
    private Boolean loggedIn;
    private String currentlyLoggedInUser;
    private String userState;

    private HashMap<String, User> usernamePasswords;
    private HashMap<String, Community> communities;

    // METHODS

    public PostIt() {
        loggedIn = false;
        usernamePasswords = new HashMap<>();
        communities = new HashMap<>();
    }

    // EFFECTS: starts the forum for the user
    public void start() {

    }

    // MODIFIES: this
    // EFFECTS: sets feed to posts from default communities if user is not logged in
    //          sets feed to posts from user's communities if logged in
    //          sorted according to current sort option
    public void getPosts() {

    }

    // REQUIRES: password is at least 8 characters long
    // MODIFIES: this
    // EFFECTS: adds the given username and password to the usernamePasswords map
    public void registerAccount(String username, String password) {

    }

    // MODIFIES: this
    // EFFECTS: if the given password matches the given username, logs in user
    public void logIntoAccount(String username, String password) {

    }

    // EFFECTS: prints out the community's description of the given community
    public void printCommunityInfo(Community c) {

    }

    // EFFECTS: print out the user info of the given user
    public void printUserInfo(User u) {

    }

    // EFFECTS:
    public void subscribeToCommunity(Community c) {

    }


}
