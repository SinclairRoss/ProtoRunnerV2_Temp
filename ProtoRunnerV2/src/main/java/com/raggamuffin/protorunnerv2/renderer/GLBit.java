package com.raggamuffin.protorunnerv2.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.raggamuffin.protorunnerv2.utils.Colour;

import android.opengl.GLES20;

public class GLBit extends GLModel
{
	public final FloatBuffer vertexBuffer;
	public final FloatBuffer barycentricCoordBuffer;
	
	private int m_Program;
	
	private int m_MVPMatrixHandle;  
    private int m_ColourHandle;
    private int m_PositionHandle;
    private int m_BarycentricHandle;
    
    private float[] m_Colour = new float[4];
	
	static final int COORDS_PER_VERTEX = 3;
	static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;	// 4 Bytes to a float.

	static float VertexCoords[] =
	{	
		 // 1
		-0.5f,  0.0f,  1.25f, // A
		-0.5f,  0.5f,  0.0f, // C
		-1.0f,  0.0f,  0.0f, // B
		
		 // 2
		-0.5f,  0.0f,  1.25f, // A
		-1.0f,  0.0f,  0.0f, // B
		-0.5f, -0.5f,  0.0f, // D
		
		 // 3
		-0.5f,  0.0f,  1.25f, // A
		-0.5f, -0.5f,  0.0f, // D
		-0.5f,  0.5f,  0.0f, // C
		
		 // 4
		 0.5f,  0.0f,  1.25f, // E
		 1.0f,  0.0f,  0.0f, // G
		 0.5f,  0.5f,  0.0f, // F		
		
		 // 5
		 0.5f,  0.0f,  1.25f, // E
		 0.5f,  0.5f,  0.0f, // F	
		 0.5f, -0.5f,  0.0f, // H

		 //6
		 0.5f,  0.0f,  1.25f, // E
		 0.5f, -0.5f,  0.0f, // H
		 1.0f,  0.0f,  0.0f, // G		
		 
		 //7
		-1.0f,  0.0f,  0.0f, // B
		-0.5f,  0.5f,  0.0f, // C
		 0.0f,  0.5f, -1.5f, // I
		
	     //8
		-1.0f,  0.0f,  0.0f, // B
		 0.0f,  0.5f, -1.5f, // I
		-0.5f, -0.5f,  0.0f, // D
		
		 // 9
		 1.0f,  0.0f,  0.0f, // G
		 0.0f,  0.5f, -1.5f, // I
		 0.5f,  0.5f,  0.0f, // F	
		
		 // 10
		 0.5f, -0.5f,  0.0f, // H
		 0.0f,  0.5f, -1.5f, // I
		 1.0f,  0.0f,  0.0f, // G
		 
		 // 11
		-0.5f,  0.5f,  0.0f, // C
		 0.5f,  0.5f,  0.0f, // F
		 0.0f,  0.5f, -1.5f, // I
			
		 // 12
		-0.5f, -0.5f,  0.0f, // D
		 0.0f,  0.5f, -1.5f, // I
		 0.5f, -0.5f,  0.0f, // H
		 
		 // 13
		 0.0f,  0.0f,  0.5f, // J
		 0.5f,  0.5f,  0.0f, // F	
		-0.5f,  0.5f,  0.0f, // C
		
		 // 14
		 0.0f,  0.0f,  0.5f,  // J
		-0.5f, -0.5f,  0.0f,  // D
		 0.5f, -0.5f,  0.0f,  // H
			
		 // 15
		 0.0f,  0.0f,  0.5f, // J
		-0.5f,  0.5f,  0.0f, // C
		-0.5f, -0.5f,  0.0f, // D
	
		 // 16
		 0.0f,  0.0f,  0.5f, // J
		 0.5f, -0.5f,  0.0f, // H
		 0.5f,  0.5f,  0.0f, // F	
		 
	};
	
	private final int vertexCount = VertexCoords.length / COORDS_PER_VERTEX;
	
	static final int BARYCENTRICCOORDS_PER_VERTEX = 3;
	static final int BARYCENTRICCOORD_STRIDE = BARYCENTRICCOORDS_PER_VERTEX * 4;	// 4 Bytes to a float.
	
	static float BarycentricCoords[] =
	{
		 // 1
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f,
			
		 // 2
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f,
				 
		 // 3
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f,
			
		 // 4
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f,
				
		 // 5
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f,
		 
		 //6
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f,
				
		 // 7
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f,
		 
		 // 8
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f,
				
		 // 9
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f,
		 
		 // 10
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f,
				
		 // 11
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f,
		 
		 // 12
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f,
		 
		 // 13
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f,
		 
		 // 14
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f,
		 
		 // 15
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f,
		 
		 // 16
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f,
	};
	
	public GLBit()
	{
		ByteBuffer bb = ByteBuffer.allocateDirect(VertexCoords.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(VertexCoords);
		vertexBuffer.position(0);
		
		ByteBuffer bcb = ByteBuffer.allocateDirect(BarycentricCoords.length * 4);
		bcb.order(ByteOrder.nativeOrder());
		barycentricCoordBuffer = bcb.asFloatBuffer();
		barycentricCoordBuffer.put(BarycentricCoords);
		barycentricCoordBuffer.position(0);
		
		m_Colour[0] = 1.0f;
		m_Colour[1] = 1.0f;
		m_Colour[2] = 1.0f;
		m_Colour[3] = 1.0f;
		
		m_Program 			= 0;
	    m_MVPMatrixHandle	= 0;  
	    m_ColourHandle		= 0;
	    m_PositionHandle	= 0;
	    m_BarycentricHandle = 0;
	    
	    InitShaders();
	}
	
	public void draw(float[] mvpMatrix)
	{
		GLES20.glUniformMatrix4fv(m_MVPMatrixHandle, 1, false, mvpMatrix, 0);
        GLES20.glUniform4fv(m_ColourHandle, 1, m_Colour, 0);

		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
	}
	
	public void SetColour(Colour colour)
	{
		m_Colour[0] = (float)colour.Red;
		m_Colour[1] = (float)colour.Green;
		m_Colour[2] = (float)colour.Blue;
		m_Colour[3] = (float)colour.Alpha;
	}
	
	public void InitShaders()
    {
		// prepare shaders and OpenGL program
        int vertexShaderHandler 	= loadShader(GLES20.GL_VERTEX_SHADER,Shaders.vertexShader_BARYCENTRIC);
        int fragmentShaderHandler 	= loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_BARYCENTRIC);

		m_Program = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_MVPMatrixHandle 		= GLES20.glGetUniformLocation(m_Program, "u_MVPMatrix");  
        m_ColourHandle 			= GLES20.glGetUniformLocation(m_Program, "u_Color");
        
        m_PositionHandle = GLES20.glGetAttribLocation(m_Program, "a_Position");
        m_BarycentricHandle = GLES20.glGetAttribLocation(m_Program, "a_Barycentric");
    }
	
	public static int loadShader(int type, String shaderCode)
    {
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    @Override
    public void InitialiseModel()
    {
        GLES20.glUseProgram(m_Program);

        GLES20.glEnableVertexAttribArray(m_PositionHandle);
        GLES20.glVertexAttribPointer(m_PositionHandle, GLBit.COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, GLBit.VERTEX_STRIDE, vertexBuffer);

        GLES20.glEnableVertexAttribArray(m_BarycentricHandle);
        GLES20.glVertexAttribPointer(m_BarycentricHandle, GLBit.BARYCENTRICCOORDS_PER_VERTEX, GLES20.GL_FLOAT, false, GLBit.BARYCENTRICCOORD_STRIDE, barycentricCoordBuffer);
    }

    @Override
    public void CleanModel()
    {
        GLES20.glDisableVertexAttribArray(m_PositionHandle);
        GLES20.glDisableVertexAttribArray(m_BarycentricHandle);
    }
}
