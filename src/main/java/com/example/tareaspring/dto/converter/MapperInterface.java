package com.example.tareaspring.dto.converter;

public interface MapperInterface<T, S> {

    S mapDaoToDto(T entity);
    T mapDtoToDao(S pojo);
}
