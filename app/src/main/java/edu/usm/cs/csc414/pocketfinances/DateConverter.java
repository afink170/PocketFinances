package edu.usm.cs.csc414.pocketfinances;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateConverter {
    @TypeConverter
    public static Calendar toDate(Long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        return timestamp == null ? null : calendar;
    }

    @TypeConverter
    public static Long toTimestamp(Calendar date) {
        return date == null ? null : date.getTimeInMillis();
    }
}
