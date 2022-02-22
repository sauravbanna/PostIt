package model;

import exceptions.EmptyFeedException;
import model.content.posts.Post;
import org.junit.jupiter.api.*;
import ui.Feed;

import static org.junit.jupiter.api.Assertions.*;

public class PostItTest {

    private PostIt testPostIt;
    private User testUser;
    private String communityChoice;

    @BeforeEach
    void runBeforeEach() {
        testPostIt = new PostIt();
        // username and password are valid
        testUser = new User("1", "12345678");

        testPostIt.addUser(testUser.getUserName(), testUser);
        testPostIt.login(testUser.getUserName());

        communityChoice = testPostIt.DEFAULT_COMMUNITIES.get(0);
    }

    @Test
    void testConstructor() {
        testPostIt = new PostIt();
        assertEquals(0, testPostIt.getUsernamePasswords().size());
        assertTrue(testPostIt.getUsernamePasswords().isEmpty());
        assertEquals(0, testPostIt.getPosts().size());
        assertTrue(testPostIt.getPosts().isEmpty());

        assertEquals(testPostIt.DEFAULT_COMMUNITIES.size(), testPostIt.getCommunities().size());

        for (String s : testPostIt.DEFAULT_COMMUNITIES) {
            assertTrue(testPostIt.getCommunities().containsKey(s));
            assertEquals(s, testPostIt.getCommunities().get(s).getCommunityName());
        }

        assertEquals(null, testPostIt.getCurrentUser());

    }

    @Test
    void testLogOut() {
        testPostIt.logOut();

        assertFalse(testPostIt.getLoggedIn());
        assertEquals(null, testPostIt.getCurrentUser());
        assertEquals(null, testPostIt.getActiveFeed());
    }

    @Test
    void testLogin() {
        assertTrue(testPostIt.getLoggedIn());
        assertEquals(testUser, testPostIt.getCurrentUser());
        assertEquals(null, testPostIt.getActiveFeed());
    }

    @Test
    void testAddUser() {
        testPostIt = new PostIt();

        assertTrue(testPostIt.getUsernamePasswords().isEmpty());
        assertFalse(testPostIt.getUsernamePasswords().containsKey(testUser.getUserName()));

        testPostIt.addUser(testUser.getUserName(), testUser);

        assertEquals(1, testPostIt.getUsernamePasswords().size());
        assertFalse(testPostIt.getUsernamePasswords().isEmpty());

        assertEquals(testUser, testPostIt.getUsernamePasswords().get(testUser.getUserName()));
    }

    @Test
    void testMakeTextPostEnoughIDs() {
        assertTrue(testPostIt.getPosts().isEmpty());
        assertTrue(testPostIt.getCommunities().get(communityChoice).getPosts().isEmpty());
        assertTrue(testPostIt.getCurrentUser().getPosts().isEmpty());

        assertTrue(testPostIt.makeTextPost("title", "body", communityChoice));

        assertEquals(1, testPostIt.getPosts().size());
        assertEquals(1, testPostIt.getCommunities().get(communityChoice).getPosts().size());
        assertEquals(1, testPostIt.getCurrentUser().getPosts().size());

        int postId = testPostIt.getPosts().keySet().iterator().next();
        Post post = testPostIt.getPosts().values().iterator().next();

        assertEquals(postId, testPostIt.getCommunities().get(communityChoice).getPosts().get(0));
        assertEquals(postId, testPostIt.getCurrentUser().getPosts().get(0));
        assertEquals(post, testPostIt.getPosts().get(postId));

    }

    @Test
    void testUpdateBio() {
        assertEquals(testUser.DEFAULT_BIO, testPostIt.getCurrentUser().getBio());

        testPostIt.updateBio("new bio");

        assertEquals("new bio", testPostIt.getCurrentUser().getBio());
    }

    @Test
    void testAddCommunity() {
        assertEquals(testPostIt.DEFAULT_COMMUNITIES.size(), testPostIt.getCommunities().size());

        testPostIt.addCommunity("newCommunity", "a fun place");

        assertEquals(testPostIt.DEFAULT_COMMUNITIES.size() + 1, testPostIt.getCommunities().size());
        assertTrue(testPostIt.getCommunities().containsKey("newCommunity"));
        assertEquals("a fun place", testPostIt.getCommunities().get("newCommunity").getCommunityAbout());
        assertEquals("newCommunity", testPostIt.getCommunities().get("newCommunity").getCommunityName());
    }

    @Test
    void testStartHomeFeedLoggedInFeedNotEmpty() {
        testPostIt.makeTextPost("title", "body", communityChoice);

        testUser.subscribeToCommunity(testPostIt.getCommunities().get(communityChoice));

        Feed activeFeedResult = null;

        try {
            activeFeedResult = testPostIt.startHomeFeed();
        } catch (EmptyFeedException efe) {
            fail("EmptyFeedException not supposed to be thrown");
        }

        assertEquals(testPostIt.getCommunities().get(communityChoice).getPosts(), activeFeedResult.getUserFeed());
        assertEquals(1, activeFeedResult.getUserFeed().size());
        assertEquals(testPostIt, activeFeedResult.getPostIt());
        assertEquals(testUser, activeFeedResult.getCurrentUser());
        assertEquals(testPostIt.getLoggedIn(), activeFeedResult.getLoggedIn());

    }

    @Test
    void testStartHomeFeedLoggedInEmptyFeed() {
        try {
            testPostIt.startHomeFeed();
            fail("EmptyFeedException supposed to be thrown");
        } catch (EmptyFeedException efe) {

        }
    }

    @Test
    void testStartHomeFeedNotLoggedIn() {
        testPostIt.logOut();
        Feed activeFeedResult = null;

        try {
            activeFeedResult = testPostIt.startHomeFeed();
        } catch (EmptyFeedException efe) {
            fail("EmptyFeedException not supposed to be thrown");
        }

        assertEquals(0, activeFeedResult.getUserFeed().size());
        assertEquals(testPostIt, activeFeedResult.getPostIt());
        assertEquals(testPostIt.getCurrentUser(), activeFeedResult.getCurrentUser());
        assertEquals(testPostIt.getLoggedIn(), activeFeedResult.getLoggedIn());
    }

    @Test
    void testShowCommunity() {
        Feed testActiveFeed1 = testPostIt.showCommunity(communityChoice);

        assertEquals(0, testActiveFeed1.getUserFeed().size());
        assertEquals(testPostIt, testActiveFeed1.getPostIt());
        assertEquals(testPostIt.getCurrentUser(), testActiveFeed1.getCurrentUser());
        assertEquals(testPostIt.getLoggedIn(), testActiveFeed1.getLoggedIn());

        testPostIt.makeTextPost("title", "body", communityChoice);
        Feed testActiveFeed2 = testPostIt.showCommunity(communityChoice);

        assertEquals(1, testActiveFeed2.getUserFeed().size());
        assertEquals(testPostIt.getCommunities().get(communityChoice).getPosts(), testActiveFeed2.getUserFeed());
        assertEquals(testPostIt, testActiveFeed2.getPostIt());
        assertEquals(testPostIt.getCurrentUser(), testActiveFeed2.getCurrentUser());
        assertEquals(testPostIt.getLoggedIn(), testActiveFeed2.getLoggedIn());
    }

    @Test
    void testClearActiveFeed() {
        Feed testActiveFeed1 = testPostIt.showCommunity(communityChoice);

        assertEquals(testActiveFeed1, testPostIt.getActiveFeed());

        testPostIt.clearActiveFeed();

        assertEquals(null, testPostIt.getActiveFeed());
    }

}
