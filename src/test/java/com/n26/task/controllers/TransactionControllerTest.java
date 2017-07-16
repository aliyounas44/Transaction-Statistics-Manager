package com.n26.task.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.task.dto.TransactionDto;
import com.n26.task.exceptions.OldTransactionException;
import com.n26.task.models.Transaction;
import com.n26.task.service.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class TransactionControllerTest {

    private final String URL = "/transactions";
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private TransactionService transactionService;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private TransactionsController transactionsController;

    @Before
    public void setUp() {
        transactionsController = new TransactionsController(transactionService, modelMapper);
        mockMvc = standaloneSetup(transactionsController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldReturnCreatedStatusCodeForTransactionWithin1Minute() throws Exception {
        TransactionDto transactionDto = createTransactionDto(true);
        Transaction transaction = new Transaction();
        when(modelMapper.map(transactionDto, Transaction.class)).thenReturn(transaction);
        doNothing().when(transactionService).save(transaction);

        this.mockMvc.perform(post(URL)
                .contentType(APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(transactionDto)))
                .andExpect(status().isCreated());

        verify(transactionService, times(1)).save(transaction);
        verify(modelMapper, times(1)).map(transactionDto, Transaction.class);
    }

    @Test
    public void shouldReturnNoContentStatusCodeForTransactionAfter1Minute() throws Exception {
        TransactionDto transactionDto = createTransactionDto(false);
        Transaction transaction = new Transaction();
        when(modelMapper.map(transactionDto, Transaction.class)).thenReturn(transaction);
        doThrow(OldTransactionException.class).when(transactionService).save(transaction);

        this.mockMvc.perform(post(URL)
                .contentType(APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(transactionDto)))
                .andExpect(status().isNoContent());

        verify(transactionService, times(1)).save(transaction);
        verify(modelMapper, times(1)).map(transactionDto, Transaction.class);
    }

    @Test
    public void shouldReturnBadRequestStatusCodeForInvalidInput() throws Exception {
        TransactionDto transactionDto = createTransactionDto(false);
        Transaction transaction = new Transaction();
        when(modelMapper.map(transactionDto, Transaction.class)).thenReturn(transaction);
        doNothing().when(transactionService).save(transaction);

        this.mockMvc.perform(post(URL)
                .contentType(APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());

        verify(transactionService, times(0)).save(transaction);
        verify(modelMapper, times(0)).map(transactionDto, Transaction.class);
    }

    private TransactionDto createTransactionDto(boolean isFresh){
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(10.0);
        transactionDto.setTimeStamp(isFresh ? System.currentTimeMillis() : (System.currentTimeMillis() - 60000));
        return transactionDto;
    }

}