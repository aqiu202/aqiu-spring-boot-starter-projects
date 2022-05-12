package com.noah.base.page;


/**
 * PageParam pageable实现
 * @author aqiu 2018/11/26 2:44 PM
 **/
public class PageParam implements PageableInput {

    private int page;
    private final int size;

    protected PageParam(int page, int size) {
        this.page = Math.max(page, 1);
        this.size = size < 1 ? 10 : size;
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
        return this.page > 1;
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
        this.page = 1;
        return this;
    }

    @Override
    public PageableInput setPage(int pageNumber) {
        this.page = pageNumber;
        return this;
    }
}
