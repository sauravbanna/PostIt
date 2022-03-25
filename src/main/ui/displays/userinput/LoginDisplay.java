package ui.displays.userinput;

import model.PostIt;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ui.PostItApp.invalidInput;

// A two-buttoned display allowing the user to input a username and password
// and login if they are valid
public class LoginDisplay extends UsernamePasswordInput {

    // METHODS

    // Constructor
    // EFFECTS: creates a new dialog, initialises its elements
    //          sets the forum to the given value
    public LoginDisplay(PostIt forum) {
        super(forum);
    }

    // MODIFIES: this, JLabel, JButton
    // EFFECTS: sets the display text to text relevant to this display
    //          and sets the display visible
    public void makeVisible() {
        this.setTitle("Login");
        button2.setText("Login");
        firstInputText.setText("Username: ");
        secondInputText.setText("Password: ");

        startDisplay();
    }

    // MODIFIES: this, JButton
    // EFFECTS: initialises the login and cancel buttons for this dialog
    public void initButtonActions() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginDisplay.this.dispose();
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginIfValid();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: checks if the user inputs are valid, and then logs the user in if they are
    //          prompts user to re-enter inputs if not
    //          lets user know that they have logged in and closes the dialog
    private void loginIfValid() {
        if (forum.getUsernamePasswords().containsKey(firstInput.getText())) {
            if (checkPassword()) {
                forum.login(firstInput.getText());
                JOptionPane.showMessageDialog(this,
                        "Successfully logged in!",
                        "Success",
                        JOptionPane.PLAIN_MESSAGE);
                this.dispose();
            } else {
                invalidInput(this, "password");
            }
        } else {
            invalidInput(this, "username");
        }
    }

    // EFFECTS: checks if the password entered in the JPasswordField matches the correct password
    //          returns true if it does, false if not
    private boolean checkPassword() {
        char[] passwordArray = password.getPassword();
        String correctPassword = forum.getUsernamePasswords().get(firstInput.getText()).getPassword();

        if (passwordArray.length != correctPassword.length()) {
            return false;
        } else {
            boolean returnValue = true;
            for (int i = 0; i < correctPassword.length(); i++) {
                returnValue = (returnValue && (passwordArray[i] == correctPassword.charAt(i)));
            }

            return returnValue;
        }
    }

}
