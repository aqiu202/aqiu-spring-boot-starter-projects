package com.github.aqiu202.starters.jpa.sql.trans;

import com.github.aqiu202.util.PropertyNameUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.transform.ResultTransformer;

public final class MapBeanTransformer implements ResultTransformer {

    public static final ResultTransformer INSTANCE = new MapBeanTransformer();

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private MapBeanTransformer() {
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Map<String, Object> result = new HashMap<>(tuple.length);
        for (int i = 0; i < tuple.length; i++) {
            String alias = aliases[i];
            if (alias != null) {
                alias = alias.toLowerCase();
                result.put(PropertyNameUtils.hump(alias), tuple[i]);
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
