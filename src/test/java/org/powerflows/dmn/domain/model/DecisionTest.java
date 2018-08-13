package org.powerflows.dmn.domain.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powerflows.dmn.domain.model.expression.ExpressionType;
import org.powerflows.dmn.domain.model.input.InputType;

import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class DecisionTest {

    @Test
    public void shoudlCreateInstance() {
        final String someTableId = "some_table_id";
        final String someTableName = "Some Table Name";
        final HitPolicy someHitPolicy = HitPolicy.UNIQUE;
        final String someInputName1 = "Some Input Name 1";
        final String someInputDescription1 = "Some Input Description 1";
        final String someInputName2 = "Some Input Name 2";
        final String someInputDescription2 = "Some Input Description 2";
        final String someOutputName1 = "Some Input Name 1";
        final String someOutputDescription1 = "Some Output Description 1";
        final String someOutputName2 = "Some Output Name 2";
        final String someOutputDescription2 = "Some Output Description 2";


        Decision decision = Decision.builder()
                .id(someTableId)
                .name(someTableName)
                .hitPolicy(someHitPolicy)
                .withInputs()
                    .name(someInputName1)
                    .description(someInputDescription1)
                    .type(InputType.INTEGER)
                    .withExpression()
                        .type(ExpressionType.LITERAL)
                        .value(5)
                        .done()
                    .next()
                        .name(someInputName2)
                        .description(someInputDescription2)
                        .withExpression()
                            .type(ExpressionType.FEEL)
                            .value("> 5")
                            .done()
                    .next()
                        .name(someInputName2)
                        .description(someInputDescription2)
                        .withExpression()
                            .type(ExpressionType.GROOVY)
                            .value("> 5")
                            .done()
                    .done()
                .withOutputs()
                    .name(someOutputName1)
                    .description(someOutputDescription1)
                    .withExpression()
                        .type(ExpressionType.GROOVY)
                        .value("> 5")
                        .done()
                    .next()
                    .name(someOutputName2)
                    .description(someOutputDescription2)
                    .withExpression()
                        .type(ExpressionType.LITERAL)
                        .value("abc")
                        .done()
                .done()
                .withRules()
                    .description("rule 1")
                    .withInputEntries()
                            .name("input entry 1:1")
                        .next()
                            .name("input entry 1:2")
                            .done()
                    .withOutputEntries()
                            .name("output 1:1")
                            .done()
                .next()
                    .description("rule 2")
                    .withInputEntries()
                        .name("input 2:1")
                        .done()
                    .withOutputEntries()
                        .name("output 2:2")
                        .done()
                    .done()
                .build();

        assertNotNull(decision);
    }
}
