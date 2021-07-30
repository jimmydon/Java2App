package com.jimmydonlogan.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class CustomerAppointBean {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private int value;
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void setValue(int newValue) {
        int oldValue = value;
        value = newValue;
        support.firePropertyChange("value", oldValue, newValue);
    }
}
