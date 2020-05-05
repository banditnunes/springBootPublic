package br.com.alura.curso.springboot.forum.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableDefault implements Pageable {
    @Override
    public int getPageNumber() {
        return 10;
    }

    @Override
    public int getPageSize() {
        return 1;
    }

    @Override
    public long getOffset() {
        return 1;
    }

    @Override
    public Sort getSort() {
        return Sort.by(Sort.DEFAULT_DIRECTION);
    }

    @Override
    public Pageable next() {
        return this;
    }

    @Override
    public Pageable previousOrFirst() {
        return this;
    }

    @Override
    public Pageable first() {
        return this;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }


}
