package ui.displays.userinput;

import model.PostIt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ui.PostItApp.checkEmptyString;
import static ui.PostItApp.invalidInput;

public class CreateCommunityDisplay extends UserInput {

    private JTextArea aboutSection;

    public CreateCommunityDisplay(PostIt forum) {
        super(forum);
        initAboutSection();
    }

    private void initAboutSection() {
        aboutSection = new JTextArea(8, 8);
        aboutSection.setWrapStyleWord(true);
        aboutSection.setLineWrap(true);
        aboutSection.setBorder(COMPOUND_BORDER_TRANSPARENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(aboutSection, gbc);
    }

    @Override
    public void initButtonActions() {
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateCommunityDisplay.this.dispose();
            }
        });

        accept.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createCommunityIfValid();
            }
        });
    }

    private void createCommunityIfValid() {
        if (!forum.getCommunities().containsKey(firstInput.getText())) {
            if (!checkEmptyString(aboutSection.getText())) {
                forum.addCommunity(firstInput.getText(), aboutSection.getText());
                JOptionPane.showMessageDialog(this,
                        "Successfully created Community!",
                        "Success",
                        JOptionPane.PLAIN_MESSAGE);
            } else {
                invalidInput(this,"about section");
            }
        } else {
            invalidInput(this,"community name, or that one already exists.");
        }
    }

    @Override
    public void makeVisible() {
        this.setTitle("Create Community");
        firstInputText.setText("Community Name: ");
        secondInputText.setText("Community About: ");
        accept.setText("Create");

        startDisplay();
    }
}
