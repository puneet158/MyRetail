package com.retail.myretail.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException() {
        super();
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
