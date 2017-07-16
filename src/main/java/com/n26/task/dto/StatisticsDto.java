package com.n26.task.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatisticsDto {
    private double sum;
    private double avg;
    private double max;
    private double min;
    private long count;
}
