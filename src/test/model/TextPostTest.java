package model;


import model.content.posts.TextPost;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TextPostTest extends PostTest {

    TextPost testTextPost;

    @BeforeEach
    void runBefore() {
        testPost = new TextPost("someOtherUser", "My Second Post", "This is my post!");
        testTextPost = new TextPost("someUser", "My First Post", "Here is my Post!");
        testContent = new TextPost("thirdUser", "Another Post", "Here is my third post!");
    }

    @Test
    void testConstructor() {
        assertEquals("someUser", testTextPost.getOpName());
        assertEquals("My First Post", testTextPost.getTitle());
        assertEquals("Here is my Post!", testTextPost.getBody());
        assertEquals(0, testTextPost.getLikes());
        assertEquals(0, testTextPost.getDislikes());
        assertEquals(0, testTextPost.getCommentCount());
        assertTrue(testTextPost.getComments().isEmpty());
    }



}
