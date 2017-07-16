package com.n26.task.repositories.impl;

import com.n26.task.datastore.TransactionDataStore;
import com.n26.task.models.Transaction;
import com.n26.task.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InMemoryTransactionRepository implements TransactionRepository {

    private final TransactionDataStore dataStore;

    @Autowired
    InMemoryTransactionRepository(TransactionDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public void save(Transaction transaction) {
        dataStore.saveData(transaction);
    }

    @Override
    public List<Transaction> getAll() {
        return dataStore.getData();
    }

    @Override
    public void saveAll(List<Transaction> freshTransaction) {
        dataStore.updateDataStore(freshTransaction);
    }

}
