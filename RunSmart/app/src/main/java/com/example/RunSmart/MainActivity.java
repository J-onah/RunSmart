package com.example.RunSmart;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;

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
import android.util.Log;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.ui.AppBarConfiguration;
import com.example.RunSmart.databinding.ActivityMainBinding;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.RunSmart.model.getDataService;
import com.example.RunSmart.model.item;
import com.example.RunSmart.model.retrofitClientInstance;
import com.example.RunSmart.model.weatherDataModel;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    final String MAX_RUN_TIME_ADVICE = "Please run for a duration below 1 hr and 30 min.";
    final String MAX_RUN_TIME_ADVICE_CHK_FORECAST = "Please run for a duration below 90 minutes.";
    Button chkForecastBtn;
    TextClock worldTime;

    private CountDownTimer countDownTimer;
    private long millis;
    SharedPreferences runningAppPref;
    final String SECONDS_KEY = "Seconds sharedPreferences";
    final String MINUTES_KEY = "Minutes sharedPreferences";
    final String HOURS_KEY = "Hours sharedPreferences";
    final String FIFTY_NINE = "59";
    final String ZERO = "00";
    public String result;
    TextView hoursOutput, minutesOutput, secondsOutput;
    EditText hoursInput, minutesInput, secondsInput;
    Button setDuration;
    Calendar calendar = Calendar.getInstance();
    EditText textForWeatherCheck;
    long test;
    TextView guide;
    TextView colonBetwHoursMin, colonBetwMinSec;

    boolean changedInput = false;

    LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        chkForecastBtn = findViewById(R.id.chkForecastBtn);
        worldTime = findViewById(R.id.worldTime);

        textForWeatherCheck = findViewById(R.id.textForWeatherCheck);

        mLinearLayout = (LinearLayout) findViewById(R.id.bg);

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

        guide = findViewById(R.id.guide);

        colonBetwHoursMin = findViewById(R.id.colonBetwHoursMin);
        colonBetwMinSec = findViewById(R.id.colonBetwMinSec);

        hoursInput = findViewById(R.id.hoursInput);
        hoursOutput = findViewById(R.id.hoursOutput);

        minutesInput = findViewById(R.id.minutesInput);
        minutesOutput = findViewById(R.id.minutesOutput);

        secondsInput = findViewById(R.id.secondsInput);
        secondsOutput = findViewById(R.id.secondsOutput);

        setDuration = findViewById(R.id.setDuration);

        alert("Made by:\n- Lian Qi Zhi\n- Vinny Koh\n- Jonah Yeo");

        // Close Soft Keyboard if no Text Field is Being Focused On
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        chkForecastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String durationTxtInDialog = "";

                // text entered by the user
                String text = textForWeatherCheck.getText().toString();

                Date dateTime = Calendar.getInstance().getTime();
                System.out.println("Current time => " + dateTime);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedDate = simpleDateFormat.format(dateTime);

                if(text.isEmpty()){
                    mLinearLayout.setBackgroundResource(R.drawable.main);
                    alert("Please enter a valid input!");

//                    guide.setTextColor(Color.parseColor("#FFFFFFFF"));
//                    textForWeatherCheck.setTextColor(Color.parseColor("#FFFFFFFF"));
//                    hoursOutput.setTextColor(Color.parseColor("#FFFFFFFF"));
//                    hoursInput.setTextColor(Color.parseColor("#FFFFFFFF"));
//                    minutesOutput.setTextColor(Color.parseColor("#FFFFFFFF"));
//                    minutesInput.setTextColor(Color.parseColor("#FFFFFFFF"));
//                    secondsOutput.setTextColor(Color.parseColor("#FFFFFFFF"));
//                    secondsInput.setTextColor(Color.parseColor("#FFFFFFFF"));
//
//                    textForWeatherCheck.setHintTextColor(Color.parseColor("#FFFFFFFF"));
//                    colonBetwHoursMin.setTextColor(Color.parseColor("#FFFFFFFF"));
//                    colonBetwMinSec.setTextColor(Color.parseColor("#FFFFFFFF"));

                }
                else {

                    int duration = Integer.parseInt(text);

                    if(duration > 90){
                        alert(MAX_RUN_TIME_ADVICE_CHK_FORECAST);
                    }
                    else {

                        // Close Soft Keyboard
                        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(mLinearLayout.getWindowToken(), 0);

                        durationTxtInDialog = TimeConverter.convertMinutesToHourMinutes(duration);

                        // Call WEATHER API HERE
                        // EXTRACT API
                        //API
                        Retrofit retrofit= retrofitClientInstance.getRetrofitInstance();
                        getDataService api=retrofit.create(getDataService.class);
                        Call<weatherDataModel> call =api.getData(formattedDate);//date param


                        call.enqueue(new Callback<weatherDataModel>() {
                            @Override
                            public void onResponse(Call<weatherDataModel> call, Response<weatherDataModel> response) {



                                if (response.isSuccessful()) {

                                    Log.i("response status","yay,response successful");
                                    weatherDataModel data=response.body();
                                    List<item> itemList= data.getItems();
                                    //always get latest entry for changi
                                    String weather=itemList.get(itemList.size()-1).getChangiForecast().getWeather();

                                    Log.i("weather",weather);
                                    Log.i("raining?", String.valueOf(weather.contains("Showers")));
                                    result=String.valueOf(weather.contains("Showers"));

                                } else {

                                    result="error";
                                    Log.i("response status","response not successful");
                                    try {

                                        Log.i("response status", String.valueOf(response.code()));
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        Log.i("response status", jObjError.getJSONObject("error").getString("message"));


                                    } catch (Exception e) {
                                        Log.i("response status",e.getMessage());

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<weatherDataModel> call, Throwable t) {
                                Log.i("response status","network failure");
                                result="error";

                            }
                        });
                        // end of API
                        
                        try {
                            if (result.equals("true")) {
                                showCustomDialog(true, durationTxtInDialog);
                                mLinearLayout.setBackgroundResource(R.drawable.rain);
                            } else if (result.equals("false")) {
                                showCustomDialog(false, durationTxtInDialog);
                                mLinearLayout.setBackgroundResource(R.drawable.sunny);
                            } else {
                                mLinearLayout.setBackgroundResource(R.drawable.main);
                                Toast.makeText(getApplicationContext(), "Error! Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (NullPointerException e){

                        }

                    }
                }
            }
        });


        hoursInput.setVisibility(View.INVISIBLE);
        minutesInput.setVisibility(View.INVISIBLE);
        secondsInput.setVisibility(View.INVISIBLE);

        hoursOutput.setVisibility(View.VISIBLE);
        minutesOutput.setVisibility(View.VISIBLE);
        secondsOutput.setVisibility(View.VISIBLE);

        final String[] forSingleDigits = new String[1];

        runningAppPref = this.getSharedPreferences("RunningApp", Context.MODE_PRIVATE); //https://stackoverflow.com/questions/3624280/how-to-use-sharedpreferences-in-android-to-store-fetch-and-edit-values

        final int[] currentSeconds = {0};
        final int[] currentMinutes = {0};
        final int[] currentHours = {0};

        currentSeconds[0] = Integer.parseInt(runningAppPref.getString(SECONDS_KEY, ZERO));
        currentMinutes[0] = Integer.parseInt(runningAppPref.getString(MINUTES_KEY, ZERO));
        currentHours[0] = Integer.parseInt(runningAppPref.getString(HOURS_KEY, ZERO));





        textForWeatherCheck.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String checkMinutes = textForWeatherCheck.getText().toString();
                String hoursText, minutesText;
                if(!checkMinutes.equals("")){
                    hoursText = String.valueOf(Integer.parseInt(checkMinutes) / 60);
                    if(hoursText.length() < 2) {
                        hoursText = "0" + hoursText;
                    }
                    hoursOutput.setText(hoursText);
                    hoursInput.setText(hoursText);

                    minutesText = String.valueOf(Integer.parseInt(checkMinutes) - (60 * (Integer.parseInt(checkMinutes) / 60)) );
                    if(minutesText.length() < 2) {
                        minutesText = "0" + minutesText;
                    }
                    minutesOutput.setText(minutesText);
                    minutesInput.setText(minutesText);
                }
            }
        });

        if(!hoursOutput.getText().toString().equals(ZERO))
        {
            thereIsHours[0] = true;
        }

        if (!minutesOutput.getText().toString().equals(ZERO))
        {
            thereIsMinutes[0] = true;
        }


        textForWeatherCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                changedInput = false;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                changedInput = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });



        setDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String hoursText = "";
                String minutesText = "";
                String secondsText = "";
                hasStop[0] = true;
                hasPlay[0] = false;

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }

                if(changedInput == true){
                    changedInput = false;
                    settingDuration[0] = false;
                    textForWeatherCheck.clearFocus();

                    String checkMinutes = textForWeatherCheck.getText().toString();
                    String hours, minutes;
                    if(!checkMinutes.equals("")){
                        hours = String.valueOf(Integer.parseInt(checkMinutes) / 60);
                        if(hours.length() < 2) {
                            hours = "0" + hours;
                        }
                        hoursOutput.setText(hours);
                        hoursInput.setText(hours);

                        if (Integer.parseInt(hours) > 0){
                            thereIsHours[0] = true;
                        }

                        minutes = String.valueOf(Integer.parseInt(checkMinutes) - (60 * (Integer.parseInt(checkMinutes) / 60)) );
                        if(minutes.length() < 2) {
                            minutes = "0" + minutes;
                        }
                        minutesOutput.setText(minutes);
                        minutesInput.setText(minutes);

                        if (Integer.parseInt(minutes) > 0){
                            thereIsMinutes[0] = true;
                        }

                        secondsOutput.setText(ZERO);
                        secondsInput.setText(ZERO);

                        hoursInput.setVisibility(View.INVISIBLE);
                        minutesInput.setVisibility(View.INVISIBLE);
                        secondsInput.setVisibility(View.INVISIBLE);

                        hoursOutput.setVisibility(View.VISIBLE);
                        minutesOutput.setVisibility(View.VISIBLE);
                        secondsOutput.setVisibility(View.VISIBLE);
                    }
                }

                else if (settingDuration[0] == false) {

                    hoursOutput.setVisibility(View.INVISIBLE);
                    minutesOutput.setVisibility(View.INVISIBLE);
                    secondsOutput.setVisibility(View.INVISIBLE);

                    hoursText = hoursOutput.getText().toString();
                    minutesText = minutesOutput.getText().toString();
                    secondsText = secondsOutput.getText().toString();

                    hoursInput.setText(hoursText);
                    minutesInput.setText(minutesText);
                    secondsInput.setText(secondsText);

                    hoursInput.setVisibility(View.VISIBLE);
                    minutesInput.setVisibility(View.VISIBLE);
                    secondsInput.setVisibility(View.VISIBLE);


                    // Useful Reference: https://stackoverflow.com/questions/11369479/how-to-detect-when-user-leaves-an-edittext

                    // Also, each change for string lengths that are less than 2 or (more than 2 and first
                    // character is 0) are all done when user directly key in the value and changes textbox.

                    hoursInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        String hoursText;

                        @Override
                        public void onFocusChange(View view, boolean b) {
                            hoursText = hoursInput.getText().toString();
                            if(hoursText.length() < 2){
                                forSingleDigits[0] = "0" + hoursText;
                                hoursInput.setText(forSingleDigits[0]);
                            }

                            else if(hoursText.length() > 2 && hoursText.charAt(0) == '0'){
                                forSingleDigits[0] = hoursText.substring(1);
                                hoursInput.setText(forSingleDigits[0]);
                            }
                        }
                    });

                    minutesInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        String minutesText;

                        @Override
                        public void onFocusChange(View view, boolean b) {
                            minutesText = minutesInput.getText().toString();
                            if(minutesText.length() < 2){
                                forSingleDigits[0] = "0" + minutesText;
                                minutesInput.setText(forSingleDigits[0]);
                            }
                            else if(minutesText.length() > 2 && minutesText.charAt(0) == '0'){
                                forSingleDigits[0] = minutesText.substring(1);
                                minutesInput.setText(forSingleDigits[0]);
                            }
                        }
                    });

                    secondsInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        String secondsText;

                        @Override
                        public void onFocusChange(View view, boolean b) {
                            secondsText = secondsInput.getText().toString();
                            if(secondsText.length() < 2){
                                forSingleDigits[0] = "0" + secondsText;
                                secondsInput.setText(forSingleDigits[0]);
                            }
                            else if(secondsText.length() > 2 && secondsText.charAt(0) == '0'){
                                forSingleDigits[0] = secondsText.substring(1);
                                secondsInput.setText(forSingleDigits[0]);
                            }
                        }
                    });

                    settingDuration[0] = true;
                }

                else{

                    if(Integer.parseInt(hoursInput.getText().toString()) > 1 || (Integer.parseInt(hoursInput.getText().toString()) == 1 && Integer.parseInt(minutesInput.getText().toString()) > 30)){
                        Toast.makeText(getApplicationContext(), MAX_RUN_TIME_ADVICE, Toast.LENGTH_LONG).show();

                    }

                    else {
                        hoursInput.setVisibility(View.INVISIBLE);
                        minutesInput.setVisibility(View.INVISIBLE);
                        secondsInput.setVisibility(View.INVISIBLE);

                        hoursText = hoursInput.getText().toString();
                        minutesText = minutesInput.getText().toString();
                        secondsText = secondsInput.getText().toString();


                        // Each change for string lengths that are less than 2 or (more than 2 and first character
                        // is 0) are all done when user directly key in the value and press SET DURATION.

                        if (Integer.parseInt(hoursText) < 10 && hoursText.length() < 2) {

                            forSingleDigits[0] = "0" + hoursText;
                            hoursOutput.setText(forSingleDigits[0]);
                        } else if (hoursText.length() > 2 && hoursText.charAt(0) == '0') {
                            forSingleDigits[0] = hoursText.substring(1);
                            hoursOutput.setText(forSingleDigits[0]);
                        } else {
                            hoursOutput.setText(hoursText);
                        }

                        if (!hoursText.equals(ZERO)) {
                            thereIsHours[0] = true;
                        }


                        // Would not change hours value if minutes value is more than 59, in case user has set hours value beforehand. Prevents user error.
                        if (Integer.parseInt(minutesText) > 59) {
                            Toast.makeText(getApplicationContext(), "Please set value for minutes within range of 0 to 59 minutes.", Toast.LENGTH_LONG).show();
                            minutesOutput.setText(ZERO);
                        } else {

                            if (Integer.parseInt(minutesText) < 10 && minutesText.length() < 2) {

                                forSingleDigits[0] = "0" + minutesText;
                                minutesOutput.setText(forSingleDigits[0]);
                            } else if (minutesText.length() > 2 && minutesText.charAt(0) == '0') {
                                forSingleDigits[0] = minutesText.substring(1);
                                minutesInput.setText(forSingleDigits[0]);
                            } else {
                                minutesOutput.setText(minutesText);
                            }

                            if (!minutesText.equals(ZERO)) {
                                thereIsMinutes[0] = true;
                            }
                        }

                        // Would not change minutes or hours values if seconds value is more than 59, in case user has set minutes or hours values beforehand. Prevents user error.
                        if (Integer.parseInt(secondsText) > 59) {
                            Toast.makeText(getApplicationContext(), "Please set value for seconds within range of 0 to 59 seconds.", Toast.LENGTH_LONG).show();
                            secondsOutput.setText(ZERO);
                        } else {

                            if (Integer.parseInt(secondsText) < 10 && secondsText.length() < 2) {

                                forSingleDigits[0] = "0" + secondsText;
                                secondsOutput.setText(forSingleDigits[0]);
                            } else if (secondsText.length() > 2 && secondsText.charAt(0) == '0') {
                                forSingleDigits[0] = secondsText.substring(1);
                                secondsInput.setText(forSingleDigits[0]);
                            } else {
                                secondsOutput.setText(secondsText);
                            }

                            if (!secondsText.equals(ZERO)) {
                                thereIsSeconds[0] = true;
                            }
                        }


                        hoursOutput.setVisibility(View.VISIBLE);
                        minutesOutput.setVisibility(View.VISIBLE);
                        secondsOutput.setVisibility(View.VISIBLE);

                        textForWeatherCheck.setText(String.valueOf(Integer.parseInt(hoursOutput.getText().toString()) * 60 + Integer.parseInt(minutesOutput.getText().toString())));

                        // Close Soft Keyboard
                        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(mLinearLayout.getWindowToken(), 0);


                        settingDuration[0] = false;
                        changedInput = false;
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

                    currentSeconds[0] = Integer.parseInt(secondsOutput.getText().toString());
                    currentMinutes[0] = Integer.parseInt(minutesOutput.getText().toString());
                    currentHours[0] = Integer.parseInt(hoursOutput.getText().toString());

                    final long[] millis_forSeconds = { currentSeconds[0] * 1000 + currentHours[0] * 3600000 + currentMinutes[0] * 60000 };
                    final long[] millis_forMinutes = { currentMinutes[0] * 60000 };
                    final long[] millis_forHours = { currentHours[0] * 3600000 };

                    if (hasPause[0] == false && hasStop[0] == false){

                        if (hasPlay[0] == false) {

                            textForWeatherCheck.setText("");

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
                                            secondsOutput.setText(forSingleDigits[0]);
                                        } else {
                                            secondsOutput.setText(String.valueOf(currentSeconds[0]));

                                        }


                                        currentSeconds[0] = Integer.parseInt(secondsOutput.getText().toString());


                                        if (currentSeconds[0] == 0) {
                                            thereIsSeconds[0] = false;
                                        }
                                    } else if (thereIsMinutes[0] != false) {

                                        secondsOutput.setText(FIFTY_NINE);
                                        thereIsSeconds[0] = true;
                                        currentSeconds[0] = 59;

                                        millis_forMinutes[0] = l;
                                        if (currentMinutes[0] > 0) {
                                            currentMinutes[0] -= 1;
                                        }

                                        if (currentMinutes[0] < 10) {
                                            forSingleDigits[0] = "0" + currentMinutes[0];
                                            minutesOutput.setText(forSingleDigits[0]);
                                        } else {
                                            minutesOutput.setText(String.valueOf(currentMinutes[0]));

                                        }


                                        currentMinutes[0] = Integer.parseInt(minutesOutput.getText().toString());
                                        Log.i("One minute passed", " Now currentMinutes is " + String.valueOf(currentMinutes[0]));


                                        if (currentMinutes[0] == 0) {
                                            thereIsMinutes[0] = false;
                                        }
                                    } else if (thereIsHours[0] != false) {


                                        minutesOutput.setText(FIFTY_NINE);
                                        thereIsMinutes[0] = true;
                                        currentMinutes[0] = 59;

                                        secondsOutput.setText(FIFTY_NINE);
                                        thereIsSeconds[0] = true;
                                        currentSeconds[0] = 59;

                                        if (currentHours[0] > 0) {
                                            currentHours[0] -= 1;
                                        }


                                        if (currentHours[0] < 10) {
                                            forSingleDigits[0] = "0" + currentHours[0];
                                            hoursOutput.setText(forSingleDigits[0]);
                                        } else {
                                            hoursOutput.setText(String.valueOf(currentHours[0]));

                                        }


                                        currentHours[0] = Integer.parseInt(hoursOutput.getText().toString());
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


                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }

                hasStop[0] = true;
                hasPause[0] = false;
                hasPlay[0] = false;
                secondsOutput.setText(ZERO);
                secondsInput.setText(ZERO);
                minutesOutput.setText(ZERO);
                minutesInput.setText(ZERO);
                hoursOutput.setText(ZERO);
                hoursInput.setText(ZERO);
                textForWeatherCheck.setText("");
            }

        });


    }

    private void showCustomDialog(boolean hasRain, String durationTxtInDialog){

        final String DIALOG_DESC_START = "You have chosen to run a duration of:";
        final String RAIN_ADVICE = "It's <b>raining</b> during your run, please run another time!";
        final String SUNNY_ADVICE = "It's a <b>good weather</b>, have a good run!";
        String adviceText = "";
        String htmlText = "";

        ConstraintLayout dialogConstraintLayout = findViewById(R.id.dialogConstraintLayout);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.forecast_dialog, dialogConstraintLayout);
        Button dialogCloseBtn = view.findViewById(R.id.dialogCloseBtn);

        ImageView weatherIcon = view.findViewById(R.id.weatherIcon);
        TextView dialogDesc = view.findViewById(R.id.dialogDesc);

        if(hasRain){
            weatherIcon.setImageResource(R.drawable.rain_icon);
            adviceText = RAIN_ADVICE;
        }
        else{
            weatherIcon.setImageResource(R.drawable.sun_icon);
            adviceText = SUNNY_ADVICE;
        }

        htmlText = DIALOG_DESC_START + "<h1><b>" + durationTxtInDialog + "</b></h1>" + adviceText;

        dialogDesc.setText(Html.fromHtml(htmlText));

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        dialogCloseBtn.findViewById(R.id.dialogCloseBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }


    private void alert(String message){
        AlertDialog dig = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Message")
                .setMessage(message)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
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
        runningAppPrefEditor.putString(SECONDS_KEY, secondsOutput.getText().toString());
        runningAppPrefEditor.putString(MINUTES_KEY, minutesOutput.getText().toString());
        runningAppPrefEditor.putString(HOURS_KEY, hoursOutput.getText().toString());
        runningAppPrefEditor.apply();

        Log.i("MainActivity: ", "onPause");
    }

}