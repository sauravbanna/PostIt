package ui;

import model.content.posts.Post;

import javax.swing.*;
import java.awt.*;

public class PostDisplay extends JPanel {


    private JButton upvoteButton;
    private JButton downvoteButton;
    private JButton comments;
    private JButton savePost;
    private JLabel title;
    private JPanel body;
    private JLabel username;
    private JLabel community;
    private JLabel likes;
    private JLabel dislikes;
    private GridBagConstraints gbc;
    private Post post;

    public PostDisplay(Post p) {
        this.post = p;
        this.setLayout(new GridBagLayout());
        initPostText();
        initUserControlButtons();
    }

    private void initPostText() {
        title = new JLabel();


    }

    private void initUserControlButtons() {

    }
}
