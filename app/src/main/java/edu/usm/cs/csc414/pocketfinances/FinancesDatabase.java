package edu.usm.cs.csc414.pocketfinances;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.commonsware.cwac.saferoom.SQLCipherUtils;
import com.commonsware.cwac.saferoom.SafeHelperFactory;


@Database(entities = { BankAccount.class, Expense.class },
        version = 1,
        exportSchema = true)
public abstract class FinancesDatabase extends RoomDatabase {

    public abstract BankAccountDao getBankAccountDao();
    public abstract ExpenseDao getExpenseDao();

    private static FinancesDatabase INSTANCE;

    public static FinancesDatabase getDatabase(Context context) {
        CustomSharedPreferences sharedPrefs = new CustomSharedPreferences(context);
        SafeHelperFactory factory = new SafeHelperFactory(sharedPrefs.getEncryptKey());

        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), FinancesDatabase.class, "finances_db")
                            .openHelperFactory(factory)
                            .build();
        }
        return INSTANCE;
    }
}
