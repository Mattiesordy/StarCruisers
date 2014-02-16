package com.example.starcruiser;
import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


public class GLRendererTSC implements Renderer  {
	private Background background;
	private SpriteFighter fighter;
	private SpriteStation station;
	public Text text;
	public TextFont textObj;
	private Context context;
	 int pointers;
	private float distance;
	private float[] pMatrix;
	public static int[] viewport = new int[16];  
	public static float[] modelViewMatrix = new float[16];  
	public static float[] projectionMatrix = new float[16];  
	public static float[] pointInPlane = new float[16];  
	float[] newcoords = new float[4];
	float heightScreen;
	float widthScreen;
	float zoom= -2f;
	float left;
	float right; 
	float bottom;
	float top; 
    float near;
    float far;
    float ratio;
    float sides=0;
    float UD=0;
    float sidesNew=0;
    float UDNew=0;
    boolean touchEvent=false;
    float winX=0;
    float winY=0;
    float ratioCoord;
    float finalX;
	float finalY ;
	float screenYvis;
	float storeWinX;
	float storeWinY;
	float camTempX;
	float camTempY;
	int selected=-1;//means nothing was selected
	float[][] unitsXY = new float[5][11];
	boolean setUnitPicked =false;
	long previousTime=6;
	long currentTime=5;
	long elapsedLoopTime=1;// not equal to 0 because of the first time that it goes through.  Prevent divide by 0
	float FPS=0;
	float tempFPStime=0;
	boolean popupStore = false;
	boolean loadedFont = false;
	String myString = "";
	//private GLRendererTSC renderer;
	
	public GLRendererTSC(Context context,int width,int height){
		//widthScreen =(float)width;
		//heightScreen = (float)height;
		//heightScreen=4f;
		
		this.context = context;
		this.fighter = new SpriteFighter();
		this.station = new SpriteStation();
		this.background = new Background();
		this.text = new Text();
		
		
			//First Fighter
		    unitsXY[0][0] = .5f;//unit type: .5 ==fighter
		    unitsXY[0][1] = 1; //fighter 1 X current position
		    unitsXY[0][2] = 1; // fighter 1 Y current position
		    unitsXY[0][3] = 1; //  this will be a bool to draw 0==false 1 ==true Weather a unit exists or not
		    unitsXY[0][4] = 0; // X Destination
		    unitsXY[0][5] = 0; // Y Destination
		    unitsXY[0][6] = 0; //Unit state.  0 not moving 1 moving
		    unitsXY[0][7] = 0; // current rotation
		    unitsXY[0][8] = 0; //final rotation dest
		    unitsXY[0][9] = 0; //calculate new final rotation
		    unitsXY[0][10] = 0;//the original starting rotation location
		    //Station
		    unitsXY[1][0] = .6f;//unit type: .5 ==fighter
		    unitsXY[1][1] = -4; //fighter 1 X
		    unitsXY[1][2] = 4; // fighter 1 Y
		    unitsXY[1][3] = 1; //  this will be a bool to draw 0==false 1 ==true
		    unitsXY[1][4] = 0; // X Destination
		    unitsXY[1][5] = 0; // Y Destination
		    unitsXY[1][6] = 0;
		    unitsXY[1][7] = 0; // current rotation
		    unitsXY[1][8] = 0; //final rotation dest
		    unitsXY[1][9] = 0; //calculate new final rotation
		    unitsXY[1][10] =0;
		    
		    //Second Fighter-  For spawn test
		    unitsXY[2][0] = .5f;//unit type: .5 ==fighter
		    unitsXY[2][1] = -4; //fighter 1 X
		    unitsXY[2][2] = 6; // fighter 1 Y
		    unitsXY[2][3] = 0; //  this will be a bool to draw 0==false 1 ==true Weather a unit exists or not
		    unitsXY[2][4] = 0; // X Destination
		    unitsXY[2][5] = 0; // Y Destination
		    unitsXY[2][6] = 0; //Unit state.  0 not moving 1 moving
		    unitsXY[2][7] = 0; // current rotation
		    unitsXY[2][8] = 0; //final rotation dest
		    unitsXY[2][9] = 0; //calculate new final rotation
		    unitsXY[2][10] = 0;//the original starting rotation location
		    //previousTime = System.currentTimeMillis();
		   /** 
		    unitsXY[2][0] = fighterState;
		    unitsXY[2][1] = fighter2State;
		    unitsXY[2][2] = fighter3State;
		    //unitsXY[2][3] = buttonState; 
		    
		    unitsXY[3][0] = fighterDestX;
		    unitsXY[3][1] = fighterDestX2;
		    //unitsXY[3][2] = fighterDestX3;
		    
		    unitsXY[4][0] = fighterDestY;
		    unitsXY[4][1] = fighterDestY2;
		    //unitsXY[4][2] = fighterDestY3;
		*/
		    
	}
//this
    public void onDrawFrame(GL10 gl) {
    	//if(tempFPStime !=0){
    	
    	elapsedLoopTime = previousTime-currentTime;
    	
    	//}
    	//
    	//tempFPStime++;
    	currentTime=System.currentTimeMillis();
    	
    	tempFPStime=currentTime;
    	
    	//tempFPStime =currentTime;
    	//elapsedLoopTime = currentTime-previousTime;
    	//current time 
    	//elapsedLoopTime;
    	
    	//FPS =FPS+1f;
    	//if(tempFPStime <= 0){
    		
    	///	tempFPStime=1000;
    	//	FPS=0;
    	//}
    	
    	// clear Screen and Depth Buffer
    	        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
    	
    	        // Reset the Modelview Matrix    	
    	        gl.glLoadIdentity();  	
    	        gl.glMatrixMode(GL10.GL_MODELVIEW);
    	       // modelViewMatrix=gl.glMultMatrixx(GL10.GL_MODULATE, 0);
    	        //gl.glMatrixMode(GL10.GL_PROJECTION);
    	        gl.glLoadIdentity();
    	        //gl.glG
    	       // gl.glGetIntegerv(arg0, arg1)glGetIntegerv( GL_VIEWPORT, m_viewport );
    	        
    	       //getXYconvert((GL11) gl);

    	       // if(touchEvent==true&&selected=0){
    	       if(touchEvent==true){
    	    	   	calcScreen();
               		convert();
               		
               		unitClickCheck();
               		//unitClickCheck();
               		
               		//if(winX!=storeWinX){
               		//camMovement();	
               		//}
               		setTouchEventFalse();
               		//setStoreTouch();
               		
    	       }
    	       //else if(selected=1){
    	       //		//do nothing
    			//}else if(selected
    	       
    	        // Drawing
    	        gl.glTranslatef(sides,UD, zoom);  
    	        
    	       // gl.glTranslatef(getXconvert()*-100, getYconvert()*-100, zoom);
    	        //GLU.gluOrtho2D(gl, 0.0f, widthScreen, 0.0f, heightScreen);
    	        
    	        
    	        // move 5 units INTO the screen    	
    	        //gl.glFrustumf(-10f, 10f, -10f, 10f, 0f, 10f);
    	        //gl.glOrthof(-10f, 10f, -15f, 15f, -1f, 1f);
    	        //gl.glOrthof(0, widthScreen, heightScreen, 0, 1, -1);
    	        //((Object) gl).glOrtho2D();
    	       
    	        
    	        background.draw(gl);    	        // is the same as moving the camera 5 units away    	
    	        //try {
				//	textObj.LoadFont("font.bff", gl);
				//} catch (IOException e) {
				//	// TODO Auto-generated catch block
				//	e.printStackTrace();
				//}
    	        //textObj.PrintAt(gl, "FPS: " + Float.toString(1000/elapsedLoopTime) , (int)widthScreen-50, (int)heightScreen-10);
    	        //Matrix.setIdentityM(fighter.vertices, 0);
    	        //Matrix.translateM(fighter.vertices, 0, 3f, 4f, 0f);
    	        //gl
    	        //fighter.setCurrentPosition(unitsXY[0][1], unitsXY[0][2]);
    	       // fighter.setCurrentPosition(unitsXY[0][1], unitsXY[0][2]);
				//fighter.setDestination(unitsXY[0][4], unitsXY[0][5]);
				//fighter.movement(elapsedLoopTime);
				//unitsXY[0][1] = fighter.currentPositionX;
    			//unitsXY[0][2] = fighter.currentPositionY;
    	        
    	        for(int i = 0; i<3; i++){
    	        if(unitsXY[i][3]==1 && unitsXY[i][0]== .5f){
    	        	
    	        fighter.setPosition(unitsXY[i][1],unitsXY[i][2]);
    	        	if(unitsXY[i][1]==unitsXY[i][4]){
    	        		unitsXY[i][6]=0;
    	        	}
    	        
    	        	if(unitsXY[i][6] == 1){
    	        		//if(unitsXY[])
    	        	fighter.setCurrentPosition(unitsXY[i][1], unitsXY[i][2]);
    				//This will check for a unit update.
    				fighter.setDestination(unitsXY[i][4], unitsXY[i][5]);
    				
    				//rotation settings
    				fighter.setFinalRotCalcBool(unitsXY[i][9]);
    				fighter.setCurrentRotation(unitsXY[i][7]);
    				
    				//if(unitsXY[i][9]!=1){
    					fighter.setFinalRotation(unitsXY[i][8]);
    					fighter.setStartingRotation(unitsXY[i][10]);
    				//}
    				fighter.movement(elapsedLoopTime);
    				
    				if(unitsXY[i][9]==1){
    					unitsXY[i][7]=fighter.getCurrentRotation();
    					unitsXY[i][8]=fighter.getFinalRotation();
    				}
    				unitsXY[i][9] = fighter.getFinalRotCalcBool();
    				
    				unitsXY[i][10] = fighter.getStartingRotation();
    				//if(unitsXY[i][9]==1){
    				//	unitsXY[i][8]=fighter.getFinalRotation();
    				//}else{
    				//	fighter.setFinalRotation(unitsXY[i][8]);
    				//}
    				unitsXY[i][7] = fighter.currentRotation;
    				//fighter.
    				//fighter.movement(elapsedLoopTime);
    				//text.myString=Float.toString( fighter.speedX);
    				unitsXY[i][1] = fighter.currentPositionX;
        			unitsXY[i][2] = fighter.currentPositionY;
        			
    	        	}
    	        	
    	        gl.glPushMatrix();
    	        //fighter.movement(0f, 0f);
    	        //fighter.d
    	        
    	        fighter.draw(gl);
    	        gl.glPopMatrix();
    	        }
    	        
    	        }//end of for loop.
    	        //fighter.setDestination(-2, -3);
    	        
    	        gl.glPushMatrix();
    	        //fighter.movement(0f, 0f);
    	        //fighter.d
    	        
    	        station.draw(gl);
    	        gl.glPopMatrix();
    	        //gl.glPushMatrix();
    	        //fighter.movement(0f, 0f);
    	        
    	       // fighter.draw(gl);
    	       // gl.glPopMatrix();
    	       // calcScreen();
    	        //convert();
    	       // text.myString=Float.toString(finalX);
    	        
    	        //working code below
    	        //text.loadGLTexture(gl, this.context);
    	        //now called in draw
    	        gl.glPushMatrix();
    	        //text.loadGLTexture(gl, this.context);
    	        text.draw(gl);
    	        
    	        //textObj.fntCellHeight = 100;
    	        //textObj.fntCellWidth = 100;
    	        //textObj.PrintAt(gl, "HELO WORLD", 100, 300);
    	        //.PrintAt(gl, "HELLO WORLD");
    	        gl.glPopMatrix();
    	        // Draw the triangle
    	       
    	        //textObj.PrintAt(gl, "FPS: " + Float.toString(1000/elapsedLoopTime) , (int)widthScreen-80, (int)heightScreen-30);
    	        //textObj.PrintAt(gl, "ROT: " + Float.toString(unitsXY[0][10]) , (int)widthScreen-80, (int)heightScreen-30);
    	        myString = Float.toString(fighter.finalRotation);
    	        textObj.PrintAt(gl, "ROT: " + myString , (int)widthScreen-90, (int)heightScreen-30);
    	  previousTime= System.currentTimeMillis();

    }
    public void onSurfaceChanged(GL10 gl, int width, int height) {
    	if(height == 0) {                   //Prevent A Divide By Zero By    		
    	height = 1;                         //Making Height Equal One
    	}
    	widthScreen=width;
    	heightScreen=height;
    	 //gl.glViewport(0, 0, width, height);     //Reset The Current Viewport    
    	 ratio = (float) width / height;
    	         gl.glMatrixMode(GL10.GL_PROJECTION);    //Select The Projection Matrix    	 
    	         gl.glLoadIdentity();                    //Reset The Projection Matrix
    	         //Matrix.frustumM(
    	        //         pMatrix, 0, 
    	        //         zoom*left, zoom*right, zoom*bottom, zoom*top, 
    	        //         near, far);
    		    //Calculate The Aspect Ratio Of The Window    	 
    	         GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
    	         //text.myString=Float.toString((float)height);
    	         //text.myString=Float.toString(finalX);
    	         //GLU.gl
    	         gl.glMatrixMode(GL10.GL_MODELVIEW);     //Select The Modelview Matrix
    	         gl.glLoadIdentity();                    //Reset The Modelview Matrix
    	// gl.glFrustumf(left*zoom, right*zoom, bottom*zoom, top*zoom, near, far);
             ///gl.glFrustumf(-1, 1, -1, 1, -10, 0);
    	      //  gl
    	     //    gl.glLoadIdentity();
    	      //   ((GL10) gl).glGetIntegerv(GL10.GL_VIEWPORT, viewport, 0);
    	      //   ((GL10) gl).glGetFloatv(GL10.GL_MODELVIEW, modelViewMatrix, 0);
    	      //   ((GL10) gl).glGetFloatv(GL10.GL_PROJECTION_MATRIX, projectionMatrix, 0);	         
    	         //gl.glGetIntegerv(GL10.GL_MAX_VIEWPORT_DIMS, results, 0);
    	         //gl.glG
    	         //GL10.Gl_ma
    }

    

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    	// Load the texture for the square
    			this.textObj = new TextFont(context, gl);
    			try {
					textObj.LoadFont("font.bff", gl);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			//try {
				//	textObj.LoadFontAlt("exportedfont.bmp", gl);
				//} catch (IOException e) {
					// TODO Auto-generated catch block
				//	e.printStackTrace();
				//}
    			//textObj.SetCursor(100, 150);
    			//textObj.SetScale(1);
    			//textObj.SetPolyColor(0f, 0f, 0f);
    			fighter.loadGLTexture(gl, this.context);
    			station.loadGLTexture(gl, this.context);
    			background.loadGLTexture(gl,this.context);
    			text.loadGLTexture(gl, this.context);
    			
    			//gl.glViewport(0, 0, widthScreen, heightScreen);
    		   // gl.glOrthof(0, 320f, 480f, 0, 0, 1);
    			
    			gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping ( NEW )
    			gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
    			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background
    			gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
    			gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
    			gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
    			
    			
    			//Really Nice Perspective Calculations
    			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST); 
    	
    }
    //, event.getX(),event.getY()
    //float winX, float winY
    public void getXYconvert(GL11 gl){
    	//float winZ;
    	//GL11.GL_VIEWPORT
    	gl.glGetIntegerv(GL11.GL_VIEWPORT, viewport, 0);
        ((GL11) gl).glGetFloatv(GL11.GL_MODELVIEW_MATRIX, modelViewMatrix, 0);
        ((GL11) gl).glGetFloatv(GL11.GL_PROJECTION_MATRIX, projectionMatrix, 0);
        
       // winY = (float)viewport[3] - (float)winY;
        //glReadPixels( winX, int(winY), 1, 1, GL_DEPTH_COMPONENT, GL_FLOAT, &winZ );
       // GLU.gluProject(0, 0, 40, modelViewMatrix, 0, projectionMatrix, 0, viewport, 0, newcoords, 0);
        GLU.gluUnProject(winX, winY, 0, modelViewMatrix, 0, projectionMatrix, 0, viewport, 0, newcoords, 0);
       // GLU.glu
    }
    public float getXconvert(){
    	return (newcoords[0]/newcoords[3]);
    }
    public float getYconvert(){
    	return (newcoords[1]/newcoords[3]);
    }
/**    
 * 
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
        case MotionEvent.ACTION_DOWN:
            pointers = 1;
            return true;
        case MotionEvent.ACTION_POINTER_2_DOWN:
            pointers = 2;
            distance = fingerDist(event);
            return true;
        case MotionEvent.ACTION_MOVE:
            if( pointers == 2 ){
                float newDist = fingerDist(event);
                float d = distance/newDist;
				//GLRendererTSC renderer = ;
                //The zoom is called from another file not form within the render file but
                //thats a guess.  I imagine it will work fine here.
				zoom(d);
                distance = newDist;
            }
            return true;
        default:
            return false;
        }

        }
    
    protected final float fingerDist(MotionEvent event){
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }
    */
    public final void zoom(float mult){
        //zoom *= mult;
    	//if(mult<-30||mult>-1){
    	//	mult=-10;
    	//}
    	//mult=mult-50;
    	
    	//was no plus there was a * instead
    	if(((zoom *= mult) <-40)){
    		zoom =-40;
    	}else if ((zoom *= mult)>-1){
    		zoom=-1;
    	}else{
    		zoom *= mult;
    	}
    	
    
		//Matrix.frustumM(
      //          pMatrix, 0, 
      //          zoom*left, zoom*right, zoom*bottom, zoom*top, 
      //          near, far);
    }
    
    public void setStoreTouch(MotionEvent event){
    	storeWinX=convertX(event.getX());
    	storeWinY=convertY(event.getY());
    	
    }
        
   // public void setScreenSize(int width,int height){
    //	widthScreen = width;
   // 	heightScreen = height;
    //	;
    //}
    
    public void touchLocation(MotionEvent event){
    	//winXorg=event.getX
    	//winYorg=
    	winX=event.getX();
    	winY=event.getY();
    }
    
    public void setTouchEventFalse(){
    	touchEvent=false;
    }
    
    public void calcScreen(){
    	screenYvis = (float) (((zoom*-1)/(Math.sin(67.5))) * Math.sin(22.5)); //only half of the height
    	ratioCoord=  (heightScreen/2f)/screenYvis;
    	
    	
    	//float screenXvis = widthScreen/ratioCoord; // all of the width
    }
    
    //public void input(){
    //	if(){
    		
    //	}
    	//winX
   // }
    public void camMovement(){
    	
    	float camMoveX=0;
    	float camMoveY=0;
    	
    	camMoveX = storeWinX*-1 + finalX;
    	camMoveY = storeWinY*-1 + finalY;
    	
    	
		//getXYconvert((GL11) gl);
		//event.getY
		//sides = tempConvert(event.getX(),1)/500f+sidesNew;
		//event.
       // UD = tempConvert(event.getY(),2)/500f+UDNew;
    	//if(sides >= 30f){
    	//	sides =30f;
    	//}else if(sides <= -30f){
    	//	sides = -30f;
    	//}else{
    	
    	// 	
    	
    		sides=(camMoveX + camTempX);	
    		
    	//}
    	//if(sides >=15){
    	//	UD = 15f;
    	//} else if(UD <= -15f){
    	//	UD = -15f;
    	//}else{
    		UD =(camMoveY + camTempY);
    	//}
    	
	}
	
  //  public final void camSavePosition(MotionEvent event){
    //sidesNew =sides;
    //UDNew =UD;
    //}

    
    public float tempConvert(float var, int XorY){
    	//1=x
    	//2=y
    	if(XorY==1){
    		float centerX = widthScreen/2f;
    		return (var-centerX);
    	}else if(XorY==2){
    		float centerY = heightScreen/2f;
    		return (centerY-var);
    	}else{
    		return 0;
    	}
    	
    	//convert the touch x and y into the new coordinate system
    	  	
    }
    
    
    //This this should be used to conver the touch pixels coord to
    //the vertex coord
    public void convert(){
    	//get the center of the screen in pixels
    	float centerY = heightScreen/2f;
    	float centerX = widthScreen/2f;
    	
    	//convert the touch x and y into the new coordinate system
    	float tempX=winX-centerX;
    	float tempY=centerY-winY;
    	
    	//figure out the vertex per pixel
    	//float tempXvert= widthScreen/ratioCoord;//the 30 will be another var that will change based on the zoom lvl
    	//float tempYvert= heightScreen/ratioCoord;
    	
    	//final XY value converted to vertex form
    	//float finalX = tempX/tempXvert;d
    	//float finalY = tempY/tempYvert;
    	finalX = tempX/ratioCoord;
    	finalY = tempY/ratioCoord;
    	
    	/**
    	//conversion onf these coordinates to the global coordinate system
    	//once passed the cameras global xy position
    	float globalCamX=0;//magically passed vals later
    	float globalCamY=0;
    	float localCamX=0;
    	float localCamY=0;
    	
    	//changed local coord center to local.
    	// Same equation needs to be applied to all vertex on the screen
    	//Thinkt hat the above is wrong becasue we will only use for touch pouints
    	if(globalCamX<0){
    		localCamX=globalCamX+globalCamX*-1;
    	}else if(globalCamX>0){
    		localCamX=globalCamX+globalCamX*-1;
    	}else{
    		localCamX=globalCamX;
    	}
    	
    	if(globalCamY<0){
    		localCamY=globalCamY+globalCamY*-1;
    	}else if(globalCamY>0){
    		localCamX=globalCamY+globalCamY*-1;
    	}else{
    		localCamX=globalCamY;
    	}
    	
    	//New
    	if(sides<0){
    		localCamX=sides+sides*-1;
    	}else if(sides>0){
    		localCamX=sides+sides*-1;
    	}else{
    		localCamX=sides;
    	}
    	
    	if(globalCamY<0){
    		localCamY=globalCamY+globalCamY*-1;
    	}else if(globalCamY>0){
    		localCamX=globalCamY+globalCamY*-1;
    	}else{
    		localCamX=globalCamY;
    	}
    	*/
    	//new new
    	//finalX = finalX+sides;
    	//finalY = finalY+UD;
    	
    	//sides=finalX;
    	//UD=finalY;
    	//---------------------------------------------
    	//this section will convert the local to the differnet zoom lvls
    	//Hopefully
    	
    }
    
    public void setCamTemp(){
    	camTempX = sides;
    	camTempY = UD;
    }
    //generated the converted Local coordinate system
    public float convertX(float pixelX){
    	
    	float centerX = widthScreen/2f;
    	float tempX=pixelX-centerX;
    	float convertedX = tempX/ratioCoord;
    	
    	return convertedX;
    }
    
    public float convertY(float pixelY){
    	
    	float centerY = heightScreen/2f;
    	float tempY=centerY-pixelY;
    	//float tempY=pixelY-centerY;
    	float convertedY = tempY/ratioCoord;
    	
    	return convertedY;
    }
    
    public float convertGlobalX(float pixelX){
    	float tempLocalX = convertX(pixelX);    	
    	return(tempLocalX + sides*-1);
    }
    
    public float convertGlobalY(float pixelY){
    	float tempLocalY = convertY(pixelY);    	
    	return(tempLocalY + UD*-1);
    }
    
    public void unitClickCheck(){
    	//array[i][j] i==which unit j==stats for which unit
    	float size =0;
    	//if(selected ==0){
    	
    	//if(selected == -1){
    	//First Loop will check if OWN ship was selected.
    	for(int i =0; i<3;i++){
    		size = unitsXY[i][0];
    		
    		 //this would be the type of ship ie .5 == fighter
    		//if( (((unitsXY[i][1]+size >= convertX(winX))) &&(unitsXY[i][1]-size <= convertX(winX))) && ((unitsXY[i][2]+size >= convertY(winY))&&(unitsXY[i][2]-size <= convertY(winY)))){
    		//if( (unitsXY[i][1]+size >= convertX(winX) && unitsXY[i][1]-size <= convertX(winX) )&& (unitsXY[i][2]+size >= convertY(winY) && unitsXY[i][2]-size <= convertY(winY) )){
    		if( ((unitsXY[i][1]+size) >= convertGlobalX(winX) && (unitsXY[i][1]-size) <= convertGlobalX(winX)) && ((unitsXY[i][2]+size) >= convertGlobalY(winY) && (unitsXY[i][2]-size) <= convertGlobalY(winY))){ 			
    			selected = i;
    			//text.myString=Float.toString(selected);
    		    setUnitPicked=true;
    		//	i=5; //break to end the for loop
    		}//else{
    		//selected =-1;
    		//}
    		
    		}
    	//}
    	//}
    	
    		//if(setUnitPicked !=true){
    		//	selected =-1;
    		//}
    	
    	//}
    	//This for loop will check if Enemy ship was selected.
    	
    	//if(selected ==0){
    	//for(int i =0; i<4;i++){
    	//	size = unitsXY[i][0]; //this would be the type of ship ie .5 == fighter
    	//	if(((unitsXY[i][1] <= convertX((winX)+size))&&(unitsXY[i][1] >=convertX((winX)-size))) && ((unitsXY[i][2] <= convertY(winY+size)&&(unitsXY[i][2] >= convertY(winY-size))))){
    	//		selected = i;
    	//		setUnitPicked=true;
    	//		break;
    	//	}else{
    	//		selected =0;
    			//setFlag=nounitpicked
    	//	}
    	//}
    	
    		//myString
    		if((selected >=0) && (setUnitPicked == true)){
    			
    			if(unitsXY[selected][0] == .6f){
    				//this is where a menu pop up command would exist for now a text indicator
    				//text.myString=Float.toString(unitsXY[selected][0]);
    				//text.myString = "true";
    				popupStore = true;
    				//myString = Boolean.toString(popupStore).toUpperCase();
    				selected =-1;
    				//setUnitPicked = ;
    			}
    		}
    		if(selected >=0 && (setUnitPicked==false)){
    		//if(pickedFlag == 1)}{
    			unitsXY[selected][4] = convertGlobalX(winX);    			
    			unitsXY[selected][5] = convertGlobalY(winY);
    			if(unitsXY[selected][0] == .5){
    				fighter.setCurrentPosition(unitsXY[selected][1], unitsXY[selected][2]);
    				//This will check for a unit update.
    				
    				fighter.setDestination(unitsXY[selected][4], unitsXY[selected][5]);
    				unitsXY[selected][9] = 1;
    				fighter.movement(elapsedLoopTime);
    				
    				unitsXY[selected][1] = fighter.currentPositionX;
        			unitsXY[selected][2] = fighter.currentPositionY;
        			
        			unitsXY[selected][6] = 1;
        			selected=-1;
        			//setUnitPicked=true;
    			}
    		//	if(unitsXY[selected][0] == .6){
    				//this is where a menu pop up command would exist for now a text indicator
    		//		text.myString=Float.toString(convertGlobalX(winX));
    				//popupStore = true;
    			//	selected =-1;
    			//}//else{
    				
    			//}
    		}else if(selected < 0 &&(setUnitPicked==false)){
    			
    			
    		camMovement();
    		//selected =-1;
    	}
    		
    		setUnitPicked =false;
    		//popupStore = false;
    }
    
	
}

