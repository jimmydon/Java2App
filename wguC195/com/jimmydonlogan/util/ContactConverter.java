package com.jimmydonlogan.util;


import com.jimmydonlogan.model.Contact;
import javafx.util.StringConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Contact converter.
 */
public class ContactConverter extends StringConverter<Contact> {

    /**
     * The Contact map.
     */
    private final Map<String, Contact> contactMap = new HashMap<>();

    @Override
    public String toString(Contact contact) {
        String name="";
        if(contact==null)
            return name;
        contactMap.put(contact.getContactName(), contact);
        return contact.getContactName();
    }

    @Override
    public Contact fromString(String name) {
        return contactMap.get(name);
    }





}
