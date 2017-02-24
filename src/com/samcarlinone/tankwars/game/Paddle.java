package com.samcarlinone.tankwars.game;

import com.samcarlinone.tankwars.Main;
import com.samcarlinone.tankwars.graphics.BasicMesh;
import com.samcarlinone.tankwars.graphics.Shader;
import com.samcarlinone.tankwars.math.Matrix4f;
import com.samcarlinone.tankwars.math.StaticRect;
import com.samcarlinone.tankwars.math.Vector3f;
import com.samcarlinone.tankwars.util.KeyboardInput;

/**
 * Created by CARLINSE1 on 2/8/2017.
 */
public class Paddle {
    private BasicMesh mesh;

    public StaticRect rect = new StaticRect(0, 0, 15, 75);
    public float speed = 5f;

    private int up, down;

    public Paddle(float x, float y, int up, int down) {
        rect.x = x;
        rect.y = y;

        mesh = new BasicMesh(new float[] {
                -1f, 1f,
                -1f, -1f,
                1f, -1f,
                1f, -1f,
                1f, 1f,
                -1f, 1f
        });

        this.up = up;
        this.down = down;
    }

    public void render(Shader shader) {
        Matrix4f mat = new Matrix4f();
        mat.identity();
        mat.scale(new Vector3f(rect.w, rect.h, 0f));
        mat.translate(new Vector3f(rect.x, rect.y, 0f));

        shader.setUniformMat4f("trs", mat);
        mesh.render();
    }

    public void update() {
        if(this.up != -1) {
            if (KeyboardInput.isKeyDown(this.up)) {
                rect.yv = speed;
            } else {
                if (KeyboardInput.isKeyDown(this.down)) {
                    rect.yv = -speed;
                } else {
                    rect.yv = 0f;
                }
            }
        }

        rect.update();

        if(rect.y - rect.h < -Main.height/2) {
            rect.y = -Main.height/2 + rect.h;
        }

        if(rect.y + rect.h > Main.height/2) {
            rect.y = Main.height/2 - rect.h;
        }
    }
}
