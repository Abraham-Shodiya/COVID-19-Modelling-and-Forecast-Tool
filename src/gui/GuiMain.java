package gui;

import data.CovidDataDaily;
import data.CsvReader;
import data.GetDataFromDatabase;
import data.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class GuiMain {
    public static DrawTimeScaleGraphs casesGraph;
    public static DrawTimeScaleGraphs deathsGraph;
    public static double screenWidth = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
    public static double screenHeight = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();
    public static DrawGui begin;
    public static int graphYear = 120;
    public static final Color graphBackgroundColour = new Color(211, 230, 131);
    public static int numWeeksToPredict = 0;
    public static List<User> users = new ArrayList<>();
    public static LoginGui loginScreen;

    public static void main(String[] args) throws FileNotFoundException, ParseException {
        String casesFilePath = "./resources/covidData/cleaned_cases.csv";
        String deathsFilePath = "./resources/covidData/cleaned_deaths.csv";
        List<CovidDataDaily> cases = CsvReader.getCovidCasesFromCsv(casesFilePath);
        List<CovidDataDaily> deaths = CsvReader.getCovidDeathsFromCsv(deathsFilePath);
        GetDataFromDatabase d = new GetDataFromDatabase();
        users = d.getData();
        loginScreen = new LoginGui(users);
        begin = new DrawGui(cases, deaths, screenWidth, screenHeight);
    }

    public static class ButtonHandlerCases implements ActionListener{
        List<CovidDataDaily> cases;

        public ButtonHandlerCases(List<CovidDataDaily> cases) {
            this.cases = cases;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame casesFrame = new JFrame();
            casesFrame.getContentPane().setBackground(graphBackgroundColour);
            List<Image> icons = new ArrayList<>();
            icons.add(new ImageIcon("./resources/images/chart-increasing.png").getImage());
            icons.add(new ImageIcon("./resources/images/chart-increasing.png").getImage());
            casesFrame.setIconImages(icons);
            casesFrame.setTitle("Cases Graph");
            casesFrame.setSize((int) (screenWidth), (int) (screenHeight));
            casesFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            casesFrame.setUndecorated(true);
            casesFrame.setVisible(true);
            casesGraph = new DrawTimeScaleGraphs(cases, graphYear, (int) screenWidth, (int) screenHeight, false, numWeeksToPredict);
            casesGraph.createAxisLabels();
            for (int i = 0; i < 12; i++){
                try {
                    casesFrame.add(casesGraph.timeIncrementLabels[i]);
                } catch(Exception ex) {
                    break;
                }
            }
            casesFrame.add(casesGraph.title);
            casesFrame.add(casesGraph.xLabel);
            casesFrame.add(casesGraph.yLabel);
            casesFrame.add(casesGraph.inc0);
            casesFrame.add(casesGraph.inc1);
            casesFrame.add(casesGraph.inc2);
            casesFrame.add(casesGraph.inc3);
            casesFrame.add(casesGraph.backButton);
            if (numWeeksToPredict != 0){
                casesFrame.add(casesGraph.predictionLabel);
            }
            casesFrame.add(casesGraph);

        }
    }

    public static class ButtonHandlerDeaths implements ActionListener{
        List<CovidDataDaily> deaths;

        public ButtonHandlerDeaths(List<CovidDataDaily> deaths) {
            this.deaths = deaths;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            JFrame deathsFrame = new JFrame();
            deathsFrame.getContentPane().setBackground(graphBackgroundColour);
            List<Image> icons = new ArrayList<>();
            icons.add(new ImageIcon("./resources/images/chart-increasing.png").getImage());
            icons.add(new ImageIcon("./resources/images/chart-increasing.png").getImage());
            deathsFrame.setIconImages(icons);
            deathsFrame.setTitle("Deaths Graph");
            deathsFrame.setSize((int) (screenWidth), (int) (screenHeight));
            deathsFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            deathsFrame.setUndecorated(true);
            deathsFrame.setVisible(true);
            deathsGraph = new DrawTimeScaleGraphs(deaths, graphYear, (int) screenWidth, (int) screenHeight, true, numWeeksToPredict);
            deathsGraph.createAxisLabels();
            for (int i = 0; i < 12; i++){
                try {
                    deathsFrame.add(deathsGraph.timeIncrementLabels[i]);
                } catch(Exception ex) {
                    break;
                }
            }
            deathsFrame.add(deathsGraph.title);
            deathsFrame.add(deathsGraph.xLabel);
            deathsFrame.add(deathsGraph.yLabel);
            deathsFrame.add(deathsGraph.inc0);
            deathsFrame.add(deathsGraph.inc1);
            deathsFrame.add(deathsGraph.inc2);
            deathsFrame.add(deathsGraph.inc3);
            deathsFrame.add(deathsGraph.backButton);
            if (numWeeksToPredict != 0){
                deathsFrame.add(deathsGraph.predictionLabel);
            }
            deathsFrame.add(deathsGraph);
        }
    }

    public static class RadioButtonHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if (begin.year2020.isSelected()){
                graphYear = 120;
            } else if (begin.year2021.isSelected()){
                graphYear = 121;
            }
        }
    }

    public static class PredictionButtonHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                numWeeksToPredict = Integer.parseInt(begin.predictionTextBox.getText());
                if (numWeeksToPredict > screenHeight){
                    numWeeksToPredict = (int) screenHeight;
                    begin.predictionTextBox.setText(String.valueOf(screenHeight));
                }
            } catch (Exception ex){
                numWeeksToPredict = 0;
                begin.predictionTextBox.setText("0");
            }
        }
    }

    /**
     * Closes current window that button is called from
     */
    public static class BackButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(casesGraph != null){
                casesGraph.createImage();
            }else if(deathsGraph != null){
                deathsGraph.createImage();
            }


            Robot r = null;
            try {
                r = new Robot();
            } catch (AWTException ex) {
                ex.printStackTrace();
            }
            assert r != null;
            r.keyPress(KeyEvent.VK_ALT);
            r.keyPress(KeyEvent.VK_F4);
            r.keyRelease(KeyEvent.VK_ALT);
            r.keyRelease(KeyEvent.VK_F4);
        }
    }
}
