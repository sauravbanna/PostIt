package ui;

import model.User;
import model.content.posts.Post;
import model.content.posts.TextPost;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //PostIt postIt = new PostIt();
        //postIt.start();

        LinkedList<Post> posts = new LinkedList<>();
        posts.add(new TextPost("dude", "bro", "insane"));
        posts.add(new TextPost("ok123", "check this out", "trolled"));
        posts.add(new TextPost("ok1234", "check this out 2", "trolled 2"));
        posts.add(new TextPost("ok12345", "check this out 3", "trolled 3"));

        Feed feed = new Feed(posts, true, new User("guy", "238289223"));
        feed.start();
    }
}
