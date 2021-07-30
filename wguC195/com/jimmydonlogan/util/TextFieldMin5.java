package com.jimmydonlogan.util;

import java.util.function.Predicate;

public class TextFieldMin5 {

    public static final Predicate<String> preLenAtLeast5 = t -> t.length() > 4;
}
