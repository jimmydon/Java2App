package com.jimmydonlogan.util;

import javafx.scene.control.TextField;

import java.util.function.Predicate;

public class TextFieldIsEmpty {

    public final Predicate<TextField> emptyValue = s -> s.getText().isEmpty();

}
