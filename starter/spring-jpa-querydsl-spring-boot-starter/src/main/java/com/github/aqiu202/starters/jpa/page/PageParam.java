package com.github.aqiu202.starters.jpa.page;

import javax.annotation.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

/**
 * PageParam pageable实现
 *
 * @author aqiu 2018/11/26 2:44 PM
 **/
public class PageParam extends com.noah.base.page.PageParam implements Pageable {

    @Nullable
    private final Sort sort;

    private PageParam(int page, int size) {
        this(page, size, null);
    }

    private PageParam(int page, int size, @Nullable Sort sort) {
        super(page, size);
        this.sort = sort;
    }

    public static PageParam of(int page, int size) {
        return new PageParam(page, size);
    }

    public static PageParam of(int page, int size, Sort sort) {
        return new PageParam(page, size, sort);
    }

    public static PageParam fromOffset(int offset, int size) {
        return fromOffset(offset, size, null);
    }

    public static PageParam fromOffset(int offset, int size, Sort sort) {
        offset = Math.max(offset, 0);
        size = Math.max(size, 1);
        int page = offset / size + 1;
        return new PageParam(page, size, sort);
    }

    public static PageParam sort(Pageable pageable, Sort sort) {
        Sort s = pageable.getSort();
        if (s.isUnsorted()) {
            s = sort;
        } else {
            s = s.and(sort);
        }
        return new PageParam(pageable.getPageNumber(), pageable.getPageSize(), s);
    }

    @Override
    public boolean isPaged() {
        return Pageable.super.isPaged();
    }

    @Override
    public boolean isUnpaged() {
        return Pageable.super.isUnpaged();
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Sort getSortOr(Sort sort) {
        return Pageable.super.getSortOr(sort);
    }

    @Override
    public PageParam next() {
        super.next();
        return this;
    }

    @Override
    public PageParam previousOrFirst() {
        super.previousOrFirst();
        return this;
    }

    @Override
    public PageParam first() {
        super.first();
        return this;
    }

    @Override
    public Pageable withPage(int pageNumber) {
        super.setPage(pageNumber);
        return this;
    }

    @Override
    public Optional<Pageable> toOptional() {
        return Pageable.super.toOptional();
    }

}
