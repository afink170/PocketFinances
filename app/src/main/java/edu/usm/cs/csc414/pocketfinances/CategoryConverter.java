package edu.usm.cs.csc414.pocketfinances;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class CategoryConverter {

    @TypeConverter
    public static ExpenseCategory toCategory(Integer categoryValue) {
        return categoryValue == null ? null : ExpenseCategory.getCategoryFromValue(categoryValue);
    }

    @TypeConverter
    public static Integer toInteger(ExpenseCategory category) {
        return category == null ? null : category.getValue();
    }
}
