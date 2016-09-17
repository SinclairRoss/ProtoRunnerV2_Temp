package com.raggamuffin.protorunnerv2.renderer;

// Author: Sinclair Ross
// Date:   21/06/2016

import android.opengl.GLES20;
import android.util.Log;

import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class GLModel_Rope
{
    private FloatBuffer m_VertexBuffer;
    private FloatBuffer m_LengthBuffer;

    private int m_Program;

    private int m_ProjMatrixHandle;
    private int m_PositionHandle;
    private int m_NormalisedLengthHandle;
    private int m_HotColourHandle;
    private int m_ColdColourHandle;
    private int m_ColourBloomPointHandle;

    private ArrayList<Vector3> m_TrailPoints;
    private ArrayList<Double> m_NormalisedLengths;

    private Vector3 m_EyePos;
    private Vector3 m_ToEye;

    public GLModel_Rope()
    {
        m_Program = 0;

        m_ProjMatrixHandle = 0;
        m_PositionHandle = 0;
        m_NormalisedLengthHandle = 0;
        m_HotColourHandle = 0;
        m_ColdColourHandle = 0;
        m_ColourBloomPointHandle = 0;

        m_TrailPoints = new ArrayList<>();
        m_NormalisedLengths = new ArrayList<>();

        m_EyePos = new Vector3();
        m_ToEye = new Vector3();

        InitShaders();
    }

    public void InitialiseModel(float[] projMatrix, Vector3 eye)
    {
        GLES20.glUseProgram(m_Program);

        GLES20.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);

        m_EyePos.SetVector(eye);
    }

    public void AddPoint(Vector3 position, double normalisedLength)
    {
        m_TrailPoints.add(position);
        m_NormalisedLengths.add(normalisedLength);
    }

    public void Draw(Colour coldColour, Colour hotColour, double bloomPoint)
    {
        m_ToEye.SetVectorDifference(m_EyePos, m_TrailPoints.get(0));
        float dist = (float) m_ToEye.GetLength();

        GLES20.glLineWidth((float) (20 * MathsHelper.FastInverseSqrt(dist)));

        GLES20.glUniform4f(m_ColdColourHandle, (float) coldColour.Red, (float) coldColour.Green, (float) coldColour.Blue, (float) coldColour.Alpha);
        GLES20.glUniform4f(m_HotColourHandle, (float) hotColour.Red, (float) hotColour.Green, (float) hotColour.Blue, (float) hotColour.Alpha);
        GLES20.glUniform1f(m_ColourBloomPointHandle, (float)bloomPoint);

        int numPoints = m_TrailPoints.size();
        float[] vertices = new float[numPoints * 3];
        float[] lengths = new float[numPoints];

        for(int i = 0; i < numPoints; i++)
        {
            Vector3 pos     = m_TrailPoints.get(i);
            vertices[i*3]   = (float)pos.I;
            vertices[i*3+1] = (float)pos.J;
            vertices[i*3+2] = (float)pos.K;

            lengths[i] = m_NormalisedLengths.get(i).floatValue();
        }

        ByteBuffer vb = ByteBuffer.allocateDirect(numPoints * 12);
        vb.order(ByteOrder.nativeOrder());
        m_VertexBuffer = vb.asFloatBuffer();
        m_VertexBuffer.put(vertices);
        m_VertexBuffer.position(0);

        GLES20.glEnableVertexAttribArray(m_PositionHandle);
        GLES20.glVertexAttribPointer(m_PositionHandle, 3, GLES20.GL_FLOAT, false, 12, m_VertexBuffer);

        ByteBuffer lb = ByteBuffer.allocateDirect(numPoints * 4);
        lb.order(ByteOrder.nativeOrder());
        m_LengthBuffer = lb.asFloatBuffer();
        m_LengthBuffer.put(lengths);
        m_LengthBuffer.position(0);

        GLES20.glEnableVertexAttribArray(m_NormalisedLengthHandle);
        GLES20.glVertexAttribPointer(m_NormalisedLengthHandle, 3, GLES20.GL_FLOAT, false, 12, m_VertexBuffer);
        GLES20.glVertexAttribPointer(m_NormalisedLengthHandle, 1, GLES20.GL_FLOAT, false, 4, m_LengthBuffer);

        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, numPoints);

        m_TrailPoints.clear();
        m_NormalisedLengths.clear();
    }

    public void CleanModel()
    {
        GLES20.glDisableVertexAttribArray(m_PositionHandle);
    }

    private void InitShaders()
    {
        int vertexShaderHandler 	= loadShader(GLES20.GL_VERTEX_SHADER,Shaders.vertexShader_ROPE);
        int fragmentShaderHandler 	= loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_ROPE);

        m_Program = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_ProjMatrixHandle = GLES20.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_HotColourHandle = GLES20.glGetUniformLocation(m_Program, "u_HotColor");
        m_ColdColourHandle = GLES20.glGetUniformLocation(m_Program, "u_ColdColor");
        m_ColourBloomPointHandle = GLES20.glGetUniformLocation(m_Program, "u_ColorBloomPoint");

        m_PositionHandle = GLES20.glGetAttribLocation(m_Program, "a_Position");
        m_NormalisedLengthHandle = GLES20.glGetAttribLocation(m_Program, "a_NormalisedLength");

    }

    private int loadShader(int type, String shaderCode)
    {
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);

        if (compiled[0] == 0)
        {
            Log.e("shader trail", "Shader failed to compile");
        }

        Log.e("shader trail", "Shader A-OK.");

        return shader;
    }
}