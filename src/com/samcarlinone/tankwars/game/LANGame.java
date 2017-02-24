package com.samcarlinone.tankwars.game;

import com.samcarlinone.tankwars.Main;
import com.samcarlinone.tankwars.graphics.ParticleManager;
import com.samcarlinone.tankwars.graphics.Shader;
import com.samcarlinone.tankwars.graphics.Text;
import com.samcarlinone.tankwars.graphics.TextRenderer;
import com.samcarlinone.tankwars.math.Matrix4f;
import com.samcarlinone.tankwars.math.Resolver;
import com.samcarlinone.tankwars.networking.TCPThread;
import com.samcarlinone.tankwars.util.KeyboardInput;

import java.util.Random;

/**
 * Created by CARLINSE1 on 2/9/2017.
 */
public class LANGame implements Module {
    private Random generator;
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

    private TCPThread conn;
    private boolean leftSide;
    private float lastSpeed=0;
    private Paddle myPaddle;
    private Paddle oPaddle;
    private boolean disconnected=false;

    public LANGame(TCPThread connection) {
        conn = connection;
        leftSide = conn.isHost();

        trs = new Shader("trs");
        part = new Shader("part");
        textShader = new Shader("text");

        renderer = new TextRenderer();
        pm = new ParticleManager();

        score = new Text("0:0", -96, Main.height/2-64);
        score.setScale(4);
        renderer.addText(score);

        init();

        kickoffLeft = true;
        p1Score = p2Score = 0;

        generator = new Random(0);
    }

    public void terminate() {
        try {
            TCPThread.sendSafe(conn, "disconnect");
            conn.command_queue.put("terminate");
        } catch(InterruptedException e) {
            System.exit(0);
        }
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
        //Check other speed
        if(conn.rx_queue.peek() != null) {
            String val = conn.rx_queue.poll();
            if(val != null)
                if(val.equals("start")) {
                    if(kickoffLeft) {
                        b.rect.xv = 4;
                        b.rect.x += 2;
                        b.rect.yv += p1.rect.yv / 5f + generator.nextFloat()/2f;
                        ballStarted = true;
                    } else {
                        b.rect.xv = -4;
                        b.rect.x += -2;
                        b.rect.yv += p2.rect.yv / 5f + generator.nextFloat()/2f;
                        ballStarted = true;
                    }
                }
                else
                    if(val.equals("disconnect")) {
                        score.setText("Disconnected");
                        score.setX(-400);
                        disconnected = true;
                    }
                    else
                        if(val.charAt(0) == 'r') {
                            String[] param = val.split(":");
                            b.rect.x = Float.parseFloat(param[1]);
                            b.rect.y = Float.parseFloat(param[2]);
                            b.rect.xv = Float.parseFloat(param[3]);
                            b.rect.yv = Float.parseFloat(param[4]);
                        }
                        else
                            oPaddle.rect.yv = Float.parseFloat(val);
        }

        if(ballStarted) {
            moveBall();
        } else {
            ballStuck();
        }

        //Send speed
        if(myPaddle.rect.yv != lastSpeed) {
            TCPThread.sendSafe(conn, Float.toString(myPaddle.rect.yv));
            lastSpeed = myPaddle.rect.yv;
        }

        if(!disconnected)
            score.setText(p1Score + ":" + p2Score);

        renderer.update();

        return null;
    }

    private void init() {
        if(leftSide) {
            myPaddle = p1 = new Paddle(-500, 0, 'W', 'S');
            oPaddle = p2 = new Paddle(500, 0, -1, -1);
        } else {
            oPaddle = p1 = new Paddle(-500, 0, -1, -1);
            myPaddle = p2 = new Paddle(500, 0, KeyboardInput.UP, KeyboardInput.DOWN);
        }
        b = new Ball(0, 0, 0, 0);
        ballStarted = false;
        pm.reset();

        lastSpeed = 0f;

        generator = new Random(p1Score);
    }

    private void moveBall() {
        p1.update();
        p2.update();
        b.update();

        Resolver.Dynamic_YLine(b.rect, -Main.height/2);
        Resolver.Dynamic_YLine(b.rect, Main.height/2);

        if(Resolver.Dynamic_Static(b.rect, p1.rect)) {
            b.rect.xv = b.rect.xv*1.025f;
            b.rect.yv += p1.rect.yv / 5f + generator.nextFloat()/2f;

            if(leftSide)
                TCPThread.sendSafe(conn, "r:"+b.rect.x+":"+b.rect.y+":"+b.rect.xv+":"+b.rect.yv);

            pm.spawnAngled(b.rect.x, b.rect.y, 20, (float)Math.PI/2f, (float)Math.PI/-2f, 10f, 60f);
        }

        if(Resolver.Dynamic_Static(b.rect, p2.rect)) {
            b.rect.xv = b.rect.xv*1.025f;
            b.rect.yv += p2.rect.yv / 5f + generator.nextFloat()/2f;

            if(!leftSide)
                TCPThread.sendSafe(conn, "r:"+b.rect.x+":"+b.rect.y+":"+b.rect.xv+":"+b.rect.yv);

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

            if(KeyboardInput.isKeyDown('D') && leftSide) {
                TCPThread.sendSafe(conn, "start");

                b.rect.xv = 4;
                b.rect.x += 2;
                b.rect.yv += p1.rect.yv / 5f + generator.nextFloat()/2f;
                ballStarted = true;
            }
        } else {
            b.rect.y = p2.rect.y;
            b.rect.x = p2.rect.x - (b.rect.w + p2.rect.w);

            if(KeyboardInput.isKeyDown(KeyboardInput.LEFT) && !leftSide) {
                TCPThread.sendSafe(conn, "start");

                b.rect.xv = -4;
                b.rect.x += -2;
                b.rect.yv += p2.rect.yv / 5f + generator.nextFloat()/2f;
                ballStarted = true;
            }
        }
    }

}
