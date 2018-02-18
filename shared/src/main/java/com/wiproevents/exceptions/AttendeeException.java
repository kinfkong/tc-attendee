package com.wiproevents.exceptions;


/**
 * The base exception for the application. Thrown if there is an error during CRUD operations.
 */
public class AttendeeException extends Exception {

    /**
     * <p>
     * This is the constructor of <code>AttendeeException</code> class with message argument.
     * </p>
     *
     * @param message the error message.
     */
    public AttendeeException(String message) {
        super(message);
    }

    /**
     * <p>
     * This is the constructor of <code>AttendeeException</code> class with message and cause arguments.
     * </p>
     *
     * @param message the error message.
     * @param cause the cause of the exception.
     */
    public AttendeeException(String message, Throwable cause) {
        super(message, cause);
    }
}


