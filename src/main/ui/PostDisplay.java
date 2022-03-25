package ui;

import jdk.nashorn.internal.scripts.JO;
import model.PostIt;
import model.content.posts.ImagePost;
import model.content.posts.Post;
import model.content.posts.TextPost;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import static ui.PostItApp.*;
import static ui.PostItApp.PADDING;

public class PostDisplay extends JPanel {

    public static final Color UPVOTE_COLOR = Color.RED;
    public static final Color DOWNVOTE_COLOR = Color.BLUE;

    private JButton upvoteButton;
    private JButton downvoteButton;
    private JButton comments;
    private JButton savePost;
    private JButton subscribe;
    private JLabel title;
    private JTextArea body;
    private JPanel image;
    private JLabel username;
    private JLabel voteCount;
    private GridBagConstraints gbc;
    private Post post;
    private PostIt forum;
    private Dimension padding;

    public PostDisplay(Post p, PostIt forum) {
        padding = new Dimension(PADDING, PADDING);
        this.post = p;
        this.forum = forum;
        this.setLayout(new GridBagLayout());
        initPostElements();
        initUserControlButtons();
        updateButtonColors();
        setVisible(true);
        setBackground(Color.CYAN);
    }

    private void initPostElements() {
        addText();

        addUserInputButtons();

        addButtons();

        initText();

        initFont();

    }

    private void addButtons() {
        JPanel buttonHolder = new JPanel();
        buttonHolder.setLayout(new BoxLayout(buttonHolder, BoxLayout.X_AXIS));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 0.2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;

        comments = new JButton();
        savePost = new JButton();
        subscribe = new JButton();

        buttonHolder.add(Box.createHorizontalGlue());
        buttonHolder.add(comments);
        buttonHolder.add(Box.createRigidArea(padding));
        buttonHolder.add(savePost);
        buttonHolder.add(Box.createRigidArea(padding));
        buttonHolder.add(subscribe);
        buttonHolder.add(Box.createHorizontalGlue());

        add(buttonHolder, gbc);
    }

    private void addText() {
        JPanel textHolder = new JPanel();
        textHolder.setLayout(new BoxLayout(textHolder, BoxLayout.Y_AXIS));
        textHolder.setBackground(Color.RED);

        title = new JLabel();
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        body = getBody();
        username = new JLabel();
        username.setAlignmentX(Component.LEFT_ALIGNMENT);

        textHolder.add(Box.createVerticalGlue());
        textHolder.add(title);
        textHolder.add(Box.createVerticalGlue());
        textHolder.add(username);
        textHolder.add(Box.createVerticalGlue());
        textHolder.add(body);
        textHolder.add(Box.createVerticalGlue());

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weighty = 0.7;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.VERTICAL;

        add(textHolder, gbc);

    }

    private void addUserInputButtons() {
        downvoteButton = new JButton();
        voteCount = new JLabel();
        upvoteButton = new JButton();

        JPanel userButtonsHolder = new JPanel();
        userButtonsHolder.setLayout(new BoxLayout(userButtonsHolder, BoxLayout.Y_AXIS));


        userButtonsHolder.add(Box.createVerticalGlue());
        userButtonsHolder.add(upvoteButton);
        userButtonsHolder.add(voteCount);
        userButtonsHolder.add(downvoteButton);
        userButtonsHolder.add(Box.createVerticalGlue());

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0.7;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.VERTICAL;

        add(userButtonsHolder, gbc);
    }

    private JTextArea getBody() {
        body = new JTextArea(20, 10);
        body.setWrapStyleWord(true);
        body.setLineWrap(true);
        body.setOpaque(false);
        body.setEditable(false);
        body.setFocusable(false);
        body.setBackground(UIManager.getColor("Label.background"));
        body.setFont(UIManager.getFont("Label.font"));
        body.setBorder(UIManager.getBorder("Label.border"));
        body.setText(post.getBody());
        return body;
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

    private void initFont() {
        title.setFont(new Font("Verdana", Font.PLAIN, 24));
        username.setFont(new Font("Verdana", Font.PLAIN, 14));
        body.setFont(new Font("Verdana", Font.PLAIN, 18));
        voteCount.setFont(new Font("Verdana", Font.PLAIN, 14));
    }

    // TODO
    // add Subscribe button to community
    private void initUserControlButtons() {
        initUpvoteAction();

        initDownvoteAction();

        initCommentsAction();

        initSavePostAction();

        initSubscribeAction();
    }

    private void initSubscribeAction() {
        subscribe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (forum.getLoggedIn()) {
                    forum.getCurrentUser().subscribeToCommunity(
                            forum.getCommunities().get(post.getCommunity()));
                    updateSubscribeButton();
                    confirmSubscribe("subscribed");
                    System.out.println("action performed");
                } else {
                    loginWarning(PostDisplay.this);
                }
            }
        });
    }

    private void initUnsubscribeAction() {
        subscribe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (forum.getLoggedIn()) {
                    forum.getCurrentUser().unsubscribeFromCommunity(
                            forum.getCommunities().get(post.getCommunity()));
                    updateSubscribeButton();
                    confirmSubscribe("unsubcribed");
                } else {
                    loginWarning(PostDisplay.this);
                }
            }
        });
    }

    private void confirmSubscribe(String action) {
        JOptionPane.showMessageDialog(this,
                "Successfully " + action + " from this community!",
                "Success",
                JOptionPane.PLAIN_MESSAGE);
    }

    // TODO
    private void initSavePostAction() {
        savePost.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
    }

    // TODO
    private void initCommentsAction() {
        comments.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
    }

    private void initDownvoteAction() {
        downvoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (forum.getLoggedIn()) {
                    forum.getCurrentUser().addDislikedPost(post);
                    updateVotes();
                    updateButtonColors();
                } else {
                    loginWarning(PostDisplay.this);
                }
            }
        });
    }

    private void initUpvoteAction() {
        upvoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (forum.getLoggedIn()) {
                    forum.getCurrentUser().addLikedPost(post);
                    updateVotes();
                    updateButtonColors();
                } else {
                    loginWarning(PostDisplay.this);
                }
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
        if (forum.getLoggedIn()) {
            if (forum.getCurrentUser().getLikedPosts().contains(post.getId())) {
                upvoteButton.setBackground(UPVOTE_COLOR);
            } else if (forum.getCurrentUser().getDislikedPosts().contains(post.getId())) {
                downvoteButton.setBackground(DOWNVOTE_COLOR);
            }
        }

        upvoteButton.setOpaque(true);
        downvoteButton.setOpaque(true);
        upvoteButton.setContentAreaFilled(false);
        downvoteButton.setContentAreaFilled(false);

        updateSubscribeButton();

        revalidate();
        repaint();
    }

    private void updateSubscribeButton() {
        if (forum.getLoggedIn() && forum.getCurrentUser().getSubscribedCommunities().contains(post.getCommunity())) {
            subscribe.setText("Unsubscribe");
            clearActionListeners(subscribe);
            initUnsubscribeAction();
        } else {
            subscribe.setText("Subscribe");
            clearActionListeners(subscribe);
            initSubscribeAction();
        }
    }

}
