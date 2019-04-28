package org.powerflows.dmn.kotlin.dsl


import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import org.powerflows.dmn.engine.model.decision.EvaluationMode
import org.powerflows.dmn.engine.model.decision.HitPolicy
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType
import org.powerflows.dmn.engine.model.decision.field.ValueType
import org.powerflows.dmn.kotlin.dsl.test.MethodSource
import kotlin.reflect.jvm.javaMethod
import kotlin.test.Test

class ApiTest {

    @Test
    fun `should create engine with dsl`() {
        val targetInstance = MethodSource()
        val decisionEngine = engine {
            staticMethod {
                name = "staticTest"
                method = MethodSource::staticMethod.javaMethod
            }
            instanceMethod {
                name = "instanceTest"
                method = MethodSource::instanceMethod.javaMethod
                instance = { targetInstance }
            }
        }

        decisionEngine shouldNotBe null
    }

    @Test
    fun `should create decision table with dsl`() {
        // given:
        val someTableId = "some_table_id"
        val someTableName = "Some Table Name"
        val someHitPolicy = HitPolicy.UNIQUE
        val someExpressionType = ExpressionType.GROOVY
        val someInput1Type = ValueType.INTEGER
        val someInput1Name = "Some Input 1 Name"
        val someInput1NameAlias = "Some Input 1 Name Alias"
        val someInput1Description = "Some Input 1 Description"
        val someInput1Expression1Type = ExpressionType.LITERAL
        val someInput1Expression1Value = 5
        val someInput2Type = ValueType.INTEGER
        val someInput2Name = "Some Input 2 Name"
        val someInput2Description = "Some Input 2 Description"
        val someOutput1Type = ValueType.BOOLEAN
        val someOutput1Name = "Some Output 1 Name"
        val someOutput1Description = "Some Output 1 Description"
        val someOutput2Type = ValueType.STRING
        val someOutput2Name = "Some Output 2 Name"
        val someOutput2Description = "Some Output 2 Description"
        val someRule1Description = "Some Rule 1 Description"
        val someRule1InputEntry1Name = someInput1Name
        val someRule1InputEntry1ExpressionType = ExpressionType.GROOVY
        val someRule1InputEntry1ExpressionValue = "> 20"
        val someRule1InputEntry2Name = someInput2Name
        val someRule1InputEntry2NameAlias = someInput2Name + "Alias"
        val someRule1InputEntry2ExpressionType = ExpressionType.FEEL
        val someRule1InputEntry2ExpressionValue = "not(\"blue\", \"purple\")"
        val someRule1OutputEntry1Name = someOutput1Name
        val someRule1OutputEntry1ExpressionType = ExpressionType.GROOVY
        val someRule1OutputEntry1ExpressionValue = "someVariable1 || someVariable2"
        val someRule2Description = "Some Rule 2 Description"
        val someRule2InputEntry1Name = someInput1Name
        val someRule2InputEntry1Value = "Some Rule 2 Input Entry 1 Value"
        val someRule2InputEntry2Name = someInput2Name
        val someRule2OutputEntry1Name = someOutput1Name
        val someRule2OutputEntry2Name = someOutput2Name
        val someRule2OutputEntry1ExpressionValue = 1
        val someEvaluationMode = EvaluationMode.BOOLEAN
        val someRule2OutputEntry2ExpressionValue = null

        // when:
        val decisionTable = decision {
            id = someTableId
            name = someTableName
            hitPolicy = someHitPolicy
            expressionType = someExpressionType
            evaluationMode = someEvaluationMode

            inputs {
                input(someInput1Name) {
                    type = someInput1Type
                    nameAlias = someInput1NameAlias
                    description = someInput1Description
                    expression(someInput1Expression1Value, someInput1Expression1Type)
                }
                input(someInput2Name) {
                    description = someInput2Description
                    type = someInput2Type
                }
            }

            outputs {
                output(someOutput1Name) {
                    description = someOutput1Description
                    type = someOutput1Type
                }

                output(someOutput2Name) {
                    description = someOutput2Description
                    type = someOutput2Type
                }
            }

            rules {
                rule {
                    description = someRule1Description
                    input(someRule1InputEntry1Name) {
                        expression(someRule1InputEntry1ExpressionValue, someRule1InputEntry1ExpressionType)
                    }

                    input(someRule1InputEntry2Name) {
                        nameAlias = someRule1InputEntry2NameAlias
                        expression(someRule1InputEntry2ExpressionValue, someRule1InputEntry2ExpressionType)
                    }

                    output(someRule1OutputEntry1Name) {
                        expression(someRule1OutputEntry1ExpressionValue, someRule1OutputEntry1ExpressionType)
                    }
                }

                rule {
                    description = someRule2Description
                    input(someRule2InputEntry1Name) {
                        evaluationMode = EvaluationMode.INPUT_COMPARISON
                        value = someRule2InputEntry1Value
                    }
                    input(someRule2InputEntry2Name)
                    output(someRule2OutputEntry1Name) {
                        value = someRule2OutputEntry1ExpressionValue
                    }
                    output(someRule2OutputEntry2Name)
                }
            }

        }

        with(decisionTable) {
            id shouldBe someTableId
            name shouldBe someTableName
            hitPolicy shouldBe someHitPolicy
            expressionType shouldBe someExpressionType
            inputs.size shouldBe 2
            outputs.size shouldBe 2
            rules.size shouldBe 2
        }

        with(decisionTable.inputs[0]) {
            name shouldBe someInput1Name
            description shouldBe someInput1Description
            type shouldBe someInput1Type

            with(expression) {
                type shouldBe someInput1Expression1Type
                value shouldBe someInput1Expression1Value
            }
        }

        with(decisionTable.inputs[1]) {
            name shouldBe someInput2Name
            description shouldBe someInput2Description
            type shouldBe someInput2Type

        }

        with(decisionTable.outputs[0]) {
            name shouldBe someOutput1Name
            description shouldBe someOutput1Description
            type shouldBe someOutput1Type
        }

        with(decisionTable.outputs[1]) {
            name shouldBe someOutput2Name
            description shouldBe someOutput2Description
            type shouldBe someOutput2Type
        }

        with(decisionTable.rules[0]) {
            description shouldBe someRule1Description
            inputEntries.size shouldBe 2
            outputEntries.size shouldBe 1

            with(inputEntries[0]) {
                name shouldBe someRule1InputEntry1Name

                with(expression) {
                    type shouldBe someRule1InputEntry1ExpressionType
                    value shouldBe someRule1InputEntry1ExpressionValue
                }
            }

            with(inputEntries[1]) {
                name shouldBe someRule1InputEntry2Name
                with(expression) {
                    type shouldBe someRule1InputEntry2ExpressionType
                    value shouldBe someRule1InputEntry2ExpressionValue
                }
            }

            with(outputEntries[0]) {
                name shouldBe someRule1OutputEntry1Name
                with(expression) {
                    type shouldBe someRule1OutputEntry1ExpressionType
                    value shouldBe someRule1OutputEntry1ExpressionValue
                }
            }
        }

        with(decisionTable.rules[1]) {
            description shouldBe someRule2Description
            inputEntries.size shouldBe 2
            outputEntries.size shouldBe 2

            with(inputEntries[0]) {
                name shouldBe someRule2InputEntry1Name
                evaluationMode shouldBe EvaluationMode.INPUT_COMPARISON
                with(expression) {
                    type shouldBe ExpressionType.LITERAL
                    value shouldBe someRule2InputEntry1Value
                }
            }

            with(inputEntries[1]) {
                name shouldBe someRule2InputEntry2Name
            }

            with(outputEntries[0]) {
                name shouldBe someRule2OutputEntry1Name
                with(expression) {
                    type shouldBe ExpressionType.LITERAL
                    value shouldBe someRule2OutputEntry1ExpressionValue
                }
            }

            with(outputEntries[1]) {
                name shouldBe someRule2OutputEntry2Name
                with(expression) {
                    type shouldBe someExpressionType
                    value shouldBe someRule2OutputEntry2ExpressionValue
                }
            }
        }
    }
}