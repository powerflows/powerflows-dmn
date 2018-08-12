package org.powerflows.dmn.domain.model;

public interface ElementBuilder<T, S extends ElementBuilder<T, S, P>, P extends Builder<?>>
        extends SubBuilder<T, P> {
    S next();
}