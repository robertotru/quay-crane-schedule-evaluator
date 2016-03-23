package com.github.robertotru.qcse.sim.model;


import com.github.robertotru.qcse.sim.EventAbstraction;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

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
