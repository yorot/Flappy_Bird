//בס"ד
package com.example.flappybird;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import java.util.Random;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    int dwidth,dHeight;//device width and height
    MyThread myThread;
    Bitmap background;
    Bird birdThread;
    TopTube topTubeThread;
    BottomTube bottomTubeThread;
    SurfaceHolder surfaceHolder;
    Point pointBird;//point for bird
    Point[]pointTop = new Point[4];//points for tubes
    Point[]pointBottom =new Point[4];
    Rect rect;
    int index;//index for the bird bitmap
    int velocity=0,gravity = 3,distance;//velocity- moving forward; gravity- gravity on the bird; distance - distance between tubes(top and bottom)
    final int GAP = 400;//gap between the top and bottom tubes
    RectF birdRect;//rect for collision
    Random rnd;//random for the generation of the tubes position
    boolean game = true;//game state
    RectF[] topRect = new RectF[4],bottomRect = new RectF[4];//rect to detect collision
    int[] numbers = {R.drawable.one};
    public MySurfaceView(Context context) {
        super(context);
        dwidth = Resources.getSystem().getDisplayMetrics().widthPixels;//getting the device height and width, make sure the game is working properly on every device
        dHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        birdThread = new Bird(this);
        background = BitmapFactory.decodeResource(getResources(),R.drawable.background);
        bottomTubeThread = new BottomTube(this);
        topTubeThread = new TopTube(this);
        topTubeThread.start();
        bottomTubeThread.start();
        pointBird = new Point();
        birdThread.start();
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        index = 0;
        rnd = new Random();
        pointBird.x = 0;
        pointBird.y = dHeight/2 - birdThread.getBheight()/2;
        rect= new Rect(0,0,dwidth,dHeight);
        myThread = new MyThread(this);
        setOnTouchListener(this);
        birdRect = new RectF(pointBird.x,pointBird.y,pointBird.x+birdThread.getBwidth(),pointBird.y+birdThread.getBheight());
        rnd = new Random();
        distance = dwidth*3/4;
        //draw top tube//initial points
        for(int i=0;i<4;i++){
            pointTop[i] = new Point();
            pointBottom[i] = new Point();
        }
        for(int i= 0;i<4;i++) {
            pointTop[i].x = dwidth +i*distance;
            pointTop[i].y = -rnd.nextInt(topTubeThread.gettHeight());
            //draw bottom tube
            pointBottom[i].x = pointTop[i].x;
            pointBottom[i].y = dHeight - GAP + pointTop[i].y;
            do {
                pointTop[i].y = -rnd.nextInt(topTubeThread.gettHeight());
                pointBottom[i].y = dHeight - GAP + pointTop[i].y;
            }
            while (pointBottom[i].y < dHeight - bottomTubeThread.getbHeight());//make sure the bottom tube won't be drawn to the screen while its bottom point is visible
            topRect[i] = new RectF(pointTop[i].x,pointTop[i].y,topTubeThread.tWidth+pointTop[i].x,topTubeThread.tHeight+pointTop[i].y);
            bottomRect[i] = new RectF(pointBottom[i].x,pointBottom[i].y,bottomTubeThread.bWidth+pointBottom[i].x,bottomTubeThread.bHeight+pointBottom[i].y);
        }


    }
    protected void drawAll(Canvas canvas){
        canvas.drawBitmap(background,null,rect,null);//help
        if(game) {
            if (index == 0)//make the bird seems like it waving it wings
                index = 1;
            else if (index == 1)
                index = 2;
            else
                index = 0;
            if (pointBird.y < dHeight - birdThread.getBheight() || velocity < 0) {
                velocity += gravity;
                pointBird.y += velocity;
            }
            for (int i = 0; i < 4 && game; i++) {
                if(birdRect.intersect(topRect[i])||birdRect.intersect(bottomRect[i]))//check bird hit on tubes
                    game = false;
                pointTop[i].x -= 10;
                pointBottom[i].x = pointTop[i].x;
                if (pointTop[i].x < -topTubeThread.gettWidth()) {
                    pointTop[i].x += 4 * distance;
                    do {
                        pointBottom[i].y = dHeight - GAP + pointTop[i].y;
                    }
                    while (pointBottom[i].y < dHeight - bottomTubeThread.getbHeight());//make sure the bottom tube won't be drawn to the screen while its bottom point is visible
                }
                canvas.drawBitmap(topTubeThread.getTopTube(), pointTop[i].x, pointTop[i].y, null);
                canvas.drawBitmap(bottomTubeThread.getBottomTube(), pointBottom[i].x, pointBottom[i].y, null);
                topRect[i].set(pointTop[i].x,pointTop[i].y,topTubeThread.tWidth+pointTop[i].x,topTubeThread.tHeight+pointTop[i].y);
                bottomRect[i].set(pointBottom[i].x,pointBottom[i].y,bottomTubeThread.bWidth+pointBottom[i].x,bottomTubeThread.bHeight+pointBottom[i].y);
            }
        }
        canvas.drawBitmap(birdThread.getBitmap(index),pointBird.x,pointBird.y,null);//bird
        birdRect.set(pointBird.x,pointBird.y,birdThread.bwidth+pointBird.x,birdThread.bheight+pointBird.y);
        if(pointBird.y>dHeight-birdThread.getBheight()||pointBird.y<0-birdThread.getBheight())//boundaries
            game = false;

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        myThread.setRunning(true);
        myThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        myThread.setRunning(false);
        while(retry){
            try{
                myThread.join();
                retry = false;
            }catch (InterruptedException e){

            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            velocity = -30;
            pointBird.x +=3;//move forward
        }

        return true;
    }

    private class MyThread extends Thread{
        MySurfaceView myView;
        private boolean running = false;
        public MyThread(MySurfaceView view) {
            myView = view;
        }

        public void setRunning(boolean run) {
            running = run;
        }

        public void run(){
            Canvas canvas = null;
            while(running){
                try{
                    canvas = myView.getHolder().lockCanvas();
                    if(canvas!=null)
                        synchronized (myView.getHolder()){
                            myView.drawAll(canvas);
                        }
                }finally {
                    if(canvas!=null)
                        myView.getHolder().unlockCanvasAndPost(canvas);
                }
                try{
                    sleep(30);
                }catch (InterruptedException e){

                }
            }
        }
    }
}
