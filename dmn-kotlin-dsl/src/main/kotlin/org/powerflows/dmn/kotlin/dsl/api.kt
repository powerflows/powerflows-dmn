package org.powerflows.dmn.kotlin.dsl

import org.powerflows.dmn.engine.DecisionEngine
import org.powerflows.dmn.engine.configuration.DefaultDecisionEngineConfiguration
import org.powerflows.dmn.engine.evaluator.expression.provider.binding.InstanceMethodBinding
import org.powerflows.dmn.engine.evaluator.expression.provider.binding.MethodBinding
import org.powerflows.dmn.engine.evaluator.expression.provider.binding.StaticMethodBinding
import org.powerflows.dmn.engine.model.decision.Decision
import org.powerflows.dmn.engine.model.decision.EvaluationMode
import org.powerflows.dmn.engine.model.decision.HitPolicy
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType
import org.powerflows.dmn.engine.model.decision.field.Input
import org.powerflows.dmn.engine.model.decision.field.ValueType
import org.powerflows.dmn.engine.model.decision.rule.Rule
import org.powerflows.dmn.engine.model.decision.rule.entry.InputEntry
import org.powerflows.dmn.engine.model.decision.rule.entry.OutputEntry
import java.io.Serializable
import java.lang.reflect.Method

/**
 * DSL Marker annotation.
 */
@DslMarker
annotation class PowerFlowsDsl

/**
 * Static method binding DSL element.
 */
@PowerFlowsDsl
data class StaticMethod(
        var name: String? = null,
        var method: Method? = null
)

/**
 * Instance method binding DSL element.
 */
@PowerFlowsDsl
data class InstanceMethod(
        var name: String? = null,
        var method: Method? = null,
        var instance: (() -> Any)? = null
)

/**
 * Method binding DSL element.
 */
@PowerFlowsDsl
class MethodBindingDsl(private val methodBindings: MutableList<MethodBinding>) {

    /**
     * Setup static method.
     */
    fun staticMethod(block: StaticMethod.() -> Unit) {
        val staticMethod = StaticMethod().apply(block)

        this.methodBindings.add(StaticMethodBinding(staticMethod.name, staticMethod.method))
    }

    /**
     * Setup instance method.
     */
    fun instanceMethod(block: InstanceMethod.() -> Unit) {
        val instanceMethod = InstanceMethod().apply(block)

        this.methodBindings.add(
                InstanceMethodBinding(
                        instanceMethod.name,
                        instanceMethod.method,
                        instanceMethod.instance
                )
        )
    }
}

/**
 * Entry point to Engine configuration DSL.
 *
 * @param block with DSL contents executed in scope of MethodBindingDsl
 */
fun engine(block: MethodBindingDsl.() -> Unit): DecisionEngine {
    val methodBindings = mutableListOf<MethodBinding>()

    MethodBindingDsl(methodBindings).apply(block)

    return DefaultDecisionEngineConfiguration()
            .methodBindings(methodBindings)
            .configure()
}

/**
 * Decision input DSL element.
 */
@PowerFlowsDsl
class InputDsl(
        private val inputBuilder: Input.Builder,
        var type: ValueType? = null,
        var nameAlias: String? = null,
        var description: String? = null,
        var evaluationMode: EvaluationMode? = null
) {
    /**
     * Setup expression on input.
     */
    fun expression(value: Serializable? = null, type: ExpressionType? = null) {
        inputBuilder.withExpression {
            if (value != null) {
                it.value(value)
            }
            if (type != null) {
                it.type(type)
            }
            it.build()
        }
    }
}

/**
 * DSL element grouping Decision inputs.
 */
@PowerFlowsDsl
class InputsBlockDsl(private val decisionBuilder: Decision.Builder) {
    /**
     * Setup Decision input.
     */
    fun input(name: String, block: InputDsl.() -> Unit) {
        decisionBuilder.withInput {
            val inputDsl = InputDsl(it).apply(block)
            it
                    .name(name)
                    .type(inputDsl.type)
                    .description(
                            inputDsl.description
                    )
            if (inputDsl.nameAlias != null) {
                it.nameAlias(inputDsl.nameAlias)
            }
            it.evaluationMode(inputDsl.evaluationMode)
                    .build()
        }
    }
}

/**
 * Decision output DSL element.
 */
@PowerFlowsDsl
data class OutputDsl(
        var type: ValueType? = null,
        var description: String? = null
)

/**
 * DSL element grouping Decision outputs.
 */
@PowerFlowsDsl
class OutputsBlockDsl(private val decisionBuilder: Decision.Builder) {
    /**
     * Setup Decision output.
     */
    fun output(name: String, block: OutputDsl.() -> Unit) {
        val outputDsl = OutputDsl().apply(block)
        decisionBuilder.withOutput {
            it
                    .name(name)
                    .type(outputDsl.type)
                    .description(
                            outputDsl
                                    .description
                    ).build()
        }
    }
}

/**
 * Decision rule input DSL element.
 */
@PowerFlowsDsl
class RuleInputDsl(
        private val inputBuilder: InputEntry.Builder,
        var nameAlias: String? = null,
        var evaluationMode: EvaluationMode? = null,
        var value: Serializable? = null
) {
    /**
     * Setup expression on rule input.
     */
    fun expression(value: Serializable? = null, type: ExpressionType? = null) {
        inputBuilder.withExpression {
            if (value != null) {
                it.value(value)
            }
            if (type != null) {
                it.type(type)
            }
            it.build()
        }
    }
}

/**
 * Decision rule output DSL element.
 */
@PowerFlowsDsl
class RuleOutputDsl(
        private val outputBuilder: OutputEntry.Builder,
        var value: Serializable? = null
) {
    /**
     * Setup expression on rule output.
     */
    fun expression(value: Serializable? = null, type: ExpressionType? = null) {
        outputBuilder.withExpression {
            if (value != null) {
                it.value(value)
            }
            if (type != null) {
                it.type(type)
            }
            it.build()
        }
    }
}

/**
 * Decision rule DSL element.
 */
@PowerFlowsDsl
class RuleDsl(
        private val ruleBuilder: Rule.Builder,
        var description: String? = null
) {
    /**
     * Setup rule input.
     */
    fun input(name: String, block: RuleInputDsl.() -> Unit = {}) {
        ruleBuilder.withInputEntry {
            val input = RuleInputDsl(it).apply(block)
            it.name(name)
            if (input.evaluationMode != null) {
                it.evaluationMode(input.evaluationMode)
            }
            if (input.nameAlias != null) {
                it.nameAlias(input.nameAlias)
            }
            if (input.value != null) {
                it.withLiteralValue(input.value)
            }

            it.build()
        }
    }

    /**
     * Setup rule output.
     */
    fun output(name: String, block: RuleOutputDsl.() -> Unit = {}) {
        ruleBuilder.withOutputEntry {
            val output = RuleOutputDsl(it).apply(block)
            it.name(name)
            if (output.value != null) {
                it.withLiteralValue(output.value)
            }
            it.build()
        }
    }
}

/**
 * DSL element grouping Decision rules.
 */
@PowerFlowsDsl
class RulesBlockDsl(private val decisionBuilder: Decision.Builder) {
    /**
     * Setup rule.
     */
    fun rule(block: RuleDsl.() -> Unit) {
        decisionBuilder.withRule {
            val ruleDsl = RuleDsl(it).apply(block)
            it.description(ruleDsl.description)
                    .build()
        }
    }
}

/**
 * Decision DSL element.
 */
@PowerFlowsDsl
class DecisionDsl(
        private val builder: Decision.Builder,
        var id: String? = null,
        var name: String? = null,
        var hitPolicy: HitPolicy? = null,
        var evaluationMode: EvaluationMode? = null,
        var expressionType: ExpressionType? = null
) {

    /**
     * Setup Decision inputs.
     */
    fun inputs(block: InputsBlockDsl.() -> Unit) {
        InputsBlockDsl(builder).apply(block)
    }

    /**
     * Setup Decision outputs.
     */
    fun outputs(block: OutputsBlockDsl.() -> Unit) {
        OutputsBlockDsl(builder).apply(block)
    }

    /**
     * Setup Decision rules.
     */
    fun rules(block: RulesBlockDsl.() -> Unit) {
        RulesBlockDsl(builder).apply(block)
    }
}

/**
 * Entry point to Decision generation DSL.
 *
 * @param block with DSL contents executed in scope of DecisionDsl
 */
fun decision(block: DecisionDsl.() -> Unit): Decision {
    val builder = Decision.builder()
    val decisionDsl = DecisionDsl(builder).apply(block)

    builder
            .id(decisionDsl.id)
            .name(decisionDsl.name)
    if (decisionDsl.evaluationMode != null) {
        builder.evaluationMode(decisionDsl.evaluationMode)
    }
    if (decisionDsl.expressionType != null) {
        builder.expressionType(decisionDsl.expressionType)
    }
    if (decisionDsl.hitPolicy != null) {
        builder.hitPolicy(decisionDsl.hitPolicy)
    }
    return builder.build()
}