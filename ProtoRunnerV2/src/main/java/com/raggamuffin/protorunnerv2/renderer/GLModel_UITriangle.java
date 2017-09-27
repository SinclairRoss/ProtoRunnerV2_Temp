package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES31;
import android.util.Log;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.ui.UIElement;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector2;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GLModel_UITriangle
{
    private final FloatBuffer m_VertexBuffer;

    private int m_NumVertices;

    private int m_Program;

    private int m_ProjMatrixHandle;
    private int m_PositionHandle;
    private int m_ScaleHandle;
    private int m_RotationHandle;
    private int m_ColourHandle;
    private int m_VertexHandle;

    public GLModel_UITriangle()
    {
        int numVertices = 3;
        float[] vertices = new float[numVertices * 3];

        double theta = 0.0;
        double deltaTheta = (Math.PI * 2.0f) / numVertices;

        for(int i = 0; i < numVertices; ++i)
        {
            int index = i*3;
            vertices[index] = (float)Math.sin(theta);
            vertices[index+1] = (float)Math.cos(theta);
            vertices[index+2] = 0.0f;

            theta += deltaTheta;
        }

        ByteBuffer vb = ByteBuffer.allocateDirect(vertices.length * 4);
        vb.order(ByteOrder.nativeOrder());
        m_VertexBuffer = vb.asFloatBuffer();
        m_VertexBuffer.put(vertices);
        m_VertexBuffer.position(0);

        m_NumVertices = vertices.length / 3;

        m_Program = 0;
        m_ProjMatrixHandle = 0;
        m_PositionHandle = 0;
        m_RotationHandle = 0;
        m_VertexHandle = 0;

        InitShaders();
    }

    public void InitialiseModel(float[] projMatrix)
    {
        GLES31.glUseProgram(m_Program);

        GLES31.glEnableVertexAttribArray(m_VertexHandle);
        GLES31.glVertexAttribPointer(m_VertexHandle, 3, GLES31.GL_FLOAT, false, 12, m_VertexBuffer);

        GLES31.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);
    }

    public void Draw(Vector2 pos, Vector2 scale, double rotation, Colour colour, double lineWidth)
    {
        GLES31.glUniform2f(m_PositionHandle, (float)pos.X, (float)pos.Y);
        GLES31.glUniform2f(m_ScaleHandle, (float)scale.X, (float)scale.Y);
     //   GLES31.glUniform4f(m_ColourHandle, (float) colour.Red, (float) colour.Green, (float)colour.Blue, (float)colour.Alpha);
        GLES31.glUniform1f(m_RotationHandle, (float)rotation);

        GLES31.glLineWidth((float)lineWidth);
        GLES31.glDrawArrays(GLES31.GL_LINE_LOOP, 0, m_NumVertices);
    }

    public void CleanModel()
    {
        GLES31.glDisableVertexAttribArray(m_VertexHandle);
    }

    protected void InitShaders()
    {
        // prepare shaders and OpenGL program
        int vertexShaderHandler = loadShader(GLES31.GL_VERTEX_SHADER,Shaders.vertexShader_UISTANDARD);
        int fragmentShaderHandler = loadShader(GLES31.GL_FRAGMENT_SHADER,Shaders.fragmentShader_BLOCKCOLOUR);

        m_Program = GLES31.glCreateProgram();             		// create empty OpenGL Program
        GLES31.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES31.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES31.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_ProjMatrixHandle      = GLES31.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_PositionHandle        = GLES31.glGetUniformLocation(m_Program, "u_Position");
        m_ScaleHandle           = GLES31.glGetUniformLocation(m_Program, "u_Scale");
        m_RotationHandle        = GLES31.glGetUniformLocation(m_Program, "u_Rotation");

        m_ColourHandle 			= GLES31.glGetUniformLocation(m_Program, "u_Color");

        m_VertexHandle = GLES31.glGetAttribLocation(m_Program, "a_Vertices");
    }

    protected int loadShader(int type, String shaderCode)
    {
        // create a vertex shader type (GLES31.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES31.GL_FRAGMENT_SHADER)
        int shader = GLES31.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES31.glShaderSource(shader, shaderCode);
        GLES31.glCompileShader(shader);

        int[] compiled = new int[1];
        GLES31.glGetShaderiv(shader, GLES31.GL_COMPILE_STATUS, compiled, 0);

        if (compiled[0] == 0)
        {
            Log.e("Shader", "Shader is broken");
        }

        return shader;
    }
}
