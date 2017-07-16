package com.n26.task.datastore.impl;

import com.n26.task.models.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryTransactionDataStoreTest {

    private InMemoryTransactionDataStore inMemoryTransactionDataStore = new InMemoryTransactionDataStore();

    @Test
    public void shouldNotReturnNullWhenNoDataAvailable() {
        //when
        List<Transaction> result = inMemoryTransactionDataStore.getData();
        //then
        assertNotNull(result);
    }

    @Test
    public void shouldReturnEmptyListWhenNoDataAvailable() {
        // given
        int expectedResult = 0;
        //when
        List<Transaction> result = inMemoryTransactionDataStore.getData();
        //then
        assertEquals(expectedResult, result.size());
    }

    @Test
    public void shouldStoreTransaction() {
        //given
        Transaction transaction = getMockedTransaction();
        //when
        inMemoryTransactionDataStore.saveData(transaction);
    }

    @Test
    public void shouldReturnRequiredDataWhenDataExists() {
        //given
        Transaction transaction = getMockedTransaction();
        inMemoryTransactionDataStore.saveData(transaction);
        //when
        List<Transaction> result = inMemoryTransactionDataStore.getData();
        //then
        assertEquals(1, result.size());
        assertEquals(transaction, result.get(0));

    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowExceptionWhenModifyingResultData() {
        //given
        Transaction transaction = getMockedTransaction();
        //when
        List<Transaction> result = inMemoryTransactionDataStore.getData();
        //then
        result.add(transaction);
    }

    private Transaction getMockedTransaction() {
        return mock(Transaction.class);
    }
}