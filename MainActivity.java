package com.example.time;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    LinearLayout mLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText check = findViewById(R.id.check);
        Button Show = findViewById(R.id.bt_show);

        mLinearLayout = (LinearLayout) findViewById(R.id.bg);

        Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String displayMsg="";

                // text entered by the user
                String Text = check.getText().toString();

                if(Text.isEmpty()){
                    mLinearLayout.setBackgroundResource(R.drawable.rain);
                    alert("Please enter a valid input!");
                }
                else {

                    int duration = Integer.parseInt(Text);
                    int hour = (int) duration/60;
                    int minutes = duration%60;
                    displayMsg = Integer.toString(hour) + ":" + Integer.toString(minutes);

                    // TODO: Call WEATHER API HERE
                    // EXTRACT API

                    mLinearLayout.setBackgroundResource(R.drawable.sunny);
                    alert(displayMsg);
                }
            }
        });



    }
    private void alert(String message){
        AlertDialog dig = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Message")
                .setMessage(message)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mLinearLayout.setBackgroundResource(R.drawable.main);
                    }
                })
                .create();
        dig.show();
    }
}