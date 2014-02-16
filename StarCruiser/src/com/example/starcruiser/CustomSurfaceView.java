package com.example.starcruiser;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import java.lang.Math;
//import java.lang.Object;

import com.example.starcruiser.R;

public class CustomSurfaceView extends Activity{
	//can implement runnable to make the Prog use multithreads but
	//as of now I dont see a been benefit.  Only a hinderence.
	customView v;
	public GLSurfaceView mGLSurfaceView;
	public GLRendererTSC renderer;
	//Display display = getWindowManager().getDefaultDisplay(); 
	Point fighter1Position = new Point();
	Point fighter2Position = new Point();
	Point fighter3Position = new Point();
	
	//New vars for new onTouch Method
	private int pointers;
	private float distance;
	private float[] pMatrix;
	float zoom;
	float left;
	float right; 
	float bottom;
	float top; 
    float near;
    float far;
	
	int windowWidth=0;
	int windowHeight=0;
	Bitmap fighter;
	Bitmap background;
	Bitmap button;
	float fighterX,fighterY,fighterX2,fighterY2,fighterX3,fighterY3,fighterDestX,fighterDestY,fighterDestX2,fighterDestY2,fighterDestX3,fighterDestY3;
	int fighterState,fighter2State,fighter3State,buttonState; //# 0 means not moving, #1 means moving, going to need another state...an it exists state maybe make 10 or greater mean it exits?
	float touchLocationX, touchLocationY;
	float fSpeedX = 3,fSpeedY =3;
	float fighterDX,fighterDY;
	float destinationX,destinationY;
	float[][] unitsXY = new float[5][4];
	int[][] units = new int[2][4];
	int unitsXYcolumnLen=3;
	int unitsXYrowLen=2;
	int selectState=0;//#0 no previous select,# 
	int previousSelect=0;
	int currentSelect=0; //#0 means nothing selected, #1 fighter1, #2 fighter2, #3 fighter3,#4 button
	boolean dest = false;
	private static final int INVALID_POINTER_ID = -1;
	float mLastTouchX=0;
	float mLastTouchY=0;
	float newDist=0;

	// The ‘active pointer’ is the one currently moving our object.
	private int mActivePointerId = INVALID_POINTER_ID;

	
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    // TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 mGLSurfaceView = new GLSurfaceView(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    super.onCreate(savedInstanceState);
	    
	    //----------------------------------------------------------------------------------
	    //First attempt at opengl setup
	    // Check if the system supports OpenGL ES 2.0.
	    //final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    //final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
	    //final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
	 
	    //if (supportsEs2)
	    //{
	        // Request an OpenGL ES 2.0 compatible context.
	     //   mGLSurfaceView.setEGLContextClientVersion(2);
	 
	        // Set the renderer to our demo renderer, defined below.
	   //     mGLSurfaceView.setRenderer(newLessonOneRenderer());
	   // }
	    //else
	   // {
	        // This is where you could create an OpenGL ES 1.x compatible
	        // renderer if you wanted to support both ES 1 and ES 2.
	   //     return;
	   // }
	 
	    
	    
	    //setContentView(mGLSurfaceView);
	   // v = new customView(this);
	   // v.setOnTouchListener(this);
	    //old view.....end
	    //-------------------------------------------------------------------------------------
	    //NEw below
	    // Initiate the Open GL view and
	            // create an instance with this activity

	            //mGLSurfaceView = new GLSurfaceView(this);
	    Display display = getWindowManager().getDefaultDisplay(); 
    	int width = display.getWidth();
    	int height = display.getHeight();
    	height =3;
    	width = 3;
    	//renderer.setScreenSize(width, height);
	            // set our renderer to be the main renderer with
	            renderer = new GLRendererTSC(this,width,height);
	            // the current activity context
	            //mGLSurfaceView.setEGLConfigChooser(false);
	            //below works
	            //mGLSurfaceView.setRenderer(new GLRendererTSC(this));
	            mGLSurfaceView.setRenderer(renderer);
	            
	            setContentView(mGLSurfaceView);
	            
	            
//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	   // display.getSize(size);
	    windowWidth = getWindowManager().getDefaultDisplay().getWidth();
	    windowHeight = getWindowManager().getDefaultDisplay().getHeight();
	    //int width = display.getWidth();
	    //int height = display.getHeight();
	    fighterX2=0;
	    fighterY2=70;
	    fighterX3=0;
	    fighterY3=140;
	    fighterDX=0;
	    fighterDY=0;
	    fighterX =0;
	    fighterY =0;
	    fighterDestX =0;
	    fighterDestY =0;
	    fighterDestX2 =0;
	    fighterDestY2 =0;
	    fighterDestX3 =0;
	    fighterDestY3 =0;
	    fighter = BitmapFactory.decodeResource(getResources(), R.drawable.fighter);
	    background = BitmapFactory.decodeResource(getResources(), R.drawable.galaxy);
	    //button = BitmapFactory.decodeResource(getResources(),R.drawable.button_selected);
	    //units[0][0] = fighter1Position;
	    
	    
	    unitsXY[0][0] = fighterX;
	    unitsXY[0][1] = fighterX2;
	    unitsXY[0][2] = fighterX3;
	    unitsXY[0][3] = windowWidth-35;
	    
	    unitsXY[1][0] = fighterY;
	    unitsXY[1][1] = fighterY2;
	    unitsXY[1][2] = fighterY3;
	    unitsXY[1][3] = windowHeight-35;
	    
	    unitsXY[2][0] = fighterState;
	    unitsXY[2][1] = fighter2State;
	    unitsXY[2][2] = fighter3State;
	    unitsXY[2][3] = buttonState;
	    
	    unitsXY[3][0] = fighterDestX;
	    unitsXY[3][1] = fighterDestX2;
	    unitsXY[3][2] = fighterDestX3;
	    
	    unitsXY[4][0] = fighterDestY;
	    unitsXY[4][1] = fighterDestY2;
	    unitsXY[4][2] = fighterDestY3;
	    
	    
	    //OLD VIEW before GL
	   // setContentView(v);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int action = event.getAction();	
		//renderer.pointers =event.getPointerCount();
		pointers = event.getPointerCount();
		switch(event.getAction()){
        case MotionEvent.ACTION_DOWN:
           // pointers = 1;
            mActivePointerId = event.getPointerId(0);
            if(pointers == 1){
            	//GLU.gluUnProject(event.getX(), event.getY(), 0, renderer.modelViewMatrix, modelOffset, project, projectOffset, view, viewOffset, obj, objOffset)
            	renderer.touchEvent=true;
            	//renderer.screenYvis=5;
            	renderer.touchLocation(event);
            	renderer.calcScreen();
            	renderer.setStoreTouch(event);
            	//renderer.unitClickCheck();
            	renderer.setCamTemp();
            	
            	//renderer.convert();
            	//renderer.camMovement(event);
            }
            
            
            return true;
        //case MotionEvent.ACTION_MOVE
       case MotionEvent.ACTION_POINTER_2_DOWN:
           // pointers = 2;
            distance = fingerDist(event);
           return true;
        case MotionEvent.ACTION_MOVE:
        	final int pointerIndex = event.findPointerIndex(mActivePointerId);
        	
            if( pointers == 2 ){
                //float newDist = fingerDist(event);
                newDist = fingerDist(event);
                float d = distance/newDist;
				//GLRendererTSC renderer = ;
                //The zoom is called from another file not form within the render file but
                //thats a guess.  I imagine it will work fine here.
                
				renderer.zoom(d);				
                distance = newDist;
                mLastTouchX = event.getX(pointerIndex);
                //mLastTouchY = y;
            }
            if(pointers == 1){
            	//GLU.gluUnProject(event.getX(), event.getY(), 0, renderer.modelViewMatrix, modelOffset, project, projectOffset, view, viewOffset, obj, objOffset)
            	renderer.touchEvent=true;
            	renderer.touchLocation(event);
            	
            	//renderer.camMovement().camMoveX
            	//renderer.screenCalc();
            	//renderer.screenYvis=5;
            	
            
            	
            }
            return true;
        case MotionEvent.ACTION_UP:
        	mActivePointerId = INVALID_POINTER_ID;
        	//break;
        	if(pointers == 1){
        		renderer.touchEvent=false;
        		float d = distance/newDist;
        		//renderer.myString = Boolean.toString(renderer.popupStore);
        		/*----STORE--------
        		//if(renderer.popupStore == true){
        			//renderer.text.myString = "true";
        			
        			//ACCESS to the popup store
        			//Because its uncommented it should do nothing.
            		//openPopupStore();
            	//	renderer.popupStore =false;
            	}
        	    */
        		//renderer
        		//renderer.camSavePosition(event);
        		//renderer.setCamera();
        	}
       // case MotionEvent.
            return true;
       // c//ase MotionEvent.ACTION_POINTER_UP: {
       //     // Extract the index of the pointer that left the touch sensor
       //     final int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) 
        //            >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        //    final int pointerId = ev.getPointerId(pointerIndex);
        //    if (pointerId == mActivePointerId) {
                // This was our active pointer going up. Choose a new
                // active pointer and adjust accordingly.
         //       final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
         //       mLastTouchX = ev.getX(newPointerIndex);
         //       mLastTouchY = ev.getY(newPointerIndex);
          //      mActivePointerId = ev.getPointerId(newPointerIndex);
          //  }
         // /  
        default:
            return false;
        }

      }
	//protected final float camMove(MotionEvent event){
	//	float x = event.getX(0) - event.getX(1);
  //      float y = event.getY(0) - event.getY(1);
	//}
    public GLSurfaceView getRender(){
    	return mGLSurfaceView;
    }
    /* ---------Pop up menu.  Uncomment if you need it.----------------------------
    public void openPopupStore(){
		//setContentView(mGLSurfaceView);
		    LayoutInflater layoutInflater 
		     = (LayoutInflater)getBaseContext()
		      .getSystemService(LAYOUT_INFLATER_SERVICE);  
		    View popupView = layoutInflater.inflate(R.layout.popup_store, null);  
		             final PopupWindow popupWindow = new PopupWindow(
		               popupView, 
		               LayoutParams.WRAP_CONTENT,  
		                     LayoutParams.WRAP_CONTENT);  
		             Button btnBuy = (Button)popupView.findViewById(R.id.buy);
		             Button btnBuy1 = (Button)popupView.findViewById(R.id.buy1);
		             Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
		    
		    btnBuy.setOnClickListener(new Button.OnClickListener(){
		         		
		    		     @Override
		    		     public void onClick(View v) {
		    		      // TODO Auto-generated method stub
		    		      renderer.unitsXY[0][3] = 1;
		    		     // mGLSurfaceView.setRenderer(renderer);
		    	            
		    	          //  setContentView(mGLSurfaceView);
		    		     }}); 
		    btnBuy1.setOnClickListener(new Button.OnClickListener(){
         		
   		     @Override
   		     public void onClick(View v) {
   		      // TODO Auto-generated method stub
   		      renderer.unitsXY[2][3] = 1;
   		     // mGLSurfaceView.setRenderer(renderer);
   	            
   	          //  setContentView(mGLSurfaceView);
   		     }});        
		             
		      btnDismiss.setOnClickListener(new Button.OnClickListener(){
		
		     @Override
		     public void onClick(View v) {
		      // TODO Auto-generated method stub
		      popupWindow.dismiss();
		     // mGLSurfaceView.setRenderer(renderer);
	            
	          //  setContentView(mGLSurfaceView);
		     }});
		             //need to invent new coordinate system.  Origin for this is at the
		             //bottom left
		             									      //X      Y
		             popupWindow.showAsDropDown(mGLSurfaceView, 275,-450);
		         
		   
    }
     */
    protected final float fingerDist(MotionEvent event){
    //	if(event.getX(0) != -1 || event.getX(1)!=-1 || event.getX(2)!=-1){
    		
    	
        float x = renderer.convertX(event.getX(0)) - renderer.convertX(event.getX(1));
        float y = renderer.convertY(event.getY(0)) - renderer.convertY(event.getY(1));
    	
        return FloatMath.sqrt(x * x + y * y);
        
        
    //	}else{
    	//	return distance;
    //	}
    }
/**	
	public boolean onTouch(MotionEvent event) {
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
				renderer.zoom(d);
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
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mGLSurfaceView.onPause();
		//OLD Below
		//v.pause();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mGLSurfaceView.onResume();
		//OLD below
		//v.resume();
	}


	public class customView extends SurfaceView implements Runnable{
		Thread thread = null;
		SurfaceHolder holder;
		boolean running = false;
		
		public customView(Context context){
			super(context);
			holder = getHolder();
		}
		
		public void run(){
			renderer.pointers = -1;
			/**
			while(running == true){
				if(!holder.getSurface().isValid()){
					continue;
				}
				Canvas c = holder.lockCanvas();
				//Paint p = new Paint();
				//c.drawPaint(p);
				//c.drawARGB(255, 255, 0, 0);
				c.drawBitmap(background, 0 ,0, null);
				//c.drawBitmap(button, windowWidth-35,windowHeight-35, null);
				c.drawBitmap(button, windowWidth-35,windowHeight-35, null);
				
				determineSelection();
				determineAction();
				updateUnits(c);
				
				//c.drawBitmap(fighter, fighterX, fighterY, null);
				//c.drawBitmap(fighter,fighterX2,fighterY2,null);
				//c.drawBitmap(fighter,fighterX3,fighterY3,null);
				previousSelect=currentSelect;
				holder.unlockCanvasAndPost(c);
			}
			*/
		}
		
		//Based on the UnitsXY array will draw movement or not of each unit
		public void updateUnits(Canvas c){
			//0 draw unit at its current position
			//1 draw the unit moving
			//2 draw the unit attacking can be moving to attack
			//3 unit was not made yet...dont draw
			//
			if(unitsXY[2][0] == 0){
				c.drawBitmap(fighter, unitsXY[0][0], unitsXY[1][0], null);
			}else if(unitsXY[2][0] == 1){
				movement(0,c);
			}
			if(unitsXY[2][1] == 0){
				c.drawBitmap(fighter, unitsXY[0][1], unitsXY[1][1], null);
			}else if(unitsXY[2][1] == 1){
				movement(1,c);
			}
			if(unitsXY[2][2] == 0){
				c.drawBitmap(fighter, unitsXY[0][2], unitsXY[1][2], null);
			}else if(unitsXY[2][2] == 1){
				movement(2,c);
			}
		}
		
		public void setDestination(){
			destinationX=touchLocationX;
			destinationY=touchLocationY;
			dest=true;
		}
		
		//going to need more than one selected. and a switch to determine what the last select was.
		public void determineAction(){
			if(currentSelect==0){
				if(previousSelect ==0){
				return;
				//}else if(previousSelect == 1 || previousSelect ==2|| previousSelect ==3){	
				}else if(previousSelect == 1){
					unitsXY[2][0]=1;
					unitsXY[3][0]=touchLocationX;
					unitsXY[4][0]=touchLocationY;
				}else if(previousSelect == 2){
					unitsXY[2][1]=1;
					unitsXY[3][1]=touchLocationX;
					unitsXY[4][1]=touchLocationY;
				}else if(previousSelect == 3){
					unitsXY[2][2]=1;
					unitsXY[3][2]=touchLocationX;
					unitsXY[4][2]=touchLocationY;
				}
			}//else if(currentSelect == 1 ){
				
			//}
		}
		
		public void determineSelection(){
			//The point of this method is to determine when action the use wants to take per
			//On screen click
			//possible actions: select unit/units,move units plus attack commands, unselect,purchase screen.
			
			//first check if touch was on a unit. Or special location
			//for(int i=0;i<2; i++){
				for(int j =0;j<3; j++){
					if((unitsXY[0][j]<=touchLocationX+25 && unitsXY[0][j]>=touchLocationX-25)&&(unitsXY[1][j]<=touchLocationY+25 && unitsXY[1][j]>=touchLocationY-25)){
					//if((touchLocationX == unitsXY[0][j]+1 || touchLocationX == unitsXY[0][j]+2 || touchLocationX == unitsXY[0][j]+3 )&& (touchLocationY == unitsXY[1][j] || touchLocationY == unitsXY[1][j]+1 || touchLocationY == unitsXY[1][j] + 2 || touchLocationY == unitsXY[1][j]+3)){
						if(j==0){
							currentSelect=1;
							return;
						}else if(j==1){
							currentSelect=2;
							return;
						}else if(j==2){
							currentSelect=3;
							return;
						}
					}else{
						currentSelect=0;
					}
				}
		//	}
			//is touch a drag?  This will be later
			
			
		}
		
		//public void getVector(float unitX, float unitY){
			public void getVector(int unit){
			//touchLocationX = touchLocationX -30;
			//touchLocationY = touchLocationY-30;
			//fighterDX=touchLocationX - unitsXY[0][unit];
			//fighterDY=touchLocationY - unitsXY[1][unit];
			fighterDX=unitsXY[3][unit] - unitsXY[0][unit];
			fighterDY=unitsXY[4][unit] - unitsXY[1][unit];
			//fighterDX = fighterDX;
			//fighterDY = fighterDY/4;
		}
		public void smoothMovement(){
		//	if(Math.abs(fighterDX) > Math.abs(fighterDY)){
		//		fSpeedX=Math.abs(fighterDX)/Math.abs(fighterDY);
		//		fSpeedY=1;
		//	}else{
		//		//fSpeedY=fighterDY/fighterDX;
		//		fSpeedX=Math.abs(fighterDY)/Math.abs(fighterDX);
		//		fSpeedX=1;
		//	}
			fSpeedX = Math.abs(fighterDX)/100;
			fSpeedY = Math.abs(fighterDY)/100;
		}
		
		//public void movement(Canvas c, unitX,unitY,destinationX,destinationY){
		public void movement(int unit, Canvas c){
			getVector(unit);
			//getVector();
			smoothMovement();
			//keep unit DX and DY
			//if(fighterDX > 0){
			//	fighterX = fighterX + fSpeedX;
			//	fighterDX = fighterDX-fSpeedX;
			//	if(fighterDX < fSpeedX){
			//		fighterDX =0;
			//	}
			//}else if(fighterDX < 0){
		//		fighterX = fighterX - fSpeedX;
			//	fighterDX = fighterDX+fSpeedX;
		//		if(fighterDX > fSpeedX){
		//			fighterDX =0;
		//		}
		//	}else if(fighterDX == 0){
		//		fighterX = touchLocationX;
		//	}
		//	if(fighterDY > 0){
		//		fighterY = fighterY + fSpeedY;
		//		fighterDY = fighterDY-fSpeedY;
		//		if(fighterDY < fSpeedY){
		//			fighterDY =0;
		//		}
		//	}else if(fighterDY < 0){
		//		fighterY = fighterY - fSpeedY;
		//		fighterDY = fighterDY+fSpeedY;
		//		if(fighterDY > fSpeedY){
		//			fighterDY =0;
		//		}
		//	}else if(fighterDY == 0){
		//		fighterY = touchLocationY;
		//	}
			
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
		
			c.drawBitmap(fighter,unitsXY[0][unit],unitsXY[1][unit],null);
		//fighterY +=3;
		}
		
		public void pause(){
			running = false;
			while(true){
				try{
					thread.join();
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				break;
			}
			thread = null;
		}
		
		public void resume(){
			running = true;
			thread = new Thread(this);
			thread.start();
		}
		//555
	}


	//@Override
	//public void run() {
		// TODO Auto-generated method stub
		
	//}


//	public boolean onTouch(MotionEvent event) {
		// TODO Auto-generated method stub
	//	return false;
//	}
//	@Override
//	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
//		return false;
//	}


//	@Override
//	public boolean onTouch(View arg0, MotionEvent arg1) {
//		// TODO Auto-generated method stub
	//	return false;
//	}

//Old on touch method
	//@Override
	//public boolean onTouch(View v, MotionEvent me) {
	//	// TODO Auto-generated method stub
	//	touchLocationX = me.getX();
	//	touchLocationY = me.getY();
	//	return false;
	//}
	
}
