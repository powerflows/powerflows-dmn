package org.powerflows.dmn.domain.model;

public interface SubBuilder<T, P extends Builder> extends Builder<T> {
    P done();
}