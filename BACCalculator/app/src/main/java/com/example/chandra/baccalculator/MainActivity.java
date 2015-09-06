//Homework 1
//MainActivity.java
//Chandra Chudeswaran Sankar, Melissa Krausse

package com.example.chandra.baccalculator;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText weight_edit;
    Switch sw;
    public final static double con = 5.14;
    DecimalFormat df = new DecimalFormat("##.###");
    public final static double men = 0.73;
    public final static double women = 0.73;
    SeekBar sb;
    TextView percent;
    boolean saved = false;
    TextView bac;
    double level = 0.00;
    ProgressBar progressBar;
    boolean changed = false;
    ArrayList<Integer> percentAlcohol = new ArrayList<Integer>();
    ArrayList<Integer> ouncesAlcohol = new ArrayList<Integer>();
    boolean first = true;
    RadioGroup rg;
    TextView status;
    TextView gender_display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.wine);

        gender_display= (TextView)findViewById(R.id.gender_id);
        status = (TextView) findViewById(R.id.status);
        status.setBackgroundColor(getResources().getColor(R.color.md_green_500));
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        bac = (TextView) findViewById(R.id.bac_level);

        sw = (Switch) findViewById(R.id.genderSwitch);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changed = true;
                saved = false;
                level = 0.00;
                if(isChecked){
                    gender_display.setText("F");
                }
                else{
                    gender_display.setText("M");
                }
            }
        });

        rg = (RadioGroup) findViewById(R.id.radioGroup);

        weight_edit = (EditText) findViewById(R.id.entered_weight);
        weight_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                changed = true;
                saved = false;
                level = 0.00;


            }
        });

        sb = (SeekBar) findViewById(R.id.alcoholPercent);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                percent = (TextView) findViewById(R.id.percentId);
                progress = progress / 5;
                progress = progress * 5;
                percent.setText(String.valueOf(progress+"%"));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void setError() {
        Log.d("BAC Convertor", "Weight is null");
        Toast.makeText(getApplicationContext(), "Enter weight in lbs", Toast.LENGTH_LONG).show();

    }

    public void onSaveButton(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        saved = true;
        Log.d("BAC Convertor", "Save Button");
        weight_edit = (EditText) findViewById(R.id.entered_weight);
        boolean found = false;

        if (weight_edit.getText().length() == 0) {
            setError();

            found = true;
        }

        if(!found && (Integer.valueOf(weight_edit.getText().toString())==0)){
            Log.d("BAC Convertor", "Zero Validation");
            Toast.makeText(getApplicationContext(),"Enter valid Weight lbs",Toast.LENGTH_LONG).show();
            found=true;
            saved=false;
        }

        if (!found && first) {
            Log.d("BAC Convertor", "New Entry");
            saved = true;
            found = true;
        }

        if (!found && changed) {
            Log.d("BAC Convertor", "Weight or Gender changed");
            if (saved) {
                Log.d("BAC Convertor", "Weight or Gender changed-Recalculating");
                int weight = Integer.valueOf(weight_edit.getText().toString());
                double gender_constant = 0.73;
                sw = (Switch) findViewById(R.id.genderSwitch);

                if (sw.isChecked()) {
                    gender_constant = 0.66;
                }
                level = calculateHistoryBAC(weight, gender_constant);
                setStatus(level);
            } else {
                Log.d("BAC Convertor", "Weight or Gender changed-Not saved");
                setError();
            }
        }
    }

    public void onAdd(View view) {

        Log.d("BAC Convertor", "Add Button");
        weight_edit = (EditText) findViewById(R.id.entered_weight);
        if (weight_edit.getText().length() == 0) {
            setError();
        } else {
            if (saved) {
                bac = (TextView) findViewById(R.id.bac_level);
                TextView tv = (TextView) findViewById(R.id.percentId);
                int pe = Integer.valueOf(tv.getText().toString().replace("%",""));
                int weight = Integer.valueOf(weight_edit.getText().toString());
                double gender_constant = 0.73;
                sw = (Switch) findViewById(R.id.genderSwitch);

                if (sw.isChecked()) {
                    gender_constant = 0.66;
                }
                double d = calculateBAC(level, getOunces(), pe, weight, gender_constant, false);
                 setStatus(d);
            } else {
                Toast.makeText(getApplicationContext(), "Press Save button to continue", Toast.LENGTH_LONG).show();
                //setError();
            }
        }
    }

    public double calculateBAC(double l, int ouncesValue, int percent, int weight_val, double gender, boolean history) {

        first = false;
        Log.d("BAC Convertor", "BAC Calculation");
        double result = 0.00;

        if (!history) {
            Log.d("BAC Convertor", "Adding to list");
            percentAlcohol.add(percent);
            ouncesAlcohol.add(ouncesValue);
        }

        result = (((percent * 0.01) * ouncesValue) * con) / (weight_val * gender);
        result = result + l;
        level = result;
        return result;
    }

    public double calculateHistoryBAC(int weight, double gender_constant) {

        double result = 0.0;
        level = 0.0;
        for (int i = 0; i < ouncesAlcohol.size(); i++) {
            result = result + calculateBAC(0.0, ouncesAlcohol.get(i), percentAlcohol.get(i), weight, gender_constant, true);
        }
        return result;
    }


    public int getOunces() {
        int ounces = 1;
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        if (R.id.oz1 == rg.getCheckedRadioButtonId()) {
            ounces = 1;
        }
        if (R.id.oz5 == rg.getCheckedRadioButtonId()) {
            ounces = 5;
        }
        if (R.id.oz12 == rg.getCheckedRadioButtonId()) {

            ounces = 12;
        }
        return ounces;
    }

    public void setStatus(double result) {

        if(result==0){
            String text="0.00";
            bac.setText(getString(R.string.bac_level1) + text);
        }
        else{
            bac.setText(getString(R.string.bac_level1) + df.format(result));
        }

        int p = (int) ((result * 100) / 0.25);
        progressBar.setProgress(p);


        if (result < 0.08) {
            status.setBackgroundColor(getResources().getColor(R.color.md_green_500));
            status.setText(getString(R.string.status_label1));
        }
        if (result > 0.08 && result < 0.2) {
            status.setBackgroundColor(getResources().getColor(R.color.md_yellow_700));
            status.setText(getString(R.string.status_label2));
        }

        if (result > 0.20) {
            status.setBackgroundColor(getResources().getColor(R.color.md_red_800));
            status.setText(getString(R.string.status_label3));
            if(result>0.25){
                enable(false);
                Toast.makeText(getApplicationContext(),"No more drinks for you.",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void enable(boolean condition) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.parentLinearLayout);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            LinearLayout l = (LinearLayout) child;
            for (int j = 0; j < l.getChildCount(); j++) {
                View v = l.getChildAt(j);
                if(v instanceof RadioGroup){
                  for(int k=0;k<((RadioGroup)v).getChildCount();k++){
                      RadioButton r = (RadioButton)((RadioGroup) v).getChildAt(k);
                      r.setEnabled(condition);
                  }
                }
                else {
                    v.setEnabled(condition);
                }
            }
        }
        findViewById(R.id.resetDrink).setEnabled(true);

        if(condition){
            RadioButton rb =(RadioButton) findViewById(R.id.oz1);
            rb.setChecked(true);
            sb.setProgress(5);
            sw.setChecked(false);
            weight_edit.setText("");
            level=0.0;
            setStatus(0);
            ouncesAlcohol.clear();
            percentAlcohol.clear();
        }
    }


    public void reset(View v) {
        enable(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ouncesAlcohol.clear();
        percentAlcohol.clear();
    }
}
