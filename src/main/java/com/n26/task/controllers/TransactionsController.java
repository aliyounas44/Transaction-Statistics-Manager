package com.n26.task.controllers;

import com.n26.task.dto.TransactionDto;
import com.n26.task.models.Transaction;
import com.n26.task.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.n26.task.controllers.TransactionsController.END_POINT_URL;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@RestController
@RequestMapping(value = END_POINT_URL)
public class TransactionsController {

    static final String END_POINT_URL = "/transactions";
    private final TransactionService transactionService;
    private final ModelMapper modelMapper;

    @Autowired
    TransactionsController(TransactionService transactionService, ModelMapper modelMapper) {
        this.transactionService = transactionService;
        this.modelMapper = modelMapper;
    }

    @RequestMapping(method = POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void postTransaction(@RequestBody TransactionDto transactionDto) {
        log.info("Got request of 'POST' with 'transaction':  " + transactionDto);
        transactionService.save(modelMapper.map(transactionDto, Transaction.class));
    }

}
