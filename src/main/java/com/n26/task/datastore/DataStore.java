package com.n26.task.datastore;

public interface DataStore<T> {
    void saveData(T t);
}
