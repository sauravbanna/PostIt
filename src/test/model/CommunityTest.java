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

        testCommunityDefault.addPost(0);

        assertEquals(1, testCommunityDefault.getPosts().size());
        assertEquals(0, testCommunityDefault.getPosts().get(0));

        testCommunityDefault.addPost(1);

        assertEquals(2, testCommunityDefault.getPosts().size());
        assertEquals(1, testCommunityDefault.getPosts().get(1));
    }
}
