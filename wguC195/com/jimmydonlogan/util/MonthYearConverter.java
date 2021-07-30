package com.jimmydonlogan.util;


import com.jimmydonlogan.model.MonthYear;
import javafx.util.StringConverter;

import java.util.HashMap;
import java.util.Map;

public class MonthYearConverter extends StringConverter<MonthYear> {

    /** Cache of Months years*/
    private final Map<String, MonthYear> monthYearHashMap = new HashMap<>();

    @Override
    public String toString(MonthYear monthYear) {
        String name="";
        if(monthYear==null)
            return name;
        monthYearHashMap.put(monthYear.getMonthYear(), monthYear);
        return monthYear.getMonthYear();
    }

    @Override
    public MonthYear fromString(String name) {
        return monthYearHashMap.get(name);
    }

}
