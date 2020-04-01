//בס"ד
package com.example.flappybird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity   {
   static boolean mute = false;//indicates if the user want sounds or not
    @Override

    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void startGame(View v){
        Intent intent = new Intent(this,Game.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id  = item.getItemId();
        if(id == R.id.mute)//user want to mute the game
            mute = true;
            if(item.getTitle().equals("Mute"))//set the title to the opposite
                item.setTitle("Unmute");
            else if(item.getTitle().equals("Unmute")){
                item.setTitle("Mute");
                mute = false;
            }

        return true;
    }
}

