package com.example.chandra.timeconvertor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Switch sw;
    EditText eh, em;
    TextView tv;
    TextView result, day, previous_day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.am_pm);
        tv.setText("AM");


        sw = (Switch) findViewById(R.id.switch_am_pm);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tv.setText("PM");
                } else {
                    tv.setText("AM");
                }
            }
        });


    }


    public void onClick(View v) {

        Log.d("Time Conversion", "Button Onclick Method");
        eh = (EditText) findViewById(R.id.hours_id);
        em = (EditText) findViewById(R.id.minutes_id);
        result = (TextView) findViewById(R.id.resultView);
        day = (TextView) findViewById(R.id.timeViewResult);
        previous_day = (TextView) findViewById(R.id.timeViewDay);

        if (eh.getText().length() == 0 || em.getText().length() == 0) {
            Log.d("Time Conversion", "Hour and Minute Null validation");
            Toast.makeText(getApplicationContext(), "Enter Hours or Minutes", Toast.LENGTH_LONG).show();
            goToOriginal();
        }

        else {

            int hours = Integer.valueOf(eh.getText().toString());
            int minutes = Integer.valueOf(em.getText().toString());

            if (hours > 12 || minutes > 59 || hours < 0 || minutes < 0) {
                Log.d("Time Conversion", "Hour and Minute 12 and 59 validation");
                Toast.makeText(getApplicationContext(), "Invalid Hours or Minutes", Toast.LENGTH_LONG).show();
                goToOriginal();
            } else {
                Log.d("Time Conversion", "Validations Success Except Zero Hour Time");
                String am_pm_text = tv.getText().toString();


                String output = null;
                if (v.getId() == R.id.estButton) {

                    Log.d("Time Conversion", "EST Button Clicked");
                    output = calculateTime(hours, minutes, am_pm_text, 5);
                    result.setText("EST:");
                    day.setText(output);
                }
                if (v.getId() == R.id.cstButton) {

                    Log.d("Time Conversion", "CST Button Clicked");
                    output = calculateTime(hours, minutes, am_pm_text, 6);
                    result.setText("CST:");
                    day.setText(output);
                }
                if (v.getId() == R.id.mstButton) {

                    Log.d("Time Conversion", "MST Button Clicked");
                    output = calculateTime(hours, minutes, am_pm_text, 7);
                    result.setText("MST:");
                    day.setText(output);
                }

                if (v.getId() == R.id.pstButton) {

                    Log.d("Time Conversion", "PST Button Clicked");
                    output = calculateTime(hours, minutes, am_pm_text, 8);
                    result.setText("PST:");
                    day.setText(output);
                }

                if (output == null) {
                    Log.d("Time Conversion","Output is Null");
                    result.setText("Result:");
                    day.setText(null);

                }

            }
        }


    }

    public void onClickClear(View v) {
     goToOriginal();
    }

    public void goToOriginal(){
        Log.d("Time Conversion","Clearing");
        eh.setText(null);
        em.setText(null);
        day.setText(null);
        previous_day.setText(null);
        result.setText("Result:");
        sw = (Switch) findViewById(R.id.switch_am_pm);
        sw.setChecked(false);
        result.setText("Result:");
    }

    public String calculateTime(int hours, int minutes, String am_pm, int factor) {

        String minutes_text = String.valueOf(minutes);
        if (minutes < 10) {
            minutes_text = "0" + minutes;
        }

        if (hours == 0) {
            Log.d("Time Conversion", "Hour Zero Validation");
            Toast.makeText(getApplicationContext(), "Enter Hours or Minutes in 12 Hour Format", Toast.LENGTH_LONG).show();
            goToOriginal();
            return null;
        }
        if (am_pm.equals("PM")) {
            Log.d("Time Conversion", "PM Calculate Method");

            if (hours == 12) {
                Log.d("Time Conversion", "PM Calculate Method - 12 PM");
                hours = 0;
            }

            hours = hours + 12;
            hours = hours - factor;

            if (hours > 12) {
                Log.d("Time Conversion", "PM Calculate Method -- Converted time in same day PM");
                hours = hours - 12;
                previous_day.setText(null);
                return String.valueOf(hours) + ":" + minutes_text + "PM";
            } else {
                Log.d("Time Conversion", "PM Calculate Method -- Converted time in same day AM");
                previous_day.setText(null);
                return String.valueOf(hours) + ":" + minutes_text + "AM";
            }

        } else {
            Log.d("Time Conversion", "AM Calculate Method");

            if (hours == 12) {
                Log.d("Time Conversion", "AM Calculate Method - 12 AM");
                hours = 0;
            }

            hours = hours - factor;
            if (hours < 0 || hours == 0) {
                if (hours < 0) {
                    Log.d("Time Conversion", "AM Calculate Method - Converted time in Previous day PM");
                    hours = hours + 12;
                    previous_day.setText("Previous day");
                    previous_day.setTextColor(Color.RED);
                    return String.valueOf(hours) + ":" + minutes_text + "PM";
                } else {
                    Log.d("Time Conversion", "AM Calculate Method - Converted time in Same day 12 AM");
                    previous_day.setText(null);
                    return String.valueOf(hours) + ":" + minutes_text + "AM";
                }
            }

            else {
                Log.d("Time Conversion", "AM Calculate Method - Converted time in same day AM");
                previous_day.setText(null);
                return String.valueOf(hours) + ":" + minutes_text + "AM";
            }
        }


    }


}