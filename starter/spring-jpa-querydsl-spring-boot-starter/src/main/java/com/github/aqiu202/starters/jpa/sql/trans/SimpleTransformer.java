package com.github.aqiu202.starters.jpa.sql.trans;

import com.github.aqiu202.starters.jpa.sql.trans.inter.AnonymousResultTransformer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.transform.ResultTransformer;
import org.springframework.util.Assert;

public final class SimpleTransformer implements ResultTransformer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final AnonymousResultTransformer anonymousResultTransformer;

    public SimpleTransformer(AnonymousResultTransformer anonymousResultTransformer) {
        Assert.notNull(anonymousResultTransformer, "构造参数anymousResultTransformer不能为空");
        this.anonymousResultTransformer = anonymousResultTransformer;
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        int length = tuple.length;
        Map<String, Object> result = new HashMap<>(length);
        for (int i = 0; i < length; i++) {
            String alias = aliases[i];
            if (alias != null) {
                alias = this.anonymousResultTransformer.handle(alias);
                result.put(alias, tuple[i]);
            }
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List transformList(List collection) {
        return collection;
    }

}
