package com.samcarlinone.tankwars.math;

/**
 * Created by CARLINSE1 on 2/7/2017.
 */
public class Vector4f {

    public float x;
    public float y;
    public float z;
    public float w;

    public Vector4f() {
    }

    public Vector4f(float newX, float newY, float newZ, float newW) {
        x = newX;
        y = newY;
        z = newZ;
        w = newW;
    }

    public final void set(Vector4f v) {
        x = v.x;
        y = v.y;
        z = v.z;
        w = v.w;
    }

    public final void set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

}
