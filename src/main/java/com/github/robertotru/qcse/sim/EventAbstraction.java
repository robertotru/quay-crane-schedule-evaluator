package com.github.robertotru.qcse.sim;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

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