package com.bbz.test.validation.exception;

public class UserNameExistsException extends Throwable{
    public UserNameExistsException(final String message) {
        super(message);
    }
}