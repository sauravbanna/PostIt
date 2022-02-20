package model;


import model.content.posts.Post;
import model.content.posts.TextPost;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    User testUser;
    Post testPost1;
    Post testPost2;
    Post testPost3;

    @BeforeEach
    void runBefore() {
        testUser = new User("coolName", "abcd1234");
        testPost1 = new TextPost("someUser", "someTitle", "someBody", "gaming");
        testPost2 = new TextPost("someOtherUser", "someOtherTitle", "someOtherBody", "news");
        testPost3 = new TextPost("thirdUser", "thirdTitle", "thirdBody", "sports");
    }

    @Test
    void testConstructor() {
        assertEquals("coolName", testUser.getUserName());
        assertEquals("abcd1234", testUser.getPassword());
        assertEquals(testUser.DEFAULT_BIO, testUser.getBio());
        assertTrue(testUser.getSubscribedCommunities().isEmpty());
        assertTrue(testUser.getDislikedPosts().isEmpty());
        assertTrue(testUser.getLikedPosts().isEmpty());
    }

    @Test
    void testSubscribeToCommunity() {
        assertTrue(testUser.getSubscribedCommunities().isEmpty());

        Community community1 = new Community("funny", "funny stuff", null);

        testUser.subscribeToCommunity(community1);

        assertEquals(1, testUser.getSubscribedCommunities().size());
        assertEquals("funny", testUser.getSubscribedCommunities().get(0));

        Community community2 = new Community("news", "The latest news", null);
        testUser.subscribeToCommunity(community2);

        assertEquals(2, testUser.getSubscribedCommunities().size());
        assertEquals("news", testUser.getSubscribedCommunities().get(1));
    }

    @Test
    void testSetBio() {
        assertEquals(testUser.DEFAULT_BIO, testUser.getBio());

        testUser.setBio("I am a cool person");

        assertEquals("I am a cool person", testUser.getBio());

        testUser.setBio("Thanks for visiting!");

        assertEquals("Thanks for visiting!", testUser.getBio());
    }

    @Test
    void testSetPassword() {
        assertEquals("abcd1234", testUser.getPassword());

        testUser.setPassword("asdf!@#$");

        assertEquals("asdf!@#$", testUser.getPassword());

        testUser.setPassword("strongpassword");

        assertEquals("strongpassword", testUser.getPassword());
    }

    @Test
    void testAddLikedPostWhenPostInAlreadyLiked() {
        assertEquals(0, testPost1.getLikes());
        assertEquals(0, testPost2.getLikes());

        testUser.addLikedPost(testPost1);
        testUser.addLikedPost(testPost2);

        assertEquals(1, testPost1.getLikes());
        assertEquals(1, testPost2.getLikes());

        assertEquals(2, testUser.getLikedPosts().size());

        assertEquals("You've already liked this post before!", testUser.addLikedPost(testPost1));
        assertEquals("You've already liked this post before!", testUser.addLikedPost(testPost2));

        assertEquals(1, testPost1.getLikes());
        assertEquals(1, testPost2.getLikes());

        assertEquals(0, testPost3.getLikes());

        assertEquals("Post added to liked posts", testUser.addLikedPost(testPost3));

        assertEquals(1, testPost3.getLikes());
        assertEquals(3, testUser.getLikedPosts().size());

        assertEquals("You've already liked this post before!", testUser.addLikedPost(testPost3));

        assertEquals(1, testPost3.getLikes());
    }

    @Test
    void testAddLikedPostWhenPostNotInLikedOrDislikedPosts() {
        assertTrue(testUser.getLikedPosts().isEmpty());
        assertTrue(testUser.getDislikedPosts().isEmpty());

        assertEquals(0, testPost1.getLikes());
        assertEquals(0, testPost2.getLikes());
        assertEquals(0, testPost3.getLikes());

        assertEquals("Post added to liked posts", testUser.addLikedPost(testPost1));

        assertEquals(1, testPost1.getLikes());

        assertEquals(1, testUser.getLikedPosts().size());
        assertTrue(testUser.getDislikedPosts().isEmpty());
        assertEquals("someUser", testUser.getLikedPosts().get(0).getOpName());
        assertEquals("someTitle", testUser.getLikedPosts().get(0).getTitle());
        assertEquals("gaming", testUser.getLikedPosts().get(0).getCommunity());

        assertEquals("Post added to liked posts", testUser.addLikedPost(testPost2));

        assertEquals(1, testPost2.getLikes());

        assertEquals(2, testUser.getLikedPosts().size());
        assertTrue(testUser.getDislikedPosts().isEmpty());
        assertEquals("someOtherUser", testUser.getLikedPosts().get(1).getOpName());
        assertEquals("someOtherTitle", testUser.getLikedPosts().get(1).getTitle());
        assertEquals("news", testUser.getLikedPosts().get(1).getCommunity());

        assertEquals("Post added to liked posts", testUser.addLikedPost(testPost3));

        assertEquals(1, testPost3.getLikes());

        assertEquals(3, testUser.getLikedPosts().size());
        assertTrue(testUser.getDislikedPosts().isEmpty());
        assertEquals("thirdUser", testUser.getLikedPosts().get(2).getOpName());
        assertEquals("thirdTitle", testUser.getLikedPosts().get(2).getTitle());
        assertEquals("sports", testUser.getLikedPosts().get(2).getCommunity());
    }

    @Test
    void testAddLikedPostWhenPostNotInLikedPostsButInDislikedPosts() {
        assertTrue(testUser.getLikedPosts().isEmpty());
        assertTrue(testUser.getDislikedPosts().isEmpty());

        assertEquals(0, testPost1.getDislikes());
        assertEquals(0, testPost2.getDislikes());
        assertEquals(0, testPost3.getDislikes());

        testUser.addDislikedPost(testPost1);
        testUser.addDislikedPost(testPost2);
        testUser.addDislikedPost(testPost3);

        assertEquals(1, testPost1.getDislikes());
        assertEquals(1, testPost2.getDislikes());
        assertEquals(1, testPost3.getDislikes());
        assertEquals(0, testPost1.getLikes());
        assertEquals(0, testPost2.getLikes());
        assertEquals(0, testPost3.getLikes());

        assertEquals(3, testUser.getDislikedPosts().size());
        assertTrue(testUser.getLikedPosts().isEmpty());

        assertEquals("Post added to liked posts", testUser.addLikedPost(testPost1));

        assertEquals(2, testUser.getDislikedPosts().size());
        assertEquals(1, testUser.getLikedPosts().size());

        assertEquals(0, testPost1.getDislikes());
        assertEquals(1, testPost1.getLikes());

        assertEquals("Post added to liked posts", testUser.addLikedPost(testPost2));

        assertEquals(1, testUser.getDislikedPosts().size());
        assertEquals(2, testUser.getLikedPosts().size());

        assertEquals(0, testPost2.getDislikes());
        assertEquals(1, testPost2.getLikes());

        assertEquals("Post added to liked posts", testUser.addLikedPost(testPost3));

        assertEquals(0, testUser.getDislikedPosts().size());
        assertEquals(3, testUser.getLikedPosts().size());

        assertEquals(0, testPost3.getDislikes());
        assertEquals(1, testPost3.getLikes());
    }

    @Test
    void testAddDislikedWhenPostAlreadyInDisliked() {
        assertEquals(0, testPost1.getDislikes());
        assertEquals(0, testPost2.getDislikes());

        testUser.addDislikedPost(testPost1);
        testUser.addDislikedPost(testPost2);

        assertEquals(1, testPost1.getDislikes());
        assertEquals(1, testPost2.getDislikes());

        assertEquals(2, testUser.getDislikedPosts().size());

        assertEquals("You've already disliked this post before!", testUser.addDislikedPost(testPost1));
        assertEquals("You've already disliked this post before!", testUser.addDislikedPost(testPost2));

        assertEquals(1, testPost1.getDislikes());
        assertEquals(1, testPost2.getDislikes());

        assertEquals(0, testPost3.getDislikes());

        assertEquals("Post added to disliked posts", testUser.addDislikedPost(testPost3));

        assertEquals(1, testPost3.getDislikes());
        assertEquals(3, testUser.getDislikedPosts().size());

        assertEquals("You've already disliked this post before!", testUser.addDislikedPost(testPost3));

        assertEquals(1, testPost3.getDislikes());
    }

    @Test
    void testAddDislikedPostWhenPostNotInDislikedOrLikedPosts() {
        assertTrue(testUser.getDislikedPosts().isEmpty());
        assertTrue(testUser.getLikedPosts().isEmpty());

        assertEquals(0, testPost1.getDislikes());
        assertEquals(0, testPost2.getDislikes());
        assertEquals(0, testPost3.getDislikes());

        assertEquals("Post added to disliked posts", testUser.addDislikedPost(testPost1));

        assertEquals(1, testPost1.getDislikes());

        assertEquals(1, testUser.getDislikedPosts().size());
        assertTrue(testUser.getLikedPosts().isEmpty());
        assertEquals("someUser", testUser.getDislikedPosts().get(0).getOpName());
        assertEquals("someTitle", testUser.getDislikedPosts().get(0).getTitle());
        assertEquals("gaming", testUser.getDislikedPosts().get(0).getCommunity());

        assertEquals("Post added to disliked posts", testUser.addDislikedPost(testPost2));

        assertEquals(1, testPost2.getDislikes());

        assertEquals(2, testUser.getDislikedPosts().size());
        assertTrue(testUser.getLikedPosts().isEmpty());
        assertEquals("someOtherUser", testUser.getDislikedPosts().get(1).getOpName());
        assertEquals("someOtherTitle", testUser.getDislikedPosts().get(1).getTitle());
        assertEquals("news", testUser.getDislikedPosts().get(1).getCommunity());

        assertEquals("Post added to disliked posts", testUser.addDislikedPost(testPost3));

        assertEquals(1, testPost3.getDislikes());

        assertEquals(3, testUser.getDislikedPosts().size());
        assertTrue(testUser.getLikedPosts().isEmpty());
        assertEquals("thirdUser", testUser.getDislikedPosts().get(2).getOpName());
        assertEquals("thirdTitle", testUser.getDislikedPosts().get(2).getTitle());
        assertEquals("sports", testUser.getDislikedPosts().get(2).getCommunity());
    }

    @Test
    void testAddDislikedPostWhenPostNotInDislikedPostsButInLikedPosts() {
        assertTrue(testUser.getDislikedPosts().isEmpty());
        assertTrue(testUser.getLikedPosts().isEmpty());

        assertEquals(0, testPost1.getLikes());
        assertEquals(0, testPost2.getLikes());
        assertEquals(0, testPost3.getLikes());

        testUser.addLikedPost(testPost1);
        testUser.addLikedPost(testPost2);
        testUser.addLikedPost(testPost3);

        assertEquals(1, testPost1.getLikes());
        assertEquals(1, testPost2.getLikes());
        assertEquals(1, testPost3.getLikes());
        assertEquals(0, testPost1.getDislikes());
        assertEquals(0, testPost2.getDislikes());
        assertEquals(0, testPost3.getDislikes());

        assertEquals(3, testUser.getLikedPosts().size());
        assertTrue(testUser.getDislikedPosts().isEmpty());

        assertEquals("Post added to disliked posts", testUser.addDislikedPost(testPost1));

        assertEquals(2, testUser.getLikedPosts().size());
        assertEquals(1, testUser.getDislikedPosts().size());

        assertEquals(0, testPost1.getLikes());
        assertEquals(1, testPost1.getDislikes());

        assertEquals("Post added to disliked posts", testUser.addDislikedPost(testPost2));

        assertEquals(1, testUser.getLikedPosts().size());
        assertEquals(2, testUser.getDislikedPosts().size());

        assertEquals(0, testPost2.getLikes());
        assertEquals(1, testPost2.getDislikes());

        assertEquals("Post added to disliked posts", testUser.addDislikedPost(testPost3));

        assertEquals(0, testUser.getLikedPosts().size());
        assertEquals(3, testUser.getDislikedPosts().size());

        assertEquals(0, testPost3.getLikes());
        assertEquals(1, testPost3.getDislikes());
    }
}