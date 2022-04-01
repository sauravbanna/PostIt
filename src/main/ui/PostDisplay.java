package ui;

import model.PostIt;
import model.content.posts.Post;
import model.content.posts.TextPost;
import ui.displays.CommentsDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ui.PostItApp.*;
import static ui.PostItApp.PADDING;
import static ui.displays.TwoButtonDisplay.THIN_BLACK_BORDER;
import static ui.displays.userinput.UserInput.TRANSPARENT_BORDER;

// A Panel that can display a post along with vote buttons, comment buttons,
public class PostDisplay extends JPanel {

    // CONSTANTS

    public static final Color UPVOTE_COLOR = new Color(169, 53, 15);
    public static final Color DOWNVOTE_COLOR = new Color(31, 66, 83);

    // FIELDS

    private JButton upvoteButton;
    private JButton downvoteButton;
    private JButton comments;
    private JButton subscribe;
    private JLabel title;
    private JTextArea bodyText;
    private JLabel image;
    private JComponent body;
    private JLabel username;
    private JLabel voteCount;
    private GridBagConstraints gbc;
    private Post post;
    private PostIt forum;
    private Dimension padding;

    // METHODS

    // Constructor
    // REQUIRES: given post is a valid post made on PostIt and is non-null
    //           given PostIt is not null
    // EFFECTS: creates a new post display with the given post and forum
    //          initialises display elements and sets display to visible
    public PostDisplay(Post p, PostIt forum) {
        padding = new Dimension(PADDING, PADDING);
        this.post = p;
        this.forum = forum;
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
        initPostElements();
        initUserControlButtons();
        updateButtonColors();
        repaint();
        setVisible(true);
    }

    // MODIFIES: this, JLabel, JButton
    // EFFECTS: initialises the panel elements and places it on the panel
    private void initPostElements() {
        addText();

        addUserInputButtons();

        addButtons();

        initText();

        initFont();

    }

    // MODIFIES: this, JButton
    // EFFECTS: initialises the panel button holder, adds buttons and places it on the panel
    private void addButtons() {
        JPanel buttonHolder = new JPanel();
        buttonHolder.setOpaque(false);
        buttonHolder.setLayout(new BoxLayout(buttonHolder, BoxLayout.X_AXIS));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 0.2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;

        comments = new JButton();
        subscribe = new JButton();

        buttonHolder.add(Box.createHorizontalGlue());
        buttonHolder.add(comments);
        buttonHolder.add(Box.createRigidArea(padding));
        buttonHolder.add(subscribe);
        buttonHolder.add(Box.createHorizontalGlue());

        add(buttonHolder, gbc);
    }

    // MODIFIES: this, JPanel, JLabel
    // EFFECTS: initialises the text holder panel, adds text to it, and places it on the panel
    private void addText() {
        JPanel textHolder = new JPanel();
        textHolder.setLayout(new BoxLayout(textHolder, BoxLayout.Y_AXIS));
        textHolder.setOpaque(false);

        initPostContent();

        addPostTextToContainer(textHolder);

        addContainerToDisplay(textHolder);

    }

    // MODIFIES: this
    // EFFECTS: adds the given container to the display
    private void addContainerToDisplay(JPanel textHolder) {
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weighty = 0.7;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.VERTICAL;

        add(textHolder, gbc);
    }

    // MODIFIES: JPanel
    // EFFECTS: adds the post's text to the given container
    private void addPostTextToContainer(JPanel textHolder) {
        textHolder.add(Box.createVerticalGlue());
        textHolder.add(title);
        textHolder.add(Box.createVerticalGlue());
        textHolder.add(username);
        textHolder.add(Box.createVerticalGlue());
        textHolder.add(body);
        textHolder.add(Box.createVerticalGlue());
    }

    // MODIFIES: this, JLabel
    // EFFECTS: initialises the post's text and body
    private void initPostContent() {
        title = new JLabel();
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setBorder(TRANSPARENT_BORDER);
        bodyText = new JTextArea(20, 10);
        image = new JLabel();
        body = getBody();
        body.setBorder(TRANSPARENT_BORDER);
        username = new JLabel();
        username.setBorder(TRANSPARENT_BORDER);
        username.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    // MODIFIES: this, JPanel, JButton
    // EFFECTS: initialises the panel's user input buttons and places it on the panel
    private void addUserInputButtons() {
        downvoteButton = new JButton();
        voteCount = new JLabel();
        upvoteButton = new JButton();

        JPanel userButtonsHolder = new JPanel();
        userButtonsHolder.setLayout(new BoxLayout(userButtonsHolder, BoxLayout.Y_AXIS));
        userButtonsHolder.setOpaque(false);


        userButtonsHolder.add(Box.createVerticalGlue());
        userButtonsHolder.add(upvoteButton);
        userButtonsHolder.add(getVoteCountDisplay());
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

    // MODIFIES: this, JPanel
    // EFFECTS: adds the vote count label to a container and returns the container
    private JPanel getVoteCountDisplay() {
        JPanel voteCountDisplay = new JPanel();
        voteCountDisplay.setBackground(DEFAULT_BACKGROUND_COLOR);
        voteCountDisplay.setLayout(new BoxLayout(voteCountDisplay, BoxLayout.X_AXIS));
        voteCountDisplay.add(Box.createHorizontalGlue());
        voteCountDisplay.add(voteCount);
        voteCountDisplay.add(Box.createHorizontalGlue());
        return voteCountDisplay;
    }

    // MODIFIES: this, JTextArea, JLabel
    // EFFECTS: if post is a text post, sets the body text area to the post's body
    //          if post is image post, sets the image label to the post's image
    //          and returns the component with the post content
    private JComponent getBody() {
        if (post.getClass().equals(TextPost.class)) {
            bodyText.setWrapStyleWord(true);
            bodyText.setLineWrap(true);
            bodyText.setOpaque(false);
            bodyText.setEditable(false);
            bodyText.setFocusable(false);
            bodyText.setBorder(THIN_BLACK_BORDER);
            bodyText.setText(post.getBody());
            return bodyText;
        } else {
            ImageIcon img = new ImageIcon(post.getBody());
            image.setPreferredSize(new Dimension(MAX_IMAGE_DIMENSION, MAX_IMAGE_DIMENSION));
            image.setIcon(img);
            return image;

        }
    }

    // MODIFIES: this, JLabel
    // EFFECTS: initialises the label and button text
    private void initText() {
        title.setText(post.getTitle());
        username.setText("Posted by: " + post.getOpName() + " in " + post.getCommunity());

        upvoteButton.setText("^");
        downvoteButton.setText("v");
        updateButtonColors();
        updateVotes();

        comments.setText(post.getCommentCount() + " comments");
    }

    // MODIFIES: this, JLabel
    // EFFECTS: initialises the label's fonts and sizes
    private void initFont() {
        title.setFont(new Font("Verdana", Font.PLAIN, 24));
        username.setFont(new Font("Verdana", Font.PLAIN, 14));
        bodyText.setFont(new Font("Verdana", Font.PLAIN, 18));
        voteCount.setFont(new Font("Verdana", Font.PLAIN, 14));
    }

    // MODIFIES: this, JButton
    // EFFECTS: initialises the panel's button actions
    private void initUserControlButtons() {
        initUpvoteAction();

        initDownvoteAction();

        initCommentsAction();

        initSubscribeAction();
    }

    // MODIFIES: this, JButton
    // EFFECTS: initialises the panel's subscribe button action
    //          if user is logged in, subscribes user to community that post is in
    //          else, tells user to log in
    private void initSubscribeAction() {
        subscribe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (forum.getLoggedIn()) {
                    forum.getCurrentUser().subscribeToCommunity(
                            forum.getCommunities().get(post.getCommunity()));
                    updateSubscribeButton();
                    confirmSubscribe("subscribed");
                } else {
                    loginWarning(PostDisplay.this);
                }
            }
        });
    }

    // MODIFIES: this, JButton
    // EFFECTS: initialises the panel's unsubscribe button action
    //          if user is logged in, unsubscribes user to community that post is in
    //          else, tells user to log in
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

    // EFFECTS: lets user know that they have subscribed or unsubscribed to a community
    private void confirmSubscribe(String action) {
        JOptionPane.showMessageDialog(this,
                "Successfully " + action + " from this community!",
                "Success",
                JOptionPane.PLAIN_MESSAGE);
    }

    // MODIFIES: this, JButton
    // EFFECTS: initialises the panel's comments button action
    //          opens the comment section of the post
    private void initCommentsAction() {
        comments.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CommentsDisplay comments = new CommentsDisplay(forum, post.getComments(), post);
                comments.makeVisible();
            }
        });
    }

    // MODIFIES: this, JButton
    // EFFECTS: initialises the panel's downvote button action
    //          if user is logged in, dislikes post
    //          else, tells user to log in
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

    // MODIFIES: this, JButton
    // EFFECTS: initialises the panel's upvote button action
    //          if user is logged in, likes post
    //          else, tells user to log in
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

    // MODIFIES: this, JLabel
    // EFFECTS: updates the vote count of the post
    private void updateVotes() {
        voteCount.setText(String.valueOf(post.getLikes() - post.getDislikes()));
        repaint();
    }

    // MODIFIES: this, JButton
    // EFFECTS: updates the vote button colors and the subscribe button text
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

    // MODIFIES: this, JButton
    // EFFECTS: updates the subscribe button text depending on whether the user is logged in
    //          and subscribed to the post's community or not
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
