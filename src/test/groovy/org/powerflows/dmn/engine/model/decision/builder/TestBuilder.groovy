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

package org.powerflows.dmn.engine.model.decision.builder

import org.powerflows.dmn.engine.model.builder.AbstractBuilder
import org.powerflows.dmn.engine.model.decision.Decision
import org.powerflows.dmn.engine.model.decision.EvaluationMode
import org.powerflows.dmn.engine.model.decision.field.Input

class TestBuilder extends AbstractBuilder<Decision> {

    private EvaluationMode evaluationMode
    private List<Input> inputs

    TestBuilder() {
        this.evaluationMode = EvaluationMode.BOOLEAN
        this.inputs = [[] as Input]
    }

    TestBuilder(final EvaluationMode evaluationMode) {
        this.evaluationMode = evaluationMode
        this.inputs = new ArrayList<>()
    }

    TestBuilder(final List<Input> inputs) {
        this.evaluationMode = EvaluationMode.BOOLEAN
        this.inputs = inputs
    }

    @Override
    protected void initProduct() {
        this.product = [] as Decision;
    }

    @Override
    protected Decision assembleProduct() {
        validateIsNonNull(evaluationMode, 'Evaluation mode is required');
        validateIsNonEmpty(inputs, 'At least one input is required');

        return this.product;
    }
}
