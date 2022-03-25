package ui.displays;

import model.PostIt;
import model.content.othercontent.Comment;
import model.content.posts.Post;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static ui.PostItApp.NUM_COMMENTS_TO_SHOW;


public class CommentsDisplay extends TwoButtonDisplay {

    // FIELDS

    private List<Comment> comments;
    private int currentCommentIndex;
    private JLabel title;
    private JTextArea commentsText;
    private Post post;

    // METHODS

    // Constructor
    // EFFECTS: creates a new dialog, initialises its elements
    //          sets the forum and the post to the given values
    //          sets the list of comments to the given value
    public CommentsDisplay(PostIt forum, List<Comment> comments, Post post) {
        super(forum);
        this.comments = comments;
        currentCommentIndex = 0;
        this.post = post;

        initDialogElements();
        initButtonActions();
        refreshText();
    }

    // MODIFIES: this, JPanel, JLabel
    // EFFECTS: initialises the panel elements and places them on the panel
    private void initDialogElements() {
        initTitle();

        initCommentsText();
    }

    // MODIFIES: this, JLabel, JTextArea
    // EFFECTS: sets the title and comment section to the current range of comments
    private void refreshText() {
        int firstCommentIndex = currentCommentIndex + 1;
        int lastCommentIndex = currentCommentIndex + NUM_COMMENTS_TO_SHOW;
        title.setText("Currently showing comments " + firstCommentIndex + " to " + lastCommentIndex);

        refreshComments();
    }

    // MODIFIES: this, JLabel, JTextArea
    // EFFECTS: sets the comment section to the current range of comments
    //          from currentCommentIndex to currentCommentIndex + NUM_COMMENTS_TO_SHOW
    private void refreshComments() {
        String result = "";
        int limit = Math.min(currentCommentIndex + NUM_COMMENTS_TO_SHOW, comments.size());
        for (int i = currentCommentIndex; i < limit; i++) {
            result += commentToText(comments.get(i));
            result += "\n\n";
        }

        commentsText.setText(result);
    }

    // EFFECTS: returns a string representation of a comment
    private String commentToText(Comment comment) {
        return comment.getOpName() + " says: "
                + "\n" + comment.getCommentBody();
    }

    // MODIFIES: this, JPanel, JLabel
    // EFFECTS: initialises the title label and places it on the panel
    private void initTitle() {
        title = new JLabel();
        title.setBorder(TRANSPARENT_BORDER);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.1;
        panel.add(title, gbc);
    }

    // MODIFIES: this, JPanel, JTextArea
    // EFFECTS: initialises the comments text area and places it on the panel
    private void initCommentsText() {
        commentsText = new JTextArea(20, 20);
        commentsText.setWrapStyleWord(true);
        commentsText.setLineWrap(true);
        commentsText.setEditable(false);
        commentsText.setBorder(COMPOUND_BORDER_TRANSPARENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 0.7;
        panel.add(commentsText, gbc);
    }

    // MODIFIES: this, JButton
    // EFFECTS: initialises the next and make comment button actions for this dialog
    @Override
    public void initButtonActions() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeComment();
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextComments();
            }
        });
    }

    // MODIFIES: this, Post
    // EFFECTS: prompts the user to enter a comment
    //          if not empty, adds comment to post
    //          and refreshed the comments display
    private void makeComment() {
        String comment = JOptionPane.showInputDialog(this,
                "Please enter your comment and press 'OK' when done.",
                "Comment",
                JOptionPane.QUESTION_MESSAGE);

        if (comment != null && comment.length() > 0) {
            post.addComment(new Comment(forum.getCurrentUser().getUserName(), comment));
            JOptionPane.showMessageDialog(this,
                    "Your comment has been added successfully!",
                    "Success",
                    JOptionPane.PLAIN_MESSAGE);

            refreshComments();
        }
    }

    // MODIFIES: this
    // EFFECTS: increments the current comment index by NUM_COMMENTS_TO_SHOW
    //          and refreshes the comment area
    private void nextComments() {
        currentCommentIndex += NUM_COMMENTS_TO_SHOW;
        refreshText();
    }

    // MODIFIES: this, JLabel, JButton
    // EFFECTS: sets the display text to text relevant to this display
    //          and sets the display visible
    @Override
    public void makeVisible() {
        this.setTitle("Comments");
        button1.setText("Make Comment");
        button2.setText("Next");


        startDisplay();
    }
}
