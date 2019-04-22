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

package org.powerflows.dmn.io.yaml.model;

import lombok.Data;
import org.powerflows.dmn.engine.model.decision.EvaluationMode;
import org.powerflows.dmn.engine.model.decision.HitPolicy;
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType;
import org.powerflows.dmn.io.yaml.model.field.YamlFields;
import org.powerflows.dmn.io.yaml.model.rule.YamlRule;

import java.util.List;

@Data
public final class YamlDecision {
    private String id;
    private String name;
    private HitPolicy hitPolicy;
    private ExpressionType expressionType;
    private EvaluationMode evaluationMode;
    private YamlFields fields;
    private List<YamlRule> rules;
}
