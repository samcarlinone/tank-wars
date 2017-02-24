package com.samcarlinone.tankwars;

import com.samcarlinone.tankwars.game.LANGame;
import com.samcarlinone.tankwars.game.Menu;
import com.samcarlinone.tankwars.game.Module;
import com.samcarlinone.tankwars.util.KeyboardInput;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Created by CARLINSE1 on 2/2/2017.
 */
public class Main implements Runnable{

    public static final int width = 1280;
    public static final int height = 720;

    private Thread thread;
    private boolean running = false;

    private long window;

    private Module currentModule;


    public void start() {
        running = true;
        thread = new Thread(this, "Game");
        thread.start();
    }

    private void init() {
        if (!glfwInit()) {
            System.err.println("Could not initialize GLFW!");
            return;
        }

        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        window = glfwCreateWindow(width, height, "NetPong", 0L, 0L);
        if (window == NULL) {
            System.err.println("Could not create GLFW window!");
            return;
        }

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

        glfwSetKeyCallback(window, new KeyboardInput());

        glfwMakeContextCurrent(window);
        glfwShowWindow(window);

        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        System.out.println("OpenGL: " + glGetString(GL_VERSION));

        //Load menu module
        currentModule = new Menu();
    }

    public void run() {
        init();

        long lastTime = System.nanoTime();
        double delta = 0.0;
        double ns = 1000000000.0 / 60.0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1.0) {
                update();
                updates++;
                delta--;
            }
            render();
            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println(updates + " ups, " + frames + " fps");
                updates = 0;
                frames = 0;
            }
            if (glfwWindowShouldClose(window))
                running = false;
        }

        if(currentModule instanceof LANGame) {
            ((LANGame) currentModule).terminate();
        }

        glfwDestroyWindow(window);
        glfwTerminate();
    }

    private void update() {
        glfwPollEvents();

        Module newModule = currentModule.update();

        if(newModule != null) {
            currentModule = newModule;
        }
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        currentModule.render();

        int error = glGetError();
        if (error != GL_NO_ERROR)
            System.err.println("GL Error: " + error);

        glfwSwapBuffers(window);
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
