package com.asmit.JobApp.exception;

public class InvalidFileException extends RuntimeException
{
    public InvalidFileException(String message)
    {
        super(message);
    }
}