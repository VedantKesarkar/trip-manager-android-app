package com.project.tripmanager.tripMain.Expenses.domain.helpers;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
    private static final String TAG = "DateHelper";
    private int totalDiffInDays=0;
    public DateHelper() {

    }

    public void setPrevDate(String prevDate)
    {
        parseDate(prevDate);
    }

    private  void parseDate(String date) {
        int y , m , d;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
            LocalDate localDate = LocalDate.parse(date,formatter);
            d = localDate.getDayOfMonth();
            m = localDate.getMonthValue();
            y = localDate.getYear();
            compareDates(y,m,d);
        }
        else {
            Log.d(TAG, "Version diff");
        }
    }

    private void compareDates(int y, int m, int d) {

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            LocalDate currentDate = LocalDate.now();
            LocalDate previousDate = LocalDate.of(y,m,d);
            Period period = Period.between(previousDate,currentDate);
            int monthDiff  = period.getMonths();
            int dayDiff = period.getDays();
            int yearDiff = period.getYears();
            totalDiffInDays = dayDiff + 30*monthDiff + 365*yearDiff;
        }
    }

    public int getTotalDiffInDays() {
        return totalDiffInDays;
    }

    public String getCurrentDate()
    {
       return  new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date());
    }

}
