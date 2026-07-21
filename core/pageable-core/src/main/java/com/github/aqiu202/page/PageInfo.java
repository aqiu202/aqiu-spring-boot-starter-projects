package com.github.aqiu202.page;

import java.util.AbstractList;
import java.util.List;

public class PageInfo<E> extends AbstractList<E> implements PageableOutput<E> {

    private final long total;
    private final List<E> rows;
    private Integer pageNumber;
    private Integer pageSize;

    private PageInfo(List<E> rows, long total) {
        this.rows = rows;
        this.total = total;
    }

    private PageInfo(List<E> rows, long total, int pageNumber, int pageSize) {
        this.rows = rows;
        this.total = total;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public static <T> PageInfo<T> of(List<T> results, long total) {
        return new PageInfo<>(results, total);
    }

    public static <T> PageInfo<T> of(List<T> results, long total, int pageNumber, int pageSize) {
        return new PageInfo<>(results, total, pageNumber, pageSize);
    }

    public List<E> getRows() {
        return rows;
    }

    public long getTotal() {
        return total;
    }

    @Override
    public Integer getPageNumber() {
        return pageNumber;
    }

    @Override
    public Integer getPageSize() {
        return pageSize;
    }

    @Override
    public E get(int index) {
        return this.rows.get(index);
    }

    @Override
    public int size() {
        return this.rows.size();
    }
}
