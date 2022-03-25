package ui.displays.userinput;

import model.PostIt;
import model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ui.PostItApp.invalidInput;

// A two-buttoned display allowing the user to input a username and password
// and register an account on the forum if they are valid
public class RegisterDisplay extends UsernamePasswordInput {

    // EFFECTS: creates a new dialog, initialises its elements
    //          sets the forum to the given value
    public RegisterDisplay(PostIt forum) {
        super(forum);

    }

    // MODIFIES: this, JLabel, JButton
    // EFFECTS: sets the display text to text relevant to this display
    //          and sets the display visible
    @Override
    public void makeVisible() {
        this.setTitle("Register");
        firstInputText.setText("Username (1-20 characters)");
        secondInputText.setText("Password (Min 8 characters)");
        button2.setText("Register");


        startDisplay();
    }

    // MODIFIES: this, JButton
    // EFFECTS: initialises the register and cancel buttons for this dialog
    @Override
    public void initButtonActions() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterDisplay.this.dispose();
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerIfValid();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: checks if the user inputs are valid, and then registers an account if they are
    //          prompts user to re-enter inputs if not
    //          lets user know that the account has been made and closes the dialog
    private void registerIfValid() {
        if (checkValidUsername(firstInput.getText())) {
            if (checkValidPassword(password.getPassword())) {
                String passwordString = getPassword(password.getPassword());
                forum.addUser(firstInput.getText(), new User(firstInput.getText(), passwordString));
                JOptionPane.showMessageDialog(this,
                        "Successfully registered your account! Use the menu above to login.",
                        "Success",
                        JOptionPane.PLAIN_MESSAGE);
                this.dispose();
            } else {
                invalidInput(this,"password");
            }
        } else {
            invalidInput(this,"username, or that username already exists.");
        }
    }

    // EFFECTS: returns true if the given string is 1-20 characters long
    //          and isn't already a username on the forum
    private boolean checkValidUsername(String str) {
        return ((str.length() > 0)
                && (str.length() <= PostIt.MAX_USERNAME_LENGTH)
                && !forum.getUsernamePasswords().containsKey(str));
    }

    // EFFECTS: returns true if the given string is at least 8 characters long
    private boolean checkValidPassword(char[] password) {
        return password.length >= PostIt.MIN_PASSWORD_LENGTH;
    }

    // EFFECTS: returns a string made from the given char array
    private String getPassword(char[] password) {
        return new String(password);
    }


}
