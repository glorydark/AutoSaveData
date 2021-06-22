package glorydark.autosave.utils;

import java.util.Calendar;

public class GetCalender {
    public static String getDateString(){
        Calendar date=Calendar.getInstance();
        int year=date.get(Calendar.YEAR);
        int month=date.get(Calendar.MONTH);
        int day=date.get(Calendar.DAY_OF_MONTH);
        int hour=date.get(Calendar.HOUR_OF_DAY);
        int minute=date.get(Calendar.MINUTE);
        int second=date.get(Calendar.SECOND);
        String AddDate=year+"-"+month+"-"+day+"-"+hour+"-"+minute+"-"+second;
        return AddDate;
    }
}
