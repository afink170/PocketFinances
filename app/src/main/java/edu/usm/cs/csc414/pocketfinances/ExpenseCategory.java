package edu.usm.cs.csc414.pocketfinances;

import java.util.HashMap;
import java.util.Map;

public enum ExpenseCategory {

    SHOPPING(0, "Shopping"),
    DINING(1, "Dining"),
    AUTO(2, "Auto"),
    GROCERIES(3, "Groceries"),
    SAVINGS(4, "Savings"),
    TRAVEL(5, "Travel"),
    UTILITIES(6, "Utilities"),
    HOUSEHOLD(7, "Household"),
    ENTERTAINMENT(8, "Entertainment"),
    SALARY(9, "Salary"),
    DEPOSIT(10, "Deposit"),
    DEBT_PAYMENT(11, "Debt Payment"),
    GIFTS(12, "Gifts"),
    HEALTH_FITNESS(13, "Health & Fitness"),
    RENT(14, "Rent"),
    TAX(15, "Tax"),
    OTHER(16, "Other");

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

    public static ExpenseCategory getCategoryFromText(String text) {
        for(ExpenseCategory cat : values()){
            if( cat.getText() == text){
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
