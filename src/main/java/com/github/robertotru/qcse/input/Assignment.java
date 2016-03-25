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

import static com.google.common.base.Preconditions.checkState;

/**
 *
 * @author Roberto Trunfio <roberto.trunfio@gmail.com>
 */
@Data
public class Assignment {

    private final int n;
    private final int m;
    private final QuayCraneDirections directions;
    private final int[] taskToQCAssignment;
    private final int[] assignedToQC;

    private int assigned;

    private int objectiveValue;
    private int lowerBound = Integer.MAX_VALUE;

    private int hashCode = 0;


    /**
     * Create a (partial) task-to-QC assignment
     *
     * @param n          number of tasks
     * @param m          number of QCs
     * @param directions QCs directions
     */
    public Assignment(final int n, final int m, final QuayCraneDirections directions) {
        this.n = n;
        this.m = m;
        this.objectiveValue = Integer.MAX_VALUE;
        this.directions = directions;
        this.taskToQCAssignment = new int[n];
        this.assignedToQC = new int[m];
        this.assigned = 0;
    }

    public final int numAssigned() {
        return assigned;
    }

    public void assign(final int task, final int QC) {
        if (taskToQCAssignment[task - 1] > 0)
            System.exit(-1);
        taskToQCAssignment[task - 1] = QC;
        assigned++;
    }

    protected void deassign(final int task) {
        taskToQCAssignment[task - 1] = 0;
        assigned--;
    }

    /**
     * Returns the QCs to which the task <em>i</em> is assigned.
     *
     * @param task
     * @return
     */
    public final int assignedTo(int task) {
        return taskToQCAssignment[task - 1];
    }

    public final boolean isAssigned(int task) {
        return taskToQCAssignment[task - 1] > 0;
    }

    public final boolean isAssigned(int task, int QC) {
        return assignedTo(task) == QC;
    }


    public final boolean isSingleton() {
        return taskToQCAssignment.length == assigned;
    }

    public final int getObjectiveValue() {
        return objectiveValue;
    }

    public final void setObjectiveValue(int objectiveValue) {
        checkState(isSingleton(), "Some tasks are not assigned, cannot assign the objective value");
        this.objectiveValue = objectiveValue;
    }

    @Override
    public final String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ObjValue: ").append(this.objectiveValue).append("\n")
                .append("Assignment: \n");

        for (int i = 0; i < m; i++) {
            stringBuilder.append("QC ").append(i + 1);
            if (directions.getLeftToRightDirections()[i]) {
                stringBuilder.append(" moves from left to right");
            } else {
                stringBuilder.append(" moves from right to left");
            }
        }
        stringBuilder.append("\n");
        for (int i = 1; i <= taskToQCAssignment.length; i++) {
            stringBuilder.append(" Task: ").append(i).append(" to QC : ").append(taskToQCAssignment[i - 1]).append("\n");
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

}