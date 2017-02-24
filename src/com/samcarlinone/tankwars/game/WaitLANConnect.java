package com.samcarlinone.tankwars.game;

import com.samcarlinone.tankwars.Main;
import com.samcarlinone.tankwars.graphics.*;
import com.samcarlinone.tankwars.math.Matrix4f;
import com.samcarlinone.tankwars.networking.LANAsyncConnector;
import com.samcarlinone.tankwars.networking.TCPThread;
import com.samcarlinone.tankwars.util.KeyboardInput;

import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glGetError;

/**
 * Created by CARLINSE1 on 2/9/2017.
 */
public class WaitLANConnect implements Module {
    private Shader textShader;
    private TextRenderer renderer;
    private int anim=0;
    private int stage=0;
    private Text text;

    private LANAsyncConnector connector;

    public WaitLANConnect() {
        textShader = new Shader("text");
        renderer = new TextRenderer();

        text = new Text("Waiting [/]", -320, 0);
        text.setScale(4);
        renderer.addText(text);

        connector = new LANAsyncConnector();
    }

    public void render () {
        textShader.enable();

        Matrix4f projection = new Matrix4f();
        projection.orthographic(-Main.width/2, Main.width/2, -Main.height/2, Main.height/2, -1, 1);

        textShader.setUniformMat4f("proj", projection);
        renderer.render();
    }

    public Module update() {
        anim ++;
        if(anim == 30) {
            stage++;

            switch(stage) {
                case 4:
                    stage = 0;

                case 0:
                    text.setText("Waiting [/]");
                    break;

                case 1:
                    text.setText("Waiting [|]");
                    break;

                case 2:
                    text.setText("Waiting [\\]");
                    break;

                case 3:
                    text.setText("Waiting [-]");
            }

            anim = 0;
        }

        renderer.update();

        TCPThread conn = connector.connect();
        if(conn != null) {
            System.out.println("Connected");
            return new LANGame(conn);
        }

        return null;
    }
}
