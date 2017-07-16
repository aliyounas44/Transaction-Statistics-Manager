package com.n26.task.repositories;

public interface GenericRepository<T> {
    void save(T t);
}
