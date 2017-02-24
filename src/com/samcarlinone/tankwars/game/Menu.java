package com.samcarlinone.tankwars.game;

import com.samcarlinone.tankwars.Main;
import com.samcarlinone.tankwars.graphics.*;
import com.samcarlinone.tankwars.math.Matrix4f;
import com.samcarlinone.tankwars.util.KeyboardInput;

/**
 * Created by CARLINSE1 on 2/9/2017.
 */
public class Menu implements Module {
    private Shader basic;
    private BasicMesh arrow1;
    private BasicMesh arrow2;

    private Shader textShader;
    private TextRenderer renderer;
    private Text t1;
    private Text t2;

    private boolean mode;

    public Menu() {
        basic = new Shader("basic");

        arrow1 = new BasicMesh(new float[] {
                -0.35f, 0.35f,
                -0.5f, -0.15f,
                -0.2f, -0.15f,
        });

        arrow2 = new BasicMesh(new float[] {
                0.35f, -0.25f,
                0.5f, 0.25f,
                0.2f, 0.25f,
        });

        textShader = new Shader("text");
        renderer = new TextRenderer();

        t1 = renderer.addText(new Text("Splitscreen", -500f, 200f));
        t1.setScale(3.5f);
        t2 = renderer.addText(new Text("LAN Game", 0f, -200f));
        t2.setScale(3.5f);

        mode = false;
    }

    public void render () {
        basic.enable();
        Matrix4f projection = new Matrix4f();
        projection.orthographic(-1, 1, -1, 1, -1, 1);
        basic.setUniformMat4f("proj", projection);

        if(!mode) {
            arrow1.render();
        } else {
            arrow2.render();
        }

        textShader.enable();
        projection = new Matrix4f();
        projection.orthographic(-Main.width/2, Main.width/2, -Main.height/2, Main.height/2, -1, 1);

        textShader.setUniformMat4f("proj", projection);
        renderer.render();

        /*
        Matrix4f projection = new Matrix4f();
        projection.orthographic(-width/2, width/2, -height/2, height/2, -1, 1);
        //projection.orthographic(-1, 1, -1, 1, -1, 1);

        basic.enable();
        basic.setUniformMat4f("proj", projection);
        p1.render(basic);
        basic.enable();
        basic.setUniformMat4f("proj", projection);
        b.render(basic);

        particle.enable();
        particle.setUniformMat4f("proj", projection);
        pm.render();
        */
    }

    public Module update() {
        renderer.update();

        if(KeyboardInput.isKeyDown(32)) {
            if(mode) {
                return new WaitLANConnect();
            } else {
                return new LocalGame();
            }
        }

        if(KeyboardInput.isKeyDown(KeyboardInput.RIGHT) || KeyboardInput.isKeyDown('D')) {
            mode = true;
        }

        if(KeyboardInput.isKeyDown(KeyboardInput.LEFT) || KeyboardInput.isKeyDown('A')) {
            mode = false;
        }

        return null;
    }
}
