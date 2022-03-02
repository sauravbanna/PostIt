package persistence;

import javafx.geometry.Pos;
import model.Community;
import model.PostIt;
import model.User;
import model.content.othercontent.Comment;
import model.content.posts.Post;
import model.content.posts.TextPost;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest extends JsonTest {

    // CONSTANTS
    public static final String VALID_LOCATION = "./data/testWriterValidLocation.json";
    public static final String EMPTY_FORUM = "./data/testWriterEmptyForum.json";

    // FIELDS
    private PostIt postIt;
    private User testUser1;
    private User testUser2;
    private String communityChoice;


    // METHODS
    @BeforeEach
    void runBeforeEach() {
        postIt = new PostIt();
        // valid usernames and passwords
        testUser1 = new User("1", "12345678");
        testUser2 = new User("otherUser", "goodPassword");
        communityChoice = PostIt.DEFAULT_COMMUNITIES.get(0);

    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter jsonWriter = new JsonWriter("./data\0illegal:filename.json");
            jsonWriter.openWriter();
            fail("IOException supposed to be thrown.");
        } catch (IOException ioe) {
            // pass
        }
    }

    @Test
    void testWriterEmptyForumNotLoggedIn() {
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
    void testForumEmptyForumWithUsersNotLoggedIn() {
        try {
            postIt.addUser(testUser1.getUserName(), testUser1);
            postIt.addUser(testUser2.getUserName(), testUser2);

            JsonWriter writer = new JsonWriter(VALID_LOCATION);
            writer.openWriter();
            writer.saveForum(postIt);
            writer.close();

            JsonReader reader = new JsonReader(VALID_LOCATION);
            postIt = reader.read();

            assertFalse(postIt.getLoggedIn());
            assertNull(postIt.getCurrentUser());
            assertFalse(postIt.getUsernamePasswords().isEmpty());
            assertTrue(postIt.getCommunities().isEmpty());
            assertTrue(postIt.getPosts().isEmpty());

            assertEquals(2, postIt.getUsernamePasswords().size());
            checkUser(postIt.getUsernamePasswords().get(testUser1.getUserName()), testUser1);
            checkUser(postIt.getUsernamePasswords().get(testUser2.getUserName()), testUser2);
        } catch (IOException ioe) {
            fail("IOException should not have been thrown.");
        }
    }

    @Test
    void testWriterEmptyForumLoggedIn() {
        try {
            postIt.addUser(testUser1.getUserName(), testUser1);
            postIt.addUser(testUser2.getUserName(), testUser2);

            postIt.login(testUser2.getUserName());

            JsonWriter writer = new JsonWriter(VALID_LOCATION);
            writer.openWriter();
            writer.saveForum(postIt);
            writer.close();

            JsonReader reader = new JsonReader(VALID_LOCATION);
            postIt = reader.read();

            assertTrue(postIt.getLoggedIn());
            checkUser(postIt.getCurrentUser(), testUser2);
        } catch (IOException ioe) {
            fail("IOException should not have been thrown.");
        }
    }

    @Test
    void testWriterEmptyForumWithDefaultCommunities() {
        try {
            postIt.addDefaultCommunitiesCheck();

            JsonWriter writer = new JsonWriter(VALID_LOCATION);
            writer.openWriter();
            writer.saveForum(postIt);
            writer.close();

            JsonReader reader = new JsonReader(VALID_LOCATION);
            postIt = reader.read();

            for (String c : PostIt.DEFAULT_COMMUNITIES) {
                assertEquals(c, postIt.getCommunities().get(c).getCommunityName());
                assertEquals(Community.DEFAULT_ABOUT_SECTION,
                        postIt.getCommunities().get(c).getCommunityAbout());
                assertEquals(Community.DEFAULT_CREATOR, postIt.getCommunities().get(c).getCreator());
            }
        } catch (IOException ioe) {
            fail("IOException should not have been thrown.");
        }

    }

    @Test
    void testWriterEmptyForumNewCommunity() {
        try {
            postIt.addUser(testUser1.getUserName(), testUser1);
            postIt.login(testUser1.getUserName());
            postIt.addCommunity("newCommunity", "some about section");

            JsonWriter writer = new JsonWriter(VALID_LOCATION);
            writer.openWriter();
            writer.saveForum(postIt);
            writer.close();

            JsonReader reader = new JsonReader(VALID_LOCATION);
            postIt = reader.read();

            Community testCommunity = new Community("newCommunity", "some about section",
                    testUser1.getUserName());

            checkCommunity(postIt.getCommunities().get("newCommunity"), testCommunity);

        } catch (IOException ioe) {
            fail("IOException should not have been thrown.");
        }
    }

    @Test
    void testWriterForumWithPosts() {
        try {
            postIt.addUser(testUser1.getUserName(), testUser1);
            postIt.login(testUser1.getUserName());
            postIt.addCommunity("newCommunity", "some about section");
            postIt.addDefaultCommunitiesCheck();
            postIt.makeTextPost("title1", "body1", "newCommunity");
            postIt.makeTextPost("title2", "body2", communityChoice);

            JsonWriter writer = new JsonWriter(VALID_LOCATION);
            writer.openWriter();
            writer.saveForum(postIt);
            writer.close();

            JsonReader reader = new JsonReader(VALID_LOCATION);
            postIt = reader.read();

            assertEquals(1, postIt.getCommunities().get(communityChoice).getPosts().size());
            assertEquals(1, postIt.getCommunities().get("newCommunity").getPosts().size());

            int defaultCommunityId = postIt.getCommunities().get(communityChoice).getPosts().get(0);
            int customCommunityPostId = postIt.getCommunities().get("newCommunity").getPosts().get(0);

            assertTrue(postIt.getPosts().containsKey(defaultCommunityId));
            assertTrue(postIt.getPosts().containsKey(customCommunityPostId));
            assertEquals("title1", postIt.getPosts().get(customCommunityPostId).getTitle());
            assertEquals("title2", postIt.getPosts().get(defaultCommunityId).getTitle());
            assertEquals("body1", postIt.getPosts().get(customCommunityPostId).getBody());
            assertEquals("body2", postIt.getPosts().get(defaultCommunityId).getBody());

        } catch (IOException ioe) {
            fail("IOException should not have been thrown.");
        }
    }

    @Test
    void testWriterForumWithPostsAndComments() {
        try {
            postIt.addUser(testUser1.getUserName(), testUser1);
            postIt.login(testUser1.getUserName());
            postIt.addCommunity("newCommunity", "some about section");
            postIt.addDefaultCommunitiesCheck();
            postIt.makeTextPost("title2", "body2", communityChoice);
            int defaultCommunityId = postIt.getCommunities().get(communityChoice).getPosts().get(0);
            postIt.getPosts().get(defaultCommunityId).addComment(
                    new Comment(testUser2.getUserName(), "wow"));

            JsonWriter writer = new JsonWriter(VALID_LOCATION);
            writer.openWriter();
            writer.saveForum(postIt);
            writer.close();

            JsonReader reader = new JsonReader(VALID_LOCATION);
            postIt = reader.read();

            assertEquals(testUser2.getUserName(),
                    postIt.getPosts().get(defaultCommunityId).getComments().get(0).getOpName());
            assertEquals("wow",
                    postIt.getPosts().get(defaultCommunityId).getComments().get(0).getCommentBody());

        } catch (IOException ioe) {
            fail("IOException should not have been thrown.");
        }
    }

    @Test
    void testWriterForumWithUserSubscribedToCommunities() {
        try {
            postIt.addUser(testUser1.getUserName(), testUser1);
            postIt.login(testUser1.getUserName());
            postIt.addCommunity("newCommunity", "some about section");
            postIt.addDefaultCommunitiesCheck();
            postIt.getUsernamePasswords().get(testUser1.getUserName())
                    .subscribeToCommunity(postIt.getCommunities().get(communityChoice));
            postIt.getUsernamePasswords().get(testUser1.getUserName())
                    .subscribeToCommunity(postIt.getCommunities().get("newCommunity"));

            JsonWriter writer = new JsonWriter(VALID_LOCATION);
            writer.openWriter();
            writer.saveForum(postIt);
            writer.close();

            JsonReader reader = new JsonReader(VALID_LOCATION);
            postIt = reader.read();

            assertEquals(2, postIt.getCurrentUser()
                    .getSubscribedCommunities().size());
            assertEquals(communityChoice, postIt.getCurrentUser()
                    .getSubscribedCommunities().get(0));
            assertEquals("newCommunity", postIt.getCurrentUser()
                    .getSubscribedCommunities().get(1));

        } catch (IOException ioe) {
            fail("IOException should not have been thrown.");
        }
    }

}
