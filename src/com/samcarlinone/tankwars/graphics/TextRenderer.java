package com.samcarlinone.tankwars.graphics;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.AbstractList;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;


/**
 * Created by CARLINSE1 on 2/14/2017.
 */
public class TextRenderer {
    private int texSize = 16;
    private int tex;
    private int vao;
    private int totalLength=0;

    private AbstractList<Text> texts;

    public TextRenderer() {
        texts = new ArrayList<>(8);

        createTexture();
        createVao();
    }

    public Text addText(Text text) {
        texts.add(text);
        return text;
    }

    public Text deleteText(Text text) {
        texts.remove(text);
        return text;
    }

    private void createTexture() {
        this.tex = glGenTextures();
        glBindTexture(GL_TEXTURE_2D_ARRAY, this.tex);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glTexImage3D(GL_TEXTURE_2D_ARRAY, 0, GL_RGB8, texSize, texSize, 256, 0, GL_RGB,
                GL_UNSIGNED_BYTE, (ByteBuffer) null);

        BufferedImage img = null;

        try {
            img = ImageIO.read(this.getClass().getResourceAsStream("/bitmap_font.png"));
        } catch (IOException e) {
            System.err.println("Image Not Loaded");
            return;
        }

        for(int j=0; j<256; j++) {
            int[] data = img.getData().getPixels(j%16 * 16, (int) Math.floor(j/16) * 16, texSize, texSize, new int[texSize * texSize * 4]);
            ByteBuffer bb = BufferUtils.createByteBuffer(texSize * texSize * 3);

            for(int y=texSize-1; y>=0; y--) {
                for(int x=0; x<texSize; x++) {
                    int i = (y*texSize + x)*4;
                    bb.put((byte) data[i]).put((byte) data[i+1]).put((byte) data[i+2]);
                }
            }
            bb.flip();
            glTexSubImage3D(GL_TEXTURE_2D_ARRAY, 0, 0, 0, j, 16, 16, 1, GL_RGB, GL_UNSIGNED_BYTE, bb);
        }

        glGenerateMipmap(GL_TEXTURE_2D_ARRAY);
        glBindTexture(GL_TEXTURE_2D_ARRAY, 0);
    }

    private void createVao() {
        this.vao = glGenVertexArrays();
        int vbo = glGenBuffers();
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        ByteBuffer bb = BufferUtils.createByteBuffer(4 * 2 * 6);
        FloatBuffer fv = bb.asFloatBuffer();
        fv.put(0).put(0);
        fv.put(0).put(0);
        fv.put(0).put(0);
        fv.put(0).put(0);
        fv.put(0).put(0);
        fv.put(0).put(0);
        glBufferData(GL_ARRAY_BUFFER, bb, GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0L);

        vbo = glGenBuffers();
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        bb = BufferUtils.createByteBuffer(4 * 3 * 6);
        fv = bb.asFloatBuffer();
        fv.put(0).put(0).put(0);
        fv.put(0).put(0).put(0);
        fv.put(0).put(0).put(0);
        fv.put(0).put(0).put(0);
        fv.put(0).put(0).put(0);
        fv.put(0).put(0).put(0);
        glBufferData(GL_ARRAY_BUFFER, bb, GL_STATIC_DRAW);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0L);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    private void regenVao() {
        totalLength = 0;

        for (Text t: texts) {
            totalLength += t.getLength();
        }

        //Position Data
        int vbo = glGenBuffers();
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        ByteBuffer bb = BufferUtils.createByteBuffer(totalLength * 4 * 2 * 6);
        FloatBuffer fv = bb.asFloatBuffer();

        for (Text t: texts) {
            char[] letters = t.getCharArray();
            float size = this.texSize * t.getScale();

            for(int i=0; i<letters.length; i++) {
                fv.put((i*size) + t.getX()).put(t.getY() + size);
                fv.put((i*size) + t.getX()).put(t.getY());
                fv.put((i*size) + t.getX() + size).put(t.getY());

                fv.put((i*size) + t.getX() + size).put(t.getY());
                fv.put((i*size) + t.getX() + size).put(t.getY() + size);
                fv.put((i*size) + t.getX()).put(t.getY() + size);
            }
        }

        glBufferData(GL_ARRAY_BUFFER, bb, GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0L);

        //Texture Data
        vbo = glGenBuffers();
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        bb = BufferUtils.createByteBuffer(totalLength * 4 * 3 * 6);
        fv = bb.asFloatBuffer();

        for(Text t: texts) {
            char[] letters = t.getCharArray();

            for(int i=0; i<letters.length; i++) {
                fv.put(0).put(1).put(letters[i]);
                fv.put(0).put(0).put(letters[i]);
                fv.put(1).put(0).put(letters[i]);

                fv.put(1).put(0).put(letters[i]);
                fv.put(1).put(1).put(letters[i]);
                fv.put(0).put(1).put(letters[i]);
            }
        }
        glBufferData(GL_ARRAY_BUFFER, bb, GL_STATIC_DRAW);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0L);

        //Unbind buffers
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void render() {
        if(totalLength == 0)
            return;

        glBindVertexArray(vao);
        glBindTexture(GL_TEXTURE_2D_ARRAY, tex);
        glDrawArrays(GL_TRIANGLES, 0, totalLength * 6);
        glBindTexture(GL_TEXTURE_2D_ARRAY, 0);
        glBindVertexArray(0);
    }

    public void update() {
        for (Text t: texts) {
            if(t.isDirty()) {
                regenVao();

                for (Text text: texts) {
                    text.setClean();
                }

                break;
            }
        }
    }
}
