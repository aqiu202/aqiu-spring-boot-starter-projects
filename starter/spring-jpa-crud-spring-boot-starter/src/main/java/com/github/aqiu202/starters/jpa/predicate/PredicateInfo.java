package com.github.aqiu202.starters.jpa.predicate;

public class PredicateInfo {

    private final String pathString;

    private final PredicatesWrapper.Logic logic;

    private final Object value;

    public PredicateInfo(String pathString, PredicatesWrapper.Logic logic, Object value) {
        this.pathString = pathString;
        this.logic = logic;
        this.value = value;
    }

    public String getPathString() {
        return pathString;
    }

    public PredicatesWrapper.Logic getLogic() {
        return logic;
    }

    public Object getValue() {
        return value;
    }
}
