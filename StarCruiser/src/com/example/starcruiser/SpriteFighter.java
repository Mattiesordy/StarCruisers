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
public class SpriteFighter {
	float currentPositionX=1;
	float currentPositionY=1;
	float nextPositionX=0;
	float nextPositionY=0;
	float distanceX;
	float distanceToTravel=0;
	float speedY =0;
	float speedX =0;
	float time =0;	
	private float positionX=0;
	private float positionY=0;
	private float dx =0f; //1 global coord a sec
	private float dy =0f;
	private float velocity = 1.5f; //1 global coordinate a second
	private float rotationalVelocity = .4f;// 30 degrees a second
	float destinationX=0;
	float destinationY=0;
	float currentRotation =0;
	float startingRotation =0;
	float finalRotation = 0;
	boolean finalRotCalc =false;
	private FloatBuffer vertexBuffer;	// buffer holding the vertices
	public float vertices[] = {
			-.5f, -.5f,  0.0f,		// V1 - bottom left
			-.5f,  .5f,  0.0f,		// V2 - top left
			 .5f, -.5f,  0.0f,		// V3 - bottom right
			 .5f,  .5f,  0.0f			// V4 - top right
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

	public SpriteFighter() {
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
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.fighter);
		
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
		//gl.glRotatex(90, 0, 0, 0);
		
		
		gl.glTranslatef(positionX, positionY, 0);
		gl.glRotatef(currentRotation, 0,0, 1);
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
	
	public void setTimeElapsed(long timeElapsed){
		
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
		 distanceX = destinationX - currentPositionX;
        float distanceY = destinationY - currentPositionY;
        // this is to make the it so that fighter stops moving.
      
        
        distanceToTravel=FloatMath.sqrt(distanceX * distanceX + distanceY * distanceY); 
        
        //call Rotation first 
        //unitRotation();
       if(unitRotation(timeElapsed) == true){//should cause the rotation before movement
        
        speedX = distanceX/(distanceToTravel/velocity*1000);//one globalUnit a second
        speedY = distanceY/(distanceToTravel/velocity*1000);
        //timeElapsed= timeElapsed/1000;
        time=timeElapsed;
        speedX = speedX*timeElapsed;
        speedY = speedY*timeElapsed;
        
        if(destinationX < currentPositionX){
        	speedX=speedX*-1;
        	if(currentPositionX-speedX<destinationX){
        		destinationX = currentPositionX;
        	}else{
        	currentPositionX=currentPositionX-speedX;
        	}
        }else{
        	if(currentPositionX+speedX>destinationX){
        		destinationX = currentPositionX;
        	}else{
        	currentPositionX=currentPositionX+speedX;
        	}
        }
        
        if(destinationY <currentPositionY){
        	speedY=speedY*-1;
        	if(currentPositionY+speedY<destinationY){
        		destinationY = currentPositionY;
        	}else{
        	currentPositionY=currentPositionY-speedY;
        	}
        	//currentPositionY=currentPositionY-speedY;
        }else{
        	if(currentPositionY+speedY>destinationY){
        		destinationY = currentPositionY;
        	}else{
        	currentPositionY=currentPositionY+speedY;
        	}
        	
        	//currentPositionY=currentPositionY+speedY;
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
	
	public void setFinalRotCalcBool(float rotBool){
		if(rotBool == 1){    //calculate the final roation var
		finalRotCalc = true;
		}else{
			finalRotCalc=false;
		}
	}
	
	public float getFinalRotCalcBool(){
		if(finalRotCalc == true){
			return 1;
		}else{
			return 0;
		}
	}
	
	public float getCurrentRotation(){
		return currentRotation;
	}
	
	public void setCurrentRotation(float currentRot){
		currentRotation = currentRot;
	}
	
	public void setFinalRotation(float finalRot){
		finalRotation = finalRot;
	}
	public float getFinalRotation(){
		return finalRotation;
	}
	
	public void setStartingRotation(float startRot){
		startingRotation = startRot;
	}
	
	public float getStartingRotation(){
		return startingRotation;
	}
	
	//will handle rotating the object to face the current destination
	public void calculateFinalRotation(){
		//Should not be recalculated unless destination changes
		//q3
		if(currentPositionX >= destinationX && currentPositionY >= destinationY ){
			//Multiply by -1 an subtract 90
			finalRotation = (float) Math.toDegrees(Math.asin(Math.abs(currentPositionY - destinationY)/distanceToTravel))+180f;
		}
		//q2
		if(currentPositionX >= destinationX && currentPositionY <= destinationY){
			//add 90
			finalRotation = 180f- (float) (Math.toDegrees(Math.asin(Math.abs(destinationY-currentPositionY)/distanceToTravel)));
		} 
		//q1
		if(currentPositionX <= destinationX && currentPositionY <= destinationY){
			//nothing special
			finalRotation = (float) Math.toDegrees(Math.asin(Math.abs(destinationY-currentPositionY)/distanceToTravel));
		}
		//q4
		if(currentPositionX <= destinationX && currentPositionY >= destinationY){
			//multiply by -1
			finalRotation = 360f- (float) (Math.toDegrees(Math.asin(Math.abs(currentPositionY-destinationY)/distanceToTravel)));
		}
    }
	
	
	
	public boolean unitRotation(long elapsedtime){
		if(finalRotCalc == true){
			calculateFinalRotation();
			startingRotation = currentRotation;
			finalRotation = finalRotation - 90;
			finalRotCalc =false;
		}
		
		//To compensate for the picture position
		
		//if(finalRotation<0){
		//	finalRotation = 360 + finalRotation;
		//}
		//if(currentRotation == 0 ){
		//	currentRotation = 360;
		//}
		
		//if(currentRotation+179f>finalRotation){
		//Will rotate left no matter what
		currentRotation = currentRotation + (((Math.abs(finalRotation-startingRotation))/(rotationalVelocity*1000))*elapsedtime);
		
		//
		
		//currentRotation = currentRotation - (((Math.abs(finalRotation-startingRotation))/(rotationalVelocity*1000))*elapsedtime);
		
		//}else{
		//	finalRotation = finalRotation-360; //make negative rotation destination
		//Will rotate right?
		//currentRotation = currentRotation - (((Math.abs(finalRotation-currentRotation))/(rotationalVelocity*1000))*elapsedtime);
		//}
		//change this to a 360 rot equation.
		
		if(currentRotation>=finalRotation){
		//finalRotation = (float) Math.toDegrees(Math.asin(Math.abs(destinationY-currentPositionY)/distanceToTravel));
		currentRotation = finalRotation;
		
		}
		//currentRotation = currentRotation+1;
		//if(currentRotation == 360){
		//	return true;
		//}
		if(finalRotation != currentRotation){					
			return false;
		}else{
			return true;
		}
	}
    
	
}
