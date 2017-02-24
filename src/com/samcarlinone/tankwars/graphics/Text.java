package com.samcarlinone.tankwars.graphics;

/**
 * Created by cobra on 2/17/2017.
 */
public class Text {
    private char[] text;
    private boolean dirty;
    private float x, y, scale;

    public Text(String in, float x, float y) {
        text = in.toCharArray();
        this.x = x;
        this.y = y;
        dirty = true;
        scale = 1;
    }

    public void setText(String text) {
        this.text = text.toCharArray();
        dirty = true;
    }

    public String getText() {
        return new String(text);
    }

    public void setX(float newX) {
        x = newX;
        dirty = true;
    }

    public void setY(float newY) {
        y = newY;
        dirty = true;
    }

    public void setScale(float newScale) {
        if(newScale <= 0){
            System.err.println("Cannot set scale <= 0");
            return;
        }

        scale = newScale;
        dirty = true;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getScale() { return scale; }

    public int getLength() {
        return text.length;
    }

    public char[] getCharArray() {
        return text;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setClean() {
        dirty = false;
    }
}
