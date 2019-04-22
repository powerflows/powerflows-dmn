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

import org.powerflows.dmn.io.yaml.model.YamlDecision;
import org.powerflows.dmn.io.yaml.model.rule.entry.YamlInputEntry;
import org.powerflows.dmn.io.yaml.model.rule.entry.YamlOutputEntry;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.SequenceNode;

import java.util.ArrayList;

public class CustomConstructor extends Constructor {
    public CustomConstructor() {
        super(YamlDecision.class);
        this.setPropertyUtils(new CustomPropertyUtils());
        this.yamlClassConstructors.put(NodeId.sequence, new ImprovedScalarConstruct());
    }

    class ImprovedScalarConstruct extends ConstructSequence {

        @Override
        public Object construct(final Node node) {
            final Object scalar;

            if (node.getType() == YamlInputEntry.class) {
                final SequenceNode sequenceNode = (SequenceNode) node;
                sequenceNode.setType(ArrayList.class);

                scalar = new YamlInputEntry(super.construct(sequenceNode));
            } else if (node.getType() == YamlOutputEntry.class) {
                final SequenceNode sequenceNode = (SequenceNode) node;
                sequenceNode.setType(ArrayList.class);

                scalar = new YamlOutputEntry(super.construct(sequenceNode));
            } else {
                scalar = super.construct(node);
            }

            return scalar;
        }
    }
}
