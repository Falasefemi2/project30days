package com.femiproject.inventorysystem;

import java.util.List;

public interface InventoryInterface<T> {
    void save(List<T> items) throws Exception;

    List<T> load() throws Exception;

}
