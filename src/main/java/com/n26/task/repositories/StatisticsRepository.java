package com.n26.task.repositories;

import com.n26.task.models.Statistics;

public interface StatisticsRepository extends GenericRepository<Statistics> {
    Statistics get();

}
