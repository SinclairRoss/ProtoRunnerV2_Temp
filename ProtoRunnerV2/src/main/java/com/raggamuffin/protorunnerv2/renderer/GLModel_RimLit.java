package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES31;

import com.raggamuffin.protorunnerv2.RenderObjects.RenderObject;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GLModel_RimLit extends GLModel
{
    private final FloatBuffer m_VertexBuffer;
    private final FloatBuffer m_NormalBuffer;
    private final FloatBuffer m_BarycentricCoordBuffer;

    private int m_NumVertices;

    private int m_Program;

    private int m_ProjMatrixHandle;
    private int m_PositionHandle;
    private int m_ForwardHandle;
    private int m_UpHandle;
    private int m_RightHandle;
    private int m_ScaleHandle;
    private int m_ColourHandle;
    private int m_EyePosHandle;
    private int m_VertexHandle;
    private int m_NormalHandle;
    private int m_BarycentricHandle;

    public GLModel_RimLit(float[] vertices)
    {
        ByteBuffer vb = ByteBuffer.allocateDirect(vertices.length * 4);
        vb.order(ByteOrder.nativeOrder());
        m_VertexBuffer = vb.asFloatBuffer();
        m_VertexBuffer.put(vertices);
        m_VertexBuffer.position(0);

        int numTriangles = vertices.length / 9;

       Vector3 normal = new Vector3();
       Vector3 toA = new Vector3();
       Vector3 toB = new Vector3();

       float[] normals = new float[vertices.length];
       for(int i = 0; i < numTriangles; ++i)
       {
           float origin_x = vertices[i * 9];
           float origin_y = vertices[i * 9 + 1];
           float origin_z = vertices[i * 9 + 2];

           float pointA_x = vertices[i * 9 + 3];
           float pointA_y = vertices[i * 9 + 4];
           float pointA_z = vertices[i * 9 + 5];

           float pointB_x = vertices[i * 9 + 6];
           float pointB_y = vertices[i * 9 + 7];
           float pointB_z = vertices[i * 9 + 8];

           toA.SetVector(pointA_x - origin_x, pointA_y - origin_y, pointA_z - origin_z);
           toA.Normalise();

           toB.SetVector(pointB_x - origin_x, pointB_y - origin_y, pointB_z - origin_z);
           toB.Normalise();

           normal.SetVectorAsCrossProduct(toB, toA);
           normal.Normalise();

           normals[i * 9] = (float)normal.X;
           normals[i * 9 + 1] = (float)normal.Y;
           normals[i * 9 + 2] = (float)normal.Z;

           normals[i * 9 + 3] = (float)normal.X;
           normals[i * 9 + 4] = (float)normal.Y;
           normals[i * 9 + 5] = (float)normal.Z;

           normals[i * 9 + 6] = (float)normal.X;
           normals[i * 9 + 7] = (float)normal.Y;
           normals[i * 9 + 8] = (float)normal.Z;
       }

        ByteBuffer nb = ByteBuffer.allocateDirect(vertices.length * 4);
        nb.order(ByteOrder.nativeOrder());
        m_NormalBuffer = nb.asFloatBuffer();
        m_NormalBuffer.put(normals);
        m_NormalBuffer.position(0);

        float[] coords = new float[vertices.length];
        for(int i = 0; i < numTriangles; i++)
        {
            coords[i * 9]     = 1.0f;
            coords[i * 9 + 1] = 0.0f;
            coords[i * 9 + 2] = 0.0f;

            coords[i * 9 + 3] = 0.0f;
            coords[i * 9 + 4] = 1.0f;
            coords[i * 9 + 5] = 0.0f;

            coords[i * 9 + 6] = 0.0f;
            coords[i * 9 + 7] = 0.0f;
            coords[i * 9 + 8] = 1.0f;
        }

        ByteBuffer bcb = ByteBuffer.allocateDirect(coords.length * 4);
        bcb.order(ByteOrder.nativeOrder());
        m_BarycentricCoordBuffer = bcb.asFloatBuffer();
        m_BarycentricCoordBuffer.put(coords);
        m_BarycentricCoordBuffer.position(0);

        m_NumVertices = vertices.length / 3;

        m_Program = 0;
        m_ProjMatrixHandle = 0;
        m_PositionHandle = 0;

        m_ColourHandle = 0;
        m_VertexHandle = 0;
        m_NormalHandle = 0;
        m_BarycentricHandle = 0;

        InitShaders();
    }

    @Override
    public void InitialiseModel(float[] projMatrix, Vector3 eye)
    {
        GLES31.glUseProgram(m_Program);

        GLES31.glEnableVertexAttribArray(m_VertexHandle);
        GLES31.glVertexAttribPointer(m_VertexHandle, 3, GLES31.GL_FLOAT, false, 12, m_VertexBuffer);

        GLES31.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);

        GLES31.glUniform3f(m_EyePosHandle, (float)eye.X, (float)eye.Y, (float)eye.Z);
    }

    @Override
    public void Draw(RenderObject obj)
    {
      // Vector3 pos = obj.GetPosition();
      // GLES31.glUniform3f(m_PositionHandle, (float) pos.X, (float) pos.Y, (float) pos.Z);

      // Vector3 fwd = obj.GetForward();
      // GLES31.glUniform3f(m_ForwardHandle, (float) fwd.X, (float) fwd.Y, (float) fwd.Z);

      // Vector3 right = obj.GetRight();
      // GLES31.glUniform3f(m_RightHandle, (float) right.X, (float) right.Y, (float) right.Z);

      // Vector3 up = obj.GetUp();
      // GLES31.glUniform3f(m_UpHandle, (float) up.X, (float) up.Y, (float) up.Z);

      // Vector3 scale = obj.GetScale();
      // GLES31.glUniform3f(m_ScaleHandle, (float)scale.X, (float)scale.Y, (float)scale.Z);

        Colour colour = obj.GetColour();
      //  GLES31.glUniform4f(m_ColourHandle, (float) colour.Red, (float) colour.Green, (float) colour.Blue, (float) colour.Alpha * 0.6f);

        GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, m_NumVertices);
    }

    @Override
    public void CleanModel()
    {
        GLES31.glDisableVertexAttribArray(m_PositionHandle);
    }

    @Override
    protected void InitShaders()
    {
        // prepare shaders and OpenGL program
        int vertexShaderHandler = loadShader(GLES31.GL_VERTEX_SHADER,Shaders.vertexShader_RIMLIT, "Rimlit vertex");
        int fragmentShaderHandler = loadShader(GLES31.GL_FRAGMENT_SHADER,Shaders.fragmentShader_RIMLIT, "Rimlit fragment");

        m_Program = GLES31.glCreateProgram();             		// create empty OpenGL Program
        GLES31.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES31.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES31.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_ProjMatrixHandle  = GLES31.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_PositionHandle    = GLES31.glGetUniformLocation(m_Program, "u_Position");
        m_ForwardHandle     = GLES31.glGetUniformLocation(m_Program, "u_Forward");
        m_UpHandle          = GLES31.glGetUniformLocation(m_Program, "u_Up");
        m_RightHandle       = GLES31.glGetUniformLocation(m_Program, "u_Right");
        m_ScaleHandle       = GLES31.glGetUniformLocation(m_Program, "u_Scale");
        m_ColourHandle 	    = GLES31.glGetUniformLocation(m_Program, "u_Color");
        m_EyePosHandle      = GLES31.glGetUniformLocation(m_Program, "u_EyePos");

        m_VertexHandle      = GLES31.glGetAttribLocation(m_Program, "a_Vertices");
    }
}