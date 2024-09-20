package com.github.aqiu202.starters.jpa.page;

import com.github.aqiu202.page.PageResult;
import com.github.aqiu202.page.PageableInput;
import com.querydsl.core.QueryResults;
import java.util.List;
import org.springframework.data.domain.Page;

public class JPAPageResult<T> extends PageResult<T> {

    private JPAPageResult(List<T> rows, long total) {
        super(rows, total);
    }

    public static <T> PageResult<T> of(Page<T> page, PageableInput pageable) {
        return of(page).pageable(pageable);
    }

    public static <T> PageResult<T> of(QueryResults<T> page, PageableInput pageable) {
        return of(page).pageable(pageable);
    }

    public static <T> PageResult<T> of(Page<T> page) {
        return of(page.getContent(), page.getTotalElements());
    }

    public static <T> PageResult<T> of(QueryResults<T> page) {
        return of(page.getResults(), page.getTotal());
    }

}
