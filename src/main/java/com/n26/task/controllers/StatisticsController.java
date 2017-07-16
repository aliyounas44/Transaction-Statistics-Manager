package com.n26.task.controllers;

import com.n26.task.dto.StatisticsDto;
import com.n26.task.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@RestController
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final ModelMapper modelMapper;

    @Autowired
    StatisticsController(StatisticsService statisticsService, ModelMapper modelMapper) {
        this.statisticsService = statisticsService;
        this.modelMapper = modelMapper;
    }

    @RequestMapping(value = "/statistics", method = GET)
    public StatisticsDto getStatistics() {
        log.info("Got request of 'GET' for statistics ");
        return modelMapper.map(this.statisticsService.getStatistics(), StatisticsDto.class);
    }
}
