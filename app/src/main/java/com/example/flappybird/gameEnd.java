package com.example.flappybird;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TableLayout;

import java.util.Date;

public class gameEnd extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//hide upper action bar
        setContentView(R.layout.activity_game_end);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);//go back to the start screen
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
