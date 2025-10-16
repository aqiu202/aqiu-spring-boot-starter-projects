package com.github.aqiu202.sqlparser.expression;

import com.github.aqiu202.sqlparser.fragment.TableFragment;

import java.util.List;

public interface AppendableExpressionFactory<M extends AppendableExpressionMeta> {

    AppendableExpressionMetaStore<M> getMetaStore();

   default M findMetaByTableName(String tableName) {
       return this.getMetaStore().get(tableName);
   }

    default List<AppendableExpressionItem> createExpressionItems(TableFragment fragment) {
        String tableName = fragment.getTableName();
        return this.buildExpressionItems(fragment, this.findMetaByTableName(tableName));
    }

    List<AppendableExpressionItem> buildExpressionItems(TableFragment fragment, M meta);

}
