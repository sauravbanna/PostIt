package model;

import model.content.posts.TextPost;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CommunityTest {

    Community testCommunity;

    @BeforeEach
    void runBefore() {
        testCommunity = new Community("funnypics", "The place to post funny pictures.");
    }

    @Test
    void testConstructor() {
        assertEquals("funnypics", testCommunity.getCommunityName());
        assertEquals("The place to post funny pictures.", testCommunity.getCommunityAbout());
        assertEquals(0, testCommunity.getSubCount());
        assertTrue(testCommunity.getPosts().isEmpty());
    }

    @Test
    void testAddPost() {
        assertTrue(testCommunity.getPosts().isEmpty());

        testCommunity.addPost(new TextPost("someUser", "someTitle", "someBody", "gaming"));

        assertEquals(1, testCommunity.getPosts().size());
        assertEquals("someUser", testCommunity.getPosts().get(0).getOpName());
        assertEquals("someTitle", testCommunity.getPosts().get(0).getTitle());
        assertEquals("gaming", testCommunity.getPosts().get(0).getCommunity());

        testCommunity.addPost(new TextPost("someOtherUser", "someOtherTitle",
                "someOtherBody", "news"));

        assertEquals(2, testCommunity.getPosts().size());
        assertEquals("someOtherUser", testCommunity.getPosts().get(1).getOpName());
        assertEquals("someOtherTitle", testCommunity.getPosts().get(1).getTitle());
        assertEquals("news", testCommunity.getPosts().get(1).getCommunity());
    }
}
