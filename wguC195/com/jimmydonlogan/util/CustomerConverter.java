package com.jimmydonlogan.util;

import com.jimmydonlogan.model.Customer;
import javafx.util.StringConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Customer converter.
 */
public class CustomerConverter extends StringConverter<Customer> {

    /**
     * The Customer map.
     */
    private final Map<String, Customer> customerMap = new HashMap<>();

    @Override
    public String toString(Customer customer) {
        String name="";
        if(customer==null)
            return name;
        customerMap.put(customer.getName(), customer);
        return customer.getName();
    }

    @Override
    public Customer fromString(String name) {
        return customerMap.get(name);
    }





}


