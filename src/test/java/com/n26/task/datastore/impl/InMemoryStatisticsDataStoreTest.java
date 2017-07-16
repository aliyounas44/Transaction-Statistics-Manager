package com.n26.task.datastore.impl;

import com.n26.task.models.Statistics;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class InMemoryStatisticsDataStoreTest {

    private InMemoryStatisticsDataStore inMemoryStatisticsDataStore = new InMemoryStatisticsDataStore();
    private static final double EQUAL_DOUBLE_OBJECTS_DELTA = 0.0;

    @Test
    public void shouldNotReturnNullWhenNoDataAvailable() {
        //when
        Statistics result = inMemoryStatisticsDataStore.getData();
        //then
        assertNotNull(result);
    }

    @Test
    public void shouldReturnStatisticsWithDefaultValuesWhenNoDataAvailable() {
        //given
        Statistics statistics = getMockedStatistics();
        inMemoryStatisticsDataStore.saveData(statistics);
        //when
        Statistics result = inMemoryStatisticsDataStore.getData();
        //then
        assertEquals(0, result.getCount());
        assertEquals(0.0, result.getSum(), EQUAL_DOUBLE_OBJECTS_DELTA);
        assertEquals(0.0, result.getAvg(), EQUAL_DOUBLE_OBJECTS_DELTA);
        assertEquals(0.0, result.getMax(), EQUAL_DOUBLE_OBJECTS_DELTA);
        assertEquals(0.0, result.getMin(), EQUAL_DOUBLE_OBJECTS_DELTA);

    }

    @Test
    public void shouldStoreStatistics() {
        //given
        Statistics statistics = getMockedStatistics();
        //when
        inMemoryStatisticsDataStore.saveData(statistics);
    }

    @Test
    public void shouldReturnCorrectStatisticsWhenDataExists() {
        //given
        Statistics statistics = getMockedStatistics();
        when(statistics.getCount()).thenReturn(2L);
        when(statistics.getSum()).thenReturn(10.0);
        when(statistics.getAvg()).thenReturn(5.0);
        when(statistics.getMax()).thenReturn(8.0);
        when(statistics.getMin()).thenReturn(2.0);
        inMemoryStatisticsDataStore.saveData(statistics);
        //when
        Statistics result = inMemoryStatisticsDataStore.getData();
        //then
        assertEquals(statistics.getCount(), result.getCount());
        assertEquals(statistics.getSum(), result.getSum(), EQUAL_DOUBLE_OBJECTS_DELTA);
        assertEquals(statistics.getAvg(), result.getAvg(), EQUAL_DOUBLE_OBJECTS_DELTA);
        assertEquals(statistics.getMax(), result.getMax(), EQUAL_DOUBLE_OBJECTS_DELTA);
        assertEquals(statistics.getMin(), result.getMin(), EQUAL_DOUBLE_OBJECTS_DELTA);
    }

    @Test
    public void shouldReturnNewStatisticsObjectEveryTime() {
        //given
        Statistics statistics = getMockedStatistics();
        inMemoryStatisticsDataStore.saveData(statistics);
        //when
        Statistics result = inMemoryStatisticsDataStore.getData();
        //then
        assertNotEquals(statistics.hashCode(), result.hashCode());
    }

    private Statistics getMockedStatistics() {
        return mock(Statistics.class);
    }

}