package ui;


import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        PostItApp postItApp = new PostItApp();
        try {
            postItApp.start();
        } catch (IOException e) {
            System.out.println("Unable to start forum, file not found.");
        }
    }
}
