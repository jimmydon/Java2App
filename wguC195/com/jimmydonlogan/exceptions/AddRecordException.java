package com.jimmydonlogan.exceptions;

/**
 * The type Add record exception.
 */
public class AddRecordException
        extends RuntimeException {
    /**
     * Instantiates a new Add record exception.
     *
     * @param errorMessage the error message
     * @param err          the err
     */
    public AddRecordException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}