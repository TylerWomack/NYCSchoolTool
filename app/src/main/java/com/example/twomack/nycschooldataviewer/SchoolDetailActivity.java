package com.example.twomack.nycschooldataviewer;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
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

    @BindView(R.id.phone_num)
    TextView phoneView;

    @BindView(R.id.website)
    TextView websiteView;

    @BindView(R.id.variety)
    TextView varietyView;

    @BindView(R.id.ap_classes)
    TextView APView;

    @BindView(R.id.sports)
    TextView sportsView;

    @BindView(R.id.sports_layout)
    LinearLayout sportsLayout;

    @BindView(R.id.AP_layout)
    LinearLayout apLayout;

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
            String mathSAT = getIntent().getStringExtra("mathSAT");
            String englishSAT = getIntent().getStringExtra("englishSAT");
            String writingSAT = getIntent().getStringExtra("writingSAT");
            String phoneNumber = getIntent().getStringExtra("phoneNumber");
            String website = getIntent().getStringExtra("website");
            String apClasses = getIntent().getStringExtra("apClasses");
            String variety = getIntent().getStringExtra("variety");
            String sports = getIntent().getStringExtra("sports");
            String location = getIntent().getStringExtra("location");

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
            averageSATView.setText(SAT + " (" + mathSAT + " M, " + englishSAT + " E)");
            phoneView.setText(phoneNumber);
            websiteView.setText(website);
            varietyView.setText(variety + "%");
            APView.setText(apClasses);
            sportsView.setText(sports);

            //here we are collapsing views if they are empty:
            if (apClasses == null || apClasses.isEmpty())
                apLayout.setVisibility(View.GONE);
            if (sports == null || sports.isEmpty())
                sportsLayout.setVisibility(View.GONE);
            if (SAT.isEmpty())
                averageSATView.setVisibility(View.GONE);


        }

        public void setErrorScreen(){
            setContentView(R.layout.error_screen);
    }
}
