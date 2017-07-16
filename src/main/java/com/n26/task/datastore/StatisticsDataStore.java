package com.n26.task.datastore;

import com.n26.task.models.Statistics;

public interface StatisticsDataStore extends DataStore<Statistics> {
    Statistics getData();
}
