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

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 *
 * @author Roberto Trunfio <roberto.trunfio@gmail.com>
 */
@Slf4j
public abstract class EventAbstraction<T extends Resource> implements Event<T> {

    protected final T triggeredResource;
    protected int scheduleTime;
    protected int duration;
    protected String name;


    protected EventAbstraction(@Nonnull final T triggeredResource, @Nonnull final String name,
                               @Nonnegative final int scheduleTime, @Nonnegative final int duration) {
        checkArgument(scheduleTime >= 0, "The event cann bot scheduled in the past. Given schedule time %d", scheduleTime);
        checkArgument(duration >= 0, "The event cannot be executed with a negative duration. Given duration %d", duration);

        this.triggeredResource = requireNonNull(triggeredResource);
        this.name = name;
        this.scheduleTime = scheduleTime;
        this.duration = duration;
    }

    @Override
    public final
    @Nonnegative
    int scheduleTime() {
        return scheduleTime;
    }

    @Override
    public final
    @Nonnegative
    int duration() {
        return duration;
    }

    @Override
    public final
    @Nonnegative
    int fireTime() {
        return scheduleTime + duration;
    }

    @Override
    public final
    @Nonnull
    String name() {
        return name;
    }

    @Override
    public final
    @Nonnull
    T triggeredResource() {
        return triggeredResource;
    }

    protected final void logOnExecute() {
        log.info("Event {} executed at time {}", name(), fireTime());
    }
}