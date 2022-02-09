package model;

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
}
