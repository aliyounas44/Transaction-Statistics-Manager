package com.n26.task.datastore;

import com.n26.task.models.Transaction;

import java.util.List;

public interface TransactionDataStore extends DataStore<Transaction> {
    List<Transaction> getData();
    void updateDataStore(List<Transaction> transactionList);
}
