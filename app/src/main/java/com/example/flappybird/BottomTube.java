package com.example.flappybird;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BottomTube extends Thread {
    protected Bitmap bottomTube;
    protected  MySurfaceView myView;
    protected int bWidth,bHeight;
    public BottomTube(MySurfaceView view){
        myView = view;
        bottomTube = BitmapFactory.decodeResource(view.getResources(),R.drawable.bottomtube);
        bWidth = bottomTube.getWidth();
        bHeight = bottomTube.getHeight();
    }

    public Bitmap getBottomTube() {
        return bottomTube;
    }

    public void setBottomTube(Bitmap bottomTube) {
        this.bottomTube = bottomTube;
    }

    public int getbWidth() {
        return bWidth;
    }

    public void setbWidth(int bWidth) {
        this.bWidth = bWidth;
    }

    public int getbHeight() {
        return bHeight;
    }

    public void setbHeight(int bHeight) {
        this.bHeight = bHeight;
    }

    @Override
    public void run() {
        try{
            sleep(30);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
