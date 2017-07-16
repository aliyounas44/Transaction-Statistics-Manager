package com.n26.task.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Transaction {
    private double amount;
    private long timeStamp;
}
