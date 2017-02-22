package com.aashreys.walls.domain.values;

import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by aashreys on 02/12/16.
 */
public class IncorrectValueException extends Exception {

    protected IncorrectValueException() {
    }

    protected IncorrectValueException(String message) {
        super(message);
    }

    protected IncorrectValueException(String message, Throwable cause) {
        super(message, cause);
    }

    protected IncorrectValueException(Throwable cause) {
        super(cause);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected IncorrectValueException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
