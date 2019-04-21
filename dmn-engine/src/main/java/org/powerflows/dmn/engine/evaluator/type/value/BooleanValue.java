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

package org.powerflows.dmn.engine.evaluator.type.value;

import java.util.List;

/**
 * {@link Boolean} value holder.
 */
public class BooleanValue extends AbstractSpecifiedTypeValues<Boolean> {

    public BooleanValue(final Boolean value) {
        super(value);
    }

    public BooleanValue(final List<Boolean> values) {
        super(values);
    }
}
