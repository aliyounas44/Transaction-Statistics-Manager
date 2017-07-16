package com.n26.task.datastore.impl;

import com.n26.task.datastore.TransactionDataStore;
import com.n26.task.models.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.Collections.unmodifiableList;

@Component
public class InMemoryTransactionDataStore implements TransactionDataStore {
    private volatile List<Transaction> data;

    InMemoryTransactionDataStore() {
        data = new CopyOnWriteArrayList<>();
    }

    @Override
    public List<Transaction> getData() {
        return unmodifiableList(data);
    }

    @Override
    public void updateDataStore(List<Transaction> transactionList) {
        data.clear();
        data.addAll(transactionList);
    }

    @Override
    public void saveData(Transaction transaction) {
        data.add(transaction);
    }
}
