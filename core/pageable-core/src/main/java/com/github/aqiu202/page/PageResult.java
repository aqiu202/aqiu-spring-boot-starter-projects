package com.github.aqiu202.page;

import java.util.List;

public class PageResult<T> {

    private final List<T> rows;
    private final long total;
    private Integer page;
    private Integer size;

    protected PageResult(List<T> rows, long total) {
        this.rows = rows;
        this.total = total;
    }

    public PageResult<T> pageable(PageableInput pageable) {
        this.page = pageable.getPageNumber();
        this.size = pageable.getPageSize();
        return this;
    }

    public static <T> PageResult<T> of(PageableOutput<T> pageableOutput, PageableInput pageable) {
        return of(pageableOutput).pageable(pageable);
    }

    public static <T> PageResult<T> of(PageableOutput<T> pageableOutput) {
        return of(pageableOutput.getRows(), pageableOutput.getTotal());
    }

    public static <T> PageResult<T> of(List<T> results, long total) {
        return new PageResult<>(results, total);
    }

    public List<T> getRows() {
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

}
