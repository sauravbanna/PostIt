package ui.displays.userinput.usernamepassword;

import model.PostIt;
import ui.displays.userinput.UserInput;

import javax.swing.*;
import java.awt.*;

// A two-buttoned display allowing the user to input a username and password
public abstract class UsernamePasswordInput extends UserInput {

    // FIELDS

    protected JPasswordField password;

    // METHODS

    // Constructor
    // EFFECTS: creates a new dialog, initialises its elements
    //          sets the forum to the given value
    public UsernamePasswordInput(PostIt forum) {
        super(forum);
        initPassword();
    }

    // MODIFIES: this, JPanel, JPasswordField
    // EFFECTS: initialises the password input field and places it on the panel
    private void initPassword() {
        password = new JPasswordField(15);
        password.setBorder(COMPOUND_BORDER_TRANSPARENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(password, gbc);
    }


}
