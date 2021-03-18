/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javafx.animation.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author Darker
 */
public class TimeHelper {
    
    //Displays main office and current users time on the login menu
    public static void loginClock(Text timeLocal, Text mainOfficeTime) {
        
        Timeline clockLocal = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            timeLocal.setText("Local Time: " + LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clockLocal.setCycleCount(Animation.INDEFINITE);
        clockLocal.play();
    
        Timeline clockMainOffice = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            ZonedDateTime officeTime = ZonedDateTime.now(ZoneId.of("America/New_York"));
            mainOfficeTime.setText("Office Time: " + officeTime.format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clockMainOffice.setCycleCount(Animation.INDEFINITE);
        clockMainOffice.play();
    }

    public static LocalDateTime convertTime (String date, String type) {

        LocalDateTime result = null;


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime dateToDateTime = LocalDateTime.parse(date, formatter);


        Timestamp ts = Timestamp.valueOf(dateToDateTime);
        LocalDateTime ldt = ts.toLocalDateTime();
        ZonedDateTime zdt = ldt.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime utczdt = zdt.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime ldtIn = utczdt.toLocalDateTime(); // date to store in db
        if (type.equals("local")) { ldtIn = dateToDateTime; }
        ZonedDateTime zdtOut = ldtIn.atZone(ZoneId.of("UTC"));
        ZonedDateTime zdtOutToLocalTZ = zdtOut.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
        LocalDateTime ldtOutFinal = zdtOutToLocalTZ.toLocalDateTime();

        if (type.equals("utc")) {
            result = ldtIn;
        }

        if (type.equals("local")) {
            result = ldtOutFinal;
        }

        return result;

    }

    public static String nthWeekdayOfMonth(int dayOfWeek, int month, int year, int week, TimeZone timeZone) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(timeZone);
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, week);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        return new SimpleDateFormat("MM-dd").format(calendar.getTime());
    }

    public static String getLastWeekday(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // set calendar to the first day of the next month
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);

        // calculate the number of days to subtract to get the last desired day of the month
        int dayValue = (13 - day) % 7;
        calendar.add(Calendar.DAY_OF_MONTH, -(((dayValue + calendar.get(Calendar.DAY_OF_WEEK)) % 7) + 1));

        return new SimpleDateFormat("MM-dd").format(calendar.getTime());
    }
}
