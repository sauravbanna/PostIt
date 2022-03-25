package ui.displays;

import model.PostIt;
import model.content.othercontent.Comment;

import javax.swing.*;
import java.util.List;

public class CommentsDisplay extends TwoButtonDisplay {

    private List<Comment> comments;
    private int currentCommentIndex;
    private JLabel title;
    private JTextArea commentsText;
    private JButton cancel;
    private JButton next;
    private PostIt forum;

    public CommentsDisplay(PostIt forum, List<Comment> comments) {
        super(forum);
        this.comments = comments;
        currentCommentIndex = 0;

        //initDialogElements();
        initButtons();
        initButtonActions();
    }


    @Override
    public void initButtonActions() {

    }

    @Override
    public void makeVisible() {

    }
}
