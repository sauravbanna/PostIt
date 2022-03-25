package ui.displays.userinput;

import model.PostIt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ui.PostItApp.checkEmptyString;
import static ui.PostItApp.invalidInput;

// A two-buttoned display allowing the user to make a community
// with a name and an about section
public class CreateCommunityDisplay extends UserInput {

    // FIELDS

    private JTextArea aboutSection;

    // METHODS

    // Constructor
    // EFFECTS: creates a new dialog, initialises its elements
    //          sets the forum to the given value
    public CreateCommunityDisplay(PostIt forum) {
        super(forum);
        initAboutSection();
    }

    // MODIFIES: this, JPanel, JTextArea
    // EFFECTS: initialises the about section input text area and places it on the panel
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

    // MODIFIES: this, JButton
    // EFFECTS: initialises the make community and cancel buttons for this dialog
    @Override
    public void initButtonActions() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateCommunityDisplay.this.dispose();
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createCommunityIfValid();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: checks if the user inputs are valid, and then makes a new community if they are
    //          prompts user to re-enter inputs if not
    //          lets user know that community has been made and closes dialog
    private void createCommunityIfValid() {
        if (!forum.getCommunities().containsKey(firstInput.getText())) {
            if (!checkEmptyString(aboutSection.getText())) {
                forum.addCommunity(firstInput.getText(), aboutSection.getText());
                JOptionPane.showMessageDialog(this,
                        "Successfully created Community!",
                        "Success",
                        JOptionPane.PLAIN_MESSAGE);
                this.dispose();
            } else {
                invalidInput(this,"about section");
            }
        } else {
            invalidInput(this,"community name, or that one already exists.");
        }
    }

    // MODIFIES: this, JLabel, JButton
    // EFFECTS: sets the display text to text relevant to this display
    //          and sets the display visible
    @Override
    public void makeVisible() {
        this.setTitle("Create Community");
        firstInputText.setText("Community Name: ");
        secondInputText.setText("Community About: ");
        button2.setText("Create");

        startDisplay();
    }
}
