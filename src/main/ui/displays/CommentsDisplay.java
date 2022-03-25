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

    private List<Comment> comments;
    private int currentCommentIndex;
    private JLabel title;
    private JTextArea commentsText;
    private Post post;

    public CommentsDisplay(PostIt forum, List<Comment> comments, Post post) {
        super(forum);
        this.comments = comments;
        currentCommentIndex = 0;
        this.post = post;

        initDialogElements();
        initButtonActions();
        refreshText();
    }

    private void initDialogElements() {
        initTitle();

        initCommentsText();
    }

    private void refreshText() {
        int firstCommentIndex = currentCommentIndex + 1;
        int lastCommentIndex = currentCommentIndex + NUM_COMMENTS_TO_SHOW;
        title.setText("Currently showing comments " + firstCommentIndex + " to " + lastCommentIndex);

        refreshComments();
    }

    private void refreshComments() {
        String result = "";
        int limit = Math.min(currentCommentIndex + NUM_COMMENTS_TO_SHOW, comments.size());
        for (int i = currentCommentIndex; i < limit; i++) {
            result += commentToText(comments.get(i));
            result += "\n\n";
        }

        commentsText.setText(result);
    }

    private String commentToText(Comment comment) {
        return comment.getOpName() + " says: "
                + "\n" + comment.getCommentBody();
    }

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

    private void nextComments() {
        currentCommentIndex += NUM_COMMENTS_TO_SHOW;
        refreshText();
    }

    @Override
    public void makeVisible() {
        this.setTitle("Comments");
        button1.setText("Make Comment");
        button2.setText("Next");


        startDisplay();
    }
}
