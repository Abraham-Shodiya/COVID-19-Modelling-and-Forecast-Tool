package regression;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Provides methods for predicting the future of given data based on naive gradient based calculations
 * These methods do not provide satisfactory results for the end version of the project
 */
public class LinearRegression {

    /**
     * Returns a double containing the prediction for the next week
     * Provides reasonable guess, but cannot predict multiple weeks ahead and seemingly never predicts a negative gradient
     * Based on formula provided by https://www.easycalculation.com/statistics/regression.php
     * @param dataByDataPoints array of weekly data
     * @return prediction
     */
    public static double regressionAttempt1(int[] dataByDataPoints, double nextX){
        double[] x = new double[dataByDataPoints.length];
        double[] y = new double[dataByDataPoints.length];
        double sumXY = 0;
        double N = dataByDataPoints.length;
        double xMean = 0;
        double yMean = 0;
        double sumXSquared = 0;
        double sumX = 0;
        //calculating terms
        {
            for (int i = 0; i < N; i++) {
                x[i] = i;
                y[i] = dataByDataPoints[i];
                sumXY += x[i] * y[i];
                sumXSquared += x[i] * x[i];
                sumX += x[i];
                xMean += x[i];
                yMean += y[i];
            }
            xMean /= N;
            yMean /= N;
        }
        double gradient = (N * sumXY - (xMean * yMean)) / (N * sumXSquared - ((int) (sumX) ^ 2));
        double intercept = (yMean - (gradient * xMean));
        return intercept + (gradient * nextX); //here N is our next value of cases/deaths
    }

    /**
     * Returns a double containing the prediction for the next week
     * A much more interesting prediction, but now we can see the limit of the naive linear regression implementation as the data goes flying off in the direction of the previous prediction
     * This approach is faster as we only use 1 loop but the data is very prone to flying off
     * @param dataByDataPoints array
     * @return Prediction
     */
    public static double regressionAttempt2(int[] dataByDataPoints, double nextX) {
        double[] x = new double[dataByDataPoints.length];
        double[] y = new double[dataByDataPoints.length];
        for (int i = 0; i < dataByDataPoints.length; i++){
            x[i] = i;
            y[i] = dataByDataPoints[i];
        }
        double xMean = mean(x);
        double yMean = mean(y);
        double result = 0;
        for (int i = 0; i < x.length; i++) {
            result += (x[i] - xMean) * (y[i] - yMean); //shortcut
        }
        result = result / nextX;
        if (result < 0){
            result = 0;
        }
        return result / 10;
    }

    /**
     * Calculates mean of a given array of doubles
     * @param data double array
     * @return mean
     */
    private static double mean(double[] data) {
        double sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }
        return sum / data.length;
    }

    /**
     * Converts a java.util.Date object to a calendar object
     * @param date java.util.Date object
     * @return Calendar object
     */
    private static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    /**
     * Better implementation using built in list functionality and lambda functions
     * @param dataByDataPoints Array of data for X-axis
     * @param nextX X value to solve for Y
     * @return Solved Y value
     */
    public static double predictY(int[] dataByDataPoints, double nextX) {
        List<Integer> y = Arrays.asList(Arrays.stream(dataByDataPoints).boxed().toArray(Integer[]::new));
        Integer[] indices = new Integer[dataByDataPoints.length];
        for (int i = 0; i < indices.length; i++){
            indices[i] = i;
        }
        List<Integer> x = Arrays.asList(indices);
        int N = x.size();
        List<Double> xSquared = x.stream().map(position -> Math.pow(position, 2)).collect(Collectors.toList());
        List<Integer> xY = IntStream.range(0, N).map(i -> x.get(i) * y.get(i)).boxed().collect(Collectors.toList());
        Integer sumX = x.stream().reduce(Integer::sum).get();
        Integer sumY = y.stream().reduce(Integer::sum).get();
        Double sumXSquared = xSquared.stream().reduce(Double::sum).get();
        Integer sumXY = xY.stream().reduce(Integer::sum).get();
        double gradient = (N * sumXY - sumY * sumX) / (N * sumXSquared - Math.pow(sumX, 2));
        double intercept = (sumY - gradient * sumX) / N;
        return (gradient * nextX) + intercept;
    }
}