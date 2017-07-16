package performance;

import com.n26.task.configurations.Configurations;
import com.n26.task.models.Statistics;
import com.n26.task.models.Transaction;
import com.n26.task.repositories.StatisticsRepository;
import com.n26.task.repositories.TransactionRepository;
import com.n26.task.service.impl.StatisticsCalculatorServiceImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

import static com.n26.task.models.Statistics.builder;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsServiceCalculatorImplPerformanceTest {
    @Mock
    private StatisticsRepository statisticsRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private Configurations configurations;
    @InjectMocks
    private StatisticsCalculatorServiceImpl statisticsCalculatorService;

    private static final int NO_OF_FRESH_TRANSACTIONS_IN_1_MIN = 30000;
    private static final int NO_OF_FRESH_STALE_TRANSACTIONS_IN_1_MIN = 10000;

    @Before
    public void setUp() {
        statisticsCalculatorService = new StatisticsCalculatorServiceImpl(statisticsRepository, transactionRepository,
                configurations);
        when(configurations.getTransactionStaleTimeSeconds()).thenReturn(60L);
        doNothing().when(statisticsRepository).save(any(Statistics.class));

        List<Transaction> transactions = new LinkedList<>();
        transactions.addAll(buildTransactions(true, NO_OF_FRESH_TRANSACTIONS_IN_1_MIN));
        transactions.addAll(buildTransactions(false, NO_OF_FRESH_STALE_TRANSACTIONS_IN_1_MIN));

        when(transactionRepository.getAll()).thenReturn(transactions);
        when(statisticsRepository.get()).thenReturn(builder().build());
    }

    @Test(timeout = 1000)
    @Ignore
    public void shouldSaveUpdatedStatisticsForTransactionsOfAMinuteWithin1Second() throws Exception {
        statisticsCalculatorService.calculateStatistics();
    }

    private List<Transaction> buildTransactions(boolean isFresh, int noOfTransactions) {
        long noOfSecondsForStaleTransactions =  isFresh ? 1000 : 70000;
        List<Transaction> transactions = new LinkedList<>();
        for(int i = 0; i < noOfTransactions; i++) {
            Transaction t1 = new Transaction();
            t1.setAmount(10.0);
            t1.setTimeStamp(System.currentTimeMillis() - noOfSecondsForStaleTransactions);
            transactions.add(t1);
        }
        return transactions;
    }
}
