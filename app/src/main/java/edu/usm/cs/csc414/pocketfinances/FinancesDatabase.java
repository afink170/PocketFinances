package edu.usm.cs.csc414.pocketfinances;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.util.Log;

import com.commonsware.cwac.saferoom.SQLCipherUtils;
import com.commonsware.cwac.saferoom.SafeHelperFactory;

import java.io.File;

import timber.log.Timber;


@Database(entities = { BankAccount.class, Expense.class },
        version = 3,
        exportSchema = true)
public abstract class FinancesDatabase extends RoomDatabase {

    public abstract BankAccountDao getBankAccountDao();
    public abstract ExpenseDao getExpenseDao();

    private static FinancesDatabase INSTANCE;

    public static FinancesDatabase getDatabase(Context context) {
        CustomSharedPreferences sharedPrefs = new CustomSharedPreferences(context);

        try {
            SafeHelperFactory factory = new SafeHelperFactory(sharedPrefs.getEncryptKey());

            if (INSTANCE == null) {
                INSTANCE =
                        Room.databaseBuilder(context.getApplicationContext(), FinancesDatabase.class, "finances_db")
                                .openHelperFactory(factory)
                                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                                .build();
            }
        } catch(Exception e) {
            Timber.e(e, "Failed to get database instance!");
        }

        return INSTANCE;
    }


    public static void deleteDatabase(Context context) {
        File db = context.getDatabasePath("finances_db");
        if (db.delete())
            System.out.println("Database deleted");
        else
            System.out.println("Failed to delete database");
    }



    // Define migration operations for migrating database from one version to another

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the tables, there's nothing else to do here.
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Expense"
                    + " ADD COLUMN is_first_occurrence INTEGER DEFAULT 1");

            database.execSQL("ALTER TABLE Expense"
                    + " ADD COLUMN next_occurrence INTEGER DEFAULT 0");

            database.execSQL("UPDATE Expense SET next_occurrence = date WHERE next_occurrence = 0");
        }
    };

}
