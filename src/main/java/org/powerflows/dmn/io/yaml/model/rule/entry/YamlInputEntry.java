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

package org.powerflows.dmn.io.yaml.model.rule.entry;

import lombok.Data;
import org.powerflows.dmn.engine.model.decision.EvaluationMode;
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType;

@Data
public final class YamlInputEntry {
    public YamlInputEntry(final Object value) {
        this.expression = value;
    }

    public YamlInputEntry() {
    }

    private String nameAlias;
    private ExpressionType expressionType = ExpressionType.LITERAL;
    private Object expression;
    private EvaluationMode evaluationMode;
}
