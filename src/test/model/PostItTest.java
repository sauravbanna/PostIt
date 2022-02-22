package model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PostItTest {

    private PostIt testPostIt;
    private User testUser;

    @BeforeEach
    void runBeforeEach() {
        testPostIt = new PostIt();
        // username and password are valid
        testUser = new User("1", "12345678");
    }

    @Test
    void testConstructor() {
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
        testPostIt.addUser(testUser.getUserName(), testUser);
        testPostIt.login(testUser.getUserName());

        assertTrue(testPostIt.getLoggedIn());
        assertEquals(testUser, testPostIt.getCurrentUser());
        assertEquals(null, testPostIt.getActiveFeed());
    }

    @Test
    void testAddUser() {
        assertTrue(testPostIt.getUsernamePasswords().isEmpty());
        assertFalse(testPostIt.getUsernamePasswords().containsKey(testUser.getUserName()));

        testPostIt.addUser(testUser.getUserName(), testUser);

        assertEquals(1, testPostIt.getUsernamePasswords().size());
        assertFalse(testPostIt.getUsernamePasswords().isEmpty());

        assertEquals(testUser, testPostIt.getUsernamePasswords().get(testUser.getUserName()));
    }
}
