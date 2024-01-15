package gui;

import data.CovidDataDaily;
import data.GetDataFromDatabase;
import data.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Draws the main menu and its components onto a new JFrame
 */
public class DrawGui extends JComponent {
    public double screenWidth;
    public double screenHeight;
    List<CovidDataDaily> cases;
    List<CovidDataDaily> deaths;
    public JRadioButton year2020 = new JRadioButton("2020");
    public JRadioButton year2021 = new JRadioButton("2021");
    public JTextField predictionTextBox = new JTextField("0");
    public JFrame mainMenu;

    public DrawGui(List<CovidDataDaily> cases, List<CovidDataDaily> deaths, double screenWidth, double screenHeight) {
        this.cases = cases;
        this.deaths = deaths;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    /**
     * Draws main menu using a layout manager
     */
    public void drawIt(){
        Color buttonColour = new Color(66, 245, 200);
        Color backgroundColour = new Color(50, 168, 168);
        GridBagConstraints c = new GridBagConstraints();
        mainMenu = new JFrame();
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.setSize((int) screenWidth, (int) screenHeight);
        mainMenu.setLayout(new GridBagLayout());
        mainMenu.setVisible(false);

        if (GuiMain.loginScreen.loggedInUser.isViewCases()) {
            //case button
            JButton caseButton = new JButton("Display cases");
            caseButton.setBounds((int) (screenWidth / 4), (int) (screenHeight - 100), 150, 30);
            caseButton.addActionListener(new GuiMain.ButtonHandlerCases(cases));
            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.PAGE_END;
            c.weightx = 0;
            c.gridx = 0;
            c.gridy = 1;
            caseButton.setBackground(buttonColour);
            mainMenu.add(caseButton, c);
        }

        if (GuiMain.loginScreen.loggedInUser.isViewDeaths()) {
            //death button
            JButton deathButton = new JButton("Display deaths");
            deathButton.setBounds((int) (screenWidth / 4), (int) (screenHeight - 400), 150, 30);
            deathButton.addActionListener(new GuiMain.ButtonHandlerDeaths(deaths));
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0;
            c.gridx = 0;
            c.gridy = 2;
            deathButton.setBackground(buttonColour);
            mainMenu.add(deathButton, c);
        }

        if (GuiMain.loginScreen.loggedInUser.isViewPredictions()) {
            //prediction text field and button
            predictionTextBox.setBounds((int) (screenWidth / 4), (int) (screenHeight - 400), 150, 30);
            predictionTextBox.setHorizontalAlignment(JTextField.CENTER);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0;
            c.gridx = 0;
            c.gridy = 5;
            mainMenu.add(predictionTextBox, c);

            JButton predictionEntryButton = new JButton("Enter number of weeks to predict");
            predictionEntryButton.setBounds((int) (screenWidth / 4), (int) (screenHeight - 400), 150, 30);
            predictionEntryButton.addActionListener(new GuiMain.PredictionButtonHandler());
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0;
            c.gridx = 0;
            c.gridy = 6;
            predictionEntryButton.setBackground(buttonColour);
            mainMenu.add(predictionEntryButton, c);
        }

        if (GuiMain.loginScreen.loggedInUser.isAddUser()) {
            JButton addUserButton = new JButton("Add user to database");
            addUserButton.setBounds((int) (screenWidth / 4), (int) (screenHeight - 400), 150, 30);
            //ActionListener
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0;
            c.gridx = 0;
            c.gridy = 7;
            addUserButton.setBackground(buttonColour);
            addUserButton.addActionListener((e -> {
                List<User> usersToAdd = new ArrayList<>();
                int stop = 1;
                while (stop == 1) {
                    String fName = JOptionPane.showInputDialog(mainMenu, "Please enter first name", null);
                    String lName = JOptionPane.showInputDialog(mainMenu, "Please enter last name", null);
                    String password = JOptionPane.showInputDialog(mainMenu, "Please enter password", null);
                    int viewDeaths = JOptionPane.showConfirmDialog(mainMenu, "Can the user view cases?", null, JOptionPane.YES_NO_OPTION);
                    int viewCases = JOptionPane.showConfirmDialog(mainMenu, "Can the user view deaths?", null, JOptionPane.YES_NO_OPTION);
                    int viewPredictions = JOptionPane.showConfirmDialog(mainMenu, "Can the user view/make predictions?", null, JOptionPane.YES_NO_OPTION);
                    int addUser = JOptionPane.showConfirmDialog(mainMenu, "Can the user add new users?", null, JOptionPane.YES_NO_OPTION);
                    usersToAdd.add(new User(fName, lName, LoginGui.hashString(password), viewDeaths == 0, viewCases == 0, viewPredictions == 0, addUser == 0));
                    stop = JOptionPane.showConfirmDialog(mainMenu, "Would you like to stop adding users?", null, JOptionPane.YES_NO_OPTION);
                }
                new GetDataFromDatabase().inputDataToDatabase(usersToAdd);
            })
            );
            mainMenu.add(addUserButton, c);
        }

        //images
        JLabel nhsImage = new JLabel(new ImageIcon("./resources/images/1200px-National_Health_Service_(England)_logo.svg.png"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 1.0;   //request any extra vertical space
        c.weightx = 0;
        c.gridx = 2;
        c.gridy = 0;
        mainMenu.add(nhsImage);

        //radio buttons
        year2020.setSelected(true);
        year2020.setBackground(backgroundColour);
        year2021.setBackground(backgroundColour);
        ButtonGroup group = new ButtonGroup();
        group.add(year2020);
        group.add(year2021);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 3;
        year2020.addActionListener(new GuiMain.RadioButtonHandler());
        mainMenu.add(year2020);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.gridx = 1;
        c.gridy = 3;
        year2021.addActionListener(new GuiMain.RadioButtonHandler());
        mainMenu.add(year2021);

        //Change system tray icon and background colour
        List<Image> icons = new ArrayList<>();
        icons.add(new ImageIcon("./resources/images/1200px-National_Health_Service_(England)_logo.svg.png").getImage());
        icons.add(new ImageIcon("./resources/images/1200px-National_Health_Service_(England)_logo.svg.png").getImage());
        mainMenu.setIconImages(icons);
        mainMenu.setTitle("Main Menu");
        mainMenu.getContentPane().setBackground(backgroundColour);

        mainMenu.pack();
        mainMenu.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public void setVisible(boolean x){
        mainMenu.setVisible(x);
    }

    @Override
    protected void paintComponent(Graphics g) {

    }

    public static class AddUserHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}
