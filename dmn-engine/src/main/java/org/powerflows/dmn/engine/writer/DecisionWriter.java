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
package org.powerflows.dmn.engine.writer;

import org.powerflows.dmn.engine.model.decision.Decision;

import java.io.OutputStream;

/**
 * Defines decision writing contract.
 */
public interface DecisionWriter {
    /**
     * Writes single Decision.
     *
     * @param decision to write
     * @param outputStream target where decision is written to
     */
    void write(Decision decision, OutputStream outputStream);

    /**
     * Writes multiple decisions.
     *
     * @param decisions collection of decisions to write. Writing is performed in the order provided by {@link Iterable}
     * @param outputStream target where decisions are written to
     */
    void writeAll(Iterable<Decision> decisions, OutputStream outputStream);
}
