package data;

import org.junit.jupiter.api.Assertions;
import regression.LinearRegression;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class LinearRegressionTest {

    @org.junit.jupiter.api.Test
    void LinearRegressionAccuracyTest() {
        int[] data = new int[]{100, 200, 300, 400 ,500 ,600};
        Assertions.assertEquals(700, LinearRegression.predictY(data, 6));
    }
}