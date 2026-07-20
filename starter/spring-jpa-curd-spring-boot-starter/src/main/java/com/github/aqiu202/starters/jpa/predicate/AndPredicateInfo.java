package com.github.aqiu202.starters.jpa.predicate;

import java.util.Collection;

public class AndPredicateInfo extends PredicateInfos {

    public AndPredicateInfo(PredicateInfo... predicates) {
        super(predicates);
    }

    public AndPredicateInfo(Collection<PredicateInfo> predicates) {
        super(predicates);
    }

    @Override
    protected PredicatesWrapper.Connector getConnector() {
        return PredicatesWrapper.Connector.and;
    }
}
