package com.n26.task.service.impl;

import com.n26.task.configurations.Configurations;
import com.n26.task.exceptions.OldTransactionException;
import com.n26.task.models.Transaction;
import com.n26.task.repositories.TransactionRepository;
import com.n26.task.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.n26.task.utils.TransactionUtils.isTransactionTimeIsInRequiredRange;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final Configurations configurations;

    @Autowired
    TransactionServiceImpl(TransactionRepository transactionRepository, Configurations configurations) {
        this.transactionRepository = transactionRepository;
        this.configurations = configurations;
    }

    @Override
    public void save(Transaction transaction) {
        if(!isTransactionTimeIsInRequiredRange(transaction.getTimeStamp(), configurations.getTransactionStaleTimeSeconds())) {
            throw new OldTransactionException("Transaction is older than configured seconds.");
        }
        transactionRepository.save(transaction);
    }
}
