package com.raggamuffin.protorunnerv2.renderer;

public class Shaders 
{
	public static final String vertexShader_STANDARD =
	        "uniform mat4 u_MVPMatrix;   \n"    
	 	+	"attribute vec4 a_Position;  \n" 
        		
	 	
	    +   "void main()                \n" 
	    +   "{"  
	    +   "	gl_Position = u_MVPMatrix * a_Position; \n" // the matrix must be included as a modifier of gl_Position  
	    +   "}";

	public static final String fragmentShader_STANDARD =
	        "precision mediump float;" 
	    +   "uniform vec4 u_Color;" 
	    
	    +   "void main()"
	    +   "{" 
	    +   "	gl_FragColor = u_Color;" 
	    +   "}";

	public static final String vertexShader_LINE =
	        "uniform mat4 u_MVPMatrix;   \n"
	 	+	"attribute vec4 a_Position;  \n"
		+	"attribute float a_Weight;	 \n"
	 	+   "varying float v_Weight;	 \n"

	    +   "void main()                 \n"
	    +   "{"
	    +	"	v_Weight = a_Weight;     \n"
	    +   "	gl_Position = u_MVPMatrix * a_Position; \n" // the matrix must be included as a modifier of gl_Position
	    +   "}";

	public static final String fragmentShader_LINE =
	        "precision mediump float;"
	    +   "uniform vec4 u_Color;"
	    +	"uniform vec4 u_EndPointColor;"

	    +   "varying float v_Weight;	 "

	    +   "void main()"
	    +   "{"
	    +   "	gl_FragColor = mix(u_Color, u_EndPointColor, v_Weight); "
	    +   "}";

	public static final String vertexShader_TEXTURED =
			"uniform mat4 u_MVPMatrix;"
			
		+	"attribute vec4 a_Position;"     	// Per-vertex position information we will pass in.
		+ 	"attribute vec2 a_TexCoord;"
		
		+ 	"varying vec2 v_TexCoord;"
		
		+	"void main()"
		+	"{"
		+	"	v_TexCoord = a_TexCoord;"
		+	"	gl_Position = u_MVPMatrix * a_Position;"
		+	"}";
	
	public static final String fragmentShader_TEXTURED =
	        "precision mediump float;"
			
	    + 	"uniform sampler2D u_Texture;"
	    +   "uniform vec2 u_TexOffset;"
	    +   "uniform vec4 u_Color;" 
	    
	    + 	"varying vec2 v_TexCoord;"
	    
	    +   "void main()"
	    +   "{" 
	    +   "	gl_FragColor = u_Color * texture2D(u_Texture, v_TexCoord + u_TexOffset);" 
	    +   "}";
	
	public static final String vertexShader_BARYCENTRIC = 
			"uniform mat4 u_MVPMatrix;"
			
		+	"attribute vec4 a_Position;"
		+	"attribute vec3 a_Barycentric;"

		+	"varying vec3 v_Barycentric;"
		
		+ 	"void main()"
		+	"{"
		+	"	v_Barycentric = a_Barycentric;"
		+	"	gl_Position = u_MVPMatrix * a_Position;"
		+	"}";
	
	public static final String fragmentShader_BARYCENTRIC =
			"precision mediump float;"
			
		+	"uniform vec4 u_Color;"
				
		+	"varying vec3 v_Barycentric;"
		
		+	"void main()"
		+	"{"
		+	"	if(any(lessThan(v_Barycentric, vec3(0.06))))"
		+	"	{"
		+	"		gl_FragColor = u_Color;"
		+	"	}"
		+	"	else"
		+	"	{"
		+	"		gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);"
		+	"	}"
		+	"}";
	

	public static final String fragmentShader_BARYCENTRIC_HOLLOW =
			"precision mediump float;"
			
		+	"uniform vec4 u_Color;"
				
		+	"varying vec3 v_Barycentric;"
		
		+	"void main()"
		+	"{"
		+	"	if(any(lessThan(v_Barycentric, vec3(0.06))))"
		+	"	{"
		+	"		gl_FragColor = u_Color;"
		+	"	}"
		+	"	else"
		+	"	{"
		+	"		gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);"
		+	"	}"
		+	"}";

	public static final String vertexShader_POINT =
		    "uniform mat4 u_MVPMatrix;      \n"     // A constant representing the combined model/view/projection matrix.
		  + "uniform float u_Size;			\n"
		  + "uniform vec4 u_EyePos;			\n"
		  + "uniform vec4 u_WorldPos;		\n"
		  
		  + "attribute vec4 a_Position;     \n"     // Per-vertex position information we will pass in.
		 
		  + "void main()                    \n"     
		  + "{                              \n"
		  + "	vec4 toEye;					\n"		
		  + " 	toEye = u_EyePos - u_WorldPos;		\n"	
		  + "   float distance =  length(toEye);	\n"
		  + "	gl_PointSize = u_Size * inversesqrt(distance);		\n"		// The size of the glPoint.
		  + "   gl_Position = u_MVPMatrix   \n"     // Multiply the vertex info by the MVPMatrix.
		  + "               * a_Position;   \n"     
		  + "}                              \n";   
	
	public static final String fragmentShader_POINT =
		    "precision mediump float;       \n"     // Set the default precision to medium. We don't need as high of a
		  + "uniform vec4 u_Color;			\n"     // triangle per fragment.
		
		  + "void main()                    \n"     // The entry point for our fragment shader.
		  + "{                              \n"   
		  + " 	gl_FragColor = u_Color;    	\n"  	// Set the fragment colour
		  + "}                              \n";
	
	public static final String fragmentShader_FADEPOINT =
		    "precision mediump float;       \n"     // Set the default precision to medium. We don't need as high of a
		  + "uniform vec4 u_Color;			\n"     // triangle per fragment.
		
		  + "void main()                    \n"     // The entry point for our fragment shader.
		  + "{                              \n"   
		  +	"	float x = (gl_PointCoord.x * 2.0) - 1.0;	\n"		// Find the position of this fragment relative to the center of the glPoint.
		  +	"	float y = (gl_PointCoord.y * 2.0) - 1.0;	\n"
		  
		  + "	float Dist = sqrt((x * x) + (y * y)); 		\n"		// Find the Distance of this fragment from the center.
		  
		  + " 	gl_FragColor = u_Color;    					\n"  	// Set the fragment colour
		  
		  + "	if(Dist > 1.0)								\n"		// if the distance from the center is less than 0.1 we set the alpha value to 1
		  + "	{                              				\n"		// this creates a solid center for the particle and creates the illusion of glow
		  + "  		gl_FragColor.a = 0.0;					\n"		// without using glow shaders.
		  + "	}                              				\n"

		  + "}                              				\n";
	
	public static final String vertexShader_SCROLLTEX = 
			"uniform mat4 u_MVPMatrix;   \n"    
	 	+	"attribute vec4 a_Position;  \n" 
		+   "attribute vec2 a_TexCoord;  \n"
	 	
		+   "varying vec2 v_TexCoord;    \n"
	 	
	    +   "void main()                 \n" 
	    +   "{"  
	    +	"   v_TexCoord = a_TexCoord; \n"
	    +   "	gl_Position = u_MVPMatrix * a_Position; \n" // the matrix must be included as a modifier of gl_Position  
	    +   "}";
	
	public static final String fragmentShader_SCROLLTEX =
	        "precision mediump float;		\n" 
	    +   "uniform vec4 u_Color;			\n" 
	    + 	"uniform sampler2D u_Texture;	\n"
	    +   "uniform vec2 u_TexOffset;		\n"	
	    
		+   "varying vec2 v_TexCoord;    	\n"
	    
	    +   "void main()"
	    +   "{" 
	    +   "	gl_FragColor = u_Color * texture2D(u_Texture, v_TexCoord + u_TexOffset);" 
	    
	    +   "   float x = 2.0 - v_TexCoord.x;		\n" 
	    +   "   float y = 2.0 - v_TexCoord.y;		\n" 
	   
		+ 	"	float Dist = (sqrt((x * x) + (y * y)) * 0.5); 		\n"		// Find the Distance of this fragment from the center.
		+	"	float Alpha = 1.0 - ((Dist - 0.4) / 0.6);			\n"		
		+ 	"	gl_FragColor.a = Alpha * 0.5 * u_Color.a;	\n"
	    +   "}";
	
	public static final String vertexShader_SCREENQUAD =
		 "attribute vec4 a_Position;" 
	   + "attribute vec2 a_TexCoord;"
				 
	   + "varying vec2 v_TexCoord;"
			
	   + "void main()" 
	   + "{"
	   + "  	gl_Position = a_Position;" 
	   + "		v_TexCoord = a_TexCoord;"
	   + "}";
	
	public static final String fragmentShader_ADD =
		  "precision mediump float;" 
		+ "uniform sampler2D u_TextureA;" 
		+ "uniform sampler2D u_TextureB;" 
		+ "varying vec2 v_TexCoord;"

		+ "void main(void)"
		+ "{"
		+ "		gl_FragColor = texture2D(u_TextureA, v_TexCoord) + texture2D(u_TextureB, v_TexCoord);"
		+ "}";
	
	
	public static final String fragmentShader_BLURV =
		  "precision mediump float;"
		+ "uniform sampler2D u_Texture;" // this should hold the texture rendered by the horizontal blur pass
		+ "uniform float u_GlowIntensity;"
		+ "varying vec2 v_TexCoord;"
	 
		+ "const float blurSize = 1.0/256.0;"
	 
		+ "void main(void)"
		+ "{"
		+ "   vec4 sum = vec4(0.0);"
	 
	   // blur in y (vertical)
	   // take nine samples, with the distance blurSize between them
		+ "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y - 4.0*blurSize)) * 0.05;"
		+ "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y - 3.0*blurSize)) * 0.09;"
		+ "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y - 2.0*blurSize)) * 0.12;"
		+ "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y - blurSize)) * 0.15;"
		+ "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y)) * 0.16;"
		+ "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y + blurSize)) * 0.15;"
		+ "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y + 2.0*blurSize)) * 0.12;"
		+ "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y + 3.0*blurSize)) * 0.09;"
		+ "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y + 4.0*blurSize)) * 0.05;"
	 
		+ "   gl_FragColor = sum * u_GlowIntensity;"

		+ "}";
	
	public static final String fragmentShader_BLURH = 
		  "  precision mediump float;"
		+ "  uniform sampler2D u_Texture;" // the texture with the scene you want to blur
		+ "	 uniform float u_GlowIntensity;"
		+ "  varying vec2 v_TexCoord;"
	 
		+ "const float blurSize = 1.0/256.0;"
		
		+ "   void main(void)"
		+ "   {"
		+ "      vec4 sum = vec4(0.0);"
	 
	   // blur in x (Horizontal)
	   // take nine samples, with the distance blurSize between them
	   + "      sum += texture2D(u_Texture, vec2(v_TexCoord.x - 4.0*blurSize, v_TexCoord.y)) * 0.05;"
	   + "      sum += texture2D(u_Texture, vec2(v_TexCoord.x - 3.0*blurSize, v_TexCoord.y)) * 0.09;"
	   + "      sum += texture2D(u_Texture, vec2(v_TexCoord.x - 2.0*blurSize, v_TexCoord.y)) * 0.12;"
	   + "      sum += texture2D(u_Texture, vec2(v_TexCoord.x - blurSize, v_TexCoord.y)) * 0.15;"
	   + "      sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y)) * 0.16;"
	   + "      sum += texture2D(u_Texture, vec2(v_TexCoord.x + blurSize, v_TexCoord.y)) * 0.15;"
	   + "      sum += texture2D(u_Texture, vec2(v_TexCoord.x + 2.0*blurSize, v_TexCoord.y)) * 0.12;"
	   + "      sum += texture2D(u_Texture, vec2(v_TexCoord.x + 3.0*blurSize, v_TexCoord.y)) * 0.09;"
	   + "      sum += texture2D(u_Texture, vec2(v_TexCoord.x + 4.0*blurSize, v_TexCoord.y)) * 0.05;"

	   + "      gl_FragColor = sum * u_GlowIntensity;" 
	   + "   }";
}









































