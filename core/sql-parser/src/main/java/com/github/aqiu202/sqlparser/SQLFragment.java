package com.github.aqiu202.sqlparser;

import com.github.aqiu202.sqlparser.fragment.ExpressionAppendable;
import com.github.aqiu202.sqlparser.fragment.ExpressionHandler;
import com.github.aqiu202.sqlparser.fragment.FromFragment;
import com.github.aqiu202.sqlparser.fragment.JoinFragment;
import com.github.aqiu202.sqlparser.fragment.WhereFragment;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SQLFragment extends ExpressionAppendable, ExpressionHandler {

    FromFragment getFromFragment();

    List<JoinFragment> getJoinFragments();

    WhereFragment getWhereFragment();

    Map<String, String> getTableAliasMap();

    Set<String> findAllTableNames();

    String asText();

}
