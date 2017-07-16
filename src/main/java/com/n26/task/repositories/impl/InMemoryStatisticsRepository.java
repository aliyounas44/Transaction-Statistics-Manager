package com.n26.task.repositories.impl;

import com.n26.task.datastore.StatisticsDataStore;
import com.n26.task.models.Statistics;
import com.n26.task.repositories.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryStatisticsRepository implements StatisticsRepository {
    private final StatisticsDataStore statisticsDataStore;

    @Autowired
    InMemoryStatisticsRepository(StatisticsDataStore statisticsDataStore) {
        this.statisticsDataStore = statisticsDataStore;
    }

    @Override
    public Statistics get() {
        return this.statisticsDataStore.getData();
    }

    @Override
    public void save(Statistics statistics) {
        statisticsDataStore.saveData(statistics);
    }

}
