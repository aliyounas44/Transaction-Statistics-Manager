package com.n26.task.utils;

import static java.lang.System.currentTimeMillis;

public class TransactionUtils {
    public static boolean isTransactionTimeIsInRequiredRange(long timeStamp, long noOfSecondsAllowed) {
        return (currentTimeMillis() - timeStamp) / 1000 < noOfSecondsAllowed;
    }
}
