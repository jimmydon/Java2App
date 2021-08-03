package com.jimmydonlogan.model;

import java.time.LocalDate;

/**
 * The type Month year.
 */
public class MonthYear {

    /**
     * The Date.
     */
    private LocalDate date;
    /**
     * The Month year.
     */
    private String monthYear;

    /**
     * Gets date.
     *
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets month year.
     *
     * @return the month year
     */
    public String getMonthYear() {
        return monthYear;
    }

    /**
     * Sets month year.
     *
     * @param monthYear the month year
     */
    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }
}
