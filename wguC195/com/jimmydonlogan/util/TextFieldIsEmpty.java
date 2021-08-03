package com.jimmydonlogan.util;

import javafx.scene.control.TextField;

import java.util.function.Predicate;

/**
 * The type Text field is empty.
 */
public class TextFieldIsEmpty {

    /**
     * The Empty value.
     *   using lambda expressions to instantiate and avoid using bulky anonymous class implementation
     */
    public final Predicate<TextField> emptyValue = s -> s.getText().isEmpty();

}
