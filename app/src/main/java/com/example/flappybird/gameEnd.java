//בס"ד
package com.example.flappybird;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class gameEnd extends AppCompatActivity {
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//hide upper action bar
        setContentView(R.layout.activity_game_end);
        tvResult = findViewById(R.id.tvResult);
        String name = MainActivity.userName;
        tvResult.setText("Hey " + name + ", your score is " + MySurfaceView.score + ". Go to the scoreboard to see if you broke any record");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);//go back to the start screen
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
