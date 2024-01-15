package data;

import org.junit.jupiter.api.Assertions;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class CsvReaderTest {

    @org.junit.jupiter.api.Test
    void getCovidCasesFromCsvNoLocation() {
        Assertions.assertThrows(FileNotFoundException.class, () -> {
            List<CovidDataDaily> covidData = CsvReader.getCovidCasesFromCsv("");

        });
    }

    @org.junit.jupiter.api.Test
    void getCovidCasesFromCsvWorks() throws FileNotFoundException, ParseException {
        List<CovidDataDaily> covidData = CsvReader.getCovidCasesFromCsv("./test/resources/cleaned_cases.csv");
        CovidDataDaily covidDataPiece = covidData.get(0);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = formatter.parse("20/10/2021");
        Assertions.assertEquals(date.toString(), covidDataPiece.getDate().toString());
        Assertions.assertEquals(49139, covidDataPiece.getNum());
        Assertions.assertEquals(TypeOfData.CASES, covidDataPiece.getType());


    }


    @org.junit.jupiter.api.Test
    void getDeathsCasesFromCsvNoLocation() {
        Assertions.assertThrows(FileNotFoundException.class, () -> {
            List<CovidDataDaily> covidData = CsvReader.getCovidDeathsFromCsv("");

        });
    }

    @org.junit.jupiter.api.Test
    void getDeathsCasesFromCsvWorks() throws FileNotFoundException, ParseException {
        List<CovidDataDaily> covidData = CsvReader.getCovidDeathsFromCsv("./test/resources/cleaned_deaths.csv");
        CovidDataDaily covidDataPiece = covidData.get(0);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = formatter.parse("08/10/2021");
        Assertions.assertEquals(date.toString(), covidDataPiece.getDate().toString());
        Assertions.assertEquals(52, covidDataPiece.getNum());
        Assertions.assertEquals(TypeOfData.DEATHS, covidDataPiece.getType());


    }
}