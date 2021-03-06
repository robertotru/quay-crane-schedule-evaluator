/*
 * Copyright 2016 Roberto Trunfio <roberto.trunfio@gmail.com>.
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
package com.github.robertotru.qcse.input;

import lombok.Data;

/**
 *
 * @author Roberto Trunfio <roberto.trunfio@gmail.com>
 */
@Data
public final class QuayCraneDirections {

    private final boolean[] leftToRightDirections;

    public QuayCraneDirections(final boolean... left2RightDirections) {
        leftToRightDirections = new boolean[left2RightDirections.length];
        System.arraycopy(left2RightDirections, 0, leftToRightDirections, 0, left2RightDirections.length);
    }

}