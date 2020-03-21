package com.example.flappybird;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class TopTube extends Thread {
    protected Bitmap topTube;
    protected MySurfaceView myView;
    int tWidth,tHeight;
    public TopTube(MySurfaceView view){
        myView = view;
        topTube = BitmapFactory.decodeResource(myView.getResources(),R.drawable.toptube);
        tWidth = topTube.getWidth();
        tHeight = topTube.getHeight();

    }

    public Bitmap getTopTube() {
        return topTube;
    }

    public void setTopTube(Bitmap topTube) {
        this.topTube = topTube;
    }


    public int gettWidth() {
        return tWidth;
    }

    public void settWidth(int tWidth) {
        this.tWidth = tWidth;
    }

    public int gettHeight() {
        return tHeight;
    }

    public void settHeight(int tHeight) {
        this.tHeight = tHeight;
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