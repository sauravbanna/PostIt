package ui.displays.userinput;

import model.PostIt;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ui.PostItApp.invalidInput;

public class LoginDisplay extends UsernamePasswordInput {

    public LoginDisplay(PostIt forum) {
        super(forum);
    }


    public void makeVisible() {
        this.setTitle("Login");
        accept.setText("Login");
        firstInputText.setText("Username: ");
        secondInputText.setText("Password: ");

        startDisplay();
    }


    public void initButtonActions() {
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginDisplay.this.dispose();
            }
        });

        accept.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginIfValid();
            }
        });
    }

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
