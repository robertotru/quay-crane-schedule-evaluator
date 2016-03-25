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
import com.github.robertotru.qcse.utils.Pair;
import static com.github.robertotru.qcse.utils.Pair.pair;
import static it.ozimov.cirneco.hamcrest.java7.AssertFluently.given;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Roberto Trunfio <roberto.trunfio@gmail.com>
 */
public class AssignmentExactEvaluatorTest {
    
    private AssignmentExactEvaluator assignmentExactEvaluator;
            
    @Before
    public void setUp() {
        final int[] cranes = {1,2};
        final int[] tasks = {1,2,3,4};
        final int[] craneInitialPosition = {1, 10};
        final int[] craneReleaseTime = {0,0};
        final int[] craneDeadlineTime = {Integer.MAX_VALUE, Integer.MAX_VALUE};
        final int[] tasksProcessingTime = {10, 10, 10, 10};
        final int[] tasksLocation = {1,1,10,10};
        final Pair[] tasksPrecedence = {pair(1,2), pair(3,4)};
        final int[][] t_taski_taskj = {{0, 0, 9, 9}, 
                                       {0, 0, 9, 9},
                                       {9, 9, 0, 0},
                                       {9, 9, 0, 0}};
        final int[][] t_k_taski = {{0, 0, 9, 9},
                                   {9, 9, 0, 0}};
        final int[][] delta_v_w = {{1},{1}};
        //always zero in the current instance
        final int[][][][] Delta_i_j_v_w = new int[tasks.length][tasks.length][cranes.length][cranes.length];
                                            
       assignmentExactEvaluator = new AssignmentExactEvaluator(cranes,
                                    tasks,
                                    craneInitialPosition,
                                    craneReleaseTime,
                                    craneDeadlineTime,
                                    tasksProcessingTime,
                                    tasksLocation,
                                    tasksPrecedence,
                                    t_taski_taskj,
                                    t_k_taski,
                                    Delta_i_j_v_w,
                                    delta_v_w);
    }
    
    @Test
    public void testGetMakespan() {
        //Arrange
        final int n = 4;
        final int m = 2;
        final QuayCraneDirections directions = new QuayCraneDirections(true, true);
        final Assignment assignment = new Assignment(n, m, directions);
        
        //Act
        final int makespan = assignmentExactEvaluator.getMakespan(assignment);
        
        //Assert
        given(makespan).withReason("I expect each quay crane to process a bay")
                .assertIs(equalTo(20));
    }
    
}
