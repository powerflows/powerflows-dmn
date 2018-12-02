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

package org.powerflows.dmn.engine.evaluator.rule;


import lombok.extern.slf4j.Slf4j;
import org.powerflows.dmn.engine.evaluator.context.ModifiableContextVariables;
import org.powerflows.dmn.engine.evaluator.entry.EntryEvaluator;
import org.powerflows.dmn.engine.model.decision.field.Input;
import org.powerflows.dmn.engine.model.decision.rule.Rule;
import org.powerflows.dmn.engine.model.evaluation.result.EntryResult;
import org.powerflows.dmn.engine.model.evaluation.result.RuleResult;

import java.util.List;
import java.util.Map;

@Slf4j
public class RuleEvaluator {

    private final EntryEvaluator entryEvaluator;

    public RuleEvaluator(EntryEvaluator entryEvaluator) {
        this.entryEvaluator = entryEvaluator;
    }

    public RuleResult evaluate(final Rule rule, final Map<String, Input> inputs, final ModifiableContextVariables contextVariables) {
        log.debug("Starting evaluation of rule: {} with inputs: {} and context variables: {}", rule, inputs, contextVariables);

        final RuleResult ruleResult;

        final List<EntryResult> entryResults = entryEvaluator.evaluate(rule.getInputEntries(), rule.getOutputEntries(), inputs, contextVariables);

        if (entryResults.isEmpty()) {
            ruleResult = null;
        } else {
            ruleResult = RuleResult.builder().entryResults(entryResults).build();
        }

        log.debug("Evaluated rule result: {}", ruleResult);

        return ruleResult;
    }


}
