package model;

import model.content.othercontent.Comment;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CommentTest {

    Comment testComment;

    @BeforeEach
    void runBefore() {
        testComment = new Comment("someUser", "I love this post!");
    }

    @Test
    void testConstructor() {
        assertEquals("someUser", testComment.getOpName());
        assertEquals("I love this post!", testComment.getBody());
        assertEquals(0, testComment.getLikes());
        assertEquals(0, testComment.getDislikes());
    }

    @Test
    void testLike() {
        assertEquals(0, testComment.getLikes());
        testComment.like();
        assertEquals(1, testComment.getLikes());
        testComment.like();
        assertEquals(2, testComment.getLikes());
    }

    @Test
    void testDislike() {
        testComment.like();
        testComment.like();
        testComment.like();
        assertEquals(3, testComment.getLikes());
        testComment.dislike();
        assertEquals(2, testComment.getLikes());
        testComment.dislike();
        assertEquals(1, testComment.getLikes());
        testComment.dislike();
        assertEquals(0, testComment.getLikes());
    }
}
