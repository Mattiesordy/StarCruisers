package com.example.starcruiser;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.example.starcruiser.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.FloatMath;

/**
 * @author impaler
 *
 */
public class SpriteStation {
	float currentPositionX=-4;
	float currentPositionY=4;
	float nextPositionX=0;
	float nextPositionY=0;
	float speedY =0;
	float speedX =0;
	float time =0;
	private float positionX=-4f;
	private float positionY=4f;
	private float dx =0f; //1 global coord a sec
	private float dy =0f;
	private float velocity = 1f;
	float destinationX=0;
	float destinationY=0;
	private FloatBuffer vertexBuffer;	// buffer holding the vertices
	public float vertices[] = {
			-.6f, -.6f,  0.0f,		// V1 - bottom left
			-.6f,  .6f,  0.0f,		// V2 - top left
			 .6f, -.6f,  0.0f,		// V3 - bottom right
			 .6f,  .6f,  0.0f			// V4 - top right
	};

	private FloatBuffer textureBuffer;	// buffer holding the texture coordinates
	private float texture[] = {    		
			// Mapping coordinates for the vertices
			0.0f, 1.0f,		// top left		(V2)
			0.0f, 0.0f,		// bottom left	(V1)
			1.0f, 1.0f,		// top right	(V4)
			1.0f, 0.0f		// bottom right	(V3)
	};
	
	/** The texture pointer */
	private int[] textures = new int[1];

	public SpriteStation() {
		// a float has 4 bytes so we allocate for each coordinate 4 bytes
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		
		// allocates the memory from the byte buffer
		vertexBuffer = byteBuffer.asFloatBuffer();
		
		// fill the vertexBuffer with the vertices
		vertexBuffer.put(vertices);
		
		// set the cursor position to the beginning of the buffer
		vertexBuffer.position(0);
		
		byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuffer.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
	}

	/**
	 * Load the texture for the square
	 * @param gl
	 * @param context
	 */
	public void loadGLTexture(GL10 gl, Context context) {
		// loading texture
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.station);
		
		gl.glEnable(GL10.GL_BLEND);  //These two lines enable transparency
	    gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
	    
	    
		// generate one texture pointer
		gl.glGenTextures(1, textures, 0);
		// ...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		// create nearest filtered texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		//Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
//		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
//		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
		
		// Use Android GLUtils to specify a two-dimensional texture image from our bitmap 
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		
		// Clean up
		bitmap.recycle();
	}

	
	/** The draw method for the square with the GL context */
	public void draw(GL10 gl) {
		// bind the previously generated texture
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		// Point to our buffers
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		// Set the face rotation
		gl.glFrontFace(GL10.GL_CW);
		
		// Point to our vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		//Magic command below must use!
		//g GLU.gluProject Detects the verticies point in on screen coordinates
		/*What I meant about GLU.Project is basically doing the reverse of unproject.
		 Unproject takes where the user clicks on the screen and converts it into 3D coordinates.
		 Project takes your 3D objects and turns them into (2D) screen coordinates
		 */
		//movement(0f,0f);
		//gl.glLoadIdentity();
		//gl.glMatrixMode(GL10.GL_MODELVIEW);
		//gl.glPushMatrix();
		gl.glTranslatef(positionX, positionY, 0);
		//gl.glTranslatef(0, 0, 0);
		// Draw the vertices as triangle strip
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);

		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
	}
	
	public void setDestination(float x ,float y){
		destinationX=x;
		destinationY=y;
	}
	
	public void setCurrentPosition(float x, float y){
		currentPositionX=x;
		currentPositionY=y;
	}
	
	public void setPosition(float x, float y){
		positionX=x;
		positionY=y;
	}
	
	//this will be the value passed to the array as the currentPos for the array.
	//basically the next frame to draw
	public void setNextPosition(float x, float y){
		nextPositionX=x;
		nextPositionY=y;
	}
	
	public void movement(long timeElapsed){
		//dx =-5f;
		//dy =2f;
		//if(dx!=6){
		//	dx=dx+.5f;
		//}
		//if(dx==6){
		//	dx=1;
		//}
		
		//given current position find the distance between
		//Maybe able to do the speed calcs once to save loop time later on OR NOT
		float distanceX = destinationX - currentPositionX;
        float distanceY = destinationY - currentPositionY;
        float distanceToTravel=FloatMath.sqrt(distanceX * distanceX + distanceY * distanceY); 
        speedX = distanceX/(distanceToTravel/velocity*1000);//one globalUnit a second
        speedY = distanceY/(distanceToTravel/velocity*1000);
        //timeElapsed= timeElapsed/1000;
        time=timeElapsed;
        speedX = speedX*timeElapsed;
        speedY = speedY*timeElapsed;
        if(destinationX < currentPositionX){
        	speedX=speedX*-1;
        	currentPositionX=currentPositionX-speedX;
        }else{
        	currentPositionX=currentPositionX+speedX;
        }
        if(destinationY <currentPositionY){
        	speedY=speedY*-1;
        	currentPositionY=currentPositionY-speedY;
        }else{
        	currentPositionY=currentPositionY+speedY;
        }
        
        setCurrentPosition(currentPositionX, currentPositionY);
        
        //should be able to get current position after by doing fighter.currentPositionX
        
        //move at the same X and Y but if there is  more X or Y you dive the X by Y 
        //for the last X move
        // < 1 Y and 3X Y/X
        // .9 /3 Y will move .3 for each 1 X should give a smooth movement
        
        /**
		if(fighterDX > 0){
			unitsXY[0][unit] = unitsXY[0][unit] + fSpeedX;
			fighterDX = fighterDX-fSpeedX;
			if(fighterDX < fSpeedX){
				fighterDX =0;
			}
		}else if(fighterDX < 0){
			unitsXY[0][unit] = unitsXY[0][unit] - fSpeedX;
			fighterDX = fighterDX+fSpeedX;
			if(fighterDX > fSpeedX){
				fighterDX =0;
			}
		}else if(fighterDX == 0){
			unitsXY[0][unit] = unitsXY[3][unit];
		}
		if(fighterDY > 0){
			unitsXY[1][unit] = unitsXY[1][unit] + fSpeedY;
			fighterDY = fighterDY-fSpeedY;
			if(fighterDY < fSpeedY){
				fighterDY =0;
			}
		}else if(fighterDY < 0){
			unitsXY[1][unit] = unitsXY[1][unit] - fSpeedY;
			fighterDY = fighterDY+fSpeedY;
			if(fighterDY > fSpeedY){
				fighterDY =0;
			}
		}else if(fighterDY == 0){
			unitsXY[1][unit] = unitsXY[4][unit];
		}
		
		if(unitsXY[0][unit] == unitsXY[3][unit] && unitsXY[1][unit] == unitsXY[4][unit] ){
			unitsXY[2][unit] =0;
		}
		*/
	}
	
}
