package com.company.cashman.lib;

/**
 *Exception thrown when enough denominations are not available matching with provided input.
 */
public class CashNotAvailableException extends Exception {

    public CashNotAvailableException() { super(); }

    public CashNotAvailableException(String message) { super(message); }

    public CashNotAvailableException(String message, Throwable cause) { super(message, cause); }

    public CashNotAvailableException(Throwable cause) { super(cause); }
}
