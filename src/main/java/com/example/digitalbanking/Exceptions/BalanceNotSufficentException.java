package com.example.digitalbanking.Exceptions;

public class BalanceNotSufficentException extends Exception{
    public BalanceNotSufficentException(String message) {
        super(message);
    }
}
