package com.example.student.BSUIR.HealthyLifestyleBot.Exception;

public class RangeExceededException extends Exception{
    public RangeExceededException() {
        super();
    }

    public RangeExceededException(String message) {
        super(message);
    }

    public RangeExceededException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }
}
