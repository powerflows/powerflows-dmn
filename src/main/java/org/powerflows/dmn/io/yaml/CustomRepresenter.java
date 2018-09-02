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
package org.powerflows.dmn.io.yaml;

import org.powerflows.dmn.engine.model.decision.expression.ExpressionType;
import org.powerflows.dmn.io.yaml.model.YamlDecision;
import org.powerflows.dmn.io.yaml.model.rule.YamlRuleEntry;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

import java.util.Collection;
import java.util.Map;

public class CustomRepresenter extends Representer {
    CustomRepresenter() {
        this.setPropertyUtils(new CustomPropertyUtils());
        this.addClassTag(YamlDecision.class, Tag.MAP);
        this.representers.put(YamlRuleEntry.class, new RepresentYamlRuleEntry());
    }

    class RepresentYamlRuleEntry implements Represent {

        @Override
        public Node representData(Object data) {
            if (data instanceof YamlRuleEntry && ((YamlRuleEntry) data).getExpressionType() == ExpressionType.LITERAL) {
                return represent(((YamlRuleEntry) data).getExpression());
            }

            return representJavaBean(getProperties(data.getClass()), data);
        }
    }

    @Override
    protected NodeTuple representJavaBeanProperty(final Object javaBean, final Property property, final Object propertyValue, final Tag customTag) {
        if (propertyValue == null || isEmpty(propertyValue)) {
            return null;
        } else {
            return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
        }
    }

    private boolean isEmpty(final Object propertyValue) {
        if (propertyValue instanceof Collection) {
            return ((Collection) propertyValue).isEmpty();
        } else if (propertyValue instanceof Map) {
            return ((Map) propertyValue).isEmpty();
        } else if (propertyValue instanceof String) {
            return ((String) propertyValue).isEmpty();
        }

        return propertyValue == ExpressionType.LITERAL;
    }
}