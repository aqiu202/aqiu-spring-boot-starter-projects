package com.noah.base.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.noah.base.page.PageableOutput;
import java.io.IOException;

/**
 * <pre>对{@link PageableOutput}的json序列化配置</pre>
 *
 * @author aqiu 2020/11/15 9:35
 **/
public class PageJsonSerializer extends JsonSerializer<PageableOutput<?>> {

    private static final String FIELD_ROWS = "rows";
    private static final String FIELD_TOTAL = "total";
    private static final String FIELD_PAGE = "page";
    private static final String FIELD_SIZE = "size";

    @Override
    public void serialize(PageableOutput<?> value, JsonGenerator gen,
            SerializerProvider serializers)
            throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeObjectField(FIELD_ROWS, value.getRows());
        gen.writeNumberField(FIELD_TOTAL, value.getTotal());
        final Integer page = value.getPage();
        if (page != null) {
            gen.writeNumberField(FIELD_PAGE, page);
        }
        final Integer size = value.getSize();
        if (size != null) {
            gen.writeNumberField(FIELD_SIZE, size);
        }
        gen.writeEndObject();
    }
}
