package com.github.robertotru.qcse.input;

import lombok.Data;

@Data
public final class QuayCraneDirections {

    private final boolean[] leftToRightDirections;

    public QuayCraneDirections(final boolean[] left2RightDirections) {
        leftToRightDirections = new boolean[left2RightDirections.length];
        System.arraycopy(left2RightDirections, 0, leftToRightDirections, 0, left2RightDirections.length);
    }

}