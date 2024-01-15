/*
    CREATED BY OWEN GREENE
 */

package gui;

import data.CovidDataDaily;
import data.TypeOfData;
import regression.LinearRegression;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *  Creates a component with a graph of case data over time along with its labels and axis
 */
public class DrawTimeScaleGraphs extends JComponent {

    private final java.util.List<CovidDataDaily> data;
    private final int screenHeight;
    private final int screenWidth;
    private final int[] dataByDataPoints; //array of all the data
    private double max = 0;
    private final double scaleFactor; //scale factor for converting case data to Y-coordinates on screen
    private final int spacing; //spacing between each data point on screen
    public JLabel[] timeIncrementLabels;
    public JLabel title, xLabel, yLabel, inc0, inc1, inc2, inc3, predictionLabel;
    private final int year; //current year
    private final int dataPoints; //number of points to plot on graph
    private final boolean isDeaths;
    private final int numberOfWeeksToPredict;
    private final int distanceFromTop = 100;
    private int resolutionHeightOffset = 0;
    private int resolutionWidthOffset = 0;
    private int numMonths = 0;
    public JButton backButton = new JButton("Back");

    public DrawTimeScaleGraphs(java.util.List<CovidDataDaily> covidData, int year, int screenWidth, int screenHeight, boolean isDeaths, int numberOfWeeksToPredict) {
        this.data = covidData;
        this.year = year;
        this.numberOfWeeksToPredict = numberOfWeeksToPredict;
        ArrayList<CovidDataDaily> thisYearsDays = getDailyData();
        dataPoints = thisYearsDays.size(); //number of data points after prediction
        this.timeIncrementLabels = new JLabel[13];
        if (screenWidth == 1920 & screenHeight == 1080){
            resolutionWidthOffset = 400;
            resolutionHeightOffset = 300;
        } else if (screenWidth == 2560 & screenHeight == 1440){
            resolutionWidthOffset = 640;
            resolutionHeightOffset = 360;
        }
        this.screenHeight = screenHeight - resolutionHeightOffset;
        this.screenWidth = screenWidth;
        this.isDeaths = isDeaths;
        dataByDataPoints = new int[dataPoints];
        spacing = (screenWidth - resolutionWidthOffset) / dataPoints;
        String[] monthStrings = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (int i = 0; i < dataPoints; i++){                              //csv data
            dataByDataPoints[i] = thisYearsDays.get(i).getNum();
            if (i % 30 == 0){
                numMonths++;
                timeIncrementLabels[i / 30] = new JLabel(monthStrings[(i / 30)]);
                timeIncrementLabels[i / 30].setBounds(i * spacing, screenHeight - resolutionHeightOffset, 100, 30);
            }
            if (max < dataByDataPoints[i]){
                max = dataByDataPoints[i];
            }
        }
        //calculating the Y-axis scaling factor
        scaleFactor = (screenHeight - distanceFromTop - resolutionHeightOffset) / max;
    }

    /**
     * This is a method that returns the index referring to the end (in terms of date) of the given year
     * @param localYear The desired year
     * @return index
     */
    public int getIndexStart(int localYear) {
        for (int i = 0; i< data.size(); i++){
            if(data.get(i).getDate().getYear() == localYear){
                return i;
            }
        }
        return 0;
    }

    /**
     * This is a method that returns the index referring to the start (in terms of date) of the given year
     * @param localYear The desired year
     * @return index
     */
    public int getIndexEnd(int localYear) {
        for(int i = data.size() - 1; i >= 0; i--){
            if(data.get(i).getDate().getYear() == localYear){
                return i;
            }
        }
        return 0;
    }

    /**
     * Returns number of weeks in a given year (rounding up)
     * @return Weeks
     */
    public int getWeeksInYear(){
        int count = 0;
        int total = 0;
        for(int i = getIndexStart(year); i < getIndexStart(year - 1); i++) {
            count++;
            if (i == getIndexStart(year - 1) && count != 7){
                total++;
            }
            if(count==7){
                total++;
                count = 0;
            }
        }
        return total;
    }

    public int getDataPoints() {
        return dataPoints;
    }

    /**
     * Calculates the total number of cases in a given month of the year from the data set
     * @param month (int between 0 and 11 inclusive)
     * @return total number of cases in given month
     */
    public int getMonthlyData(int month){
        int total = 0;
        for (CovidDataDaily datum : data) {
            if (datum.getDate().getMonth() == month && datum.getDate().getYear() == year) {
                total += datum.getNum();
            }
        }
        return total;
    }

    /**
     * Gets the data for the week sent through the param
     * @param week the week we are looking at
     * @return total data for week
     */
    public int getWeeklyData(int week) {
        int yearEndIndex = getIndexStart(year);
        int yearStartIndex = getIndexEnd(year);
        int total = 0;
        for (int i = yearStartIndex - (week * 7); i >= yearEndIndex + ((dataPoints - week - 1) * 7); i--) {
            total += data.get(i).getNum();
        }
        return total / 7;
    }

    /**
     * Converts the raw data to an array containing only the daily data from the year we are documenting
     * @return Sanitised array of data
     */
    public ArrayList<CovidDataDaily> getDailyData() {
        int yearEndIndex = getIndexStart(year);
        int yearStartIndex = getIndexEnd(year);
        ArrayList<CovidDataDaily> out = new ArrayList<>();
        for (int i = yearStartIndex; i >= yearEndIndex; i--) {
            out.add(data.get(i));
        }
        return out;
    }

    /**
     * Creates the labels for the x and y-axis based on the number of data points
     */
    public void createAxisLabels(){
        if (isDeaths){
            title = new JLabel("Deaths per day in " + (year + 1900));
            yLabel = new JLabel("No. of deaths");
        } else {
            title = new JLabel("Cases per day in " + (year + 1900));
            yLabel = new JLabel("No. of cases");
        }
        title.setBounds(((dataPoints / 2) * spacing), distanceFromTop - 80, 600, 30);
        title.setFont(new Font("Serif", Font.BOLD, 20));
        xLabel = new JLabel("Days in " + (year + 1900));
        xLabel.setBounds(((dataPoints / 2) * spacing), screenHeight + 20, 400, 30);
        predictionLabel = new JLabel("Pink = predicted data");
        predictionLabel.setBounds(((dataPoints / 2) * spacing), screenHeight + 40, 400, 30);
        predictionLabel.setForeground(Color.MAGENTA);
        yLabel.setBounds(10, screenHeight / 2 , 120, 30);
        inc0 = new JLabel("0");
        inc0.setBounds(10, screenHeight - 15, 100, 30);
        inc1 = new JLabel(String.valueOf((int) (max / 4)));
        inc1.setBounds(10, (int) ((screenHeight - ((max * scaleFactor) / 4)))  - 15, 100, 30);
        inc2 = new JLabel(String.valueOf((int) ((max / 4) * 3)));
        inc2.setBounds(10, (int) ((screenHeight - ((max * scaleFactor) * 0.75)))  - 15, 100, 30);
        inc3 = new JLabel(String.valueOf((int) max));
        inc3.setBounds(10, (int) (screenHeight - (max * scaleFactor)) - 15, 100, 30);
        backButton.setBounds(screenWidth - 100 - resolutionWidthOffset, screenHeight - 50, 100, 30);
        backButton.addActionListener(new GuiMain.BackButtonHandler());
        backButton.setBackground(new Color(66, 245, 200));
    }

    /**
     * Draws graph of inputted data and its x and y-axis
     * @param g Graphics object
     */
    @Override
    protected void paintComponent(Graphics g) {
        int numPieces = 8;
        int[] piece1 = new int[(dataPoints / numPieces)];
        int[] piece2 = new int[(dataPoints / numPieces)];
        int[] piece3 = new int[(dataPoints / numPieces)];
        int[] piece4 = new int[(dataPoints / numPieces)];
        int[] piece5 = new int[(dataPoints / numPieces)];
        int[] piece6 = new int[(dataPoints / numPieces)];
        int count = 0;
        int iterator = 0;
        boolean piece2Visited = false;
        boolean piece3Visited = false;
        boolean piece4Visited = false;
        boolean piece5Visited = false;
        boolean piece6Visited = false;
        for (int i = 0; i < dataPoints - 1; i++){
            count++;
            if (count <= (dataPoints / numPieces)){
                piece1[iterator] = dataByDataPoints[i];
            } else if (count <= (dataPoints / numPieces) * 2 && count > dataPoints / numPieces){
                if (!piece2Visited){
                    iterator = 0;
                    piece2Visited = true;
                }
                piece2[iterator] = dataByDataPoints[i];
            } else if (count <= (dataPoints / numPieces) * 3 && count > (dataPoints / numPieces) * 2){
                if (!piece3Visited){
                    iterator = 0;
                    piece3Visited = true;
                }
                piece3[iterator] = dataByDataPoints[i];
            } else if (count <= (dataPoints / numPieces) * 4 && count > (dataPoints / numPieces) * 3){
                if (!piece4Visited){
                    iterator = 0;
                    piece4Visited = true;
                }
                piece4[iterator] = dataByDataPoints[i];
            } else if (count <= (dataPoints / numPieces) * 5 && count > (dataPoints / numPieces) * 4){
                if (!piece5Visited){
                    iterator = 0;
                    piece5Visited = true;
                }
                piece5[iterator] = dataByDataPoints[i];
            } else if (count <= (dataPoints / numPieces) * 6 && count > (dataPoints / numPieces) * 5){
                if (!piece6Visited){
                    iterator = 0;
                    piece6Visited = true;
                }
                piece6[iterator] = dataByDataPoints[i];
            }
            iterator++;
        }
        int[] lineEnds = new int[numPieces];
        lineEnds[0] = piece1[0];
        lineEnds[1] = Math.max((int) LinearRegression.predictY(piece1, piece1.length), 0);
        lineEnds[2] = Math.max((int) LinearRegression.predictY(piece2, piece2.length * 2), 0);
        lineEnds[3] = Math.max((int) LinearRegression.predictY(piece3, piece3.length * 3), 0);
        lineEnds[4] = Math.max((int) LinearRegression.predictY(piece4, piece4.length * 4), 0);
        lineEnds[5] = Math.max((int) LinearRegression.predictY(piece5, piece5.length * 5), 0);
        lineEnds[6] = Math.max((int) LinearRegression.predictY(piece6, piece6.length * 6), 0);
        lineEnds[7] = dataByDataPoints[dataPoints - 1];
        if (numberOfWeeksToPredict > 0){
            g.drawLine(spacing, screenHeight, ((dataPoints + numberOfWeeksToPredict) * spacing), screenHeight); //x-axis line
            g.drawLine(spacing, screenHeight, spacing, distanceFromTop); //y-axis line
            g.drawLine(spacing, distanceFromTop, ((dataPoints + numberOfWeeksToPredict) * spacing), distanceFromTop); //top of graph line
            for (int i = 0; i < numMonths; i++){
                g.drawLine((i + 1) * 30 * spacing, screenHeight, (i + 1) * 30 * spacing, distanceFromTop);
            }
        } else {
            g.drawLine(spacing, screenHeight, (dataPoints * spacing), screenHeight); //x-axis line
            g.drawLine(spacing, screenHeight, spacing, distanceFromTop); //y-axis line
            g.drawLine(spacing, distanceFromTop, (dataPoints * spacing), distanceFromTop); //top of graph line
            g.drawLine((spacing * dataPoints), screenHeight, (dataPoints * spacing), distanceFromTop); //right of graph line
        }
        for (int i = 0; i < numMonths - 1; i++){
            g.drawLine((i + 1) * 30 * spacing, screenHeight, (i + 1) * 30 * spacing, distanceFromTop);
        }

        //plotting the data points onto the graph
        for (int i = 0; i < dataPoints - 1; i++){
            g.setColor(Color.RED);
            g.drawLine(((i + 1) * spacing), (int) (screenHeight - (dataByDataPoints[i] * scaleFactor)), ((i + 2) * spacing), (int) (screenHeight - (dataByDataPoints[i + 1] * scaleFactor)));
        }

        //piecewise (drawn in black)
        int newSpacing = ((screenWidth - resolutionWidthOffset) / numPieces);
        double newMax = 0;
        for (int lineEnd : lineEnds) {
            if (lineEnd > newMax) {
                newMax = lineEnd;
            }
        }
        double newScaleFactor = (screenHeight - distanceFromTop - resolutionHeightOffset) / newMax;
        for (int i = 0; i < lineEnds.length - 1; i++){
            if (i == lineEnds.length - 2){
                g.setColor(Color.BLACK);
                g.drawLine(i * newSpacing + spacing, screenHeight - (int) (lineEnds[i] * newScaleFactor), (((dataPoints - 1) + 2) * spacing) - spacing, (int) (screenHeight - (dataByDataPoints[dataByDataPoints.length - 1] * scaleFactor)));
            } else {
                g.setColor(Color.BLACK);
                g.drawLine(i * newSpacing + spacing, screenHeight - (int) (lineEnds[i] * newScaleFactor), (i + 1) * newSpacing + spacing, screenHeight - (int) (lineEnds[(i + 1)] * newScaleFactor));
            }
        }
        //fitted prediction
        if (numberOfWeeksToPredict > 0) {
            int fittedPrediction = Math.max((int) LinearRegression.predictY(lineEnds, lineEnds.length), 0);
            g.setColor(Color.magenta);
            g.drawLine(((dataPoints + 1) * spacing) - spacing, (int) (screenHeight - (dataByDataPoints[dataByDataPoints.length - 1] * scaleFactor)), ((dataPoints + (numberOfWeeksToPredict * 7)) * spacing), screenHeight - (int) (fittedPrediction * scaleFactor));
        }
    }

    public void createImage(){
        BufferedImage bi = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g2 = bi.createGraphics();
        this.paint(g2);  //this == JComponent
        g2.dispose();
        try{ImageIO.write(bi,"png",new File("resources/images/test.png"));}catch (Exception ignored) {}
    }
}