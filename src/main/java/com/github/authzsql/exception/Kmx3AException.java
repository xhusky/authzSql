package com.github.authzsql.exception;

/**
 * This guy is lazy, nothing left.
 *
 * @author Think Wong
 */
public class Kmx3AException extends RuntimeException {
    public Kmx3AException() {
        super();
    }

    public Kmx3AException(String message) {
        super(message);
    }

    public Kmx3AException(String message, Throwable cause) {
        super(message, cause);
    }

    public Kmx3AException(Throwable cause) {
        super(cause);
    }

    protected Kmx3AException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
