package com.n26.task.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TransactionDto {
    private double amount;
    private long timeStamp;
}
