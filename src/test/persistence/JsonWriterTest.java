package persistence;

import javafx.geometry.Pos;
import model.Community;
import model.PostIt;
import model.User;
import model.content.othercontent.Comment;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {

    // CONSTANTS
    public static final String VALID_LOCATION = "./data/forum.json";

    // FIELDS
    private PostIt postIt;
    private User testUser1;
    private User testUser2;


    // METHODS
    @BeforeEach
    void runBeforeEach() {
        postIt = new PostIt();
        // valid usernames and passwords
        testUser1 = new User("1", "12345678");
        testUser2 = new User("otherUser", "goodPassword");

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
            JsonWriter writer = new JsonWriter(VALID_LOCATION);
            writer.openWriter();
            writer.saveForum(postIt);
            writer.close();

            JsonReader reader = new JsonReader(VALID_LOCATION);
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
            assertEquals(testUser1.getUserName(),
                    postIt.getUsernamePasswords().get(testUser1.getUserName()).getUserName());
            assertEquals(testUser2.getUserName(),
                    postIt.getUsernamePasswords().get(testUser2.getUserName()).getUserName());
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
            assertEquals(testUser2.getUserName(), postIt.getCurrentUser().getUserName());
            assertEquals(testUser2.getBio(), postIt.getCurrentUser().getBio());
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

            assertEquals("newCommunity",
                    postIt.getCommunities().get("newCommunity").getCommunityName());
            assertEquals("some about section",
                    postIt.getCommunities().get("newCommunity").getCommunityAbout());
            assertEquals(testUser1.getUserName(),
                    postIt.getCommunities().get("newCommunity").getCreator());

        } catch (IOException ioe) {
            fail("IOException should not have been thrown.");
        }
    }

    @Test
    void testWriterForumWithPosts() {
        try {
            String communityChoice = PostIt.DEFAULT_COMMUNITIES.get(0);

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
            String communityChoice = PostIt.DEFAULT_COMMUNITIES.get(0);

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
            String communityChoice = PostIt.DEFAULT_COMMUNITIES.get(0);

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
