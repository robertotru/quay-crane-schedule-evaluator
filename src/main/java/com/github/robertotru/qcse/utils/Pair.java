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
package com.github.robertotru.qcse.utils;

import lombok.Builder;
import lombok.Data;

/**
 *
 * @author Roberto Trunfio <roberto.trunfio@gmail.com>
 */
@Data
@Builder
public class Pair implements Cloneable, Comparable<Pair> {

    public final int i;
    public final int j;

    public Pair(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public static Pair pair(int i, int j) {
        return new Pair(i, j);
    }
    
    @Override
    public final Pair clone() {
        return new Pair(i, j);
    }

    @Override
    public int compareTo(Pair o) {
        if (Integer.compare(o.i, i) == 0 && Integer.compare(o.j, j) == 0) {
            return 0;
        } else if (o.i + o.j < i + j) {
            return -1;
        } else {
            return 1;
        }
    }

}