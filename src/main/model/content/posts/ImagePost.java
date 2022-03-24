package model.content.posts;

import org.json.JSONObject;

public class ImagePost extends Post {

    // CONSTANTS
    public static final String IMAGE_LOCATION_KEY = "image";


    // FIELDS
    private final String imageLocation;

    // METHODS

    // Constructor
    // REQUIRES: given name is a registered user, given community is an existing community on PostIt, given id
    //          is not already assigned to another post on PostIt
    // EFFECTS: creates a new Text Post with given poster name, title, id, and body in the given community
    public ImagePost(String opName, String title, String image, String community, int id) {
        super(opName, title, community, id);
        this.imageLocation = image;
    }

    // EFFECTS: returns the image post's file location
    public String getBody() {
        return imageLocation;
    }

    @Override
    public JSONObject toJson() {
        JSONObject imagePost = super.toJson();
        imagePost.put(IMAGE_LOCATION_KEY, imageLocation);
        return imagePost;
    }
}
