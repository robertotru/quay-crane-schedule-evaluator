package com.github.robertotru.qcse.sim;

import javax.annotation.Nonnull;

import static java.lang.StrictMath.signum;
import static java.util.Objects.requireNonNull;

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