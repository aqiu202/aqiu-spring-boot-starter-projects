package com.noah.base.page;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.noah.base.json.PageJsonSerializer;
import java.util.List;

/**
 * <pre>分页结果数据</pre>
 *
 * @author aqiu 2020/11/15 10:24
 **/
@JsonSerialize(using = PageJsonSerializer.class)
public interface PageableOutput<E> {

    List<E> getRows();

    long getTotal();

    Integer getPage();

    Integer getSize();

}
