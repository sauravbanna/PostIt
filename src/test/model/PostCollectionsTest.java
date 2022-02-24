package model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public abstract class PostCollectionsTest {

    PostCollections testPostCollection;

    @Test
    void testAddUserPost() {
        assertTrue(testPostCollection.getPosts().isEmpty());
        testPostCollection.addPosts(0);
        assertEquals(1, testPostCollection.getPosts().size());
        assertEquals(0, testPostCollection.getPosts().get(0));
        testPostCollection.addPosts(2);
        assertEquals(2, testPostCollection.getPosts().size());
        assertEquals(2, testPostCollection.getPosts().get(1));
    }

}
