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


    private JTextArea body;



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

    private void initButtonActions() {
        initMakePostAction();

        initCancelAction(this);
    }

    private void initMakePostAction() {
        makePost.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makePostIfValid();
            }
        });
    }

    private void makePostIfValid() {
        if (checkIfEmpty(title.getText())) {
            if (checkIfEmpty(body.getText())) {
                if (confirmMakePost()) {
                    forum.makeTextPost(title.getText(),
                            body.getText(),
                            (String)(community.getSelectedItem()));
                    postSuccessfullyMade();
                }
            } else {
                invalidInput(this, "body");
            }
        } else {
            invalidInput(this, "title");
        }
    }



    private boolean checkIfEmpty(String str) {
        return (str != null && !(str.length() == 0));
    }

    private boolean confirmMakePost() {
        int userChoice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to post this?",
                "Cofirm Post",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        return userChoice == JOptionPane.YES_OPTION;
    }

    private void postSuccessfullyMade() {
        JOptionPane.showMessageDialog(this,
                "Post Successfully Made!",
                "Success",
                JOptionPane.PLAIN_MESSAGE);
        this.dispose();
    }


}
