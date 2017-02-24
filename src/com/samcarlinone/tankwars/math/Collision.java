package com.samcarlinone.tankwars.math;

/**
 * Created by CARLINSE1 on 2/10/2017.
 */
public class Collision {
    public static boolean RectXRect(Rect r1, Rect r2) {
        if(r1.x+r1.w > r2.x-r2.w && r2.x+r2.w > r1.x-r1.w) {
            if(r1.y+r1.h > r2.y-r2.h && r2.y+r2.h > r1.y-r1.h) {
                return true;
            }
        }

        return false;
    }
}
