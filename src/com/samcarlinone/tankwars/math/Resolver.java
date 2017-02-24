package com.samcarlinone.tankwars.math;

/**
 * Created by cobra on 2/18/2017.
 */
public class Resolver {
    public static int iterations = 128;

    public static boolean Dynamic_Static(DynamicRect r1, StaticRect r2) {
        for(int i=Resolver.iterations; i>0; i--) {
            float x1 = r1.x - r1.xv*((float)i/iterations);
            float x2 = r2.x - r2.xv*((float)i/iterations);
            float y1 = r1.y - r1.yv*((float)i/iterations);
            float y2 = r2.y - r2.yv*((float)i/iterations);

            if(x1 + r1.w > x2 - r2.w && x2 + r2.w > x1 - r1.w) {
                if(y1 + r1.h > y2 - r2.h && y2 + r2.h > y1 - r1.h) {
                    //Rollback to this point
                    r1.x = x1;
                    r1.y = y1;
                    //Check more significant axis
                    if((r1.w+r2.w)-Math.abs(x1-x2) < (r1.h+r2.h)-Math.abs(y1-y2)) {
                        //Invert vector
                        r1.xv = -r1.xv + r2.xv;
                    } else {
                        //Invert vector
                        r1.yv = -r1.yv + r2.yv;
                    }
                    //Fastforward
                    r1.x += r1.xv*(1-(i/iterations));
                    r1.y += r1.yv*(1-(i/iterations));

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean Dynamic_XLine(DynamicRect r1, float xL) {
        for(int i=Resolver.iterations; i>0; i--) {
            float x1 = r1.x - r1.xv*((float)i/iterations);
            float y1 = r1.y - r1.yv*((float)i/iterations);

            if(x1 + r1.w > xL && xL > x1 - r1.w) {
                //Rollback to this point
                r1.x = x1;
                r1.y = y1;
                //Invert vector
                r1.xv = -r1.xv;
                //Fastforward
                r1.x += r1.xv*(1-(i/iterations));
                r1.y += r1.yv*(1-(i/iterations));

                return true;
            }
        }

        return false;
    }

    public static boolean Dynamic_YLine(DynamicRect r1, float yL) {
        for(int i=Resolver.iterations; i>0; i--) {
            float x1 = r1.x - r1.xv*((float)i/iterations);
            float y1 = r1.y - r1.yv*((float)i/iterations);

            if(y1 + r1.h > yL && yL > y1 - r1.h) {
                //Rollback to this point
                r1.x = x1;
                r1.y = y1;
                //Invert vector
                r1.yv = -r1.yv;
                //Fastforward
                r1.x += r1.xv*(1-(i/iterations));
                r1.y += r1.yv*(1-(i/iterations));

                return true;
            }
        }

        return false;
    }
}
