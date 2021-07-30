package com.jimmydonlogan.exceptions;

public class AddRecordException
        extends RuntimeException {
    public AddRecordException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}