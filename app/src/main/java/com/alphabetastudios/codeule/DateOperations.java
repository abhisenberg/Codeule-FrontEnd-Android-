package com.alphabetastudios.codeule;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.alphabetastudios.codeule.Settings.UserPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by abheisenberg on 29/7/17.
 */

public class DateOperations {
    private static final String TAG = "dateformatter";

    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy MMM dd hh:mm aa");
    private String contest_date_str;

    private Date now;

    public void setContest_date(String contest_date_str){
        this.contest_date_str = contest_date_str;
        now = new Date();
    }

    public boolean isDateTimePassed(){
        if(contest_date_str.length() == 0){
            return false;
        }

        Date contest_date = stringToDate(contest_date_str);

//        Log.d("datecom", "comparing "+contest_date.toString()+" and "+now.toString());
//        if(contest_date.compareTo(now) < 0){
//            Log.d("datecom", "result "+contest_date.toString()+" has passed");
//            return true;
//        } else {
//            Log.d("datecom", "result "+contest_date.toString()+" is later");
//            return false;
//        }

        return contest_date.compareTo(now) < 0;
    }

    private static Date stringToDate(String contest_date_str){
        Date date = null;

        try {
            date = formatter.parse(contest_date_str);
        } catch (ParseException e) {
            Log.d(TAG, "stringToDate: ERROR CANNOT PARSE -> "+contest_date_str);
            e.printStackTrace();

            Log.d(TAG, "stringToDate: Making another attempt to parse");

            try {
                int hour = Integer.valueOf(
                        contest_date_str.substring(
                            contest_date_str.length()-8,
                            contest_date_str.length()-6
                ));

                int AMorPM = contest_date_str.charAt(contest_date_str.length()-2);
                if(AMorPM == 'P'){
                    if(hour != 12){
                        hour += 12;
                    }
                } else if (AMorPM == 'A'){
                    if (hour == 12){
                        hour = 0;
                    }
                }

                String hourString = String.valueOf(hour);

                String properHourString = hourString.length() == 1? "0"+hourString :
                        hourString;

                String newDateString = contest_date_str.substring(0, 12) + properHourString +
                        contest_date_str.substring(14, contest_date_str.length()-3);

                SimpleDateFormat otherFormat = new SimpleDateFormat("yyyy MMM dd hh:mm");

                date = otherFormat.parse(newDateString);

            } catch (IndexOutOfBoundsException | ParseException e1){
                e1.printStackTrace();
            }
        }
        return date;
    }

    public void markInCalendarHR(Context context, String contest_name, String start_date){
        long msecsToStart = getRemainingMSecs(start_date);
        if(msecsToStart == -1){
            Toast.makeText(context, "Indefinite starting time, Cannot add alarm to calendar!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(UserPreferences.getAlarmEarly(context)) {
            Toast.makeText(context, "Setting alarm 15 minutes earlier than starting time of contest.",
                    Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Marking early ");
            msecsToStart -= 15*60*1000;
        }

        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis()+msecsToStart);
        intent.putExtra("title","HackerRank: "+contest_name);
        intent.putExtra("hasAlarm", true);
        Toast.makeText(context, "Please check ending time from the website" +
                "", Toast.LENGTH_LONG).show();
        context.startActivity(intent);
    }

    public void markInCalendarHECC(Context context, String contest_name, String start_date, String end_date){
        long msecsToStart = getRemainingMSecs(start_date);
        long msecsToEnd = getRemainingMSecs(end_date);

        Log.d(TAG, "msecs to start "+msecsToStart);
        Log.d(TAG, "msecs to end "+msecsToEnd);

        if(UserPreferences.getAlarmEarly(context)) {
            Toast.makeText(context, "Setting alarm 15 minutes earlier than starting time of contest.",
                    Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Marking early ");
            msecsToStart -= 15*60*1000;
        }

        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent((Intent.ACTION_EDIT));
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("title","CodeChef: "+contest_name);

        if(msecsToStart <0 && msecsToEnd <0 ){
            Toast.makeText(context, "Please check the start and end time from the website.", Toast.LENGTH_SHORT).show();
        } else {
            if(msecsToStart <0 ){
                Toast.makeText(context, "Please check the start time from the website.", Toast.LENGTH_SHORT).show();
            } else {
                intent.putExtra("beginTime", cal.getTimeInMillis()+msecsToStart);
            }

            if(msecsToEnd <0 ){
                Toast.makeText(context, "Please check the closing time from the website.", Toast.LENGTH_SHORT).show();
            } else {
                intent.putExtra("endTime", cal.getTimeInMillis()+msecsToEnd);
            }
        }

        intent.putExtra("hasAlarm", true);
        context.startActivity(intent);
    }

    public static long getRemainingMSecs(String time){
        Date contest_date = stringToDate(time);
        if(contest_date == null){
            return -1;
        }
        return contest_date.getTime() - System.currentTimeMillis();

    }
}
