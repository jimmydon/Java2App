package com.jimmydonlogan.util;

import com.jimmydonlogan.model.Country;
import javafx.util.StringConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Country converter.
 */
public class CountryConverter extends StringConverter<Country> {

    /**
     * The Country map.
     */
    private final Map<String, Country> countryMap = new HashMap<>();

    @Override
    public String toString(Country country) {
        String name="";
        if(country==null)
            return name;
        countryMap.put(country.getCountryName(), country);
        return country.getCountryName();
    }

    @Override
    public Country fromString(String name) {
        return countryMap.get(name);
    }

}