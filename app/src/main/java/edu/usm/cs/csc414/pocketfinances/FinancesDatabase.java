package edu.usm.cs.csc414.pocketfinances;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.commonsware.cwac.saferoom.SQLCipherUtils;
import com.commonsware.cwac.saferoom.SafeHelperFactory;


@Database(entities = { BankAccount.class, Expense.class },
        version = 2,
        exportSchema = true)
public abstract class FinancesDatabase extends RoomDatabase {

    private static final String TAG = "FinancesDatabase";

    public abstract BankAccountDao getBankAccountDao();
    public abstract ExpenseDao getExpenseDao();
    //public abstract RecurringExpenseDao getRecurringExpenseDao();

    private static FinancesDatabase INSTANCE;

    public static FinancesDatabase getDatabase(Context context) {
        CustomSharedPreferences sharedPrefs = new CustomSharedPreferences(context);

        try {
            SafeHelperFactory factory = new SafeHelperFactory(sharedPrefs.getEncryptKey());

            if (INSTANCE == null) {
                INSTANCE =
                        Room.databaseBuilder(context.getApplicationContext(), FinancesDatabase.class, "finances_db")
                                .openHelperFactory(factory)
                                .build();
            }
        } catch(Exception e) {
            Log.e(TAG, "Failed to get database instance!", e);
        }

        return INSTANCE;
    }
}
