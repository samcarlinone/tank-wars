package com.samcarlinone.tankwars.math;

/**
 * Created by cobra on 2/18/2017.
 */
public class StaticRect extends Rect {
    public float xv;
    public float yv;

    public StaticRect(float x, float y, float w, float h) {
        super(x, y, w, h);
    }

    public void update() {
        x += xv;
        y += yv;
    }
}
