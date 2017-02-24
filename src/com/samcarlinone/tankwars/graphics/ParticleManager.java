package com.samcarlinone.tankwars.graphics;

import com.samcarlinone.tankwars.util.BufferUtils;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Created by CARLINSE1 on 2/8/2017.
 */
public class ParticleManager {
    private final int MAX_PARTICLES = 2048;

    private Particle[] parts;
    private float[] verts;
    private int numAlive = 0;
    private int bufferSize = 0;
    private int vao, vbo;

    public ParticleManager() {
        parts = new Particle[MAX_PARTICLES];

        for(int i=0; i<MAX_PARTICLES; i++){
            parts[i] = new Particle();
        }

        vao = glGenVertexArrays();
        vbo = glGenBuffers();
    }

    public void reset() {
        for(int i=0; i<MAX_PARTICLES; i++){
            parts[i].alive = false;
        }

        update();
    }

    public void update() {
        //Update particles, check if alive
        numAlive = 0;

        for(int i=0; i<MAX_PARTICLES; i++) {
            if(parts[i].alive) {
                parts[i].life -= 1;

                if(parts[i].life < 0) {
                    parts[i].alive = false;
                    continue;
                }

                parts[i].x0 += parts[i].xv;
                parts[i].y0 += parts[i].yv;
                parts[i].x1 += parts[i].xv;
                parts[i].y1 += parts[i].yv;
                parts[i].x2 += parts[i].xv;
                parts[i].y2 += parts[i].yv;

                numAlive ++;
            }
        }

        //If any living particles create buffer
        if(numAlive > 0) {
            bufferSize = MAX_PARTICLES * 6;

            verts = new float[bufferSize];
            int index = 0;

            for (int i = 0; i < MAX_PARTICLES; i++) {
                if (parts[i].alive) {
                    verts[index] = parts[i].x0;
                    verts[index + 1] = parts[i].y0;
                    verts[index + 2] = parts[i].x1;
                    verts[index + 3] = parts[i].y1;
                    verts[index + 4] = parts[i].x2;
                    verts[index + 5] = parts[i].y2;

                    index += 6;
                } else {
                    verts[index] = 0f;
                    verts[index + 1] = 0f;
                    verts[index + 2] = 0f;
                    verts[index + 3] = 0f;
                    verts[index + 4] = 0f;
                    verts[index + 5] = 0f;
                }
            }

            glBindVertexArray(vao);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(verts), GL_DYNAMIC_DRAW);
            glVertexAttribPointer(Shader.VERT_ATTRIB, 2, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(Shader.VERT_ATTRIB);
        }
    }

    private void bind() {
        glBindVertexArray(vao);
    }

    private void draw() {
        glDrawArrays(GL_TRIANGLES, 0, bufferSize);
    }

    public void render() {
        if(numAlive == 0) {
            return;
        }

        bind();
        draw();
    }

    public void spawn(float x, float y, int num) {
        int index = 0;

        while(num > 0 && index < MAX_PARTICLES) {
            if(!parts[index].alive) {
                parts[index].init(x, y, (float)Math.random()-0.5f, (float)Math.random()-0.5f, 60f + (float)Math.random()*20f);

                num--;
            }

            index++;
        }
    }

    public void spawnAngled(float x, float y, int num, float angleRangeStart, float angleRangeEnd, float maxPow, float life) {
        int index = 0;

        while(num > 0 && index < MAX_PARTICLES) {
            if(!parts[index].alive) {
                float angle = (angleRangeEnd-angleRangeStart)*(float)Math.random() + angleRangeStart;
                float pow = ((float)Math.random()/2f + 0.5f)*maxPow;

                parts[index].init(x, y, (float)Math.cos(angle)*pow, (float)Math.sin(angle)*pow, life*((float)Math.random()/2f+0.5f));

                num--;
            }

            index++;
        }
    }
}
