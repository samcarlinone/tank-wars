package com.samcarlinone.tankwars.graphics;

/**
 * Created by CARLINSE1 on 2/8/2017.
 */
public class Particle {
    public boolean alive = false;

    public float life = 0f;
    public float xv = 0f;
    public float yv = 0f;

    public float x0 = 0f;
    public float y0 = 0f;
    public float x1 = 0f;
    public float y1 = 0f;
    public float x2 = 0f;
    public float y2 = 0f;

    public Particle() {

    }

    public void init(float x, float y, float nXV, float nYV, float nLife) {
        life = nLife;

        xv = nXV;
        yv = nYV;

        double rot = Math.random() * 2 * Math.PI;
        float scale = (float)(Math.random()/2 + 0.5f) * 10f;

        x0 = (float)Math.cos(rot) * scale + x;
        y0 = (float)Math.sin(rot) * scale + y;
        x1 = (float)Math.cos(rot+Math.PI*2/3) * scale + x;
        y1 = (float)Math.sin(rot+Math.PI*2/3) * scale + y;
        x2 = (float)Math.cos(rot+Math.PI*4/3) * scale + x;
        y2 = (float)Math.sin(rot+Math.PI*4/3) * scale + y;

        alive = true;
    }
}
