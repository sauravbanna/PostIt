package ui;

import model.User;
import model.content.posts.ImagePost;
import model.content.posts.Post;
import model.content.posts.TextPost;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PostDisplay extends JPanel {

    public static final Color UPVOTE_COLOR = Color.RED;
    public static final Color DOWNVOTE_COLOR = Color.BLUE;

    private JButton upvoteButton;
    private JButton downvoteButton;
    private JButton comments;
    private JButton savePost;
    private JLabel title;
    private JLabel body;
    private JPanel image;
    private JLabel username;
    private JLabel voteCount;
    private GridBagConstraints gbc;
    private Post post;
    private User currentUser;

    public PostDisplay(Post p, User currentUser, Dimension dimension) {
        this.post = p;
        this.currentUser = currentUser;
        this.setPreferredSize(dimension);
        this.setLayout(new GridBagLayout());
        initPostElements();
        initUserControlButtons();
        updateButtonColors();
        setVisible(true);
        setBackground(Color.CYAN);
    }

    private void initPostElements() {

        gbc = new GridBagConstraints();
        title = new JLabel();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.weightx = 0.03;
        gbc.weighty = 0.1;
        add(title, gbc);

        gbc = new GridBagConstraints();
        username = new JLabel();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        gbc.weighty = 0.1;
        gbc.gridwidth = 4;
        add(username, gbc);

        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        gbc.gridheight = 4;
        gbc.weightx = 1;
        gbc.weighty = 1;
        add(getBody(), gbc);

        gbc = new GridBagConstraints();
        upvoteButton = new JButton();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.05;
        gbc.weighty = 0.05;
        add(upvoteButton, gbc);

        gbc = new GridBagConstraints();
        voteCount = new JLabel();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.05;
        gbc.weighty = 0.05;
        add(voteCount, gbc);

        gbc = new GridBagConstraints();
        downvoteButton = new JButton();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.05;
        gbc.weighty = 0.05;
        add(downvoteButton, gbc);

        gbc = new GridBagConstraints();
        comments = new JButton();
        gbc.gridx = 1;
        gbc.gridy = 7;
        add(comments, gbc);

        gbc = new GridBagConstraints();
        savePost = new JButton();
        gbc.gridx = 3;
        gbc.gridy = 7;
        add(savePost, gbc);

        initText();

    }

    private JComponent getBody() {
        if (post.getClass().equals(TextPost.class)) {
            body = new JLabel();
            body.setText(post.getBody());
            image = new JPanel();
            image.setBackground(Color.GREEN);
            return image;
            //return body;
        } else if (post.getClass().equals(ImagePost.class)) {
            image = new JPanel();
            return image;
        }

        return null;
    }

    private void initText() {
        title.setText(post.getTitle());
        username.setText("Posted by: " + post.getOpName() + " in " + post.getCommunity());

        upvoteButton.setText("^");
        downvoteButton.setText("v");
        updateButtonColors();
        updateVotes();

        comments.setText(post.getCommentCount() + " comments");
        savePost.setText("Save Post");
    }

    private void initUserControlButtons() {
        upvoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println(post.getLikes() + " " + post.getDislikes());
                currentUser.addLikedPost(post);
                updateVotes();
                updateButtonColors();
                System.out.println(post.getLikes() + " " + post.getDislikes());
            }
        });

        downvoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                currentUser.addDislikedPost(post);
                updateVotes();
                updateButtonColors();
            }
        });

        comments.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        savePost.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
    }

    private void updateVotes() {
        voteCount.setText(String.valueOf(post.getLikes() - post.getDislikes()));
        repaint();
    }

    private void updateButtonColors() {
        upvoteButton.setBackground(new JButton().getBackground());
        downvoteButton.setBackground(new JButton().getBackground());
        if (currentUser.getLikedPosts().contains(post.getId())) {
            upvoteButton.setBackground(UPVOTE_COLOR);
        } else if (currentUser.getDislikedPosts().contains(post.getId())) {
            downvoteButton.setBackground(DOWNVOTE_COLOR);
        }

        upvoteButton.setOpaque(true);
        downvoteButton.setOpaque(true);
        upvoteButton.setContentAreaFilled(false);
        downvoteButton.setContentAreaFilled(false);

        revalidate();
        repaint();
    }

    private void initButtonActions() {

    }
}
