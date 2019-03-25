package org.powerflows.dmn.engine.model.builder;

/**
 * Buildable types contract.
 * @param <T> built type
 */
public interface Buildable<T> {
    T build();
}