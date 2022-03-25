package ui.displays.userinput;

import model.PostIt;
import ui.displays.TwoButtonDisplay;

import javax.swing.*;
import java.awt.*;

// A two-buttoned display allowing the user to input in one field
public abstract class UserInput extends TwoButtonDisplay {

    // FIELDS

    protected JLabel firstInputText;
    protected JTextField firstInput;
    protected JLabel secondInputText;

    // METHODS

    // Constructor
    // EFFECTS: creates a new dialog, initialises its elements
    //          sets the forum to the given value
    public UserInput(PostIt forum) {
        super(forum);

        initFirstInputText();
        initUsername();
        initPasswordText();
    }

    // MODIFIES: this, JPanel, JLabel
    // EFFECTS: initialises the first input label and places it on the panel
    private void initFirstInputText() {
        firstInputText = new JLabel();
        firstInputText.setText("");
        firstInputText.setBorder(TRANSPARENT_BORDER);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0.3;
        panel.add(firstInputText, gbc);
    }

    // MODIFIES: this, JPanel, JTextField
    // EFFECTS: initialises the username input field and places it on the panel
    private void initUsername() {
        firstInput = new JTextField(15);
        firstInput.setBorder(COMPOUND_BORDER_TRANSPARENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        panel.add(firstInput, gbc);
    }

    // MODIFIES: this, JPanel, JLabel
    // EFFECTS: initialises the password label and places it on the panel
    private void initPasswordText() {
        secondInputText = new JLabel();
        secondInputText.setText("");
        secondInputText.setBorder(TRANSPARENT_BORDER);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.3;
        panel.add(secondInputText, gbc);
    }




}
