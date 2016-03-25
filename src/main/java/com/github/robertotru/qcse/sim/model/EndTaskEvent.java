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
package com.github.robertotru.qcse.sim.model;


import com.github.robertotru.qcse.sim.EventAbstraction;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 *
 * @author Roberto Trunfio <roberto.trunfio@gmail.com>
 */
@Slf4j
public class EndTaskEvent extends EventAbstraction<QuayCrane> {

    public EndTaskEvent(@Nonnull final QuayCrane triggeredResource, @Nonnull final String name,
                        @Nonnegative final int scheduleTime, @Nonnegative final int duration) {
        super(triggeredResource, name, scheduleTime, duration);
    }

    @Override
    public void execute() {
        logOnExecute();
        triggeredResource.setTaskCompleted();
    }
}
