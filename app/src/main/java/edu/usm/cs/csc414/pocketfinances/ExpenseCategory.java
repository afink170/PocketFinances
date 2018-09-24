package edu.usm.cs.csc414.pocketfinances;

import java.util.HashMap;
import java.util.Map;

public enum ExpenseCategory {

    SHOPPING(1, "Shopping"),
    DINING(2, "Dining"),
    AUTO(3, "Auto"),
    GROCERIES(4, "Groceries"),
    SAVINGS(5, "Savings"),
    TRAVEL(6, "Travel"),
    UTILITIES(7, "Utilities"),
    HOUSEHOLD(8, "Household"),
    ENTERTAINMENT(9, "Entertainment"),
    SALARY(10, "Salary"),
    DEPOSIT(11, "Deposit"),
    DEBT_PAYMENT(12, "Debt Payment"),
    GIFTS(13, "Gifts"),
    HEALTH_FITNESS(14, "Health & Fitness"),
    RENT(15, "Rent"),
    TAX(16, "Tax"),
    OTHER(17, "Other");

    private final Integer value;
    private final String text;

    /**
     * A mapping between the integer code and its corresponding text to facilitate lookup by code.
     */
    private static Map<Integer, ExpenseCategory> valueToTextMapping;

    ExpenseCategory(Integer value, String text){
        this.value = value;
        this.text = text;
    }


    private static void initMapping(){
        valueToTextMapping = new HashMap<>();
        for(ExpenseCategory s : values()){
            valueToTextMapping.put(s.value, s);
        }
    }

    public static ExpenseCategory getCategoryFromValue(int val) {
        for(ExpenseCategory cat : values()){
            if( cat.getValue() == val){
                return cat;
            }
        }
        return null;
    }

    public Integer getValue(){
        return value;
    }

    public String getText(){
        return text;
    }

    @Override
    public String toString(){
        final StringBuilder sb = new StringBuilder();
        sb.append("Status");
        sb.append("{value=").append(value);
        sb.append(", text='").append(text).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
