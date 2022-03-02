package persistence;

import model.Community;
import model.User;
import model.content.othercontent.Comment;
import model.content.posts.Post;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

    protected void checkUser(User actual, User expected) {
        assertEquals(expected.getUserName(), actual.getUserName());
        assertEquals(expected.getBio(), actual.getBio());
        assertEquals(expected.getSubscribedCommunities(), actual.getSubscribedCommunities());
        assertEquals(expected.getLikedPosts(), actual.getLikedPosts());
        assertEquals(expected.getDislikedPosts(), actual.getDislikedPosts());
        assertEquals(expected.getPosts(), actual.getPosts());
        assertEquals(expected.getPassword(), actual.getPassword());
    }

    void checkPost(Post actual, Post expected) {
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getOpName(), actual.getOpName());
        assertEquals(expected.getBody(), actual.getBody());
        assertEquals(expected.getCommentCount(), actual.getCommentCount());
        assertEquals(expected.getCommunity(), actual.getCommunity());
        assertEquals(expected.getLikes(), actual.getLikes());
        assertEquals(expected.getDislikes(), actual.getDislikes());
    }

    void checkCommunities(HashMap<String, Community> actual, List<Community> expected) {
        for (Community c : expected) {
            assertEquals(c.getCommunityName(), actual.get(c.getCommunityName()).getCommunityName());
            assertEquals(c.getCommunityAbout(), actual.get(c.getCommunityName()).getCommunityAbout());
            assertEquals(c.getCreator(), actual.get(c.getCommunityName()).getCreator());
            assertEquals(c.getSubCount(), actual.get(c.getCommunityName()).getSubCount());
        }
    }

    void checkComments(List<Comment> actual, List<Comment> expected) {
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getCommentBody(), actual.get(i).getCommentBody());
            assertEquals(expected.get(i).getOpName(), actual.get(i).getOpName());
        }
    }

    void checkCommunity(Community actual, Community expected) {
        assertEquals(expected.getCommunityName(), actual.getCommunityName());
        assertEquals(expected.getCommunityAbout(), actual.getCommunityAbout());
        assertEquals(expected.getCreator(), actual.getCreator());
    }
}
