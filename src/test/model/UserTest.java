package model;


import model.content.posts.Post;
import model.content.posts.TextPost;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    User testUser;

    @BeforeEach
    void runBefore() {
        testUser = new User("coolName", "abcd1234");
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

        Community community1 = new Community("funny", "funny stuff");

        testUser.subscribeToCommunity(community1);

        assertEquals(1, testUser.getSubscribedCommunities().size());
        assertEquals("funny", testUser.getSubscribedCommunities().get(0));

        Community community2 = new Community("news", "The latest news");
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
    void testAddLikedPost() {
        Post testPost1 = new TextPost("someUser", "wow", "amazing", "gaming");
        Post testPost2 = new TextPost("someOtherUser", "wow2", "amazing2", "news");

        assertTrue(testUser.getLikedPosts().isEmpty());

        testUser.addLikedPost(testPost1);

        assertEquals(1, testUser.getLikedPosts().size());

        testUser.addLikedPost(testPost2);

        assertEquals(2, testUser.getLikedPosts().size());
    }

    @Test
    void testAddDislikedPost() {
        Post testPost1 = new TextPost("someUser", "wow", "amazing", "gaming");
        Post testPost2 = new TextPost("someOtherUser", "wow2", "amazing2", "news");

        assertTrue(testUser.getDislikedPosts().isEmpty());

        testUser.addDislikedPost(testPost1);

        assertEquals(1, testUser.getDislikedPosts().size());

        testUser.addDislikedPost(testPost2);

        assertEquals(2, testUser.getDislikedPosts().size());
    }

    @Test
    void testRemoveLikedPost() {
        Post testPost1 = new TextPost("someUser", "wow", "amazing", "gaming");
        Post testPost2 = new TextPost("someOtherUser", "wow2", "amazing2", "news");

        testUser.addLikedPost(testPost1);
        testUser.addLikedPost(testPost2);

        assertEquals(2, testUser.getLikedPosts().size());

        testUser.removeLikedPost(testPost1);

        assertEquals(1, testUser.getLikedPosts().size());

        testUser.removeLikedPost(testPost2);

        assertEquals(0, testUser.getLikedPosts().size());
    }

    @Test
    void testRemoveDislikedPost() {
        Post testPost1 = new TextPost("someUser", "wow", "amazing", "gaming");
        Post testPost2 = new TextPost("someOtherUser", "wow2", "amazing2", "news");

        testUser.addDislikedPost(testPost1);
        testUser.addDislikedPost(testPost2);

        assertEquals(2, testUser.getDislikedPosts().size());

        testUser.removeDislikedPost(testPost1);

        assertEquals(1, testUser.getDislikedPosts().size());

        testUser.removeDislikedPost(testPost2);

        assertEquals(0, testUser.getDislikedPosts().size());
    }
}