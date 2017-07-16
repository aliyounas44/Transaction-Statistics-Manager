package com.n26.task.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Statistics {
    double sum;
    double avg;
    double max;
    double min;
    long count;
}
