//בס"ד
package com.example.flappybird;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
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
    Point pointBird,pointTop,pointBottom;
    Rect rect;
    int index;
    int velocity=0,gravity = 3, forward = 10;
    final int GAP = 400;
    RectF birdRect,topRect,bottomRect;
    Random rnd;
    int distance = 100;//distance between the tubes
    public MySurfaceView(Context context) {
        super(context);
        dwidth = Resources.getSystem().getDisplayMetrics().widthPixels;//this way, bird will always be in the center of any device
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
        //pointBird.x = dwidth/2 - birdThread.getBwidth()/2;//Point represent the intial point for the bird, which is the middle
        pointBird.x = 0;
        pointBird.y = dHeight/2 - birdThread.getBheight()/2;
        rect= new Rect(0,0,dwidth,dHeight);
        myThread = new MyThread(this);
        setOnTouchListener(this);
        birdRect = new RectF(pointBird.x,pointBird.y,pointBird.x+birdThread.getBwidth(),pointBird.y+birdThread.getBheight());
        rnd = new Random();
        //draw top tube
        pointTop = new Point();
        pointTop.x = dwidth-topTubeThread.gettWidth();
        pointTop.y = -rnd.nextInt(topTubeThread.gettHeight());
        //draw bottom tube
        pointBottom = new Point();
        pointBottom.x = pointTop.x;
        pointBottom.y = dHeight - GAP + pointTop.y;
        do{
            pointTop.y = -rnd.nextInt(topTubeThread.gettHeight());
            pointBottom.y = dHeight - GAP + pointTop.y;}
        while(pointBottom.y<dHeight-bottomTubeThread.getbHeight());//make sure the bottom tube won't be drawn to the screen while its bottom point is visible


    }
    protected void drawAll(Canvas canvas){
        canvas.drawBitmap(background,null,rect,null);//help
        if(index == 0)//make the bird seems like it waving it wings
            index = 1;
        else if(index == 1)
            index = 2;
        else
            index = 0;
        if(pointBird.y<dHeight-birdThread.getBheight()||velocity < 0){
        velocity +=gravity;
        pointBird.y +=velocity;
        }
        pointTop.x-=10;
        pointBottom.x = pointTop.x;
        canvas.drawBitmap(topTubeThread.getTopTube(),pointTop.x,pointTop.y,null);
        canvas.drawBitmap(bottomTubeThread.getBottomTube(),pointBottom.x,pointBottom.y,null);
        canvas.drawBitmap(birdThread.getBitmap(index),pointBird.x,pointBird.y,null);//bird
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
