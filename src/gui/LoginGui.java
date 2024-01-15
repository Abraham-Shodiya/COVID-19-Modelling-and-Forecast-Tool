package gui;

import data.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Objects;
import javax.swing.JOptionPane;

import static gui.GuiMain.*;

public class LoginGui {
    public boolean isVisible = true;
    public final List<User> users;
    public User loggedInUser;
    public JFrame loginMenu;
    public boolean loggedIn = false;

    public LoginGui(List<User> users) {
        this.users = users;
        Color buttonColour = new Color(66, 245, 200);
        Color backgroundColour = new Color(50, 168, 168);
        JPanel panel = new JPanel();
        panel.setLayout(null);


        loginMenu = new JFrame();
        loginMenu.setTitle("LOGIN PAGE");
        loginMenu.setSize((int) screenWidth, (int) screenHeight);
        //loginMenu.setLayout(new GridBagLayout());
        loginMenu.setLayout(panel.getLayout());
        loginMenu.setVisible(isVisible);
        loginMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// Close action.
        loginMenu.getContentPane().setBackground(backgroundColour);
        loginMenu.add(panel);

        JLabel t = new JLabel("");
        t.setBounds(750, 280, 400,400);
        t.setBorder(BorderFactory.createLineBorder(buttonColour, 1));
        JLabel title = new JLabel(" WELCOME!");
        title.setFont(new Font("Serif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createLineBorder(buttonColour, 1));
        title.setBounds(800, 340, 200, 40);
        JLabel firstNameLabel = new JLabel("First name");
        firstNameLabel.setBounds(800, 400, 100, 20);
        JTextField firstName = new JTextField();
        firstName.setBackground(buttonColour);
        firstName.setBounds(800, 420, 150, 20);
        JLabel lastNameLabel = new JLabel("Surname");
        lastNameLabel.setBounds(800, 480, 100, 20);
        JTextField lastName = new JTextField();
        lastName.setBounds(800, 500, 150, 20);
        lastName.setBackground(buttonColour);
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(800, 560, 100, 20);
        JPasswordField password = new JPasswordField();
        password.setBounds(800, 580, 150, 20);
        password.setBackground(buttonColour);
        password.setEchoChar('*');
        JButton enterButton = new JButton("Enter");
        enterButton.setBounds(1000, 580, 100, 20);

        loginMenu.add(t);
        loginMenu.add(title);
        loginMenu.add(firstNameLabel);
        loginMenu.add(firstName);
        loginMenu.add(lastNameLabel);
        loginMenu.add(lastName);
        loginMenu.add(passwordLabel);
        loginMenu.add(password);
        enterButton.setBackground(buttonColour);
        enterButton.addActionListener(e -> {
            for (User user : users){
                if (Objects.equals(firstName.getText(), user.getFirstName()) && Objects.equals(lastName.getText(), user.getSurname()) && hashString(new String(password.getPassword())).equals(user.getPassword()) && !loggedIn){
                    loggedIn = true;
                    loggedInUser = user;
                }
            }
            if (loggedIn){
                begin.drawIt();
                begin.setVisible(true);
                loginMenu.setVisible(false);
            } else {
                MessageBox.infoBox("That data is not found in the database", "Incorrect data");
            }
        });
        firstName.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    for (User user : users){
                        if (Objects.equals(firstName.getText(), user.getFirstName()) && Objects.equals(lastName.getText(), user.getSurname()) && hashString(new String(password.getPassword())).equals(user.getPassword()) && !loggedIn){
                            loggedIn = true;
                            loggedInUser = user;
                        }
                    }
                    if (loggedIn){
                        begin.drawIt();
                        begin.setVisible(true);
                        loginMenu.setVisible(false);
                    } else {
                        MessageBox.infoBox("That data is not found in the database", "Incorrect data");
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        lastName.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    for (User user : users){
                        if (Objects.equals(firstName.getText(), user.getFirstName()) && Objects.equals(lastName.getText(), user.getSurname()) && hashString(new String(password.getPassword())).equals(user.getPassword()) && !loggedIn){
                            loggedIn = true;
                            loggedInUser = user;
                        }
                    }
                    if (loggedIn){
                        begin.drawIt();
                        begin.setVisible(true);
                        loginMenu.setVisible(false);
                    } else {
                        MessageBox.infoBox("That data is not found in the database", "Incorrect data");
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        password.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    for (User user : users){
                        if (Objects.equals(firstName.getText(), user.getFirstName()) && Objects.equals(lastName.getText(), user.getSurname()) && hashString(new String(password.getPassword())).equals(user.getPassword()) && !loggedIn){
                            loggedIn = true;
                            loggedInUser = user;
                        }
                    }
                    if (loggedIn){
                        begin.drawIt();
                        begin.setVisible(true);
                        loginMenu.setVisible(false);
                    } else {
                        MessageBox.infoBox("That data is not found in the database", "Incorrect data");
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        loginMenu.add(enterButton);

        loginMenu.pack();
        loginMenu.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /**
     * Creates hash of password
     * @param input Input password
     * @return Hashed password
     */
    public static String hashString(String input){
        int hash = 7;
        for (int i = 0; i < input.length(); i++) {
            hash = hash*31 + input.charAt(i);
        }
        return String.valueOf(hash);
    }

    public static class MessageBox {
        public static void infoBox(String infoMessage, String titleBar) {
            JOptionPane.showMessageDialog(null, infoMessage, "Warning: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
