package com.n26.task.controllers;

import com.n26.task.dto.StatisticsDto;
import com.n26.task.models.Statistics;
import com.n26.task.service.StatisticsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsControllerTest {

    private final String URL = "/statistics";
    private MockMvc mockMvc;

    @Mock
    private StatisticsService statisticsService;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private StatisticsController statisticsController;

    @Before
    public void setUp() {
        statisticsController = new StatisticsController(statisticsService, modelMapper);
        mockMvc = standaloneSetup(statisticsController).build();
    }

    @Test
    public void shouldReturnOkStatusCodeForEveryRequest() throws Exception {
        this.mockMvc.perform(get(URL))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatistics() throws Exception {
        Statistics statistics = buildStatistics();
        when(statisticsService.getStatistics()).thenReturn(statistics);
        when(modelMapper.map(statistics, StatisticsDto.class)).thenReturn(new StatisticsDto());

        this.mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.sum", is(statistics.getSum())))
                .andExpect(jsonPath("$.avg", is(statistics.getAvg())))
                .andExpect(jsonPath("$.count", is((int) statistics.getCount())))
                .andExpect(jsonPath("$.min", is(statistics.getMin())))
                .andExpect(jsonPath("$.max", is(statistics.getMax())));

        verify(statisticsService, times(1)).getStatistics();
        verify(modelMapper, times(1)).map(statistics, StatisticsDto.class);
    }

    private Statistics buildStatistics(){
       return Statistics.builder().build();
    }

}