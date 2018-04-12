package com.example.twomack.nycschooldataviewer;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.twomack.nycschooldataviewer.data.MainApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterActivity extends AppCompatActivity {

    @BindView(R.id.SAT_seek)
    SeekBar SAT;

    @BindView(R.id.safety_seek)
    SeekBar safety;

    @BindView(R.id.graduation_seek)
    SeekBar graduation;

    @BindView(R.id.AP_seek)
    SeekBar AP;

    @BindView(R.id.college_seek)
    SeekBar college;

    @BindView(R.id.SAT_position)
    TextView SATPosition;

    @BindView(R.id.safety_position)
    TextView safetyPosition;

    @BindView(R.id.graduation_position)
    TextView graduationPosition;

    @BindView(R.id.AP_position)
    TextView APPosition;

    @BindView(R.id.college_position)
    TextView collegePosition;

    @BindView(R.id.manhattan_checkbox)
    CheckedTextView manhattanCheckbox;

    @BindView(R.id.brooklyn_checkbox)
    CheckedTextView brooklynCheckbox;

    @BindView(R.id.bronx_checkbox)
    CheckedTextView bronxCheckbox;

    @BindView(R.id.queens_checkbox)
    CheckedTextView queensCheckbox;

    @BindView(R.id.staten_island_checkbox)
    CheckedTextView statenIslandCheckbox;

    public FilterActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_form);
        ButterKnife.bind(this);
        setUpListeners();
    }

    public void setUpListeners(){

        SAT.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                SATPosition.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        safety.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                safetyPosition.setText(String.valueOf(i) + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        graduation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                graduationPosition.setText(String.valueOf(i) + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        AP.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                APPosition.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        college.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                collegePosition.setText(String.valueOf(i) + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        manhattanCheckbox.setChecked(true);
        brooklynCheckbox.setChecked(true);
        bronxCheckbox.setChecked(true);
        queensCheckbox.setChecked(true);
        statenIslandCheckbox.setChecked(true);

        manhattanCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (manhattanCheckbox.isChecked()){
                    manhattanCheckbox.setBackgroundColor(Color.WHITE);
                    manhattanCheckbox.setTextColor(Color.BLACK);
                    manhattanCheckbox.setChecked(false);
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        manhattanCheckbox.setBackgroundColor(getColor(R.color.colorPrimary));
                        manhattanCheckbox.setTextColor(Color.WHITE);
                        manhattanCheckbox.setChecked(true);
                    }
                }
            }
        });

        brooklynCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (brooklynCheckbox.isChecked()){
                    brooklynCheckbox.setBackgroundColor(Color.WHITE);
                    brooklynCheckbox.setTextColor(Color.BLACK);
                    brooklynCheckbox.setChecked(false);
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        brooklynCheckbox.setBackgroundColor(getColor(R.color.colorPrimary));
                        brooklynCheckbox.setTextColor(Color.WHITE);
                        brooklynCheckbox.setChecked(true);
                    }
                }
            }
        });

        bronxCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bronxCheckbox.isChecked()){
                    bronxCheckbox.setBackgroundColor(Color.WHITE);
                    bronxCheckbox.setTextColor(Color.BLACK);
                    bronxCheckbox.setChecked(false);
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        bronxCheckbox.setBackgroundColor(getColor(R.color.colorPrimary));
                        bronxCheckbox.setTextColor(Color.WHITE);
                        bronxCheckbox.setChecked(true);
                    }
                }
            }
        });

        queensCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (queensCheckbox.isChecked()){
                    queensCheckbox.setBackgroundColor(Color.WHITE);
                    queensCheckbox.setTextColor(Color.BLACK);
                    queensCheckbox.setChecked(false);
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        queensCheckbox.setBackgroundColor(getColor(R.color.colorPrimary));
                        queensCheckbox.setTextColor(Color.WHITE);
                        queensCheckbox.setChecked(true);
                    }
                }
            }
        });

        statenIslandCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (statenIslandCheckbox.isChecked()){
                    statenIslandCheckbox.setBackgroundColor(Color.WHITE);
                    statenIslandCheckbox.setTextColor(Color.BLACK);
                    statenIslandCheckbox.setChecked(false);
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        statenIslandCheckbox.setBackgroundColor(getColor(R.color.colorPrimary));
                        statenIslandCheckbox.setTextColor(Color.WHITE);
                        statenIslandCheckbox.setChecked(true);
                    }
                }
            }
        });


    }

    public void filterButtonClicked(View v){
        List<Integer> sliderResults = new ArrayList<>();
        //the order here matters - we'll be plugging these into our filter in SchoolDataUtility in this order.
        sliderResults.add(safety.getProgress());
        sliderResults.add(SAT.getProgress());
        sliderResults.add(graduation.getProgress());
        sliderResults.add(AP.getProgress());
        sliderResults.add(college.getProgress());

        if (manhattanCheckbox.isChecked()){
            sliderResults.add(1);
        }else sliderResults.add(0);

        if (brooklynCheckbox.isChecked()){
            sliderResults.add(1);
        }else sliderResults.add(0);

        if (bronxCheckbox.isChecked()){
            sliderResults.add(1);
        }else sliderResults.add(0);

        if (queensCheckbox.isChecked()){
            sliderResults.add(1);
        }else sliderResults.add(0);

        if (statenIslandCheckbox.isChecked()){
            sliderResults.add(1);
        }else sliderResults.add(0);



        //MainApplication.getApplicationDataModule().getFilterRequirements().setValue(sliderResults);
        MainApplication.getApplicationDataModule().setFilterRequirements(sliderResults);

        //ends this activity and closes the view. It is necessary to call this to make your observer (the one listening to the requirements Observable) active in MainActivity.
        finish();
    }

}
