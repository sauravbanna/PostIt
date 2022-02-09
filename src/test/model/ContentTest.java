package model;

import model.content.Content;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ContentTest {

    Content testPost;

    @Test
    void testLike() {
        assertEquals(0, testPost.getLikes());
        testPost.like();
        assertEquals(1, testPost.getLikes());
        testPost.like();
        assertEquals(2, testPost.getLikes());
    }

    @Test
    void testDislike() {
        testPost.like();
        testPost.like();
        testPost.like();
        assertEquals(3, testPost.getLikes());
        testPost.dislike();
        assertEquals(2, testPost.getLikes());
        testPost.dislike();
        assertEquals(1, testPost.getLikes());
        testPost.dislike();
        assertEquals(0, testPost.getLikes());
    }
}
