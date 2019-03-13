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

package org.powerflows.dmn.engine.reader;


import org.powerflows.dmn.engine.model.decision.Decision;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * Decision reading contract.
 */
public interface DecisionReader {

    /**
     * Reads first found decision in the given stream.
     * Convenient for example for files defining single decision table.
     * @param inputStream stream with data
     * @return empty optional if no decision found
     */
    Optional<Decision> read(InputStream inputStream);

    /**
     * Finds single decision with given identifier in provided data.
     * @param inputStream stream with data
     * @param decisionId decision identifier
     * @return empty optional if no decision found
     */
    Optional<Decision> read(InputStream inputStream, String decisionId);

    /**
     * Reads all decisions present in given data.
     * @param inputStream stream with data
     * @return collection of decisions ordered as they are in source data
     */
    List<Decision> readAll(InputStream inputStream);

}
