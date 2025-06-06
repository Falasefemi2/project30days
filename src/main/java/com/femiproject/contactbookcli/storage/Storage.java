package com.femiproject.contactbookcli.storage;

import java.util.List;

public interface Storage<T> {
    void save(List<T> items) throws Exception;

    List<T> load() throws Exception;
}
