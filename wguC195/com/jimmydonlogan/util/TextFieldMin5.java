package com.jimmydonlogan.util;

import java.util.function.Predicate;

/**
 * The type Text field min 5.
 */
public class TextFieldMin5 {

    /**
     * The constant preLenAtLeast5. using lambda concise code,avoid using bulky anonymous class implementation
     */
    public static final Predicate<String> preLenAtLeast5 = t -> t.length() > 4;
}
