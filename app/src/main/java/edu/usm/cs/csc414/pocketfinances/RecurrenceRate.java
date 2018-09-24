package edu.usm.cs.csc414.pocketfinances;

import java.util.HashMap;
import java.util.Map;

public enum RecurrenceRate {

    ONCE(0, "Once"),
    DAILY(1, "Daily"),
    WEEKLY(2, "Weekly"),
    BIWEEKLY(3, "Biweekly"),
    MONTHLY(4, "Monthly"),
    THREE_MONTHS(5, "Every 3 Months"),
    SIX_MONTHS(6, "Every 6 Months"),
    YEARLY(7, "Yearly");

    private final Integer value;
    private final String text;

    /**
     * A mapping between the integer code and its corresponding text to facilitate lookup by code.
     */
    private static Map<Integer, RecurrenceRate> valueToTextMapping;

    RecurrenceRate(Integer value, String text){
        this.value = value;
        this.text = text;
    }


    private static void initMapping(){
        valueToTextMapping = new HashMap<>();
        for(RecurrenceRate s : values()){
            valueToTextMapping.put(s.value, s);
        }
    }


    public static RecurrenceRate getRateFromValue(int val) {
        for(RecurrenceRate rate : values()){
            if( rate.getValue() == val){
                return rate;
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
