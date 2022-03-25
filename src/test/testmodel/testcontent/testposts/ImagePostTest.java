package testmodel.testcontent.testposts;

import model.content.posts.ImagePost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ImagePostTest extends PostTest {

    ImagePost testImagePost;

    @BeforeEach
    void runBefore() {
        testPost = new ImagePost("someOtherUser", "My Second Post", "./data/images/background.png", "funny", 0);
        testImagePost = new ImagePost("someUser", "My First Post", "./data/images/47758.jpg", "sports", 1);
        testContent = new ImagePost("thirdUser", "Another Post", "./data/images/19525.jpg", "gaming", 2);
    }

    @Test
    void testConstructor() {
        assertEquals("someUser", testImagePost.getOpName());
        assertEquals("My First Post", testImagePost.getTitle());
        assertEquals("./data/images/47758.jpg", testImagePost.getBody());
        assertEquals("sports", testImagePost.getCommunity());
        assertEquals(0, testImagePost.getLikes());
        assertEquals(0, testImagePost.getDislikes());
        assertEquals(0, testImagePost.getCommentCount());
        assertTrue(testImagePost.getComments().isEmpty());
        assertEquals(1, testImagePost.getId());
    }

}
