package model;

import org.json.JSONObject;
import persistence.Writable;

// A group on PostIt with a community name, about section, subscriber count, creator name, and list of post ids
// that user can view and post to
public class Community extends PostCollections implements Writable {

    // CONSTANTS
    public static final String DEFAULT_CREATOR = "PostIt";
    public static final String DEFAULT_ABOUT_SECTION = "This is a default community.";

    public static final String NAME_KEY = "name";
    public static final String ABOUT_KEY = "about";
    public static final String SUBCOUNT_KEY = "subs";
    public static final String POSTS_KEY = "posts";
    public static final String CREATOR_KEY = "creator";

    // FIELDS
    private final String communityName;
    private final String communityAbout;
    private int subCount;
    private final String creator;

    // METHODS

    // Constructor
    // REQUIRES: creator name is either null or a registered user on PostIt
    //           about section is either null or a String
    // EFFECTS: creates a new community with the given name with 0 posts and subscribers
    //          if about section is given, sets that as community about section, else
    //          if about section is null, sets about section of community to DEFAULT_ABOUT_SECTION
    //          if creator name is given, sets that as community creator, else
    //          if creator is null, sets creator of community to DEFAULT_CREATOR
    public Community(String name, String about, String creator) {
        super();
        this.communityName = name;
        this.subCount = 0;
        if (about != null) {
            this.communityAbout = about;
        } else {
            this.communityAbout = DEFAULT_ABOUT_SECTION;
        }
        if (creator != null) {
            this.creator = creator;
        } else {
            this.creator = DEFAULT_CREATOR;
        }
    }

    // EFFECTS: return the name of this community
    public String getCommunityName() {
        return communityName;
    }

    // EFFECTS: return the about section of this community
    public String getCommunityAbout() {
        return communityAbout;
    }

    // EFFECTS: return the subscriber count of this community
    public int getSubCount() {
        return subCount;
    }

    // REQUIRES: given postId is valid (between 0 and PostIt.MAX_ID)
    //           and is not a duplicate id of an existing post on PostIt
    // MODIFIES: this
    // EFFECTS: adds a post's post id to the community
    public void addPost(Integer postId) {
        super.addPosts(postId);
    }

    // MODIFIES: this
    // EFFECTS: adds 1 subscriber to this community's sub count
    public void addSubscriber() {
        this.subCount++;
    }

    // REQUIRES: subCount > 0
    // MODIFIES: this
    // EFFECTS: removes 1 subscriber from this community's sub count
    public void removeSubscriber() {
        this.subCount--;
    }

    // EFFECTS: returns the creator name of the community
    public String getCreator() {
        return this.creator;
    }

    // REQUIRES: given subcount >= 0
    // MODIFIES: this
    // EFFECTS: sets the community's subcount to the given number
    public void setSubCount(int subCount) {
        this.subCount = subCount;
    }

    @Override
    public JSONObject toJson() {
        JSONObject community = new JSONObject();
        community.put(NAME_KEY, communityName);
        community.put(ABOUT_KEY, communityAbout);
        community.put(CREATOR_KEY, creator);
        community.put(SUBCOUNT_KEY, subCount);
        community.put(POSTS_KEY, postsToJson(getPosts()));
        return community;
    }

}
