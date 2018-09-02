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

import org.powerflows.dmn.engine.model.decision.Decision;
import org.powerflows.dmn.engine.reader.DecisionWriteException;
import org.powerflows.dmn.engine.reader.DecisionWriter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class YamlDecisionWriter implements DecisionWriter {
    private final Yaml yaml;
    private final YamlDecisionConverter converter = new YamlDecisionConverter();

    public YamlDecisionWriter() {
        final DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setPrettyFlow(true);
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        yaml = new Yaml(new CustomRepresenter(), dumperOptions);
    }

    @Override
    public void write(final Decision decision, final OutputStream outputStream) {
        try {
            yaml.dump(converter.to(decision), createWriter(outputStream));
        } catch (final Exception e) {
            throw new DecisionWriteException("Unable to write decision: " + decision, e);
        }
    }

    @Override
    public void writeAll(final Iterable<Decision> decisions, final OutputStream outputStream) {
        try {
            yaml.dumpAll(
                    StreamSupport.stream(decisions.spliterator(), false)
                            .map(converter::to)
                            .collect(Collectors.toList())
                            .iterator(),
                    createWriter(outputStream));
        } catch (final Exception e) {
            throw new DecisionWriteException("Unable to write decision: " + decisions, e);
        }
    }

    private Writer createWriter(final OutputStream outputStream) {
        return new BufferedWriter(new OutputStreamWriter(outputStream));
    }
}
