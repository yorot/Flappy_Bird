//בס"ד
package com.example.flappybird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {
   static boolean mute = false;//indicates if the user want sounds or noTextView myName;
    Dialog d;
    EditText name;
    Button save;
    SharedPreferences sp;
    Date date = new Date(System.currentTimeMillis());
    static String userName;
    @Override

    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setBackgroundResource(backgroundSet());//set background

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(receiver,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);//unregister receiver
    }

    BroadcastReceiver receiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,WifiManager.WIFI_STATE_UNKNOWN);//if didnt get the wifi state, it is unknown
            if(wifiState == WifiManager.WIFI_STATE_DISABLED)//Wifi is off
                Toast.makeText(context, "Wifi is off", Toast.LENGTH_SHORT).show();
        }
    };
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
            else{
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
        else{//start game
            if(name.getText().toString().equals(""))//empty name
                userName = "No name";//default name
            userName = name.getText().toString();
            sp = getSharedPreferences("scoreboard",MODE_PRIVATE);
            //edit shared preferences file
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("name",userName);//save user name
            editor.putLong("time", date.getTime()).apply();
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
    //get the current hour, and return the correct id of the background according to the hour
    public static int backgroundSet(){
        Calendar time = Calendar.getInstance();//get the time
        int hour = time.get(Calendar.HOUR_OF_DAY);//get hour in 24 hours(0-23)
        if(hour>=19||hour<=6)//Night
            return R.drawable.night_background;//night background
        return R.drawable.background;//day background

    }
}


