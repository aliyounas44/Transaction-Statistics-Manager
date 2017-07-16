package com.n26.task.service.impl;

import com.n26.task.models.Statistics;
import com.n26.task.repositories.StatisticsRepository;
import com.n26.task.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    private final StatisticsRepository statisticsRepository;

    @Autowired
    StatisticsServiceImpl(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    @Override
    public Statistics getStatistics() {
        return statisticsRepository.get();
    }
}
