//בס"ד
package com.example.flappybird;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
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
    Point pointBird;//point for bird
    Point[]pointTop = new Point[4];//points for tubes
    Point[]pointBottom =new Point[4];
    Rect rect;
    int index;//index for the bird bitmap
    Point digits, tens,hunds;
    int velocity=0,gravity = 3,distance;//velocity- moving forward; gravity- gravity on the bird; distance - distance between tubes(top and bottom)
    final int GAP = 400;//gap between the top and bottom tubes
    RectF birdRect;//rect for collision
    Random rnd;//random for the generation of the tubes position
    boolean game = true;//game state
    RectF[] topRect = new RectF[4],bottomRect = new RectF[4];//rect to detect collision
    Bitmap[] numbers = new Bitmap[10];//bitmaps of the numbers photos
    int score = 0;
    int digit,ten,hund,thounds;
    SoundPool sp;
    int wing,point;//wing and point sounds
    Intent intent;
    boolean mute= MainActivity.mute;
    public MySurfaceView(Context context) {
        super(context);
        dwidth = Resources.getSystem().getDisplayMetrics().widthPixels;//getting the device height and width, make sure the game is working properly on every device
        dHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        birdThread = new Bird(this);
        background = BitmapFactory.decodeResource(getResources(),R.drawable.background);
        numbers[0] = BitmapFactory.decodeResource(getResources(),R.drawable.zero);
        numbers[1] = BitmapFactory.decodeResource(getResources(),R.drawable.one);
        numbers[2] = BitmapFactory.decodeResource(getResources(),R.drawable.two);
        numbers[3] = BitmapFactory.decodeResource(getResources(),R.drawable.three);
        numbers[4] = BitmapFactory.decodeResource(getResources(),R.drawable.four);
        numbers[5] = BitmapFactory.decodeResource(getResources(),R.drawable.five);
        numbers[6] = BitmapFactory.decodeResource(getResources(),R.drawable.six);
        numbers[7] = BitmapFactory.decodeResource(getResources(),R.drawable.seven);
        numbers[8] = BitmapFactory.decodeResource(getResources(),R.drawable.eight);
        numbers[9] = BitmapFactory.decodeResource(getResources(),R.drawable.nine);
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
        digits = new Point();
        digits.y = 80;
        digits.x = dwidth/2-numbers[0].getWidth()/2;
        tens = new Point();
        hunds = new Point();
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
        //soundpool
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes aa = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_GAME).build();
            sp = new SoundPool.Builder().setMaxStreams(10).setAudioAttributes(aa).build();
        }
        else
            sp = new SoundPool(10, AudioManager.STREAM_MUSIC,1);
        wing = sp.load(getContext(),R.raw.wing,1);
        point = sp.load(getContext(),R.raw.point,1);
    }
    protected void drawAll(Canvas canvas) {
        canvas.drawBitmap(background, null, rect, null);//help
        if (game) {
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
                if (birdRect.intersect(topRect[i]) || birdRect.intersect(bottomRect[i]))//check bird hit on tubes
                    game = false;
                if(score>5){//make the tube go faster
                    pointTop[i].x-=15;
                }
                else
                    pointTop[i].x -= 10;
                pointBottom[i].x = pointTop[i].x;
                if (pointTop[i].x < -topTubeThread.gettWidth()) {//bird passed the tube
                    score++;//update the score
                    if(!mute)//based on player preference in the home screen
                        sp.play(point,1,1,0,0,1);//play point sound, indicating the player received another point
                    pointTop[i].x += 4 * distance;//update the x to a new x
                    do {
                        pointBottom[i].y = dHeight - GAP + pointTop[i].y;
                    }
                    while (pointBottom[i].y < dHeight - bottomTubeThread.getbHeight());//make sure the bottom tube won't be drawn to the screen while its bottom point is visible
                }

                canvas.drawBitmap(topTubeThread.getTopTube(), pointTop[i].x, pointTop[i].y, null);
                canvas.drawBitmap(bottomTubeThread.getBottomTube(), pointBottom[i].x, pointBottom[i].y, null);
                topRect[i].set(pointTop[i].x, pointTop[i].y, topTubeThread.tWidth + pointTop[i].x, topTubeThread.tHeight + pointTop[i].y);
                bottomRect[i].set(pointBottom[i].x, pointBottom[i].y, bottomTubeThread.bWidth + pointBottom[i].x, bottomTubeThread.bHeight + pointBottom[i].y);
            }

            canvas.drawBitmap(birdThread.getBitmap(index), pointBird.x, pointBird.y, null);//bird
            birdRect.set(pointBird.x, pointBird.y, birdThread.bwidth + pointBird.x, birdThread.bheight + pointBird.y);
            digit = score % 10;//get digits
            ten = score / 10;//get tens
            hund = score / 100;//get hundreds
            if (digit > 9)
                digit = 0;
            if (ten >= 9)
                ten = 0;
           if (score == 10)//display tens
                setTens();
           if(score == 100)//display hunds
               setHunds();
            canvas.drawBitmap(numbers[digit], digits.x, digits.y, null);
            if(score>=10)
                canvas.drawBitmap(numbers[ten],tens.x,tens.y,null);
            if(score>=100)
                canvas.drawBitmap(numbers[hund],hunds.x,hunds.y,null);
            if (pointBird.y > dHeight - birdThread.getBheight() || pointBird.y < 0 - birdThread.getBheight())//boundaries
                game = false;
        }
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
            if(!mute)//based on player preference
                sp.play(wing,1,1,0,0,1);//play wing sounds
        }

        return true;
    }
    //in-nothing
    //sets the score display to tens
    public void setTens(){
        tens.x = dwidth/2-numbers[0].getWidth()/2-40;
        tens.y = digits.y;
    }
    //in-nothing
    //sets the score display to hunds
    public void setHunds(){
        hunds.x = dwidth/2-numbers[0].getWidth()/2-80;
        hunds.y = digits.y;
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
