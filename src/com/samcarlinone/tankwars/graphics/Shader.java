package com.samcarlinone.tankwars.graphics;

import com.samcarlinone.tankwars.math.Matrix4f;
import com.samcarlinone.tankwars.math.Vector2f;
import com.samcarlinone.tankwars.math.Vector3f;
import com.samcarlinone.tankwars.util.ShaderUtils;

import static org.lwjgl.opengl.GL20.*;

import java.io.File;
import java.util.HashMap;
/**
 * Created by CARLINSE1 on 2/6/2017.
 */
public class Shader {
    public static final int VERT_ATTRIB = 0;

    private boolean enabled = false;
    private final int ID;

    private HashMap<String, Integer> uniformCache = new HashMap<>();

    public Shader(String name) {
        ID = ShaderUtils.load("shaders/" +name+".vert", "shaders/" +name+".frag");
    }

    public int findUniform(String name) {
        if(uniformCache.containsKey(name))
            return uniformCache.get(name);

        int result = glGetUniformLocation(ID, name);

        if(result != -1) {
            uniformCache.put(name, result);
        } else {
            System.err.println("Could not find uniform: "+name);
        }

        return result;
    }

    public void setUniform1f(String name, float val) {
        if(!enabled)
            enable();

        glUniform1f(findUniform(name), val);
    }

    public void setUniform2f(String name, Vector2f vec) {
        if(!enabled)
            enable();

        glUniform2f(findUniform(name), vec.x, vec.y);
    }

    public void setUniform3f(String name, Vector3f vec) {
        if(!enabled)
            enable();

        glUniform3f(findUniform(name), vec.x, vec.y, vec.z);
    }

    public void setUniformMat4f(String name, Matrix4f mat) {
        if(!enabled)
            enable();

        glUniformMatrix4fv(findUniform(name), false, mat.toFloatBuffer());
    }

    public void enable() {
        glUseProgram(ID);
        enabled = true;
    }

    public void disable() {
        glUseProgram(0);
        enabled = false;
    }
}
