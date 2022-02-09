package model;

import model.content.othercontent.Comment;
import model.content.posts.Post;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class PostTest extends ContentTest {

    Post testPost;

    @Test
    void testAddComment() {
        assertEquals(0, testPost.getCommentCount());
        assertTrue(testPost.getComments().isEmpty());

        testPost.addComment(new Comment("user1", "wow"));

        assertEquals(1, testPost.getCommentCount());
        assertEquals(1, testPost.getComments().size());
        assertEquals("user1", testPost.getComments().get(0).getOpName());
        assertEquals("wow", testPost.getComments().get(0).getCommentBody());

        Comment secondComment = new Comment("user2", "also wow");
        secondComment.like();
        secondComment.like();
        secondComment.like();
        secondComment.dislike();

        testPost.addComment(secondComment);

        assertEquals(2, testPost.getCommentCount());
        assertEquals(2, testPost.getComments().size());

        List<Comment> bro = testPost.getComments();

        Comment justAdded = testPost.getComments().get(1);

        assertEquals("user2", testPost.getComments().get(1).getOpName());
        assertEquals("also wow", testPost.getComments().get(1).getCommentBody());
        assertEquals(3, testPost.getComments().get(1).getLikes());
        assertEquals(1, testPost.getComments().get(1).getDislikes());

    }
}
