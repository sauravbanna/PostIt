package model;

import model.content.Content;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public abstract class ContentTest {

    Content testContent;

    @Test
    void testLike() {
        assertEquals(0, testContent.getLikes());
        testContent.like();
        assertEquals(1, testContent.getLikes());
        testContent.like();
        assertEquals(2, testContent.getLikes());
    }

    @Test
    void testDislike() {
        assertEquals(0, testContent.getDislikes());
        testContent.dislike();
        assertEquals(1, testContent.getDislikes());
        testContent.dislike();
        assertEquals(2, testContent.getDislikes());
        testContent.dislike();
        assertEquals(3, testContent.getDislikes());
    }

    @Test
    void testUnDislike() {
        testContent.dislike();
        testContent.dislike();
        testContent.dislike();

        assertEquals(3, testContent.getDislikes());
        testContent.unDislike();
        assertEquals(2, testContent.getDislikes());
        testContent.unDislike();
        assertEquals(1, testContent.getDislikes());
        testContent.unDislike();
        assertEquals(0, testContent.getDislikes());
    }

    @Test
    void testUnLike() {
        testContent.like();
        testContent.like();
        testContent.like();

        assertEquals(3, testContent.getLikes());
        testContent.unLike();
        assertEquals(2, testContent.getLikes());
        testContent.unLike();
        assertEquals(1, testContent.getLikes());
        testContent.unLike();
        assertEquals(0, testContent.getLikes());
    }
}
