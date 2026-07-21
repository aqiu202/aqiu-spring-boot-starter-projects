package com.github.aqiu202.page.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.aqiu202.page.PageableOutput;

import java.io.IOException;

/**
 * <pre>对{@link PageableOutput}的json序列化配置</pre>
 *
 * @author aqiu 2020/11/15 9:35
 **/
public class PageJsonSerializer extends JsonSerializer<PageableOutput<?>> {

    private static final String FIELD_ROWS = "rows";
    private static final String FIELD_TOTAL = "total";
    private static final String FIELD_PAGE = "pageNumber";
    private static final String FIELD_SIZE = "pageSize";

    @Override
    public void serialize(PageableOutput<?> value, JsonGenerator gen,
            SerializerProvider serializers)
            throws IOException {
        gen.writeStartObject();
        gen.writeObjectField(FIELD_ROWS, value.getRows());
        gen.writeNumberField(FIELD_TOTAL, value.getTotal());
        final Integer pageNumber = value.getPageNumber();
        if (pageNumber != null) {
            gen.writeNumberField(FIELD_PAGE, pageNumber);
        }
        final Integer pageSize = value.getPageSize();
        if (pageSize != null) {
            gen.writeNumberField(FIELD_SIZE, pageSize);
        }
        gen.writeEndObject();
    }
}
