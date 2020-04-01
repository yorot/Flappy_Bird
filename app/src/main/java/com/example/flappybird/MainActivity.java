//בס"ד
package com.example.flappybird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {
   static boolean mute = false;//indicates if the user want sounds or noTextView myName;
    Dialog d;
    EditText name;
    Button save;
    SharedPreferences sp;
    Date date = new Date(System.currentTimeMillis());
    @Override

    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.playBtn){//save name before the game starts
            createDialog();
        }
        else if(v.getId()==R.id.scoreBtn){//display the score board
            Intent intent = new Intent(this,scoreBoard.class);
            startActivity(intent);
            finish();
        }
        else{
            sp = getSharedPreferences("scoreboard",MODE_PRIVATE);
            //edit shared preferences file
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("name",name.getText().toString());//save user name
            editor.putLong("time", date.getTime()).apply();
            editor.putInt("Score",0);
            editor.commit();//save file
            d.dismiss();//stop displaying the dialog
            Intent intent = new Intent(this,Game.class);//start the game
            startActivity(intent);
            finish();
        }
    }
    //method to create the dialog
    public void createDialog(){
        d= new Dialog(this);
        d.setContentView(R.layout.dialog_template);
        d.setTitle("Enter you name");
        d.setCancelable(true);
        name = d.findViewById(R.id.name);
        save = d.findViewById(R.id.save);
        save.setOnClickListener(this);
        d.show();//display dialog
    }
}


