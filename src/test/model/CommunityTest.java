package model;

import model.content.posts.TextPost;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CommunityTest extends PostCollectionsTest {

    Community testCommunityDefault;
    Community testCommunityUserCreated;

    @BeforeEach
    void runBefore() {
        testCommunityDefault = new Community("funnypics", null, null);
        testCommunityUserCreated = new Community("anotherCommunity", "A user-created community", "someUser");
        testPostCollection = new Community("thirdCommunity", null, null);
    }

    @Test
    void testConstructor() {
        assertEquals("funnypics", testCommunityDefault.getCommunityName());
        assertEquals(Community.DEFAULT_ABOUT_SECTION, testCommunityDefault.getCommunityAbout());
        assertEquals("A user-created community", testCommunityUserCreated.getCommunityAbout());
        assertEquals(0, testCommunityDefault.getSubCount());
        assertTrue(testCommunityDefault.getPosts().isEmpty());
        assertEquals(Community.DEFAULT_CREATOR, testCommunityDefault.getCreator());
        assertEquals("someUser", testCommunityUserCreated.getCreator());
    }

    @Test
    void testAddSubscriber() {
        assertEquals(0, testCommunityDefault.getSubCount());
        testCommunityDefault.addSubscriber();
        assertEquals(1, testCommunityDefault.getSubCount());
        testCommunityDefault.addSubscriber();
        assertEquals(2, testCommunityDefault.getSubCount());


    }

    @Test
    void testRemoveSubscriber() {
        testCommunityDefault.addSubscriber();
        testCommunityDefault.addSubscriber();
        testCommunityDefault.addSubscriber();

        assertEquals(3, testCommunityDefault.getSubCount());
        testCommunityDefault.removeSubscriber();
        assertEquals(2, testCommunityDefault.getSubCount());
        testCommunityDefault.removeSubscriber();
        assertEquals(1, testCommunityDefault.getSubCount());


    }
}
