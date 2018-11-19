package edu.usm.cs.csc414.pocketfinances;

import android.arch.persistence.room.TypeConverter;

public class RecurrenceRateConverter {

    @TypeConverter
    public static RecurrenceRate toRecurrenceRate(Integer rate) {
        return rate == null ? null : RecurrenceRate.getRateFromValue(rate);
    }

    @TypeConverter
    public static Integer toInteger(RecurrenceRate rate) {
        return rate == null ? null : rate.getValue();
    }
}
