package ui;

import exceptions.EmptyFeedException;
import exceptions.EndOfFeedException;
import exceptions.StartOfFeedException;
import model.Community;
import model.PostIt;
import model.User;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.displays.userinput.CreateCommunityDisplay;
import ui.displays.userinput.LoginDisplay;
import ui.displays.userinput.RegisterDisplay;
import ui.displays.userinput.MakePostDisplay;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

// A forum where a user can register an account and login / logout
// Contains communities that the user can create, post to, visit and view the posts of
// as well as user profiles of the user and other registered users
public class PostItApp extends JFrame {

    // CONSTANTS
    public static final String FORUM_DATA = "./data/forum.json";
    public static final String BACKGROUND_IMAGE = "./data/images/background.png";
    public static final String WINDOW_TITLE = "PostIt";
    public static final int DEFAULT_WIDTH_PX = 1280;
    public static final int DEFAULT_HEIGHT_PX = 720;
    public static final int TITLE_FONT_SIZE = 40;
    public static final int PADDING = 10;
    public static final Color DEFAULT_FOREGROUND_COLOR = new Color(77, 121, 174);
    public static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;

    // FIELDS
    private PostIt postIt;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private JPanelWithImage forum;
    private JPanel title;
    private JPanel buttons;
    private JPanel feed;
    private JButton backButton;
    private JButton nextButton;
    private JButton makePostButton;
    private JButton editProfileButton;
    private JButton createCommunityButton;
    private JLabel titleText;
    private JLabel aboutSectionText;
    private JLabel subCountText;
    private Feed activeFeed;
    private int widthPx;
    private int heightPx;
    private String userBeingViewed;
    private String communityBeingViewed;
    private Image background;


    // TODO
    // Make Image Post



    // METHODS

    // Constructor
    // EFFECTS: creates a new PostIt Forum and instantiates the Scanner input to take in user input
    //          and instantiates the JSON writer and reader to the correct file location
    public PostItApp() {
        jsonWriter = new JsonWriter(FORUM_DATA);
        jsonReader = new JsonReader(FORUM_DATA);

        initForum();

        this.widthPx = DEFAULT_WIDTH_PX;
        this.heightPx = DEFAULT_HEIGHT_PX;

        initForumWindow();
        initButtonActions();
        initMenuBar();
        initButtons(false);
        initResizeWindow();
        getHomeFeed();
        setVisible(true);
    }

    // TODO
    private void initResizeWindow() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeWindow();
            }
        });

        addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                resizeWindow();
            }
        });
    }

    // TODO
    private void resizeWindow() {
        this.widthPx = getWidth();
        this.heightPx = getHeight();
        forum.setImageBounds(widthPx, heightPx);

    }

    private void initForum() {
        try {
            postIt = jsonReader.read();
        } catch (IOException ioe) {
            System.out.println("File Read failed");
        }
        postIt.addDefaultCommunitiesCheck();
    }

    private void initForumWindow() {
        background = getBackgroundImage();
        forum = new JPanelWithImage(background);
        forum.setLayout(new GridBagLayout());
        forum.requestFocusInWindow();
        setContentPane(forum);
        setTitle(WINDOW_TITLE);
        setSize(widthPx, heightPx);

        addForumElements();

        //UIManager.put("OptionPane.minimumSize", new Dimension(dialogWidthPx, dialogHeightPx));

        initWindowListener();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private Image getBackgroundImage() {
        Image image = null;
        try {
            image = ImageIO.read(new File(BACKGROUND_IMAGE));
        } catch (IOException ioe) {
            System.out.println("Background Image not Found");
        }

        return image;
    }

    private Image resizeBackground() {
        return background.getScaledInstance(widthPx, heightPx, Image.SCALE_DEFAULT);
    }

    private void addForumElements() {
        addTitle();

        initTitle();

        addFeed();

        addButtons();
    }

    private void addButtons() {
        buttons = new JPanel();
        buttons.setBackground(DEFAULT_FOREGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 0.2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        forum.add(buttons, gbc);
    }

    private void addFeed() {
        feed = new JPanel();
        feed.setLayout(new BoxLayout(feed, BoxLayout.X_AXIS));
        feed.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.8;
        gbc.fill = GridBagConstraints.BOTH;
        forum.add(feed, gbc);
    }

    private void addTitle() {
        title = new JPanel();
        title.setLayout(new GridBagLayout());
        title.setBorder(new MatteBorder(PADDING, PADDING, PADDING, PADDING, DEFAULT_FOREGROUND_COLOR));
        title.setBackground(DEFAULT_FOREGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        forum.add(title, gbc);
    }

    private void initTitle() {
        titleText = new JLabel();
        titleText.setFont(new Font("Verdana", Font.PLAIN, TITLE_FONT_SIZE));
        aboutSectionText = new JLabel();
        aboutSectionText.setFont(new Font("Verdana", Font.PLAIN, (int)(TITLE_FONT_SIZE / 2)));
        subCountText = new JLabel();
        subCountText.setFont(new Font("Verdana", Font.PLAIN, (int)(TITLE_FONT_SIZE / 2)));

        initTitlePosition();
    }

    private void initTitlePosition() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        title.add(titleText, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        title.add(aboutSectionText, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        title.add(subCountText, gbc);
    }

    private void refreshTitleText(boolean loggedIn) {
        if (loggedIn) {
            User user = postIt.getCurrentUser();
            titleText.setText("Welcome to your custom home feed, " + user.getUserName() + "!");
            aboutSectionText.setText("Here you can see posts from communities you subscribed to");
            subCountText.setText("Current Communities: " + user.getSubscribedCommunities());
        } else {
            titleText.setText("Welcome to the default home feed!");
            aboutSectionText.setText("Here you can see posts from default communities.");
            subCountText.setText("Current Default Communities: " + PostIt.DEFAULT_COMMUNITIES);
        }

        repaint();
    }

    private void refreshTitleText(User user) {
        titleText.setText("Welcome to " + user.getUserName() + "'s profile!");
        aboutSectionText.setText(user.getBio());
        subCountText.setText("Here are " + user.getUserName() + "'s posts:");
        repaint();
    }

    private void refreshTitleText(Community community) {
        titleText.setText("Welcome to the " + community.getCommunityName() + " community!");
        aboutSectionText.setText(community.getCommunityAbout());
        subCountText.setText("Current Subscribers: " + community.getSubCount());
        repaint();
    }

    private void refreshTitleText() {
        if (userBeingViewed != null) {
            refreshTitleText(postIt.getUsernamePasswords().get(userBeingViewed));
        } else if (communityBeingViewed != null) {
            refreshTitleText(postIt.getCommunities().get(communityBeingViewed));
        } else {
            refreshTitleText(postIt.getLoggedIn());
        }
    }

    private void initButtons(boolean editProfile) {
        buttons.removeAll();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(Box.createHorizontalGlue());
        buttons.add(backButton);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(makePostButton);
        buttons.add(Box.createHorizontalGlue());
        if (editProfile) {
            buttons.add(editProfileButton);
            buttons.add(Box.createHorizontalGlue());
        }
        buttons.add(createCommunityButton);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(nextButton);
        buttons.add(Box.createHorizontalGlue());
    }

    private void initButtonActions() {
        initBackButtonAction();
        initNextButtonAction();
        initEditProfileButtonAction();
        initMakePostButtonAction();
        initCreateCommunityButtonAction();

    }

    private void initCreateCommunityButtonAction() {
        createCommunityButton = new JButton();
        createCommunityButton.setText("Create Community");
        createCommunityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (postIt.getLoggedIn()) {
                    createCommunity();
                } else {
                    loginWarning(PostItApp.this);
                }
            }
        });
    }

    private void initMakePostButtonAction() {
        makePostButton = new JButton();
        makePostButton.setText("Make Post");
        makePostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (postIt.getLoggedIn()) {
                    makePostPressed();
                } else {
                    loginWarning(PostItApp.this);
                }
            }
        });
    }

    private void initEditProfileButtonAction() {
        editProfileButton = new JButton();
        editProfileButton.setText("Edit Profile");
        editProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editProfilePressed();
            }
        });
    }

    private void initNextButtonAction() {
        nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextButtonPressed();
            }
        });
    }

    private void initBackButtonAction() {
        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backButtonPressed();
            }
        });
    }

    private void createCommunity() {
        CreateCommunityDisplay display = new CreateCommunityDisplay(postIt);
        display.makeVisible();
    }

    // TODO
    private void backButtonPressed() {
        try {
            activeFeed.back();
        } catch (StartOfFeedException eofe) {
            JOptionPane.showMessageDialog(this,
                    "You've reached the start of the Feed!",
                    "Warning", JOptionPane.ERROR_MESSAGE);
        }
    }

    // TODO
    private void nextButtonPressed() {
        try {
            activeFeed.next();
            repaint();
        } catch (EndOfFeedException eofe) {
            JOptionPane.showMessageDialog(this,
                    "You've reached the end of the Feed!",
                    "Warning", JOptionPane.ERROR_MESSAGE);
        }
    }



    // TODO
    private void initWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveForumCheck();
            }
        });
    }

    // TODO
    private void saveForumCheck() {
        int userChoice = JOptionPane.showConfirmDialog(this,
                "Would you like to save your posts? Close this message to Auto-save.",
                "Save Posts", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (userChoice == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }

    private JMenu makeNavigateMenu() {
        JMenu navigateMenu = new JMenu("Navigate");
        navigateMenu.setMnemonic(KeyEvent.VK_N);
        addToMenu(navigateMenu, new HomeFeedAction(),
                KeyStroke.getKeyStroke("control H"));
        addToMenu(navigateMenu, new VisitCommunityAction(),
                KeyStroke.getKeyStroke("control V"));
        addToMenu(navigateMenu, new BrowseCommunitiesAction(),
                KeyStroke.getKeyStroke("control B"));
        addToMenu(navigateMenu, new VisitUserAction(),
                KeyStroke.getKeyStroke("control U"));
        return navigateMenu;
    }

    private JMenu makeUserMenu() {
        JMenu userMenu = new JMenu("Your Profile");
        userMenu.setMnemonic(KeyEvent.VK_U);
        if (postIt.getLoggedIn()) {
            addToMenu(userMenu, new UserProfileAction(),
                    KeyStroke.getKeyStroke("control P"));
        }
        addToMenu(userMenu, new RegisterAction(),
                KeyStroke.getKeyStroke("control R"));
        addToMenu(userMenu, new LoginAction(),
                KeyStroke.getKeyStroke("control L"));
        addToMenu(userMenu, new LogoutAction(),
                KeyStroke.getKeyStroke("control O"));
        return userMenu;
    }

    // TODO
    public void initMenuBar() {
        JMenuBar menu = new JMenuBar();
        menu.add(makeNavigateMenu());
        menu.add(makeUserMenu());
        setJMenuBar(menu);
    }

    private void getHomeFeed() {
        try {
            userBeingViewed = null;
            communityBeingViewed = null;
            this.activeFeed = postIt.startHomeFeed();
            activeFeed.setDisplay(feed);
        } catch (Exception e) {
            e.getStackTrace();
        }

        initButtons(false);
        refreshTitleText();
    }


    private void editProfilePressed() {
        String newBio = JOptionPane.showInputDialog(this,
                "Please enter your new bio.",
                "Update Profile",
                JOptionPane.QUESTION_MESSAGE);

        if (!checkEmptyString(newBio)) {
            postIt.getCurrentUser().setBio(newBio);
            JOptionPane.showMessageDialog(this,
                    "Profile Successfully Updated!",
                    "Success",
                    JOptionPane.PLAIN_MESSAGE);
        } else {
            invalidInput(this, "bio");
        }
    }

    public static void invalidInput(Container parent, String inputType) {
        JOptionPane.showMessageDialog(parent,
                "Please enter a valid " + inputType,
                "Invalid Input",
                JOptionPane.WARNING_MESSAGE);
    }


    private void makePostPressed() {
        MakePostDisplay makePost = new MakePostDisplay(postIt, (int)(widthPx / 2), heightPx);
        System.out.println("ok");
    }

    private void getCommunityFeed(String community) {
        try {
            userBeingViewed = null;
            communityBeingViewed = community;
            this.activeFeed = postIt.startCommunityFeed(community);
            activeFeed.setDisplay(feed);
        } catch (EmptyFeedException e) {
            e.getStackTrace();
            System.out.println("empty");
        }

        initButtons(false);
        refreshTitleText();
    }

    private void getUserFeed(String username) {
        try {
            userBeingViewed = username;
            communityBeingViewed = null;
            this.activeFeed = postIt.visitUser(username);
            activeFeed.setDisplay(feed);
        } catch (EmptyFeedException e) {
            e.getStackTrace();
        }

        if (postIt.getCurrentUser() != null
                && postIt.getCurrentUser().getUserName().equals(username)) {
            initButtons(true);
        } else {
            initButtons(false);
        }
        refreshTitleText();
    }

    // Method taken from AlarmControllerUI class in
    // https://github.students.cs.ubc.ca/CPSC210/AlarmSystem
    // EFFECTS: Adds an item with given handler to the given menu
    private void addToMenu(JMenu theMenu, AbstractAction action, KeyStroke accelerator) {
        JMenuItem menuItem = new JMenuItem(action);
        menuItem.setMnemonic(menuItem.getText().charAt(0));
        menuItem.setAccelerator(accelerator);
        theMenu.add(menuItem);
    }

    private void getCommunitySelection(Object[] communities) {
        String communityChoice = (String)JOptionPane.showInputDialog(PostItApp.this,
                "Please select the community you would like to visit.",
                "Browse Communities",
                JOptionPane.QUESTION_MESSAGE,
                null,
                communities,
                communities[0]);
        System.out.println(communityChoice);
        checkCommunityInput(communityChoice);

    }

    private void getCommunityInput() {
        String communityChoice = JOptionPane.showInputDialog(PostItApp.this,
                "Please select the community you would like to visit.",
                "Visit a Community",
                JOptionPane.QUESTION_MESSAGE);
        checkCommunityInput(communityChoice);
    }

    public static boolean checkEmptyString(String str) {
        return (str == null || str.length() == 0);
    }

    private void checkCommunityInput(String communityChoice) {
        if (postIt.getCommunities().containsKey(communityChoice)) {
            System.out.println(communityChoice);
            getCommunityFeed(communityChoice);
        } else if (!checkEmptyString(communityChoice)) {
            notFoundError("Community");
        }
    }

    private void notFoundError(String notFound) {
        JOptionPane.showMessageDialog(PostItApp.this,
                notFound + " not Found!",
                "Error",
                JOptionPane.WARNING_MESSAGE);
    }

    private void getUserNameInput() {
        String userChoice = JOptionPane.showInputDialog(PostItApp.this,
                "Please select the user you would like to visit.",
                "Visit a User",
                JOptionPane.QUESTION_MESSAGE);
        checkUserNameInput(userChoice);
    }

    private void checkUserNameInput(String userChoice) {
        if (postIt.getUsernamePasswords().containsKey(userChoice)) {
            getUserFeed(userChoice);
        } else if (!checkEmptyString(userChoice)) {
            notFoundError("User");
        }
    }

    private void register() {
        if (postIt.getLoggedIn()) {
            JOptionPane.showMessageDialog(this,
                    "You're already logged in, log out first to create a new account.",
                    "You're Logged In",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            RegisterDisplay register = new RegisterDisplay(postIt);
            register.makeVisible();
        }
    }

    private void login() {
        if (postIt.getLoggedIn()) {
            JOptionPane.showMessageDialog(this,
                    "You're already logged in!",
                    "You're Logged In",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            LoginDisplay login = new LoginDisplay(postIt);
            login.makeVisible();
            login.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    getHomeFeed();
                }
            });
        }
    }

    private void logout() {
        if (postIt.getLoggedIn()) {
            int userChoice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to log out?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (userChoice == JOptionPane.YES_OPTION) {
                postIt.logOut();
                initMenuBar();
                getHomeFeed();
            }
        } else {
            loginWarning(this);
        }
    }

    public static void loginWarning(Component parent) {
        JOptionPane.showMessageDialog(parent,
                "You have to be logged in to do that!",
                "Not Logged In",
                JOptionPane.WARNING_MESSAGE);
    }

    public class HomeFeedAction extends AbstractAction {

        HomeFeedAction() {
            super("Go Home");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getHomeFeed();

        }
    }

    public class VisitCommunityAction extends AbstractAction {

        VisitCommunityAction() {
            super("Visit a Community");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getCommunityInput();
        }

    }

    public class BrowseCommunitiesAction extends AbstractAction {

        BrowseCommunitiesAction() {
            super("Browse all Communities");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object[] communities = postIt.getCommunities().keySet().toArray(new String[0]);
            getCommunitySelection(communities);
        }

    }

    public class VisitUserAction extends AbstractAction {

        VisitUserAction() {
            super("Visit a User");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getUserNameInput();
        }
    }

    public class UserProfileAction extends AbstractAction {

        UserProfileAction() {
            super("Profile");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getUserFeed(postIt.getCurrentUser().getUserName());
        }

    }

    public class LogoutAction extends AbstractAction {

        LogoutAction() {
            super("Logout");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            logout();
        }

    }

    public class LoginAction extends AbstractAction {

        LoginAction() {
            super("Login");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            login();
        }

    }

    public class RegisterAction extends AbstractAction {

        RegisterAction() {
            super("Register");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            register();
        }

    }

    public static void clearActionListeners(JButton button) {
        for (ActionListener al : button.getActionListeners()) {
            button.removeActionListener(al);
        }

    }


}
