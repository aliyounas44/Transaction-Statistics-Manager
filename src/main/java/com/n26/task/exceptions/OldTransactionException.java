package com.n26.task.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Transaction is older than configured seconds.")
public class OldTransactionException extends RuntimeException {

    public OldTransactionException(String message) {
        super(message);
    }

}
