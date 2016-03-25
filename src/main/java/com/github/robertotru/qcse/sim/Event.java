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
package com.github.robertotru.qcse.sim;

import javax.annotation.Nonnull;

import static java.lang.StrictMath.signum;
import static java.util.Objects.requireNonNull;

/**
 *
 * @author Roberto Trunfio <roberto.trunfio@gmail.com>
 */
public interface Event<T extends Resource> extends Comparable<Event> {


    /**
     * Return when the event was scheduled
     */
    int scheduleTime();

    /**
     * Return the amount of time units is required from the scheduleTime to fire the event
     */
    int duration();

    /**
     * Return when the event has to be executed
     */
    int fireTime();

    /**
     * Return the name of the event
     */
    @Nonnull
    String name();

    /**
     * Return the resource that has to be triggered when the event is fired
     */
    @Nonnull
    T triggeredResource();

    /**
     * Something to be executed at fire-time
     */
    default void execute() {
    }

    @Override
    default int compareTo(@Nonnull final Event e) {
        return (int) signum(this.fireTime() - requireNonNull(e).fireTime());
    }

}