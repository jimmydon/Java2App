package com.jimmydonlogan.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The type Customer appoint bean.
 */
public class CustomerAppointBean {

    /**
     * The Support.
     */
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    /**
     * The Value.
     */
    private int value;

    /**
     * Add property change listener.
     *
     * @param listener the listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Sets value.
     *
     * @param newValue the new value
     */
    public void setValue(int newValue) {
        int oldValue = value;
        value = newValue;
        support.firePropertyChange("value", oldValue, newValue);
    }
}
