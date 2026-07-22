package com.github.aqiu202.starters.jpa.predicate;

import java.util.Collection;

public class OrPredicateInfo extends PredicateInfos {

    public OrPredicateInfo(PredicateInfo... predicates) {
        super(predicates);
    }

    public OrPredicateInfo(Collection<PredicateInfo> predicates) {
        super(predicates);
    }

    @Override
    protected PredicatesWrapper.Connector getConnector() {
        return PredicatesWrapper.Connector.or;
    }
}
