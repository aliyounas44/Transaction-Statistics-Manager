package com.n26.task.service.impl;

import com.n26.task.configurations.Configurations;
import com.n26.task.models.Statistics;
import com.n26.task.models.Transaction;
import com.n26.task.repositories.StatisticsRepository;
import com.n26.task.repositories.TransactionRepository;
import com.n26.task.service.StatisticsCalculatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

import static com.n26.task.utils.TransactionUtils.isTransactionTimeIsInRequiredRange;
import static java.lang.Double.MAX_VALUE;
import static java.lang.Double.MIN_VALUE;
import static java.lang.Math.max;
import static java.lang.Math.min;

@Slf4j
@Service
public class StatisticsCalculatorServiceImpl implements StatisticsCalculatorService {

    private final StatisticsRepository statisticsRepository;
    private final TransactionRepository transactionRepository;
    private final Configurations config;

    @Autowired
    public StatisticsCalculatorServiceImpl(StatisticsRepository statisticsRepository,
                                    TransactionRepository transactionRepository,
                                    Configurations configurations) {
        this.statisticsRepository = statisticsRepository;
        this.transactionRepository = transactionRepository;
        this.config = configurations;
    }

    @Scheduled(cron = "${statistics-refresh-cron-expression}")
    @Override
    public void calculateStatistics() {
        Statistics statistics = statisticsRepository.get();
        List<Transaction> freshTransaction = new LinkedList<>();
        double max = MIN_VALUE;
        double min = MAX_VALUE;
        long count = 0;
        double sum = 0.0;
        List<Transaction> transactions = transactionRepository.getAll();
        for (Transaction transaction : transactions) {
            if (isTransactionTimeIsInRequiredRange(transaction.getTimeStamp(), config.getTransactionStaleTimeSeconds())) {
                sum += transaction.getAmount();
                max = max(transaction.getAmount(), max);
                min = min(transaction.getAmount(), min);
                count++;
                freshTransaction.add(transaction);
            }
        }
        if (Long.compare(count, 0) > 0) {
            populateStatistics(statistics, max, min, count, sum);
        } else {
            populateStatistics(statistics, 0.0, 0.0, 0, 0.0);
        }
        transactionRepository.saveAll(freshTransaction);
        statisticsRepository.save(statistics);
    }

    private void populateStatistics(Statistics statistics, double max, double min, long count, double sum) {
        statistics.setSum(sum);
        statistics.setAvg(Long.compare(count, 0L) == 0 ? 0 : sum / count);
        statistics.setCount(count);
        statistics.setMin(min);
        statistics.setMax(max);
    }

}