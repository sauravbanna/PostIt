package model;

import java.util.ArrayList;
import java.util.List;

public class User {

    // CONSTANTS
    public final String DEFAULT_BIO = "No bio yet...";

    // FIELDS
    private String userName;
    private String password;
    private String bio;
    private List<String> subscribedCommunities;

    // METHODS

    // EFFECTS: creates a user with the given username and password
    //          with a default bio and no subscribed communities
    public User(String name, String password) {
        this.userName = name;
        this.password = password;
        this.bio = DEFAULT_BIO;
        this.subscribedCommunities = new ArrayList<String>();

    }


    // EFFECTS: returns user's username
    public String getUserName() {
        return userName;
    }

    // EFFECTS: returns user's password
    public String getPassword() {
        return password;
    }

    // EFFECTS: returns user's bio
    public String getBio() {
        return bio;
    }

    // EFFECTS: returns user's subscribed communities
    public List<String> getSubscribedCommunities() {
        return subscribedCommunities;
    }

    // EFFECTS: adds the given community to user's subscribed communities
    public void subscribeToCommunity(Community c) {
        this.subscribedCommunities.add(c.getCommunityName());
    }

    // REQUIRES: password is at least 8 characters long
    // MODIFIES: this
    // EFFECTS: sets user's password to given string
    public void setPassword(String password) {
        this.password = password;
    }

    // MODIFIES: this
    // EFFECTS: sets user's bio to given string
    public void setBio(String bio) {
        this.bio = bio;
    }




}
