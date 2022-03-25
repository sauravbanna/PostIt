package testmodel.testcontent.othercontent;

import model.content.othercontent.Comment;
import org.junit.jupiter.api.*;
import testmodel.testcontent.ContentTest;

import static org.junit.jupiter.api.Assertions.*;

public class CommentTest extends ContentTest {

    Comment testComment;

    @BeforeEach
    void runBefore() {
        testComment = new Comment("someUser", "I love this post!");
        testContent = new Comment("someOtherUser", "I also like this!");
    }

    @Test
    void testConstructor() {
        assertEquals("someUser", testComment.getOpName());
        assertEquals("I love this post!", testComment.getCommentBody());
        assertEquals(0, testComment.getLikes());
        assertEquals(0, testComment.getDislikes());
    }
}
