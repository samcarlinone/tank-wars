package com.samcarlinone.tankwars.game;

/**
 * Created by CARLINSE1 on 2/9/2017.
 */
public interface Module {

    /**
     * Render using opengl, called unpredictably
     */
    public void render();

    /**
     * Update game, called 60 times per second
     * @return next module or null
     */
    public Module update();
}
