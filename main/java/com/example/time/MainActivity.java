package com.example.time;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.widget.LinearLayout;
import android.widget.TextClock;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import androidx.navigation.ui.AppBarConfiguration;
import com.example.tiktok.databinding.ActivityMainBinding;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private CountDownTimer countDownTimer;
    private long millis;
    SharedPreferences runningAppPref;
    final String SECONDS_KEY = "Seconds sharedPreferences";
    final String MINUTES_KEY = "Minutes sharedPreferences";
    final String HOURS_KEY = "Hours sharedPreferences";
    final String FIFTY_NINE = "59";
    final String ZERO = "00";
    TextView hours_output, minutes_output, seconds_output;
    EditText hours_input, minutes_input, seconds_input;
    Button setDuration;
    Calendar calendar = Calendar.getInstance();
    EditText check;
    long test;

    LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        check = findViewById(R.id.check);


        Button Show = findViewById(R.id.bt_show);
        TextClock world = findViewById(R.id.worldtime);

        mLinearLayout = (LinearLayout) findViewById(R.id.bg);

        Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String displayMsg="";

                // text entered by the user
                String Text = check.getText().toString();

                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                if(Text.isEmpty()){
                    mLinearLayout.setBackgroundResource(R.drawable.rainy);
                    alert("Please enter a valid input!");
                }
                else {

                    int duration = Integer.parseInt(Text);

                    if(duration > 90){
                        alert(formattedDate);
                    }
                    else {
                        int hour = (int) duration/60;
                        int minutes = duration%60;
                        displayMsg = Integer.toString(hour) + ":" + Integer.toString(minutes);

                        // TODO: Call WEATHER API HERE
                        // EXTRACT API

                        mLinearLayout.setBackgroundResource(R.drawable.sunny);
                        alert(displayMsg);
                    }
                }
            }
        });

        
        

        final Boolean[] settingDuration = {false};


        MaterialButton play, pause, stop;
        final Boolean[] thereIsHours = { false };
        final Boolean[] thereIsMinutes = { false };
        final Boolean[] thereIsSeconds = { false };
        final Boolean[] hasPlay = { false };
        final Boolean[] hasPause = { false };
        final Boolean[] hasStop = { false };

        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        stop = findViewById(R.id.stop);


        hours_input = findViewById(R.id.hours_input);
        hours_output = findViewById(R.id.hours_output);

        minutes_input = findViewById(R.id.minutes_input);
        minutes_output = findViewById(R.id.minutes_output);

        seconds_input = findViewById(R.id.seconds_input);
        seconds_output = findViewById(R.id.seconds_output);

        setDuration = findViewById(R.id.setDuration);


        hours_input.setVisibility(View.INVISIBLE);
        minutes_input.setVisibility(View.INVISIBLE);
        seconds_input.setVisibility(View.INVISIBLE);

        hours_output.setVisibility(View.VISIBLE);
        minutes_output.setVisibility(View.VISIBLE);
        seconds_output.setVisibility(View.VISIBLE);

        final String[] forSingleDigits = new String[1];

        runningAppPref = this.getSharedPreferences("RunningApp", Context.MODE_PRIVATE); //https://stackoverflow.com/questions/3624280/how-to-use-sharedpreferences-in-android-to-store-fetch-and-edit-values

        final int[] currentSeconds = {0};
        final int[] currentMinutes = {0};
        final int[] currentHours = {0};

        currentSeconds[0] = Integer.parseInt(runningAppPref.getString(SECONDS_KEY, ZERO));
        currentMinutes[0] = Integer.parseInt(runningAppPref.getString(MINUTES_KEY, ZERO));
        currentHours[0] = Integer.parseInt(runningAppPref.getString(HOURS_KEY, ZERO));





        check.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String checkMinutes = check.getText().toString();
                String hours_text, minutes_text;
                if(!checkMinutes.equals("")){
                    hours_text = String.valueOf(Integer.parseInt(checkMinutes) / 60);
                    if(hours_text.length() < 2) {
                        hours_text = "0" + hours_text;
                    }
                    hours_output.setText(hours_text);
                    hours_input.setText(hours_text);

                    minutes_text = String.valueOf(Integer.parseInt(checkMinutes) - (60 * (Integer.parseInt(checkMinutes) / 60)) );
                    if(minutes_text.length() < 2) {
                        minutes_text = "0" + minutes_text;
                    }
                    minutes_output.setText(minutes_text);
                    minutes_input.setText(minutes_text);
                }
            }
        });

        if(!hours_output.getText().toString().equals(ZERO))
        {
            thereIsHours[0] = true;
        }

        if (!minutes_output.getText().toString().equals(ZERO))
        {
            thereIsMinutes[0] = true;
        }



        setDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String hours_text = "";
                String minutes_text = "";
                String seconds_text = "";
                hasStop[0] = true;
                hasPlay[0] = false;

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }

                if (settingDuration[0] == false) {

                    hours_output.setVisibility(View.INVISIBLE);
                    minutes_output.setVisibility(View.INVISIBLE);
                    seconds_output.setVisibility(View.INVISIBLE);

                    hours_text = hours_output.getText().toString();
                    minutes_text = minutes_output.getText().toString();
                    seconds_text = seconds_output.getText().toString();

                    hours_input.setText(hours_text);
                    minutes_input.setText(minutes_text);
                    seconds_input.setText(seconds_text);

                    hours_input.setVisibility(View.VISIBLE);
                    minutes_input.setVisibility(View.VISIBLE);
                    seconds_input.setVisibility(View.VISIBLE);

                    String checkMinutes = check.getText().toString();
                    String hours, minutes;
                    if(!checkMinutes.equals("")){
                        hours = String.valueOf(Integer.parseInt(checkMinutes) / 60);
                        if(hours.length() < 2) {
                            hours = "0" + hours;
                        }
                        hours_output.setText(hours);
                        hours_input.setText(hours);

                        minutes = String.valueOf(Integer.parseInt(checkMinutes) - (60 * (Integer.parseInt(checkMinutes) / 60)) );
                        if(minutes.length() < 2) {
                            minutes = "0" + minutes;
                        }
                        minutes_output.setText(minutes);
                        minutes_input.setText(minutes);

                        seconds_output.setText(ZERO);
                        seconds_input.setText(ZERO);
                    }


                    //https://stackoverflow.com/questions/11369479/how-to-detect-when-user-leaves-an-edittext

                    // Each change for string lengths that are less than 2 or (more than 2 and first
                    // character is 0) are all done when user directly key in the value and changes textbox.

                    hours_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        String hours_text;

                        @Override
                        public void onFocusChange(View view, boolean b) {
                            hours_text = hours_input.getText().toString();
                            if(hours_text.length() < 2){
                                forSingleDigits[0] = "0" + hours_text;
                                hours_input.setText(forSingleDigits[0]);
                            }

                            else if(hours_text.length() > 2 && hours_text.charAt(0) == '0'){
                                forSingleDigits[0] = hours_text.substring(1);
                                hours_input.setText(forSingleDigits[0]);
                            }
                        }
                    });

                    minutes_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        String minutes_text;

                        @Override
                        public void onFocusChange(View view, boolean b) {
                            minutes_text = minutes_input.getText().toString();
                            if(minutes_text.length() < 2){
                                forSingleDigits[0] = "0" + minutes_text;
                                minutes_input.setText(forSingleDigits[0]);
                            }
                            else if(minutes_text.length() > 2 && minutes_text.charAt(0) == '0'){
                                forSingleDigits[0] = minutes_text.substring(1);
                                minutes_input.setText(forSingleDigits[0]);
                            }
                        }
                    });

                    seconds_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        String seconds_text;

                        @Override
                        public void onFocusChange(View view, boolean b) {
                            seconds_text = seconds_input.getText().toString();
                            if(seconds_text.length() < 2){
                                forSingleDigits[0] = "0" + seconds_text;
                                seconds_input.setText(forSingleDigits[0]);
                            }
                            else if(seconds_text.length() > 2 && seconds_text.charAt(0) == '0'){
                                forSingleDigits[0] = seconds_text.substring(1);
                                seconds_input.setText(forSingleDigits[0]);
                            }
                        }
                    });


                    settingDuration[0] = true;
                }

                else{

                    if(Integer.parseInt(hours_input.getText().toString()) > 1 || (Integer.parseInt(hours_input.getText().toString()) == 1 && Integer.parseInt(minutes_input.getText().toString()) > 30)){
                        Toast.makeText(getApplicationContext(), "Please set a duration below 1 hr and 30 min.", Toast.LENGTH_LONG).show();

                    }

                    else {
                        hours_input.setVisibility(View.INVISIBLE);
                        minutes_input.setVisibility(View.INVISIBLE);
                        seconds_input.setVisibility(View.INVISIBLE);

                        hours_text = hours_input.getText().toString();
                        minutes_text = minutes_input.getText().toString();
                        seconds_text = seconds_input.getText().toString();


                        // Each change for string lengths that are less than 2 or (more than 2 and first character
                        // is 0) are all done when user directly key in the value and press SET DURATION.

                        if (Integer.parseInt(hours_text) < 10 && hours_text.length() < 2) {

                            forSingleDigits[0] = "0" + hours_text;
                            hours_output.setText(forSingleDigits[0]);
                        } else if (hours_text.length() > 2 && hours_text.charAt(0) == '0') {
                            forSingleDigits[0] = hours_text.substring(1);
                            hours_output.setText(forSingleDigits[0]);
                        } else {
                            hours_output.setText(hours_text);
                        }

                        if (!hours_text.equals(ZERO)) {
                            thereIsHours[0] = true;
                        }


                        if (Integer.parseInt(minutes_text) > 59) {
                            Toast.makeText(getApplicationContext(), "Please set value for minutes within range of 0 to 59 minutes.", Toast.LENGTH_LONG).show();
                            minutes_output.setText(ZERO);
                        } else {

                            if (Integer.parseInt(minutes_text) < 10 && minutes_text.length() < 2) {

                                forSingleDigits[0] = "0" + minutes_text;
                                minutes_output.setText(forSingleDigits[0]);
                            } else if (minutes_text.length() > 2 && minutes_text.charAt(0) == '0') {
                                forSingleDigits[0] = minutes_text.substring(1);
                                minutes_input.setText(forSingleDigits[0]);
                            } else {
                                minutes_output.setText(minutes_text);
                            }

                            if (!minutes_text.equals(ZERO)) {
                                thereIsMinutes[0] = true;
                            }
                        }


                        if (Integer.parseInt(seconds_text) > 59) {
                            Toast.makeText(getApplicationContext(), "Please set value for seconds within range of 0 to 59 seconds.", Toast.LENGTH_LONG).show();
                            seconds_output.setText(ZERO);
                        } else {

                            if (Integer.parseInt(seconds_text) < 10 && seconds_text.length() < 2) {

                                forSingleDigits[0] = "0" + seconds_text;
                                seconds_output.setText(forSingleDigits[0]);
                            } else if (seconds_text.length() > 2 && seconds_text.charAt(0) == '0') {
                                forSingleDigits[0] = seconds_text.substring(1);
                                seconds_input.setText(forSingleDigits[0]);
                            } else {
                                seconds_output.setText(seconds_text);
                            }

                            if (!seconds_text.equals(ZERO)) {
                                thereIsSeconds[0] = true;
                            }
                        }


                        hours_output.setVisibility(View.VISIBLE);
                        minutes_output.setVisibility(View.VISIBLE);
                        seconds_output.setVisibility(View.VISIBLE);

                        check.setText(String.valueOf(Integer.parseInt(hours_output.getText().toString()) * 60 + Integer.parseInt(minutes_output.getText().toString())));

                        settingDuration[0] = false;
                    }
                }




            }
        });



        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(thereIsHours[0] != false || thereIsMinutes[0] != false || thereIsSeconds[0] != false){

                    hasStop[0] = false;
                    hasPause[0] = false;

                    currentSeconds[0] = Integer.parseInt(seconds_output.getText().toString());
                    currentMinutes[0] = Integer.parseInt(minutes_output.getText().toString());
                    currentHours[0] = Integer.parseInt(hours_output.getText().toString());

                    final long[] millis_forSeconds = { currentSeconds[0] * 1000 + currentHours[0] * 3600000 + currentMinutes[0] * 60000 };
                    final long[] millis_forMinutes = { currentMinutes[0] * 60000 };
                    final long[] millis_forHours = { currentHours[0] * 3600000 };

                    if (hasPause[0] == false && hasStop[0] == false){

                        if (hasPlay[0] == false) {

                            check.setText("");

                            hasPlay[0] = true;

                            Log.i("Play Button: ", "Clicked");


                            //https://www.youtube.com/watch?v=zmjfAcnosS0
                            countDownTimer = new CountDownTimer(millis_forSeconds[0], 1000) {
                                @Override
                                public void onTick(long l) {

                                    millis_forSeconds[0] = l;

                                    Log.i("One second passed", " Now currentSeconds is " + String.valueOf(currentSeconds[0]));

                                    if (thereIsSeconds[0] != false) {

                                        if (currentSeconds[0] > 0) {
                                            currentSeconds[0] -= 1;
                                        }

                                        if (currentSeconds[0] < 10) {
                                            forSingleDigits[0] = "0" + currentSeconds[0];
                                            seconds_output.setText(forSingleDigits[0]);
                                        } else {
                                            seconds_output.setText(String.valueOf(currentSeconds[0]));

                                        }


                                        currentSeconds[0] = Integer.parseInt(seconds_output.getText().toString());


                                        if (currentSeconds[0] == 0) {
                                            thereIsSeconds[0] = false;
                                        }
                                    } else if (thereIsMinutes[0] != false) {

                                        seconds_output.setText(FIFTY_NINE);
                                        thereIsSeconds[0] = true;
                                        currentSeconds[0] = 59;

                                        millis_forMinutes[0] = l;
                                        if (currentMinutes[0] > 0) {
                                            currentMinutes[0] -= 1;
                                        }

                                        if (currentMinutes[0] < 10) {
                                            forSingleDigits[0] = "0" + currentMinutes[0];
                                            minutes_output.setText(forSingleDigits[0]);
                                        } else {
                                            minutes_output.setText(String.valueOf(currentMinutes[0]));

                                        }


                                        currentMinutes[0] = Integer.parseInt(minutes_output.getText().toString());
                                        Log.i("One minute passed", " Now currentMinutes is " + String.valueOf(currentMinutes[0]));


                                        if (currentMinutes[0] == 0) {
                                            thereIsMinutes[0] = false;
                                        }
                                    } else if (thereIsHours[0] != false) {


                                        minutes_output.setText(FIFTY_NINE);
                                        thereIsMinutes[0] = true;
                                        currentMinutes[0] = 59;

                                        seconds_output.setText(FIFTY_NINE);
                                        thereIsSeconds[0] = true;
                                        currentSeconds[0] = 59;

                                        if (currentHours[0] > 0) {
                                            currentHours[0] -= 1;
                                        }


                                        if (currentHours[0] < 10) {
                                            forSingleDigits[0] = "0" + currentHours[0];
                                            hours_output.setText(forSingleDigits[0]);
                                        } else {
                                            hours_output.setText(String.valueOf(currentHours[0]));

                                        }


                                        currentHours[0] = Integer.parseInt(hours_output.getText().toString());
                                        Log.i("One hour passed", " Now currentHours is " + String.valueOf(currentHours[0]));

                                        if (currentHours[0] == 0) {
                                            thereIsHours[0] = false;
                                        }
                                    } else {
                                        countDownTimer.cancel();
                                        hasStop[0] = true;
                                        Toast.makeText(getApplicationContext(), "Run time is completed!", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFinish() {

                                    Log.i("Timer: ", "Finished");

                                }
                            }.start();
                        }


                    }
                }
            }
        });

        if (hasStop[0] == true){
            countDownTimer.cancel();
        }

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(hasPause[0] == false) {

                    if (countDownTimer != null) {
                        countDownTimer.cancel();

                        hasPause[0] = true;
                        hasPlay[0] = false;
                    }
                }

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(hasStop[0] == false) {
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        hasStop[0] = true;
                        hasPlay[0] = false;
                        seconds_output.setText(ZERO);
                        seconds_input.setText(ZERO);
                        minutes_output.setText(ZERO);
                        minutes_input.setText(ZERO);
                        hours_output.setText(ZERO);
                        hours_input.setText(ZERO);
                    }
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


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MainActivity: ", "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("MainActivity: ", "onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MainActivity: ", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MainActivity: ", "onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MainActivity: ", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor runningAppPrefEditor = runningAppPref.edit();


        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        runningAppPrefEditor.putString(SECONDS_KEY, seconds_output.getText().toString());
        runningAppPrefEditor.putString(MINUTES_KEY, minutes_output.getText().toString());
        runningAppPrefEditor.putString(HOURS_KEY, hours_output.getText().toString());
        runningAppPrefEditor.apply();

        Log.i("MainActivity: ", "onPause");
    }





