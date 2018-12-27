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
import org.powerflows.dmn.engine.evaluator.context.EvaluationContext;
import org.powerflows.dmn.engine.evaluator.entry.InputEntryEvaluator;
import org.powerflows.dmn.engine.evaluator.entry.OutputEntryEvaluator;
import org.powerflows.dmn.engine.model.decision.EvaluationMode;
import org.powerflows.dmn.engine.model.decision.field.Input;
import org.powerflows.dmn.engine.model.decision.field.Output;
import org.powerflows.dmn.engine.model.decision.rule.Rule;
import org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry;
import org.powerflows.dmn.engine.model.decision.rule.entry.OutputEntry;
import org.powerflows.dmn.engine.model.evaluation.result.EntryResult;
import org.powerflows.dmn.engine.model.evaluation.result.RuleResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableList;

@Slf4j
public class RuleEvaluator {

    private final InputEntryEvaluator inputEntryEvaluator;
    private final OutputEntryEvaluator outputEntryEvaluator;

    public RuleEvaluator(final InputEntryEvaluator inputEntryEvaluator,
                         final OutputEntryEvaluator outputEntryEvaluator) {
        this.inputEntryEvaluator = inputEntryEvaluator;
        this.outputEntryEvaluator = outputEntryEvaluator;
    }

    public RuleResult evaluate(final Rule rule,
                               final EvaluationMode decisionEvaluationMode,
                               final Map<String, Input> inputs,
                               final Map<String, Output> outputs,
                               final EvaluationContext evaluationContext) {
        log.debug("Starting evaluation of rule: {} with evaluationMode: {}, inputs: {}, outputs: {} and decision evaluation variable: {}", rule, decisionEvaluationMode, inputs, outputs, evaluationContext);

        final RuleResult ruleResult;

        final List<EntryResult> entryResults = evaluateRule(rule, decisionEvaluationMode, inputs, outputs, evaluationContext);

        if (entryResults.isEmpty()) {
            ruleResult = null;
        } else {
            ruleResult = RuleResult.builder().entryResults(entryResults).build();
        }

        log.debug("Evaluated rule result: {}", ruleResult);

        return ruleResult;
    }

    private List<EntryResult> evaluateRule(final Rule rule,
                                           final EvaluationMode evaluationMode,
                                           final Map<String, Input> inputs,
                                           final Map<String, Output> outputs,
                                           final EvaluationContext evaluationContext) {
        final List<EntryResult> entryResults = new ArrayList<>();
        boolean positive = true;

        for (InputEntry inputEntry : rule.getInputEntries()) {
            final Input input = inputs.get(inputEntry.getName());

            if (!inputEntryEvaluator.evaluate(inputEntry, evaluationMode, input, evaluationContext)) {
                positive = false;
                break;
            }
        }

        if (positive) {
            for (OutputEntry outputEntry : rule.getOutputEntries()) {
                final Output output = outputs.get(outputEntry.getName());
                final EntryResult entryResult = outputEntryEvaluator.evaluate(outputEntry, output, evaluationContext);
                entryResults.add(entryResult);
            }
        }

        return unmodifiableList(entryResults);
    }
}
