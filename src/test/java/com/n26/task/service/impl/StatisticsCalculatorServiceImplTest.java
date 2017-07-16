package com.n26.task.service.impl;

import com.n26.task.configurations.Configurations;
import com.n26.task.models.Statistics;
import com.n26.task.models.Transaction;
import com.n26.task.repositories.StatisticsRepository;
import com.n26.task.repositories.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

import static com.n26.task.models.Statistics.builder;
import static java.util.Collections.EMPTY_LIST;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsCalculatorServiceImplTest {

    @Mock
    private StatisticsRepository statisticsRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private Configurations configurations;
    @InjectMocks
    private StatisticsCalculatorServiceImpl statisticsCalculatorService;

    private ArgumentCaptor<Statistics> argumentCaptor;

    @Before
    public void setUp() {
        statisticsCalculatorService = new StatisticsCalculatorServiceImpl(statisticsRepository, transactionRepository,
                configurations);
        when(configurations.getTransactionStaleTimeSeconds()).thenReturn(60L);
        doNothing().when(statisticsRepository).save(any(Statistics.class));
        this.argumentCaptor = ArgumentCaptor.forClass(Statistics.class);
    }

    @Test
    public void shouldSaveDefaultStatisticsWhenNoTransactionIsInDataStore() throws Exception {
        when(transactionRepository.getAll()).thenReturn(EMPTY_LIST);
        when(statisticsRepository.get()).thenReturn(builder().build());

        statisticsCalculatorService.calculateStatistics();

        verify(transactionRepository, times(1)).getAll();
        verify(statisticsRepository, times(1)).save(argumentCaptor.capture());

        assertDefaultStatistics();
    }

    private void assertDefaultStatistics() {
        Statistics actualValue = argumentCaptor.getValue();
        assertEquals(0, actualValue.getCount());
        assertEquals(0.0, actualValue.getMax(),0.0);
        assertEquals(0.0, actualValue.getMin(), 0.0);
        assertEquals(0.0, actualValue.getSum(),0.0);
        assertEquals(0.0, actualValue.getAvg(),0.0);
    }

    @Test
    public void shouldSaveUpdatedStatisticsWhenAllTransactionsAreFresh() throws Exception {
        when(transactionRepository.getAll()).thenReturn(buildTransactions(true));
        when(statisticsRepository.get()).thenReturn(builder().build());

        statisticsCalculatorService.calculateStatistics();

        verify(transactionRepository, times(1)).getAll();
        verify(statisticsRepository, times(1)).save(argumentCaptor.capture());

        assertNonDefaultStatistics();
    }

    @Test
    public void shouldSaveUpdatedStatisticsWhenAllTransactionsAreStaleForCurrentTime() throws Exception {
        when(transactionRepository.getAll()).thenReturn(buildTransactions(false));
        when(statisticsRepository.get()).thenReturn(builder()
                .count(2)
                .max(20.0)
                .min(10.0)
                .sum(30.0)
                .avg(150.).build());

        statisticsCalculatorService.calculateStatistics();

        verify(transactionRepository, times(1)).getAll();
        verify(statisticsRepository, times(1)).save(argumentCaptor.capture());

        assertDefaultStatistics();
    }

    private void assertNonDefaultStatistics() {
        Statistics actualValue = argumentCaptor.getValue();
        assertEquals(2, actualValue.getCount());
        assertEquals(20.0, actualValue.getMax(),0.0);
        assertEquals(10.0, actualValue.getMin(), 0.0);
        assertEquals(30.0, actualValue.getSum(),0.0);
        assertEquals(15.0, actualValue.getAvg(),0.0);
    }

    @Test
    public void shouldSaveUpdatedStatisticsWhenAllTransactionsAreStale() throws Exception {
        when(transactionRepository.getAll()).thenReturn(buildTransactions(false));
        when(statisticsRepository.get()).thenReturn(builder().build());

        statisticsCalculatorService.calculateStatistics();

        verify(transactionRepository, times(1)).getAll();
        verify(statisticsRepository, times(1)).save(argumentCaptor.capture());

        assertDefaultStatistics();
    }

    @Test
    public void shouldSaveUpdatedStatisticsForFreshAndStaleTransactions() throws Exception {
        List<Transaction> transactions = buildTransactions(false);
        transactions.addAll(buildTransactions(true));
        when(statisticsRepository.get()).thenReturn(builder().build());
        when(transactionRepository.getAll()).thenReturn(transactions);

        statisticsCalculatorService.calculateStatistics();

        verify(transactionRepository, times(1)).getAll();
        verify(statisticsRepository, times(1)).save(argumentCaptor.capture());
        assertNonDefaultStatistics();
    }

    private List<Transaction> buildTransactions(boolean isFresh) {
        long noOfSecondsForStaleTransactions =  isFresh ? 1000 : 70000;

        Transaction t1 = new Transaction();
        t1.setAmount(10.0);
        t1.setTimeStamp(System.currentTimeMillis() - noOfSecondsForStaleTransactions);

        Transaction t2 = new Transaction();
        t2.setAmount(20.0);
        t2.setTimeStamp(System.currentTimeMillis() - noOfSecondsForStaleTransactions);

        List<Transaction> transactions = new LinkedList<>();
        transactions.add(t1);
        transactions.add(t2);
        return transactions;
    }

}