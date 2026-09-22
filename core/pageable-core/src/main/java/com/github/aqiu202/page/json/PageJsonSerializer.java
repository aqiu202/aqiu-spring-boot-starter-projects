package com.github.aqiu202.page.json;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import com.github.aqiu202.page.PageableOutput;

/**
 * <pre>对{@link PageableOutput}的json序列化配置</pre>
 *
 * @author aqiu 2020/11/15 9:35
 **/
public class PageJsonSerializer extends ValueSerializer<PageableOutput<?>> {

    private static final String FIELD_ROWS = "rows";
    private static final String FIELD_TOTAL = "total";
    private static final String FIELD_PAGE = "pageNumber";
    private static final String FIELD_SIZE = "pageSize";

    @Override
    public void serialize(PageableOutput<?> value, JsonGenerator gen,
            SerializationContext ctxt) {
        gen.writeStartObject();
        gen.writeName(FIELD_ROWS);
        gen.writePOJO(value.getRows());
        gen.writeName(FIELD_TOTAL);
        gen.writeNumber(value.getTotal());
        final Integer pageNumber = value.getPageNumber();
        if (pageNumber != null) {
            gen.writeName(FIELD_PAGE);
            gen.writeNumber(pageNumber);
        }
        final Integer pageSize = value.getPageSize();
        if (pageSize != null) {
            gen.writeName(FIELD_SIZE);
            gen.writeNumber(pageSize);
        }
        gen.writeEndObject();
    }
}
