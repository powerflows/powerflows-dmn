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
import org.powerflows.dmn.io.AbstractDecisionReader;
import org.powerflows.dmn.io.yaml.model.YamlDecision;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;


public class YamlReader extends AbstractDecisionReader {

    private final Yaml yaml = new Yaml(new YamlDecisionConstructor());
    private final YamlDecsionConverter converter = new YamlDecsionConverter();

    @Override
    public List<Decision> readAll(final InputStream inputStream) {
        return StreamSupport.stream(yaml.loadAll(inputStream).spliterator(), true)
                .map(o -> (YamlDecision) o)
                .map(converter::from)
                .collect(toList());
    }

    @Override
    public Decision read(final InputStream inputStream) {
        return converter.from(yaml.load(inputStream));
    }

}
