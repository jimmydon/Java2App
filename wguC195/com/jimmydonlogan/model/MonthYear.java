package com.jimmydonlogan.model;

import java.time.LocalDate;

public class MonthYear {

    private LocalDate date;
    private String monthYear;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }
}
