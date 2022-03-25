package ui.displays.userinput.makepost;

import model.PostIt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static ui.PostItApp.*;
import static ui.displays.TwoButtonDisplay.FOREGROUND_BORDER;

public class MakeTextPostDisplay extends MakePostDisplay {

    // FIELDS

    private JTextArea body;

    // METHODS

    // Constructor
    // EFFECTS: creates a new dialog, initialises its elements, and sets it visible
    //          sets the forum, width and height to the given values
    public MakeTextPostDisplay(PostIt forum, int width, int height) {
        super(forum, width, height);
        initBody();
        initCommunityText(2);
        initCommunity(2);
        initButtons(3);
        initButtonActions();
        setLocationRelativeTo(getParent());
        setVisible(true);
    }

    // MODIFIES: this, JPanel, JTextArea
    // EFFECTS: initialises the body input text area and places it on the panel
    private void initBody() {
        body = new JTextArea(20, 20);
        body.setLineWrap(true);
        body.setWrapStyleWord(true);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(body, gbc);
    }

    // MODIFIES: this, JButton
    // EFFECTS: initialises the button actions for this dialog
    private void initButtonActions() {
        initMakePostAction();

        initCancelAction(this);
    }

    // MODIFIES: this, JButton
    // EFFECTS: initialises the make post action for this dialog
    private void initMakePostAction() {
        makePost.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makePostIfValid();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: checks if the user inputs are valid, and then makes a text post if they are
    //          prompts user to re-enter inputs if not
    //          loops until post is made if unqiue id not generated
    //          lets user know that post has been made
    private void makePostIfValid() {
        if (checkIfEmpty(title.getText())) {
            if (checkIfEmpty(body.getText())) {
                if (confirmMakePost()) {
                    boolean makePost = forum.makeTextPost(title.getText(),
                            body.getText(),
                            (String)(community.getSelectedItem()));
                    while (!makePost) {
                        makePost = forum.makeTextPost(title.getText(),
                                body.getText(),
                                (String)(community.getSelectedItem()));
                    }
                    postSuccessfullyMade(this);
                }
            } else {
                invalidInput(this, "body");
            }
        } else {
            invalidInput(this, "title");
        }
    }




}
