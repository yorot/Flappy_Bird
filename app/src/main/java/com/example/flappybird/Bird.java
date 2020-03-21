//בס"ד
package com.example.flappybird;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.RectF;

public class Bird extends Thread {
    Bitmap[] images;
    int bwidth, bheight;
    MySurfaceView myView;
    public Bird(MySurfaceView view){
        myView = view;
        images = new Bitmap[3];
        images[0] = BitmapFactory.decodeResource(myView.getResources(),R.drawable.yellowbirddownflap);
        images[1] = BitmapFactory.decodeResource(myView.getResources(),R.drawable.yellowbirdmidflap);
        images[2] = BitmapFactory.decodeResource(myView.getResources(),R.drawable.yellowbirdupflap);
        bheight = images[0].getHeight();
        bwidth = images[0].getWidth();
    }
    public Bitmap getBitmap(int index){
        return images[index];
    }

    public int getBheight() {
        return bheight;
    }

    public void setBheight(int bheight) {
        this.bheight = bheight;
    }

    public int getBwidth() {
        return bwidth;
    }

    public void setBwidth(int bwidth) {
        this.bwidth = bwidth;
    }
    @Override
    public void run(){
        try{
            sleep(30);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
