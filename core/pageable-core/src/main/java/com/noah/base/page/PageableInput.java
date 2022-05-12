package com.noah.base.page;

/**
 * <pre>分页参数</pre>
 *
 * @author aqiu 2020/11/15 11:13
 **/
public interface PageableInput {

    int getPageNumber();

    int getPageSize();

    long getOffset();

    boolean hasPrevious();

    PageableInput next();

    PageableInput previousOrFirst();

    PageableInput first();

    PageableInput setPage(int pageNumber);
}
