package com.github.robertotru.qcse.utils;

import lombok.Data;

@Data
public class Pair implements Cloneable, Comparable<Pair> {

    public final int i;
    public final int j;

    public Pair(int i, int j) {
        this.i = i;
        this.j = j;
    }


    @Override
    public final Pair clone() {
        return new Pair(i, j);
    }

    @Override
    public int compareTo(Pair o) {
        if (Integer.compare(o.i, i) == 0 && Integer.compare(o.j, j) == 0) {
            return 0;
        } else if (o.i + o.j < i + j) {
            return -1;
        } else {
            return 1;
        }
    }

}