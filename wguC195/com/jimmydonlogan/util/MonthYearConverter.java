package com.jimmydonlogan.util;


import com.jimmydonlogan.model.MonthYear;
import javafx.util.StringConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Month year converter.
 */
public class MonthYearConverter extends StringConverter<MonthYear> {

    /**
     * The Month year hash map.
     */
    private final Map<String, MonthYear> monthYearHashMap = new HashMap<>();

    @Override
    public String toString(MonthYear monthYear) {
        try {
            String name="";
            if(monthYear==null)
                return name;
            monthYearHashMap.put(monthYear.getMonthYear(), monthYear);
            return monthYear.getMonthYear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public MonthYear fromString(String name) {
        return monthYearHashMap.get(name);
    }

}
