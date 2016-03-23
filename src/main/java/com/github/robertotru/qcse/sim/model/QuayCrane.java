package com.github.robertotru.qcse.sim.model;


import com.github.robertotru.qcse.AssignmentExactEvaluator;
import com.github.robertotru.qcse.InstanceDataContainer;
import com.github.robertotru.qcse.sim.Event;
import com.github.robertotru.qcse.sim.Resource;

import static com.google.common.base.Preconditions.checkArgument;

public class QuayCrane implements Comparable<QuayCrane>, Resource {

    private final int id;
    private final int[] schedule;
    /*
     * Crane completion time
     */
    int currentCompletionTime;
    int expectedE;
    /*
     * Crane utilization
     */
    double U;
    int minTravelTime;
    private int releaseTime;
    private int schedule_size;
    private boolean released;
    private boolean scheduleCompleted;
    private boolean working;
    private int currentTask;
    private AssignmentExactEvaluator assignmentExactEvaluator;

    public QuayCrane(final int id, final int releaseTime, final int numTasks, final AssignmentExactEvaluator assignmentExactEvaluator) {
        this.assignmentExactEvaluator = assignmentExactEvaluator;
        this.id = id;
        this.releaseTime = releaseTime;
        schedule = new int[numTasks];
    }

    private static void arrangeSchedule(final int[] schedule, final int size,
                                        final boolean leftToRight) {
        if (leftToRight) {
            java.util.Arrays.sort(schedule, 0, size + 1);
        } else {
            for (int i = 0; i < size / 2; i++) {
                int temp = schedule[size - 1 - i];
                schedule[size - 1 - i] = schedule[i];
                schedule[i] = temp;
            }
            int to = -1;
            int loc = InstanceDataContainer.l(schedule[to + 1]);
            int start = 0;
            for (int i = 1; i < size; i++) {
                to++;
                if (loc != InstanceDataContainer.l(schedule[i])) {
                    // Riordina i task tra start ed il predecessore di current
                    if (start < to) {
                        java.util.Arrays.sort(schedule, start, to + 1);
                    }
                    loc = InstanceDataContainer.l(schedule[i]);
                    start = i;
                } else if ((loc == InstanceDataContainer.l(schedule[i]) && i == size - 1)) {
                    if (start < ++to) {
                        java.util.Arrays.sort(schedule, start, to + 1);
                    }
                }
            }
        }
    }

    public void performCurrentTask(final int time) {
        final int now = this.assignmentExactEvaluator.now();

        final Event event = new EndTaskEvent(this, String.format("QC %d is released", id),
                now, time);

        assignmentExactEvaluator.scheduleEvent(event);
    }

    /**
     * @return
     */
    public boolean isFirstTask() {
        if (currentTask == 1) {
            return true;
        } else {
            return false;
        }
    }

    protected void setTaskCompleted() {
        assignmentExactEvaluator.setTaskCompleted(getCurrentTask());
        setNotWorking();
        if (!setNextTask()) {
            setScheduleCompleted();
        }
    }

    private boolean setNextTask() {
        currentTask++;
        if (currentTask > schedule_size) {
            return false;
        } else {
            return true;
        }
    }

    public int getCurrentTask() {
        return schedule[currentTask - 1];
    }

    public int getPreviousTask() {
        return schedule[currentTask - 2];
    }

    public void reset() {
        schedule_size = 0;

        currentTask = 1;
        working = false;
        released = false;
        currentCompletionTime = 0;
        if (currentTask == schedule_size) {
            scheduleCompleted = true;
        } else {
            scheduleCompleted = false;
        }
    }

    public void init() {
        final int now = this.assignmentExactEvaluator.now();

        final Event event = new ReleaseEvent(this, String.format("QC %d completes task %d", id, getCurrentTask()),
                now, releaseTime);
        assignmentExactEvaluator.scheduleEvent(event);
    }

    @Override
    public int compareTo(QuayCrane k) {
        return (int) Math.signum(this.currentCompletionTime - k.currentCompletionTime);
    }

    public boolean scheduleIsEmpty() {
        return schedule_size == 0;
    }

    public void addTask(int task) {
        schedule[schedule_size++] = task;
    }

    public boolean isReleased() {
        return released;
    }

    public boolean isWorking() {
        return working;
    }

    public boolean isScheduleCompleted() {
        return scheduleCompleted;
    }

    public void setReleased() {
        released = true;
    }

    public void setWorking() {
        working = true;
    }

    public void setNotWorking() {
        working = false;
    }

    public void setScheduleCompleted() {
        scheduleCompleted = true;
    }

    public int currentCompletionTime() {
        return currentCompletionTime;
    }

    public void updateCompletionTime(final int updatedCompletionTime) {
        checkArgument(updatedCompletionTime >= currentCompletionTime, "Trying to set the completion time for QC %d to value in the pasy",
                this.id);

        this.currentCompletionTime = updatedCompletionTime;
    }

    @Override
    public int id() {
        return id;
    }

    public void arrangeSchedule(final boolean leftToRight) {
        arrangeSchedule(schedule, schedule_size, leftToRight);
    }

}