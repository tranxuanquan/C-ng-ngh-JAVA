package vn.edu.eaut.lab5.model;

import java.util.List;

public class PageResult<T> {
    private final List<T> items;
    private final int totalRows;

    public PageResult(List<T> items, int totalRows) {
        this.items = items;
        this.totalRows = totalRows;
    }

    public List<T> getItems() {
        return items;
    }

    public int getTotalRows() {
        return totalRows;
    }
}
