package com.samcarlinone.tankwars.game;

import com.samcarlinone.tankwars.Main;
import com.samcarlinone.tankwars.graphics.ParticleManager;
import com.samcarlinone.tankwars.graphics.Shader;
import com.samcarlinone.tankwars.graphics.Text;
import com.samcarlinone.tankwars.graphics.TextRenderer;
import com.samcarlinone.tankwars.math.Collision;
import com.samcarlinone.tankwars.math.Matrix4f;
import com.samcarlinone.tankwars.math.Resolver;
import com.samcarlinone.tankwars.util.KeyboardInput;

import java.security.Key;

/**
 * Created by CARLINSE1 on 2/9/2017.
 */
public class LocalGame implements Module {

    private Shader trs;
    private Shader part;
    private Shader textShader;
    private TextRenderer renderer;
    private Text score;
    private ParticleManager pm;
    private Paddle p1;
    private Paddle p2;
    private Ball b;
    private Boolean ballStarted;
    private Boolean kickoffLeft;
    private int p1Score, p2Score;

    public LocalGame() {
        trs = new Shader("trs");
        part = new Shader("part");
        textShader = new Shader("text");

        renderer = new TextRenderer();
        pm = new ParticleManager();

        score = new Text("0:0", -96, Main.height/2-64);
        score.setScale(4);
        renderer.addText(score);

        init();

        kickoffLeft = Math.random() > 0.5;
        p1Score = p2Score = 0;
    }

    public void render() {
        Matrix4f projection = new Matrix4f();
        projection.orthographic(-Main.width/2, Main.width/2, -Main.height/2, Main.height/2, -1, 1);

        textShader.enable();
        textShader.setUniformMat4f("proj", projection);
        renderer.render();

        trs.enable();
        trs.setUniformMat4f("proj", projection);
        p1.render(trs);
        p2.render(trs);
        b.render(trs);

        part.enable();
        part.setUniformMat4f("proj", projection);
        pm.render();
    }

    public Module update() {
        if(ballStarted) {
            moveBall();
        } else {
            ballStuck();
        }

        score.setText(p1Score + ":" + p2Score);
        renderer.update();

        return null;
    }

    private void init() {
        p1 = new Paddle(-500, 0, 'W', 'S');
        p2 = new Paddle(500, 0, KeyboardInput.UP, KeyboardInput.DOWN);
        b = new Ball(0, 0, 0, 0);
        ballStarted = false;
        pm.reset();
    }

    private void moveBall() {
        p1.update();
        p2.update();
        b.update();

        Resolver.Dynamic_YLine(b.rect, -Main.height/2);
        Resolver.Dynamic_YLine(b.rect, Main.height/2);

        if(Resolver.Dynamic_Static(b.rect, p1.rect)) {
            b.rect.xv = b.rect.xv*1.025f;
            b.rect.yv += p1.rect.yv / 5f + Math.random()/2f;

            pm.spawnAngled(b.rect.x, b.rect.y, 20, (float)Math.PI/2f, (float)Math.PI/-2f, 10f, 60f);
        }

        if(Resolver.Dynamic_Static(b.rect, p2.rect)) {
            b.rect.xv = b.rect.xv*1.025f;
            b.rect.yv += p2.rect.yv / 5f + Math.random()/2f;

            pm.spawnAngled(b.rect.x, b.rect.y, 20, (float)Math.PI/2f, (float)Math.PI*3f/2f, 10f, 60f);
        }

        pm.spawn(b.rect.x, b.rect.y, 1);
        pm.update();

        if(b.rect.x < -Main.width/2-10) {
            p2Score ++;
            kickoffLeft = true;
            init();
            ballStuck();
        }

        if(b.rect.x > Main.width/2+10) {
            p1Score ++;
            kickoffLeft = false;
            init();
            ballStuck();
        }
    }

    private void ballStuck() {
        p1.update();
        p2.update();

        if(kickoffLeft) {
            b.rect.y = p1.rect.y;
            b.rect.x = p1.rect.x + b.rect.w + p1.rect.w;

            if(KeyboardInput.isKeyDown('D')) {
                b.rect.xv = 4;
                b.rect.x += 2;
                b.rect.yv += p1.rect.yv / 5f + Math.random()/2f;
                ballStarted = true;
            }
        } else {
            b.rect.y = p2.rect.y;
            b.rect.x = p2.rect.x - (b.rect.w + p2.rect.w);

            if(KeyboardInput.isKeyDown(KeyboardInput.LEFT)) {
                b.rect.xv = -4;
                b.rect.x += -2;
                b.rect.yv += p2.rect.yv / 5f + Math.random()/2f;
                ballStarted = true;
            }
        }
    }

}
