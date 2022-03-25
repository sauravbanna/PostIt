package ui;

import model.PostIt;
import model.User;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ui.PostItApp.PADDING;
import static ui.PostItApp.invalidInput;

public class LoginDisplay extends JDialog {

    protected PostIt forum;
    protected JLabel usernameText;
    protected JTextField username;
    protected JLabel passwordText;
    protected JPasswordField password;
    protected JPanel panel;
    protected JButton login;
    protected JButton cancel;
    protected Border loweredBevel;
    protected Border transparent;
    protected Border border;

    public LoginDisplay(PostIt forum, int width, int height) {
        this.forum = forum;
        this.setTitle("Login");
        this.panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING,PADDING,PADDING,PADDING));
        panel.setLayout(new GridBagLayout());
        this.setSize(width, height);

        loweredBevel = BorderFactory.createLoweredBevelBorder();
        transparent = BorderFactory.createMatteBorder(PADDING, PADDING, PADDING, PADDING,
                new JDialog().getBackground());
        border = BorderFactory.createCompoundBorder(transparent, loweredBevel);

        initLoginElements();
    }

    public void makeVisible() {
        initButtonActions();

        this.setContentPane(panel);
        setLocationRelativeTo(getParent());
        pack();
        setVisible(true);
    }

    private void initLoginElements() {
        initUsernameText();
        initUsername();
        initPasswordText();
        initPassword();
        initButtons();

    }

    private void initUsernameText() {
        usernameText = new JLabel();
        usernameText.setText("Username: ");
        usernameText.setBorder(transparent);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0.3;
        panel.add(usernameText, gbc);
    }

    private void initUsername() {
        username = new JTextField(15);
        username.setBorder(border);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        panel.add(username, gbc);
    }

    private void initPasswordText() {
        passwordText = new JLabel();
        passwordText.setText("Password: ");
        passwordText.setBorder(transparent);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.3;
        panel.add(passwordText, gbc);
    }

    private void initPassword() {
        password = new JPasswordField(15);
        password.setBorder(border);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(password, gbc);
    }

    private void initButtons() {
        JPanel buttonHolder = new JPanel();
        buttonHolder.setLayout(new BoxLayout(buttonHolder, BoxLayout.X_AXIS));
        login = new JButton();
        login.setText("Login");
        login.setBorder(transparent);
        cancel = new JButton();
        cancel.setText("Cancel");
        cancel.setBorder(transparent);
        buttonHolder.add(Box.createHorizontalGlue());
        buttonHolder.add(cancel);
        buttonHolder.add(Box.createHorizontalGlue());
        buttonHolder.add(login);
        buttonHolder.add(Box.createHorizontalGlue());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 0.3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(buttonHolder, gbc);
    }

    private void initButtonActions() {
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginDisplay.this.dispose();
            }
        });

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginIfValid();
            }
        });
    }

    private void loginIfValid() {
        if (forum.getUsernamePasswords().containsKey(username.getText())) {
            if (checkPassword()) {
                forum.login(username.getText());
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
        String correctPassword = forum.getUsernamePasswords().get(username.getText()).getPassword();

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
