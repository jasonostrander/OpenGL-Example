package com.example;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLU;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ByteOrder;
import android.opengl.GLSurfaceView;

public class ExampleRenderer implements GLSurfaceView.Renderer {
    private float mRed;
    private float mGreen;
    private float mBlue;
    private float mAngle; 
    float[] mVertices = new float[]{-1.0f, -1.0f, 0, 1.0f, -1.0f, 0, 0.0f,  1.0f, 0};
    float[] mColors = new float[]{1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f};
    FloatBuffer mVertexBuffer;
    FloatBuffer mColorBuffer;
    public static final float ROTATION_SPEED = 30; //Degrees per second

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //Set up the triangle vertices in FloatBuffers as needed by OpenGl
        ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(mVertices.length * 4);
        vertexByteBuffer.order(ByteOrder.nativeOrder()); 
        mVertexBuffer = vertexByteBuffer.asFloatBuffer();
        mVertexBuffer.put(mVertices);
        mVertexBuffer.position(0);

        ByteBuffer colorByteBuffer = ByteBuffer.allocateDirect(mColors.length * 4);
        colorByteBuffer.order(ByteOrder.nativeOrder()); 
        mColorBuffer = colorByteBuffer.asFloatBuffer();
        mColorBuffer.put(mColors);
        mColorBuffer.position(0);

        //Turn on some things
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
    }

    long mLastFrameTime = 0;

    public void updateAngle() {
        long now = System.currentTimeMillis();
        if(mLastFrameTime != 0) {
            //Rotate 10 degree per second
            mAngle += ROTATION_SPEED * (now-mLastFrameTime)/1000.0;
        }
        mLastFrameTime = now;
    }
    public void onDrawFrame(GL10 gl) {
        updateAngle();
        gl.glClearColor(mRed, mGreen, mBlue, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -7.0f);

        gl.glRotatef(mAngle, 0.0f, 0.0f, 1.0f);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);    //Select The Projection Matrix
        gl.glLoadIdentity();                    //Reset The Projection Matrix
        //Calculate The Aspect Ratio Of The Window
        GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);     //Select The Modelview Matrix
        gl.glLoadIdentity();
    }

    public void setColor(float red, float green, float blue) {
        mRed = red;
        mGreen = green;
        mBlue = blue;
    }
}
