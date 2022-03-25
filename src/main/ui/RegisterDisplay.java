package ui;

import model.PostIt;
import model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ui.PostItApp.invalidInput;

public class RegisterDisplay extends LoginDisplay {

    public RegisterDisplay(PostIt forum, int width, int height) {
        super(forum, width, height);
        this.setTitle("Register");

    }

    @Override
    public void makeVisible() {
        usernameText.setText("Username \n(1-20 characters)");
        passwordText.setText("Password \n(Min 8 characters)");


        initButtonActions();

        this.setContentPane(panel);
        setLocationRelativeTo(getParent());
        pack();
        setVisible(true);
    }

    private void initButtonActions() {
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterDisplay.this.dispose();
            }
        });

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerIfValid();
            }
        });
    }

    private void registerIfValid() {
        if (checkValidUsername(username.getText())) {
            if (checkValidPassword(password.getPassword())) {
                String passwordString = getPassword(password.getPassword());
                forum.addUser(username.getText(), new User(username.getText(), passwordString));
                JOptionPane.showMessageDialog(this,
                        "Successfully registered your account! Use the menu above to login.",
                        "Success",
                        JOptionPane.PLAIN_MESSAGE);
                this.dispose();
            } else {
                invalidInput(this,"password");
            }
        } else {
            invalidInput(this,"username");
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
