package com.github.robertotru.qcse.sim.model;

import com.github.robertotru.qcse.sim.EventAbstraction;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class ReleaseEvent extends EventAbstraction<QuayCrane> {

    public ReleaseEvent(@Nonnull final QuayCrane triggeredResource, @Nonnull final String name,
                        @Nonnegative final int scheduleTime, @Nonnegative final int duration) {
        super(triggeredResource, name, scheduleTime, duration);
    }

    @Override
    public void execute() {
        logOnExecute();

        triggeredResource.updateCompletionTime(fireTime());
        triggeredResource.setReleased();
    }
}
