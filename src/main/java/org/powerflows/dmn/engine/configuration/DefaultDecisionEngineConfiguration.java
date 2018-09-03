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

package org.powerflows.dmn.engine.configuration;


import org.powerflows.dmn.engine.DecisionEngine;
import org.powerflows.dmn.engine.DefaultDecisionEngine;
import org.powerflows.dmn.engine.evaluator.decision.DecisionEvaluator;
import org.powerflows.dmn.engine.evaluator.entry.EntryEvaluator;
import org.powerflows.dmn.engine.evaluator.entry.InputEntryEvaluator;
import org.powerflows.dmn.engine.evaluator.entry.OutputEntryEvaluator;
import org.powerflows.dmn.engine.evaluator.entry.expression.provider.EvaluationProviderFactory;
import org.powerflows.dmn.engine.evaluator.rule.RuleEvaluator;

public class DefaultDecisionEngineConfiguration extends AbstractDecisionEngineConfiguration {

    private DecisionEvaluator decisionEvaluator;
    private RuleEvaluator ruleEvaluator;
    private EntryEvaluator entryEvaluator;
    private InputEntryEvaluator inputEntryEvaluator;
    private OutputEntryEvaluator outputEntryEvaluator;
    private EvaluationProviderFactory evaluationProviderFactory;

    @Override
    public DecisionEngine configure() {
        initEvaluationProviderFactory();
        initInputEntryEvaluator();
        initOutputEntryEvaluator();
        initEntryEvaluator();
        initRuleEvaluator();
        initDecisionEvaluator();

        return new DefaultDecisionEngine(decisionReader, decisionEvaluator);
    }

    private void initEvaluationProviderFactory() {
        evaluationProviderFactory = new EvaluationProviderFactory();
    }

    private void initInputEntryEvaluator() {
        inputEntryEvaluator = new InputEntryEvaluator(evaluationProviderFactory);
    }

    private void initOutputEntryEvaluator() {
        outputEntryEvaluator = new OutputEntryEvaluator(evaluationProviderFactory);
    }

    private void initEntryEvaluator() {
        entryEvaluator = new EntryEvaluator(inputEntryEvaluator, outputEntryEvaluator);
    }

    private void initRuleEvaluator() {
        ruleEvaluator = new RuleEvaluator(entryEvaluator);
    }

    private void initDecisionEvaluator() {
        decisionEvaluator = new DecisionEvaluator(ruleEvaluator);
    }
}
