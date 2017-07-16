package com.n26.task.datastore.impl;

import com.n26.task.datastore.StatisticsDataStore;
import com.n26.task.models.Statistics;
import org.springframework.stereotype.Component;

@Component
public class InMemoryStatisticsDataStore implements StatisticsDataStore {
    private volatile Statistics statistics;

    InMemoryStatisticsDataStore() {
        this.statistics = Statistics.builder().build();
    }

    @Override
    public Statistics getData() {
        return Statistics.builder()
                .sum(statistics.getSum())
                .avg(statistics.getAvg())
                .count(statistics.getCount())
                .max(statistics.getMax())
                .min(statistics.getMin())
                .build();
    }

    @Override
    public void saveData(Statistics statistics) {
        this.statistics = statistics;
    }
}
