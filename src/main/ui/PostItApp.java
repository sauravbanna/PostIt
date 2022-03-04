package ui;

import exceptions.EmptyFeedException;
import model.Community;
import model.PostIt;
import model.User;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static model.PostIt.*;
import static ui.Feed.showCommands;


// A forum where a user can register an account and login / logout
// Contains communities that the user can create, post to, visit and view the posts of
// as well as user profiles of the user and other registered users
public class PostItApp {

    // CONSTANTS
    public static final String FORUM_DATA = "./data/forum.json";

    // FIELDS
    private Scanner input;
    private String userInput;
    private PostIt postIt;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // METHODS

    // Constructor
    // EFFECTS: creates a new PostIt Forum and instantiates the Scanner input to take in user input
    //          and instantiates the JSON writer and reader to the correct file location
    public PostItApp() {
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(FORUM_DATA);
        jsonReader = new JsonReader(FORUM_DATA);
    }

    // MODIFIES: this
    // EFFECTS: starts the forum for the user
    @SuppressWarnings("methodlength")
    public void start() throws IOException {
        postIt = jsonReader.read();
        postIt.addDefaultCommunitiesCheck();
        System.out.println("Welcome to PostIt!");
        System.out.println("Type " + HOME_COMMAND + " to browse your home feed, "
                + VIEW_COMMUNITY_COMMAND + " to view a specific community, "
                + SHOW_AVAILABLE_COMMUNITIES + " to see all available communities, "
                + "\n"
                + SUBSCRIBE_TO_COMMUNITY_COMMAND + " to subscribe to a community, "
                + VIEW_USER_COMMAND + " to view a user's profile, "
                + LOGIN_COMMAND + " to log in to your account, "
                + "\n"
                + CREATE_COMMUNITY_COMMAND + " to create a new community, "
                + REGISTER_COMMAND + " to register an account, and "
                + EXIT_COMMAND + " to exit the forum.");
        userInput = input.nextLine();
        while (true) {
            switch (userInput) {
                case HOME_COMMAND:
                    userInput = showHomeFeed();
                    break;
                case SUBSCRIBE_TO_COMMUNITY_COMMAND:
                    userInput = subscribeToCommunity();
                    break;
                case SHOW_AVAILABLE_COMMUNITIES:
                    userInput = showAvailableCommunities();
                    break;
                case CREATE_COMMUNITY_COMMAND:
                    userInput = createCommunity();
                    break;
                case VIEW_COMMUNITY_COMMAND:
                    userInput = visitCommunity();
                    break;
                case VIEW_USER_COMMAND:
                    userInput = viewUser();
                    break;
                case LOGIN_COMMAND:
                    userInput = loginCheck();
                    break;
                case LOGOUT_COMMAND:
                    userInput = logOut();
                    break;
                case MAKE_POST_COMMAND:
                    userInput = makeTextPost();
                    break;
                case REGISTER_COMMAND:
                    userInput = registerCheckIfLoggedIn();
                    break;
                case EXIT_COMMAND:
                    userInput = exitForum();
                    break;
                case NEXT_ACTION_COMMAND:
                    userInput = nextAction();
                    break;
                case HELP_COMMAND:
                    showCommands();
                    userInput = NEXT_ACTION_COMMAND;
                    break;
                default:
                    System.out.println("Sorry, I didn't understand you, please enter a valid command. Type "
                            + HELP_COMMAND + " to see all available commands.");
                    userInput = input.nextLine();
                    break;
            }
        }
    }

    // EFFECTS: checks if user if already logged in, if yes, lets user know and returns NEXT_ACTION_COMMAND
    //          else, starts the log in process
    public String loginCheck() {
        if (postIt.getLoggedIn()) {
            System.out.println("You're already logged in!");
            return NEXT_ACTION_COMMAND;
        } else {
            return logIntoAccount();
        }
    }

    // MODIFIES: this, JsonWriter, PostIt
    // EFFECTS: tries to exit the forum and save the forum to file
    //          if successful, clears active feed and exits
    //          if save is unsucessful due to file not being found
    //          asks user if they still want to exit and have their work be deleted
    //          if yes, exits. if no, returns NEXT_ACTION_COMMAND
    //          prompts user to give input again if invalid input is entered
    public String exitForum() {
        try {
            jsonWriter.openWriter();
            jsonWriter.saveForum(postIt);
            jsonWriter.close();
        } catch (FileNotFoundException fe) {
            System.out.println("Unable to save your posts during this session, are you sure you want to exit? (Y/N)");
            userInput = input.nextLine();
            while (!userInput.equals("Y") && !userInput.equals("N")) {
                System.out.println("Sorry, I didn't understand you, please type Y to exit or N to not exit.");
                userInput = input.nextLine();
            }
            if (userInput.equals("N")) {
                return NEXT_ACTION_COMMAND;
            }
        }
        System.out.println("Thanks for using PostIt!");
        postIt.clearActiveFeed();
        System.exit(0);
        return null;
    }

    // MODIFIES: this, PostIt
    // EFFECTS: if active feed doesn't exist, asks user what they want to do next
    //          returns user input
    //          if active feed exists, starts active feed again
    public String nextAction() {
        if (postIt.getActiveFeed() != null) {
            System.out.println("Resuming your previous feed ...");
            return postIt.getActiveFeed().start();
        } else {
            System.out.println("What would you like to do next?");
            return input.nextLine();
        }
    }

    // MODIFIES: this, PostIt
    // EFFECTS: if user is logged in, logs them out and clears the feed
    //          if user is not logged in, tells them that they are not
    public String logOut() {
        if (!postIt.getLoggedIn()) {
            System.out.println("You're not logged in!");
        } else {
            System.out.println("Successfully logged out!");
            postIt.logOut();
        }
        return NEXT_ACTION_COMMAND;
    }

    // MODIFIES: this
    // EFFECTS: checks if user is logged in or not
    //          if logged in, asks user if they want to log out and register a new account
    //          if yes, starts registration process
    //          if no or invalid input, returns NEXT_ACTION_COMMAND
    //          if user is not logged in, starts registration process
    public String registerCheckIfLoggedIn() {
        if (postIt.getLoggedIn()) {
            System.out.println("You're already logged in, do you want to log out? (Y/N)");
            userInput = input.nextLine();
            if (userInput.equals("Y")) {
                logOut();
                return registerAccount();
            } else {
                if (userInput.equals("N")) {
                    System.out.println("Ok, cancelling registration ... ");
                } else {
                    System.out.println("Sorry, I didn't understand what you said.");
                }
                return NEXT_ACTION_COMMAND;
            }
        } else {
            return registerAccount();
        }
    }

    // EFFECTS: prints out the names of all currently existing communities
    //          and returns the NEXT_ACTION_COMMAND
    public String showAvailableCommunities() {
        System.out.println("These are the currently available communities.");
        for (String community : postIt.getCommunities().keySet()) {
            System.out.print(community + " ");
        }
        System.out.println();

        return NEXT_ACTION_COMMAND;
    }

    // MODIFIES: this, PostIt
    // EFFECTS: prompts user to enter a username and password, and if valid, creates a new user with those details
    //          and adds it to the forum
    //          if not valid, lets user know to enter a valid input
    //          username is valid if it is between 1-20 characters, does not already exist on PostIt,
    //          and is not SELF_PROFILE_COMMAND
    //          password is valid if it is >= 8 characters
    //          returns NEXT_ACTION_COMMAND at the end or when user types EXIT_COMMAND
    public String registerAccount() {
        System.out.println("You can always type "  + EXIT_COMMAND + " to cancel the registration.");
        System.out.println("Please enter your desired username between 1-" + MAX_USERNAME_LENGTH + " characters.");
        System.out.println("You can't change this later.");
        if (checkUsernameInput()) {
            String username = userInput;
            System.out.println("Please enter a password longer than " + MIN_PASSWORD_LENGTH + " characters.");
            if (checkPasswordInput()) {
                String password = userInput;
                postIt.addUser(username, new User(username, password));
                System.out.println("Successfully registered your account! Log in by typing " + LOGIN_COMMAND);
            }
        }
        return NEXT_ACTION_COMMAND;
    }

    // MODIFIES: this
    // EFFECTS: takes in a username as input from user and if valid, returns true
    //          username valid according to isValid()
    //          if not, loops until user enters valid input
    //          returns false if user enters EXIT_COMMAND
    public Boolean checkUsernameInput() {
        do {
            userInput = input.nextLine();
            if (userInput.equals(EXIT_COMMAND)) {
                return false;
            }
            if (isInvalid(userInput)) {
                System.out.println("Please enter a valid username, or that username already exists.");
            }
        } while (isInvalid(userInput));

        return true;
    }

    // MODIFIES: this
    // EFFECTS: takes in a password as input from user and if valid, returns true
    //          password valued if length > MIN_PASSWORD_LENGTH
    //          if not, loops until user enters valid input
    //          returns false if user enters EXIT_COMMAND
    public Boolean checkPasswordInput() {
        do {
            userInput = input.nextLine();
            if (userInput.equals(EXIT_COMMAND)) {
                return false;
            }
            if (userInput.length() < MIN_PASSWORD_LENGTH) {
                System.out.println("Please enter a password longer than " + MIN_PASSWORD_LENGTH + " characters.");
            }
        } while (userInput.length() < MIN_PASSWORD_LENGTH);

        return true;
    }

    // EFFECTS: returns true if the given username is invalid, false if valid
    //          username is valid if it is between 1-20 characters, does not already exist on PostIt,
    //          and is not SELF_PROFILE_COMMAND
    public Boolean isInvalid(String username) {
        return (username.length() < 1 || username.length() > MAX_USERNAME_LENGTH
                || postIt.getUsernamePasswords().containsKey(username)
                || username.equals(SELF_PROFILE_COMMAND));
    }

    // MODIFIES: this, PostIt
    // EFFECTS: if the given password matches the given username, logs in user
    //          otherwise, prompts user to re-enter details
    //          returns NEXT_ACTION_COMMAND at the end or when user types EXIT_COMMAND
    public String logIntoAccount() {
        System.out.println("You can always type "  + EXIT_COMMAND + " to cancel the log in.");
        System.out.println("Please enter your username: ");
        String username = input.nextLine();
        if (username.equals(EXIT_COMMAND)) {
            return NEXT_ACTION_COMMAND;
        }
        System.out.println("Please enter your password: ");
        if (checkPasswordWhenLogin(username)) {
            postIt.login(username);
            System.out.println("Successfully logged in!");
        }
        return NEXT_ACTION_COMMAND;
    }

    // MODIFIES: this
    // EFFECTS: checks the user entered password when logging in
    //          returns false if user enters EXIT_COMMAND or user-entered username is not registered
    //          prompt user to enter password again if wrong password
    //          returns true once right password is entered
    public Boolean checkPasswordWhenLogin(String username) {
        do {
            userInput = input.nextLine();
            if (userInput.equals(EXIT_COMMAND)) {
                return false;
            }
            if (!postIt.getUsernamePasswords().containsKey(username)) {
                System.out.println("That username doesn't exist!");
                return false;
            }
            if (!(userInput.equals(postIt.getUsernamePasswords().get(username).getPassword()))) {
                System.out.println("That's the wrong password!");
            }
        } while (!(userInput.equals(postIt.getUsernamePasswords().get(username).getPassword())));
        return true;
    }

    // EFFECTS: prints out the community info of the given community
    public void printCommunityInfo(Community c) {
        System.out.println("Welcome to the " + c.getCommunityName() + " community!");
        System.out.println(c.getCommunityAbout());
        System.out.println("Current Subscribers: " + c.getSubCount());
        System.out.println("Created by: " + c.getCreator());
        System.out.println("Here are the posts from this community: ");
    }

    // EFFECTS: print out the user info of the given user
    public void printUserInfo(User u) {
        System.out.println("Welcome to " + u.getUserName() + "'s profile!");
        System.out.println("Bio: " + u.getBio());
        System.out.println("Subscribed Communities: " + u.getSubscribedCommunities());
    }

    // MODIFIES: this, PostIt
    // EFFECTS: if logged in, subscribes user to specified community
    //          if valid community name is given
    //          if not logged in, tells user to log in
    //          returns NEXT_ACTION_COMMAND at the end or when user types EXIT_COMMAND
    public String subscribeToCommunity() {
        if (postIt.getLoggedIn()) {
            System.out.println("Please enter the name of an "
                    + "existing community that you would like to join.");
            if (communitySubscribeCheck()) {
                System.out.println("Successfully subscribed you to the " + userInput + " community!");
            }
        } else {
            System.out.println("You have to be logged in to do that!");
        }
        return NEXT_ACTION_COMMAND;
    }

    // MODIFIES: this, PostIt, User, Community
    // EFFECTS: asks user to enter a community name, if user enters EXIT_COMMAND, returns false
    //          if user enters a non-existent community, prompts user to re-enter community name
    //          if user enters a community they've already subscribed to, prompts user to re-enter community name
    //          returns true once valid community name is entered
    public Boolean communitySubscribeCheck() {
        do {
            userInput = input.nextLine();
            if (userInput.equals(EXIT_COMMAND)) {
                return false;
            }
            if (!postIt.getCommunities().containsKey(userInput)) {
                System.out.println("That's not an existing community! Type " + EXIT_COMMAND
                        + " and use " + SHOW_AVAILABLE_COMMUNITIES
                        + " to see all available communities.");
            }
            if (postIt.getCurrentUser().getSubscribedCommunities().contains(userInput)) {
                System.out.println("You're already subscribed to this community!");
            }
        } while (!postIt.getCommunities().containsKey(userInput)
                || postIt.getCurrentUser().getSubscribedCommunities().contains(userInput));

        postIt.getCurrentUser().subscribeToCommunity(postIt.getCommunities().get(userInput));

        return true;
    }

    // MODIFIES: this, PostIt
    // EFFECTS: if logged in, prompts user to input a valid community name that does not already exist
    //          and an about section, and creates and adds community to list of communities on PostIt
    //          prompts user to re-enter details if invalid community name is entered
    //          if not logged in, tells user to log in
    //          returns NEXT_ACTION_COMMAND at the end or when user types EXIT_COMMAND
    public String createCommunity() {
        if (postIt.getLoggedIn()) {
            System.out.println("You can cancel making a community at anytime by typing " + EXIT_COMMAND);
            System.out.println("Please enter the name of the community you want to create: ");

            String communityName = input.nextLine();
            if (!communityName.equals(EXIT_COMMAND)) {
                if (postIt.getCommunities().containsKey(communityName)) {
                    System.out.println("That community already exists! Please enter another name.");
                    return createCommunity();
                }
                System.out.println("Please enter the about section for " + communityName + ":");
                String aboutCommunity = input.nextLine();
                if (!aboutCommunity.equals(EXIT_COMMAND)) {
                    postIt.addCommunity(communityName, aboutCommunity);
                    System.out.println("Community successfully created!");
                }
            }
        } else {
            System.out.println("You have to be logged in to do that!");
        }

        return NEXT_ACTION_COMMAND;
    }

    // REQUIRES: DEFAULT_COMMUNITIES has at least 1 community
    // MODIFIES: this, PostIt
    // EFFECTS: if user is logged in, shows user posts from their subscribed communities
    //          if user is not logged in, shows user posts from default communities
    //          returns NEXT_ACTION_COMMAND if there are no posts to show
    public String showHomeFeed() {
        System.out.println("This is your Home Feed!");
        if (postIt.getLoggedIn()) {
            System.out.println("Showing you posts from your favorite communities!");
        } else {
            System.out.println("Showing you posts from default communities, "
                    + "log in to see posts from your favorite communities!");
        }
        try {
            return postIt.startHomeFeed().start();
        } catch (EmptyFeedException e) {
            System.out.println("There are no posts to show, please subscribe to some communities!");
            postIt.clearActiveFeed();
            return NEXT_ACTION_COMMAND;
        }
    }

    // MODIFIES: this, PostIt
    // EFFECTS: prompts user to type in a community name
    //          if valid community name, shows user posts of that community
    //          if invalid, tells user that community was not found and returns the next command
    //          returns NEXT_ACTION_COMMAND at the end or when user types EXIT_COMMAND
    public String visitCommunity() {
        System.out.println("Please enter the name of the community you want to visit.");
        System.out.println("Type " + EXIT_COMMAND + " to cancel.");
        userInput = input.nextLine();

        if (userInput.equals(EXIT_COMMAND)) {
            return NEXT_ACTION_COMMAND;
        }

        if (postIt.getCommunities().containsKey(userInput)) {
            printCommunityInfo(postIt.getCommunities().get(userInput));
            return postIt.showCommunity(userInput).start();
        } else {
            System.out.println("Sorry, that community was not found. Use " + SHOW_AVAILABLE_COMMUNITIES
                    + " to see all available communities.");
            return NEXT_ACTION_COMMAND;
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts user to enter a username
    //          if user is a valid user (exists in list of registered users), displays user profile
    //          else, tells user that the user was not found
    //          returns NEXT_ACTION_COMMAND at the end
    public String viewUser() {
        postIt.clearActiveFeed();
        System.out.println("Please enter the name of a registered user who's profile you want to view, "
                + "or " + SELF_PROFILE_COMMAND + " for your own profile");
        userInput = input.nextLine();

        if (postIt.getUsernamePasswords().containsKey(userInput)) {
            User chosenUser = postIt.getUsernamePasswords().get(userInput);
            printUserInfo(chosenUser);
            return viewUserPosts(chosenUser);
        } else if (userInput.equals(SELF_PROFILE_COMMAND)) {
            if (postIt.getLoggedIn()) {
                printUserInfo(postIt.getCurrentUser());
                return editProfile();
            } else {
                System.out.println("You're not logged in! "
                        + "Register an account with " + REGISTER_COMMAND + " and login with "
                        + LOGIN_COMMAND);
            }
        } else {
            System.out.println("Sorry, that user was not found.");
        }

        return NEXT_ACTION_COMMAND;
    }

    // MODIFIES: this
    // EFFECTS: prompts user to view the current user's posts,
    public String viewUserPosts(User u) {
        System.out.println("Type " + VIEW_USER_POSTS + " to view this user's posts, "
                + "or any other command to do something else.");
        userInput = input.nextLine();

        if (userInput.equals(VIEW_USER_POSTS)) {
            System.out.println("Showing you " + u.getUserName() + "'s posts...");
            Feed userPosts = new Feed(u.getPosts(), postIt.getLoggedIn(), postIt.getCurrentUser(), postIt);
            return userPosts.start();
        } else {
            return userInput;
        }
    }

    // MODIFIES: this, PostIt, User
    // EFFECTS: prompts user to edit their bio, and updates their bio if user chooses to
    //          and returns NEXT_ACTION_COMMAND
    //          otherwise, returns any other command the user types
    //          returns NEXT_ACTION_COMMAND at the end
    public String editProfile() {
        System.out.println("Type " + EDIT_PROFILE_COMMAND + " to edit your profile, "
                + VIEW_USER_POSTS + " to view your own posts, "
                + "or any other command to do something else.");
        userInput = input.nextLine();

        if (userInput.equals(EDIT_PROFILE_COMMAND)) {
            System.out.println("Please enter your new bio: ");
            System.out.println("Type " + EXIT_COMMAND + " to exit.");
            String newBio = input.nextLine();

            if (!newBio.equals(EXIT_COMMAND)) {
                postIt.updateBio(newBio);
                System.out.println("Bio successfully updated!");
            }
        } else if (userInput.equals(VIEW_USER_POSTS)) {
            System.out.println("Showing you your own posts...");
            Feed userPosts = new Feed(postIt.getCurrentUser().getPosts(), postIt.getLoggedIn(),
                    postIt.getCurrentUser(), postIt);
            return userPosts.start();
        } else {
            return userInput;
        }

        return NEXT_ACTION_COMMAND;
    }

    // MODIFIES: this, PostIt
    // EFFECTS: if user is logged in, makes a post according to user specifications
    //          in a valid, existing community
    //          if user is not logged in, tells user that they have to log in
    //          returns NEXT_ACTION_COMMAND at the end or when user types EXIT_COMMAND
    public String makeTextPost() {
        if (postIt.getLoggedIn()) {
            System.out.println("You can cancel making a post at anytime by typing " + EXIT_COMMAND);
            System.out.println("Please enter what community you want to post in: ");
            if (checkCommunityInput()) {
                String communityChoice = userInput;
                System.out.println("Please enter your post's title:");
                String title = input.nextLine();
                if (!title.equals(EXIT_COMMAND)) {
                    System.out.println("Please enter your post body:");
                    String body = input.nextLine();
                    if (!body.equals(EXIT_COMMAND)) {
                        Boolean postMade = postIt.makeTextPost(title, body, communityChoice);
                        while (!postMade) {
                            postMade = postIt.makeTextPost(title, body, communityChoice);
                        }
                        System.out.println("Successfully made post!");
                    }
                }
            }
        } else {
            System.out.println("You have to be logged in to do that!");
        }
        return NEXT_ACTION_COMMAND;
    }

    // MODIFIES: this
    // EFFECTS: takes in a community name as input from user and if valid, returns true
    //          valid if community exists on PostIt
    //          if not, loops until user enters valid input
    //          returns false if user enters EXIT_COMMAND
    public Boolean checkCommunityInput() {
        do {
            userInput = input.nextLine();
            if (userInput.equals(EXIT_COMMAND)) {
                return false;
            }
            if (!postIt.getCommunities().containsKey(userInput)) {
                System.out.println("Please enter the name of a valid, existing community.");
            }
        } while (!postIt.getCommunities().containsKey(userInput));

        return true;
    }
}
