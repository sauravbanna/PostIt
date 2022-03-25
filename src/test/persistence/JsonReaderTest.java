package persistence;

import model.Community;
import model.PostIt;
import model.User;
import model.content.othercontent.Comment;
import model.content.posts.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest extends JsonTest {

    // CONSTANTS
    public static final String EMPTY_FORUM = "./data/testReaderEmptyForum.json";
    public static final String FORUM_WITH_DATA =
            "./data/testReaderForumWithData.json";

    // FIELDS
    private PostIt postIt;
    private User testUser1;
    private Post testPost1;
    private Post testPost2;
    private Post testImagePost;
    private List<Comment> testComments;
    private List<Community> testCommunities;

    // METHODS
    @BeforeEach
    void runBeforeEach() {
        postIt = new PostIt();
        // valid usernames and passwords
        testUser1 = new User("1", "12345678");


    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            PostIt postIt = reader.read();
            fail("IOException supposed to be thrown.");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyForumNotLoggedIn() {
        try {
            JsonWriter writer = new JsonWriter(EMPTY_FORUM);
            writer.openWriter();
            writer.saveForum(postIt);
            writer.close();

            JsonReader reader = new JsonReader(EMPTY_FORUM);
            postIt = reader.read();

            assertFalse(postIt.getLoggedIn());
            assertNull(postIt.getCurrentUser());
            assertTrue(postIt.getUsernamePasswords().isEmpty());
            assertTrue(postIt.getCommunities().isEmpty());
            assertTrue(postIt.getPosts().isEmpty());
        } catch (IOException ioe) {
            fail("IOException should not have been thrown.");
        }
    }

    @Test
    void testReaderForumWithData() {
        try {
            JsonReader reader = new JsonReader(FORUM_WITH_DATA);
            postIt = reader.read();

            assertTrue(postIt.getLoggedIn());

            initAssertUser();
            testPost2 = initAssertPost("title2", "body2", "1", "funny", 2,
                    0, 0, 26768);
            testPost1 = initAssertPost("title1", "body1", "1", "newCommunity", 1,
                    1, 3, 19766);
            testImagePost = initAssertImagePost("Flower", "./data/images/flower.jpg", "1",
                    "news", 0, 0, 0, 47758);
            testComments = initAssertComments();

            checkUser(postIt.getCurrentUser(), testUser1);
            checkPost(postIt.getPosts().get(19766), testPost1);
            checkPost(postIt.getPosts().get(26768), testPost2);
            checkPost(postIt.getPosts().get(47758), testImagePost);
            checkComments(postIt.getPosts().get(19766).getComments(), testComments);

            initAssertCommunities();
            checkCommunities(postIt.getCommunities(), testCommunities);
        } catch (IOException ioe) {
            fail("IOException should not have been thrown.");
        }
    }

    void initAssertUser() {
        List<Integer> posts = new ArrayList<>();
        posts.add(19766);
        posts.add(26768);
        List<Integer> likedPosts = new ArrayList<>();
        likedPosts.add(26768);
        List<Integer> dislikedPosts = new ArrayList<>();
        dislikedPosts.add(19766);
        List<String> subscribedCommunities = new ArrayList<>();
        subscribedCommunities.add("funny");


        testUser1.setPosts(posts);
        testUser1.setSubscribedCommunities(subscribedCommunities);
        testUser1.setDislikedPosts(dislikedPosts);
        testUser1.setLikedPosts(likedPosts);
    }

    Post initAssertPost(String title, String body, String username, String community, int likes,
                         int dislikes, int comments, int id) {
        Post post = new TextPost(username, title, body, community, id);
        post.setCommentCount(comments);
        post.setLikes(likes);
        post.setDislikes(dislikes);
        return post;
    }

    Post initAssertImagePost(String title, String image, String username, String community, int likes,
                             int dislikes, int comments, int id) {
        Post post = new ImagePost(username, title, image, community, id);
        post.setCommentCount(comments);
        post.setLikes(likes);
        post.setDislikes(dislikes);
        return post;
    }

    List<Comment> initAssertComments() {
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("1", "wow"));
        comments.add(new Comment("1", "this is amazing"));
        comments.add(new Comment("1", "great!"));
        return comments;
    }

    void initAssertCommunities() {
        testCommunities = new ArrayList<>();

        Community funny = new Community("funny", null, null);
        funny.addSubscriber();
        List<Integer> posts = new ArrayList<>();
        posts.add(26768);
        funny.setPosts(posts);

        Community news = new Community("funny", null, null);
        posts = new ArrayList<>();
        posts.add(47758);
        news.setPosts(posts);

        Community newCommunity = new Community("newCommunity", "some about section", "1");
        List<Integer> newPosts = new ArrayList<>();
        newPosts.add(19766);
        newCommunity.setPosts(newPosts);
        for (String c : PostIt.DEFAULT_COMMUNITIES) {
            if (!c.equals("funny") && !c.equals("news")) {
                testCommunities.add(new Community(c, null, null));
            }
        }
    }
}
