package edu.usm.cs.csc414.pocketfinances;

import java.util.Calendar;
import java.util.Locale;

public abstract class ExpenseTypeConverters {

    public static String dateToString(Calendar date) {
        return String.format(Locale.US, "%02d/%02d/%4d",
                date.get(Calendar.MONTH)+1,
                date.get(Calendar.DAY_OF_MONTH),
                date.get(Calendar.YEAR));
    }

    public static String amountToString(double amount) {
        return String.format(Locale.US, "$%.2f", amount);
    }

}
