/*
 * Copyright (c) 2018-present PowerFlows.org - all rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.powerflows.dmn.engine.model.builder;

import org.powerflows.dmn.engine.model.decision.DecisionBuildException;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public abstract class AbstractBuilder<T> implements Buildable<T> {
    protected T product;

    public AbstractBuilder() {
        initProduct();
    }

    protected abstract void initProduct();

    protected T assembleProduct() {
        return this.product;
    }

    @Override
    public final T build() {
        final T temp = assembleProduct();
        if (temp == null) {
            throw new IllegalStateException("Only single build() call is allowed");
        }
        this.product = null;

        return temp;
    }

    protected void validateIsNonNull(final Serializable value, final String message) {
        if (Objects.isNull(value)) {
            throw new DecisionBuildException(message);
        }
    }

    protected void validateIsNonEmpty(final List value, final String message) {
        if (value.isEmpty()) {
            throw new DecisionBuildException(message);
        }
    }
}