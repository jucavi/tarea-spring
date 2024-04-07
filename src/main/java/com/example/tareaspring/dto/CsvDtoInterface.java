package com.example.tareaspring.dto;

/**
 * Interface for csv dto
 */
public interface CsvDtoInterface<T> {

    /**
     * Method needed to convert to DAO
     *
     * @return Bean
     */
    T mapToDao();
}
