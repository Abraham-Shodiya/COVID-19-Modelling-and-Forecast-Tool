package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CsvReader {

    /**
     * Gets the Covid cases from the given csv filepath and puts them into an ArrayList of CovidDataDaily objects
     * @param csvLocation (filepath to the desired csv file)
     * @return List of type CovidDataDaily
     * @throws FileNotFoundException
     * @throws ParseException
     */
    public static List<CovidDataDaily> getCovidCasesFromCsv(String csvLocation) throws FileNotFoundException, ParseException {
        List<CovidDataDaily> cases = new ArrayList<>();
        String content = new Scanner(new File(csvLocation)).useDelimiter("\\Z").next();
        String[] lines = content.split("\n");
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        for (String line : lines) {
            String[] dataPoints = line.split(",");
            CovidDataDaily dailyCase = new CovidDataDaily(formatter.parse(dataPoints[0]), Integer.parseInt(dataPoints[1].replaceAll("\\r", "")), TypeOfData.CASES);
            cases.add(dailyCase);
        }
        return cases;
    }

    /**
     * Gets the Covid deaths from the given csv filepath and puts them into an ArrayList of CovidDataDaily objects
     * @param csvLocation (filepath to the desired csv file)
     * @return List of type CovidDataDaily
     * @throws FileNotFoundException
     * @throws ParseException
     */
    public static List<CovidDataDaily> getCovidDeathsFromCsv(String csvLocation) throws FileNotFoundException, ParseException {
        List<CovidDataDaily> deaths = new ArrayList<>();
        String content = new Scanner(new File(csvLocation)).useDelimiter("\\Z").next();
        String[] lines = content.split("\n");
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        for (String line : lines) {
            String[] dataPoints = line.split(",");
            CovidDataDaily dailyDeath = new CovidDataDaily(formatter.parse(dataPoints[0]), Integer.parseInt(dataPoints[1].replaceAll("\\r", "")), TypeOfData.DEATHS);
            deaths.add(dailyDeath);
        }
        return deaths;
    }
}
