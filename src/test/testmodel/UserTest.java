package testmodel;


import model.Community;
import model.User;
import model.content.posts.Post;
import model.content.posts.TextPost;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class UserTest extends PostCollectionsTest {

    User testUser;
    Post testPost1;
    Post testPost2;
    Post testPost3;

    @BeforeEach
    void runBefore() {
        testUser = new User("coolName", "abcd1234");
        testPost1 = new TextPost("someUser", "someTitle", "someBody", "gaming", 0);
        testPost2 = new TextPost("someOtherUser", "someOtherTitle", "someOtherBody", "news", 1);
        testPost3 = new TextPost("thirdUser", "thirdTitle", "thirdBody", "sports", 2);
        testPostCollection = new User("someName", "somePassword");
    }

    @Test
    void testConstructor() {
        assertEquals("coolName", testUser.getUserName());
        assertEquals("abcd1234", testUser.getPassword());
        assertEquals(User.DEFAULT_BIO, testUser.getBio());
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
    void testUnsubscribeToCommunity() {

        Community community1 = new Community("funny", "funny stuff", null);

        testUser.subscribeToCommunity(community1);

        Community community2 = new Community("news", "The latest news", null);
        testUser.subscribeToCommunity(community2);

        assertEquals(2, testUser.getSubscribedCommunities().size());
        assertEquals(1, community1.getSubCount());
        assertEquals(1, community2.getSubCount());

        testUser.unsubscribeFromCommunity(community1);

        assertEquals(1, testUser.getSubscribedCommunities().size());
        assertEquals(0, community1.getSubCount());
        assertEquals(1, community2.getSubCount());

        testUser.unsubscribeFromCommunity(community2);

        assertEquals(0, testUser.getSubscribedCommunities().size());
        assertEquals(0, community1.getSubCount());
        assertEquals(0, community2.getSubCount());
    }

    @Test
    void testSetBio() {
        assertEquals(User.DEFAULT_BIO, testUser.getBio());

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

        testUser.addLikedPost(testPost1);
        testUser.addLikedPost(testPost2);

        assertEquals(0, testPost1.getLikes());
        assertEquals(0, testPost2.getLikes());

        assertEquals(0, testPost3.getLikes());

        testUser.addLikedPost(testPost3);

        assertEquals(1, testPost3.getLikes());
        assertEquals(1, testUser.getLikedPosts().size());

        testUser.addLikedPost(testPost3);

        assertEquals(0, testPost3.getLikes());
    }

    @Test
    void testAddLikedPostWhenPostNotInLikedOrDislikedPosts() {
        assertTrue(testUser.getLikedPosts().isEmpty());
        assertTrue(testUser.getDislikedPosts().isEmpty());

        assertEquals(0, testPost1.getLikes());
        assertEquals(0, testPost2.getLikes());
        assertEquals(0, testPost3.getLikes());

        testUser.addLikedPost(testPost1);

        assertEquals(1, testPost1.getLikes());

        assertEquals(1, testUser.getLikedPosts().size());
        assertTrue(testUser.getDislikedPosts().isEmpty());
        assertEquals(0, testUser.getLikedPosts().get(0));

        testUser.addLikedPost(testPost2);

        assertEquals(1, testPost2.getLikes());

        assertEquals(2, testUser.getLikedPosts().size());
        assertTrue(testUser.getDislikedPosts().isEmpty());
        assertEquals(1, testUser.getLikedPosts().get(1));

        testUser.addLikedPost(testPost3);

        assertEquals(1, testPost3.getLikes());

        assertEquals(3, testUser.getLikedPosts().size());
        assertTrue(testUser.getDislikedPosts().isEmpty());
        assertEquals(2, testUser.getLikedPosts().get(2));
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

        testUser.addLikedPost(testPost1);

        assertEquals(2, testUser.getDislikedPosts().size());
        assertEquals(1, testUser.getLikedPosts().size());

        assertEquals(0, testPost1.getDislikes());
        assertEquals(1, testPost1.getLikes());

        testUser.addLikedPost(testPost2);

        assertEquals(1, testUser.getDislikedPosts().size());
        assertEquals(2, testUser.getLikedPosts().size());

        assertEquals(0, testPost2.getDislikes());
        assertEquals(1, testPost2.getLikes());

        testUser.addLikedPost(testPost3);

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

        testUser.addDislikedPost(testPost1);
        testUser.addDislikedPost(testPost2);

        assertEquals(0, testPost1.getDislikes());
        assertEquals(0, testPost2.getDislikes());

        assertEquals(0, testPost3.getDislikes());

        testUser.addDislikedPost(testPost3);

        assertEquals(1, testPost3.getDislikes());
        assertEquals(1, testUser.getDislikedPosts().size());

        testUser.addDislikedPost(testPost3);

        assertEquals(0, testPost3.getDislikes());
    }

    @Test
    void testAddDislikedPostWhenPostNotInDislikedOrLikedPosts() {
        assertTrue(testUser.getDislikedPosts().isEmpty());
        assertTrue(testUser.getLikedPosts().isEmpty());

        assertEquals(0, testPost1.getDislikes());
        assertEquals(0, testPost2.getDislikes());
        assertEquals(0, testPost3.getDislikes());

        testUser.addDislikedPost(testPost1);

        assertEquals(1, testPost1.getDislikes());

        assertEquals(1, testUser.getDislikedPosts().size());
        assertTrue(testUser.getLikedPosts().isEmpty());
        assertEquals(0, testUser.getDislikedPosts().get(0));

        testUser.addDislikedPost(testPost2);

        assertEquals(1, testPost2.getDislikes());

        assertEquals(2, testUser.getDislikedPosts().size());
        assertTrue(testUser.getLikedPosts().isEmpty());
        assertEquals(1, testUser.getDislikedPosts().get(1));

        testUser.addDislikedPost(testPost3);

        assertEquals(1, testPost3.getDislikes());

        assertEquals(3, testUser.getDislikedPosts().size());
        assertTrue(testUser.getLikedPosts().isEmpty());
        assertEquals(2, testUser.getDislikedPosts().get(2));
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

        testUser.addDislikedPost(testPost1);

        assertEquals(2, testUser.getLikedPosts().size());
        assertEquals(1, testUser.getDislikedPosts().size());

        assertEquals(0, testPost1.getLikes());
        assertEquals(1, testPost1.getDislikes());

        testUser.addDislikedPost(testPost2);

        assertEquals(1, testUser.getLikedPosts().size());
        assertEquals(2, testUser.getDislikedPosts().size());

        assertEquals(0, testPost2.getLikes());
        assertEquals(1, testPost2.getDislikes());

        testUser.addDislikedPost(testPost3);

        assertEquals(0, testUser.getLikedPosts().size());
        assertEquals(3, testUser.getDislikedPosts().size());

        assertEquals(0, testPost3.getLikes());
        assertEquals(1, testPost3.getDislikes());
    }


}