package com.github.aqiu202.starters.jpa.predicate;

import java.util.Collection;

public abstract class PredicateInfos extends PredicateInfo {

    private final PredicateInfo[] predicates;

    public PredicateInfos(PredicateInfo... predicates) {
        super(null, null, null);
        this.predicates = predicates;
    }

    protected abstract PredicatesWrapper.Connector getConnector();

    public PredicateInfos(Collection<PredicateInfo> predicates) {
        this(predicates.toArray(new PredicateInfo[0]));
    }

    public PredicateInfo[] getPredicates() {
        return predicates;
    }
}
