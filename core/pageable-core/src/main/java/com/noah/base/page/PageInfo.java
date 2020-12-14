package com.noah.base.page;

import java.util.AbstractList;
import java.util.List;

public class PageInfo<E> extends AbstractList<E> implements PageableOutput<E> {

    private final long total;
    private final List<E> rows;
    private Integer page;
    private Integer size;

    private PageInfo(List<E> rows, long total) {
        this.rows = rows;
        this.total = total;
    }

    private PageInfo(List<E> rows, long total, int page, int size) {
        this.rows = rows;
        this.total = total;
        this.page = page;
        this.size = size;
    }

    public static <T> PageInfo<T> of(List<T> results, long total) {
        return new PageInfo<>(results, total);
    }

    public static <T> PageInfo<T> of(List<T> results, long total, int page, int size) {
        return new PageInfo<>(results, total, page, size);
    }

    public List<E> getRows() {
        return rows;
    }

    public long getTotal() {
        return total;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
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
