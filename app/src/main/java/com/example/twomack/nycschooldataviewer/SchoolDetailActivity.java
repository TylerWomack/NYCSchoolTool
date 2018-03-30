package com.example.twomack.nycschooldataviewer;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SchoolDetailActivity extends AppCompatActivity {

    @BindView(R.id.school_name)
    TextView schoolNameView;

    @BindView(R.id.neighborhood)
    TextView neighborhoodView;

    @BindView(R.id.borough)
    TextView boroughView;

    @BindView(R.id.grades_served)
    TextView gradesServedView;

    @BindView(R.id.num_students)
    TextView numStudentsView;

    @BindView(R.id.average_SAT)
    TextView averageSATView;

    @BindView(R.id.gradu_rate)
    TextView graduationRateView;

    @BindView(R.id.college_rate)
    TextView collegeRateView;

    @BindView(R.id.safe_rate)
    TextView safetyRatingView;

    @BindView(R.id.description)
    TextView descriptionView;

    @BindView(R.id.attendance_rate)
    TextView attendanceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent();
    }

    public void setContent(){

            setContentView(R.layout.school_detail);
            ButterKnife.bind(this);
            String name = getIntent().getStringExtra("schoolName");
            String dbn = getIntent().getStringExtra("dbn");
            String gradesServed = getIntent().getStringExtra("gradesServed");
            String numStudents = getIntent().getStringExtra("numStudents");
            String gradRate = getIntent().getStringExtra("gradRate");
            String collegeRate = getIntent().getStringExtra("collegeCareerRate");
            String safeRate = getIntent().getStringExtra("safeRate");
            String borough = getIntent().getStringExtra("borough");
            String neighborhood = getIntent().getStringExtra("neighborhood");
            String schoolDescription = getIntent().getStringExtra("description");
            String attendance = getIntent().getStringExtra("attendance");
            String SAT = getIntent().getStringExtra("totalSAT");

            schoolNameView.setText(name);
            neighborhoodView.setText(neighborhood + ", ");
            boroughView.setText(borough);
            gradesServedView.setText(gradesServed);
            numStudentsView.setText(numStudents);
            graduationRateView.setText(gradRate + "%");
            collegeRateView.setText(collegeRate + "%");
            safetyRatingView.setText(safeRate + "%");
            descriptionView.setText(schoolDescription);
            attendanceView.setText(attendance + "%");
            averageSATView.setText(SAT);
        }

        public void setErrorScreen(){
            setContentView(R.layout.error_screen);
    }
}
