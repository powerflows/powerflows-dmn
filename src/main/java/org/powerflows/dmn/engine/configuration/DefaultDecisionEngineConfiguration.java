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
import org.powerflows.dmn.engine.evaluator.entry.InputEntryEvaluator;
import org.powerflows.dmn.engine.evaluator.entry.OutputEntryEvaluator;
import org.powerflows.dmn.engine.evaluator.entry.mode.provider.EvaluationModeProviderFactory;
import org.powerflows.dmn.engine.evaluator.expression.provider.ExpressionEvaluationProviderFactory;
import org.powerflows.dmn.engine.evaluator.expression.script.DefaultScriptEngineProvider;
import org.powerflows.dmn.engine.evaluator.expression.script.ScriptEngineProvider;
import org.powerflows.dmn.engine.evaluator.rule.RuleEvaluator;
import org.powerflows.dmn.engine.evaluator.type.converter.TypeConverterFactory;

import javax.script.ScriptEngineManager;

public class DefaultDecisionEngineConfiguration implements DecisionEngineConfiguration {

    private DecisionEvaluator decisionEvaluator;
    private RuleEvaluator ruleEvaluator;
    private EvaluationModeProviderFactory evaluationModeProviderFactory;
    private InputEntryEvaluator inputEntryEvaluator;
    private OutputEntryEvaluator outputEntryEvaluator;
    private ScriptEngineProvider scriptEngineProvider;
    private ExpressionEvaluationProviderFactory expressionEvaluationProviderFactory;
    private TypeConverterFactory typeConverterFactory;

    @Override
    public DecisionEngine configure() {
        initScriptEngineProvider();
        initEvaluationProviderFactory();
        initTypeConverterFactory();
        initEvaluationModeProviderFactory();
        initInputEntryEvaluator();
        initOutputEntryEvaluator();
        initRuleEvaluator();
        initDecisionEvaluator();

        return new DefaultDecisionEngine(decisionEvaluator);
    }

    private void initEvaluationProviderFactory() {
        expressionEvaluationProviderFactory = new ExpressionEvaluationProviderFactory(scriptEngineProvider);
    }

    private void initTypeConverterFactory() {
        typeConverterFactory = new TypeConverterFactory();
    }

    private void initScriptEngineProvider() {
        scriptEngineProvider = new DefaultScriptEngineProvider(new ScriptEngineManager());
    }

    private void initEvaluationModeProviderFactory() {
        evaluationModeProviderFactory = new EvaluationModeProviderFactory();
    }

    private void initInputEntryEvaluator() {
        inputEntryEvaluator = new InputEntryEvaluator(expressionEvaluationProviderFactory, typeConverterFactory, evaluationModeProviderFactory);
    }

    private void initOutputEntryEvaluator() {
        outputEntryEvaluator = new OutputEntryEvaluator(expressionEvaluationProviderFactory, typeConverterFactory);
    }

    private void initRuleEvaluator() {
        ruleEvaluator = new RuleEvaluator(inputEntryEvaluator, outputEntryEvaluator);
    }

    private void initDecisionEvaluator() {
        decisionEvaluator = new DecisionEvaluator(ruleEvaluator);
    }
}
