package persistence;

import model.PostIt;

import java.io.FileNotFoundException;
import java.io.PrintWriter;


// CITATION
// JsonSerializationDemo from CPSC 210

public class JsonWriter {

    // CONSTANTS

    // FIELDS

    private static final int INDENT_FACTOR = 4;
    private PrintWriter writer;
    private String communities;
    private String posts;
    private String users;

    // METHODS

    public JsonWriter(String communities, String users, String posts) {
        this.communities = communities;
        this.users = users;
        this.posts = posts;
    }

    public void openPostsWriter() throws FileNotFoundException {
        writer = new PrintWriter(posts);
    }

    public void openCommunitiesWriter() throws FileNotFoundException {
        writer = new PrintWriter(communities);
    }

    public void openUsersWriter() throws FileNotFoundException {
        writer = new PrintWriter(users);
    }

    public void saveForum(PostIt postIt) {

    }

    public void close() {
        writer.close();
    }



}
