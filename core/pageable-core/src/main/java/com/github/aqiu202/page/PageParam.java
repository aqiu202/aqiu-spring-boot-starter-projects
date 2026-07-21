package com.github.aqiu202.page;

/**
 * PageParam pageable实现
 * @author aqiu 2018/11/26 2:44 PM
 **/
public class PageParam implements PageableInput {

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MIN_PAGE_NUM = 1;
    public static final int MIN_PAGE_SIZE = 1;

    private int page = MIN_PAGE_SIZE;
    private int size = DEFAULT_PAGE_SIZE;

    public PageParam() {
    }

    public PageParam(int page, int size) {
        this.setPage(page);
        this.setSize(size);
    }

    public static PageParam of(int page, int size) {
        return new PageParam(page, size);
    }

    public static PageParam fromOffset(int offset, int size) {
        offset = Math.max(offset, 0);
        size = Math.max(size, 1);
        int page = offset / size + 1;
        return new PageParam(page, size);
    }

    public int getPageNumber() {
        return page;
    }

    public int getPageSize() {
        return size;
    }

    public long getOffset() {
        return (long) (page - 1) * size;
    }

    public boolean hasPrevious() {
        return this.page > MIN_PAGE_NUM;
    }

    @Override
    public PageableInput next() {
        this.page++;
        return this;
    }

    @Override
    public PageableInput previousOrFirst() {
        if (hasPrevious()) {
            this.page--;
        }
        return this;
    }

    @Override
    public PageableInput first() {
        this.page = MIN_PAGE_NUM;
        return this;
    }

    @Override
    public PageableInput setPage(int pageNumber) {
        this.page = Math.max(page, MIN_PAGE_NUM);
        return this;
    }

    @Override
    public PageableInput setSize(int pageSize) {
        this.size = size < MIN_PAGE_SIZE ? DEFAULT_PAGE_SIZE : size;
        return this;
    }
}
