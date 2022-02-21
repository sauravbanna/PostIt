package model;

import model.content.posts.TextPost;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CommunityTest {

    Community testCommunityDefault;
    Community testCommunityUserCreated;

    @BeforeEach
    void runBefore() {
        testCommunityDefault = new Community("funnypics", "The place to post funny pictures.", null);
        testCommunityUserCreated = new Community("anotherCommunity", "A user-created community", "someUser");
    }

    @Test
    void testConstructor() {
        assertEquals("funnypics", testCommunityDefault.getCommunityName());
        assertEquals("The place to post funny pictures.", testCommunityDefault.getCommunityAbout());
        assertEquals(0, testCommunityDefault.getSubCount());
        assertTrue(testCommunityDefault.getPosts().isEmpty());
        assertEquals("PostIt", testCommunityDefault.getCreator());
        assertEquals("someUser", testCommunityUserCreated.getCreator());
    }

    @Test
    void testAddPost() {
        assertTrue(testCommunityDefault.getPosts().isEmpty());

        testCommunityDefault.addPost();

        assertEquals(1, testCommunityDefault.getPosts().size());
        assertEquals("someUser", testCommunityDefault.getPosts().get(0).getOpName());
        assertEquals("someTitle", testCommunityDefault.getPosts().get(0).getTitle());
        assertEquals("gaming", testCommunityDefault.getPosts().get(0).getCommunity());

        testCommunityDefault.addPost(new TextPost("someOtherUser", "someOtherTitle",
                "someOtherBody", "news"));

        assertEquals(2, testCommunityDefault.getPosts().size());
        assertEquals("someOtherUser", testCommunityDefault.getPosts().get(1).getOpName());
        assertEquals("someOtherTitle", testCommunityDefault.getPosts().get(1).getTitle());
        assertEquals("news", testCommunityDefault.getPosts().get(1).getCommunity());
    }
}
