package data;

import java.util.Date;

public class CovidDataDaily {

    private Date date;
    private int num;
    private TypeOfData type;

    /**
     * stores data from csv files
     * @param date Java Date to store date of case/death
     * @param num Number of cases/deaths on a given day
     * @param type Whether the data is a death or a case
     */
    public CovidDataDaily(Date date, int num, TypeOfData type) {
        this.date = date;
        this.num = num;
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public TypeOfData getType() {
        return type;
    }

    public void setType(TypeOfData type) {
        this.type = type;
    }
}
