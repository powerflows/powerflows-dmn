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
import org.powerflows.dmn.engine.reader.DecisionReadException;
import org.powerflows.dmn.engine.reader.DecisionReader;
import org.powerflows.dmn.io.yaml.model.YamlDecision;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;


public class YamlDecisionReader implements DecisionReader {

    public static final String UNABLE_TO_READ_FROM_STREAM = "Unable to read from stream";
    private final Yaml yaml = new Yaml(new CustomConstructor());
    private final YamlDecisionConverter converter = new YamlDecisionConverter();

    @Override
    public List<Decision> readAll(final InputStream inputStream) {
        try {
            return StreamSupport.stream(yaml.loadAll(inputStream).spliterator(), true)
                    .map(o -> (YamlDecision) o)
                    .map(converter::from)
                    .collect(toList());
        } catch (final Exception e) {
            throw new DecisionReadException(UNABLE_TO_READ_FROM_STREAM, e);
        }
    }

    @Override
    public Optional<Decision> read(final InputStream inputStream) {
        try {
            return Optional.of((YamlDecision) yaml.load(inputStream)).map(converter::from);
        } catch (final Exception e) {
            throw new DecisionReadException(UNABLE_TO_READ_FROM_STREAM, e);
        }
    }

    @Override
    public Optional<Decision> read(final InputStream inputStream, final String decisionId) {
        try {
            return StreamSupport.stream(yaml.loadAll(inputStream).spliterator(), true)
                    .map(o -> (YamlDecision) o)
                    .filter(d -> decisionId.equals(d.getId()))
                    .map(converter::from)
                    .findFirst();
        } catch (final Exception e) {
            throw new DecisionReadException(UNABLE_TO_READ_FROM_STREAM, e);
        }
    }

}
