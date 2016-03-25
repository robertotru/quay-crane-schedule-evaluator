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
package com.github.robertotru.qcse;

import com.github.robertotru.qcse.input.Assignment;
import com.github.robertotru.qcse.input.QuayCraneDirections;
import com.github.robertotru.qcse.sim.Event;
import com.github.robertotru.qcse.sim.model.QuayCrane;
import com.github.robertotru.qcse.utils.Pair;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import static java.lang.StrictMath.max;
import java.util.Objects;
import static java.util.Objects.requireNonNull;

/**
 *
 * @author Roberto Trunfio <roberto.trunfio@gmail.com>
 */
@Slf4j
public final class AssignmentExactEvaluator {

    private final QuayCrane[] cranes;
    private final int upperBound = Integer.MAX_VALUE;
    private final ArrayList<int[]> predecessors;
    private final ArrayList<int[]> successors;
    private final int[] Cranes;
    private final int[] Tasks;
    // CRANE DATA
    private final int m;
    private final int[] craneInitialPosition;
    private final int[] craneReleaseTime;
    private final int[] craneDeadlineTime;
    // TASK DATA
    private final int n;
    private final int[] TasksProcessingTime;
    private final int[] TasksLocation;
    private final Pair[] TasksPrecedence;
    private final int[][] t_taski_taskj;
    private final int[][] t_k_taski;
    // MIXED DATA
    private final int[][][][] Delta_i_j_v_w;
    private final int[][] delta_v_w;
    // SIMULATION DATA
    private final PriorityQueue<Event> eventQueue = new PriorityQueue<>();
    // SIMULATION MODEL DATA
    private final ArrayList<QuayCrane> enabledList;
    private Assignment currentSolution;

    private int task_i, task_j;

    private int currentTime;
    
    private final boolean[] completed;
    private final boolean[] toBePerformed;
    private final int[] C;
    private final int[] relatedCrane;
    
    private boolean infeasible;
    private boolean precedence_constraint_violated;
    private boolean safety_constraint_violated;

    public AssignmentExactEvaluator(final int[] cranes,
                                    final int[] tasks,
                                    final int[] craneInitialPosition,
                                    final int[] craneReleaseTime,
                                    final int[] craneDeadlineTime,
                                    final int[] tasksProcessingTime,
                                    final int[] tasksLocation,
                                    final Pair[] tasksPrecedence,
                                    final int[][] t_taski_taskj,
                                    final int[][] t_k_taski,
                                    final int[][][][] Delta_i_j_v_w,
                                    final int[][] delta_v_w) {


        n = tasks.length;
        m = cranes.length;


        this.Cranes = new int[cranes.length];
        System.arraycopy(cranes, 0, this.Cranes, 0, cranes.length);

        this.Tasks = new int[tasks.length];
        System.arraycopy(tasks, 0, this.Tasks, 0, tasks.length);

        this.craneInitialPosition = new int[craneInitialPosition.length];
        System.arraycopy(craneInitialPosition, 0, this.craneInitialPosition, 0, craneInitialPosition.length);

        this.craneReleaseTime = new int[craneReleaseTime.length];
        System.arraycopy(craneReleaseTime, 0, this.craneReleaseTime, 0, craneReleaseTime.length);

        this.craneDeadlineTime = new int[craneDeadlineTime.length];
        System.arraycopy(craneDeadlineTime, 0, this.craneDeadlineTime, 0, craneDeadlineTime.length);

        this.TasksLocation = new int[tasksLocation.length];
        System.arraycopy(tasksLocation, 0, this.TasksLocation, 0, tasksLocation.length);

        this.TasksProcessingTime = new int[tasksProcessingTime.length];
        System.arraycopy(tasksProcessingTime, 0, this.TasksProcessingTime, 0, tasksProcessingTime.length);

        this.t_k_taski = new int[t_k_taski.length][t_k_taski[0].length];
        for (int i = 0; i < t_k_taski.length; i++) {
            System.arraycopy(t_k_taski[i], 0, this.t_k_taski[i], 0, t_k_taski[0].length);
        }

        this.t_taski_taskj = new int[t_taski_taskj.length][t_taski_taskj[0].length];
        for (int i = 0; i < t_taski_taskj.length; i++) {
            System.arraycopy(t_taski_taskj[i], 0, this.t_taski_taskj[i], 0, t_taski_taskj[0].length);
        }

        this.Delta_i_j_v_w = new int[Delta_i_j_v_w.length][Delta_i_j_v_w[0].length][Delta_i_j_v_w[0][0].length][Delta_i_j_v_w[0][0][0].length];
        for (int i = 0; i < Delta_i_j_v_w.length; i++) {
            for (int j = 0; j < Delta_i_j_v_w[0].length; j++) {
                for (int k = 0; k < Delta_i_j_v_w[0][0].length; k++) {
                    System.arraycopy(Delta_i_j_v_w[i][j][k], 0, this.Delta_i_j_v_w[i][j][k], 0, Delta_i_j_v_w[0][0][0].length);
                }
            }
        }

        this.delta_v_w = new int[delta_v_w.length][delta_v_w[0].length];
        for (int i = 0; i < delta_v_w.length; i++) {
            System.arraycopy(delta_v_w[i], 0, this.delta_v_w[i], 0, delta_v_w[0].length);
        }


        this.predecessors = new ArrayList<>(n);
        this.successors = new ArrayList<>(n);
        this.TasksPrecedence = new Pair[tasksPrecedence.length];
        for (int i = 0; i < tasksPrecedence.length; i++) {
            this.TasksPrecedence[i] = tasksPrecedence[i].clone();
        }
        for (int i = 1; i <= n; i++) {
            final List<Integer> pred = new ArrayList<>();
            final List<Integer> succ = new ArrayList<>();
            for (Pair p : tasksPrecedence) {
                if (p.i == i) {
                    succ.add(p.j);
                } else if (p.j == i) {
                    pred.add(p.i);
                }
            }

            int[] p = new int[pred.size()];
            int[] s = new int[succ.size()];
            for (int h = 0; h < p.length; h++) {
                p[h] = pred.get(h);
            }
            for (int h = 0; h < s.length; h++) {
                s[h] = succ.get(h);
            }
            predecessors.add(p);
            successors.add(s);
        }


        this.cranes = new QuayCrane[m];

        for (int q = 1; q <= m; q++) {
            this.cranes[q - 1] = new QuayCrane(q, craneReleaseTime[q - 1], n, this);
        }

        completed = new boolean[n];
        C = new int[tasks.length];
        relatedCrane = new int[n];
        toBePerformed = new boolean[n];

        enabledList = new ArrayList<>(m);
    }


    /**
     * Compute the makespan of the given solution (ir can be a partial solution, e.g. if you need a lower bound=
     */
    public int getMakespan(final Assignment solution) {
        int objFun = Integer.MAX_VALUE;

        currentSolution = solution;

        objFunEvaluator(solution, solution.getDirections());
        if (!infeasible) {
            int maxE = 0;
            for (QuayCrane k : cranes) {
                maxE = max(k.currentCompletionTime(), maxE);
            }
            objFun = maxE; // KIMPARK OBJ FUN
        }
        solution.setObjectiveValue(objFun);
        return objFun;

    }


    public int m() {
        return m;
    }

    public int[] Q() {
        return Cranes;
    }

    public int n() {
        return n;
    }

    public int[] Omega() {
        return Tasks;
    }

    private int p(int i) {
        return TasksProcessingTime[i - 1];
    }

    public int l(int i) {
        return TasksLocation[i - 1];
    }

    public final int l_k(int k) {
        return craneInitialPosition[k - 1];
    }

    public final int t(int i, int j) {
        return t_taski_taskj[i - 1][j - 1];
    }

    public final int t_k(int i, int k) {
        return t_k_taski[i - 1][k - 1];
    }

    public final int r(int k) {
        return craneReleaseTime[k - 1];
    }

    public final int d(int k) {
        return craneDeadlineTime[k - 1];
    }

    public final int Delta(int i, int j, int v, int w) {
        return Delta_i_j_v_w[i - 1][j - 1][v - 1][w - 1];
    }

    public final int delta(int v, int w) {
        return delta_v_w[v - 1][w - 1];
    }

    private int getC(int task) {
        return C[task - 1];
    }

    private void setC(int task, int val) {
        C[task - 1] = val;
    }

    /*
     * The first element has to control the size;
     */
    private void arrangeScheduleExceptFirst(int[] schedule, boolean direction) { // true, from left to right
        int size = schedule[0] + 1;
        if (direction) {
            java.util.Arrays.sort(schedule, 1, size);
        } else {
            int temp;
            for (int i = 1; i < size / 2; i++) {
                temp = schedule[size - 1 - i];
                schedule[size - 1 - i] = schedule[i];
                schedule[i] = temp;
            }
            int to = -1;
            int loc = TasksLocation[schedule[to + 1] - 1];
            int start = 0;
            for (int i = 2; i < size; i++) {
                to++;
                int locI = TasksLocation[schedule[i] - 1];
                if (loc != locI) {
                    // Riordina i task tra start ed il predecessore di current
                    if (start < to) {
                        java.util.Arrays.sort(schedule, start, to + 1);
                    }
                    loc = locI;
                    start = i;

                } else if ((loc == locI && i == size - 1)) {
                    if (start < ++to) {
                        java.util.Arrays.sort(schedule, start, to + 1);
                    }
                }
            }
        }
    }

    public void controller() {
        currentTime = 0;
        eventQueue.clear();

        for (final QuayCrane k : cranes) {
            k.init();
        }
        while (true) {
            if (eventQueue.isEmpty()) // no more events = no more work to do
            {
                break;
            }
            // force firing of most imminent Event
            final Event e = eventQueue.poll();
            currentTime = e.fireTime();

            if (currentTime >= upperBound) {
                infeasible = true;
                break;
            }
            e.execute();

            if (!eventQueue.isEmpty()) {
                if (eventQueue.peek().fireTime() == now()) {
                    continue;
                }
            }

            do {
                checkEnabled();
            } while (selectNext());
        }
        infeasible = endSimCheckUnfeasibility();
    }

    public final int now() {
        return currentTime;
    }

    public final void scheduleEvent(@Nonnull final Event event) {
        eventQueue.add(requireNonNull(event));
    }

    private boolean endSimCheckUnfeasibility() {
        int now = now();
        for (final QuayCrane k : cranes) {
            if (!k.isScheduleCompleted() || k.currentCompletionTime() > d(k.id())
                    || max(now(), k.currentCompletionTime()) >= upperBound) {
                return true;
            }
        }
        return false;
    }

    private void checkEnabled() {
        enabledList.clear();

        for (QuayCrane k : cranes) {
            if (k.isReleased() && !k.isWorking() && !k.isScheduleCompleted()) {
                task_i = k.getCurrentTask();
                precedence_constraint_violated = false;
                // EVALUATING PRECEDENCE CONSTRAINTS
                task_j = max(1, task_i - 1);
                if (l(task_j) == l(task_i) && task_j < task_i) {

                    if (!completed[task_j - 1]) {
                        precedence_constraint_violated = true;
                    }
                }

                if (precedence_constraint_violated) {
                    continue;
                }

                safety_constraint_violated = false;
                for (QuayCrane v : cranes) {
                    if (v.isWorking() && v != k) {
                        if (Delta(task_i, v.getCurrentTask(), k.id(), v.id()) > 0D) {
                            safety_constraint_violated = true;
                            break;
                        }
                    }
                }
                if (safety_constraint_violated) {
                    continue;
                }

                enabledList.add(k);
            }
        }
    }

    private boolean selectNext() {
        int minVal = Integer.MAX_VALUE;
        QuayCrane nextCrane = null;
        int now = now();
        for (final QuayCrane k : enabledList) {
            // Computing residual time before to start
            // COMPUTING TRAVEL TIME
            task_i = k.getCurrentTask();
            int res_travel_time;
            if (k.isFirstTask()) { // Crane first task
                res_travel_time = max(r(k.id()) + t_k(task_i, k.id()) - now, 0);
            } else {
                res_travel_time = max(k.currentCompletionTime() + t(k.getPreviousTask(), task_i) - now, 0);
            }

            int res_security_delay = 0, delay;
            for (int aTask_j : Tasks) {
                if (currentSolution.isAssigned(aTask_j)) {
                    if (aTask_j != k.getCurrentTask()) {
                        if (completed[aTask_j - 1] && relatedCrane[aTask_j - 1] != k.id()) {
                            delay = getC(aTask_j) + Delta(aTask_j, task_i, relatedCrane[aTask_j - 1], k.id()) - now;
                            if (delay > res_security_delay) {
                                res_security_delay = delay;
                            }
                        }
                    }
                }
            }

            int val = (max(res_security_delay, res_travel_time) + p(task_i));
            if (val < minVal) {
                minVal = val;
                nextCrane = k;
            }
        }

        if (nextCrane == null) {
            return false;
        } else {
            int completionTime = currentTime + minVal;
            setC(nextCrane.getCurrentTask(), completionTime);
            nextCrane.updateCompletionTime(completionTime);
            toBePerformed[nextCrane.getCurrentTask() - 1] = true;
            nextCrane.setWorking();
            nextCrane.performCurrentTask(minVal);
            enabledList.remove(nextCrane);
            return true;
        }
    }

    private void objFunEvaluator(final Assignment solution, final QuayCraneDirections quayCraneDirections) {
        for (final QuayCrane q : cranes) {
            q.reset();
        }
        for (int i = 1; i <= n; i++) {
            C[i - 1] = Integer.MAX_VALUE;
            completed[i - 1] = false;
            toBePerformed[i - 1] = false;

            if (solution.isAssigned(i)) {
                int crane = solution.assignedTo(i);
                cranes[crane - 1].addTask(i);
                relatedCrane[i - 1] = crane;
            }
        }

        currentTime = 0;
        infeasible = false;

        enabledList.clear();

        for (final QuayCrane k : cranes) {
            if (k.scheduleIsEmpty()) {
                k.setScheduleCompleted();
            } else {
                if (!quayCraneDirections.getLeftToRightDirections()[k.id() - 1]) {
                    k.arrangeSchedule(false);
                }
            }
        }

        controller();
    }

    public void setTaskCompleted(final int taskId) {
        this.completed[taskId - 1] = true;
    }

    private final class RightToLeftTaskComparator implements java.util.Comparator<Integer> {

        @Override
        public int compare(final Integer t1, final Integer t2) {
            if (t1 < t2) {
                if (l(t1) == l(t2) && t1 < t2) {
                    return -1;
                }
                return 1;
            } else if (Objects.equals(t1, t2)) {
                return 0;
            } else {
                return -1;
            }
        }
    }


}
