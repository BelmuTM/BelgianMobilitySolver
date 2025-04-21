package com.belmu.belgianmobilitysolver.model;

@FunctionalInterface
public interface ReferenceResolver<T> {
    void resolveEntity(T entity);
}
