package com.n26.task.repositories;

import com.n26.task.models.Transaction;

import java.util.List;

public interface TransactionRepository extends GenericRepository<Transaction> {
    List<Transaction> getAll();
    void saveAll(List<Transaction> freshTransaction);
}
