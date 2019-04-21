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
package org.powerflows.dmn.io.xml;

import lombok.extern.slf4j.Slf4j;
import org.powerflows.dmn.engine.model.decision.Decision;
import org.powerflows.dmn.engine.reader.DecisionReadException;
import org.powerflows.dmn.engine.reader.DecisionReader;
import org.powerflows.dmn.io.xml.model.XMLDecision;
import org.powerflows.dmn.io.xml.model.XMLDefinitions;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public class XmlDecisionReader implements DecisionReader {

    private final XMLInputFactory factory;
    private final JAXBContext decisionContext;
    private final XMLDecisionConverter converter = new XMLDecisionConverter();
    private final boolean strict;

    public XmlDecisionReader() {
        this(false);
    }

    public XmlDecisionReader(final boolean strict) {
        this.factory = XMLInputFactory.newInstance();
        this.strict = strict;

        try {
            this.decisionContext = JAXBContext.newInstance(XMLDefinitions.class);
        } catch (JAXBException e) {
            throw new DecisionReadException("Unable to instantiate JAXB context", e);
        }
    }

    @Override
    public Optional<Decision> read(final InputStream inputStream) {
        return readAll(inputStream).stream().findFirst();
    }

    @Override
    public Optional<Decision> read(final InputStream inputStream, final String decisionId) {
        return readAll(inputStream).stream().filter(decision -> Objects.equals(decision.getId(), decisionId)).findFirst();
    }

    @Override
    public List<Decision> readAll(final InputStream inputStream) {
        try {
            final List<XMLDecision> results = new ArrayList<>();
            doReadDecisionsStream(factory.createXMLStreamReader(inputStream), results::add);

            return convert(results);
        } catch (XMLStreamException | JAXBException e) {
            throw new DecisionReadException("Unable to read decision", e);
        }
    }

    private List<Decision> convert(final List<XMLDecision> results) {
        return results.stream()
                .filter(d -> d.getDecisionTable() != null)
                .map(converter::from)
                .collect(Collectors.toList());
    }

    private Unmarshaller getUnmarshaller() {
        try {
            final Unmarshaller decisionUnmarshaller = decisionContext.createUnmarshaller();
            if (strict) {
                decisionUnmarshaller.setEventHandler(new DefaultValidationEventHandler());
            }

            return decisionUnmarshaller;
        } catch (JAXBException e) {
            throw new DecisionReadException("Error creating unmarshaller", e);
        }
    }

    private void doReadDecisionsStream(final XMLStreamReader r,
                                       final Consumer<XMLDecision> decisionConsumer) throws XMLStreamException, JAXBException {
        final Unmarshaller decisionUnmarshaller = getUnmarshaller();
        final XMLDefinitions definitions = (XMLDefinitions) JAXBIntrospector
                .getValue(decisionUnmarshaller.unmarshal(r, XMLDefinitions.class));
        log.trace("Read XML document: {}", definitions);
        definitions.getDecisions().forEach(decisionConsumer);

        r.close();
    }
}