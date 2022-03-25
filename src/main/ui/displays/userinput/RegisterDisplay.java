package ui.displays.userinput;

import model.PostIt;
import model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ui.PostItApp.invalidInput;

public class RegisterDisplay extends UsernamePasswordInput {

    public RegisterDisplay(PostIt forum) {
        super(forum);

    }

    @Override
    public void makeVisible() {
        this.setTitle("Register");
        firstInputText.setText("Username (1-20 characters)");
        secondInputText.setText("Password (Min 8 characters)");
        accept.setText("Register");


        startDisplay();
    }

    @Override
    public void initButtonActions() {
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterDisplay.this.dispose();
            }
        });

        accept.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerIfValid();
            }
        });
    }

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

    private boolean checkValidUsername(String str) {
        return ((str.length() > 0)
                && (str.length() <= PostIt.MAX_USERNAME_LENGTH)
                && !forum.getUsernamePasswords().containsKey(str));
    }

    private boolean checkValidPassword(char[] password) {
        return password.length >= PostIt.MIN_PASSWORD_LENGTH;
    }

    private String getPassword(char[] password) {
        return new String(password);
    }


}
