package model;


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
}