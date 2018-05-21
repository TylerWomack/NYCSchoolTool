package com.example.twomack.nycschooldataviewer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.twomack.nycschooldataviewer.data.DetailedSchool;

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
            DetailedSchool school = (DetailedSchool) getIntent().getSerializableExtra("school");
            String name = school.getSchoolName();
            String gradesServed = school.getGrades2018();
            String numStudents = school.getTotalStudents();
            String gradRate = school.getGraduationRate();
            String collegeRate = school.getCollegeCareerRate();
            String safeRate = school.getPctStuSafe();
            String borough = school.getBorough();
            String neighborhood = school.getNeighborhood();
            String schoolDescription = school.getOverviewParagraph();
            String attendance = school.getAttendanceRate();
            String SAT = school.getTotalSATScore();
            String mathSAT = school.getMathSATScore();
            String englishSAT = school.getEnglishSATScore();
            String writingSAT = school.getWritingSATScore();
            String phoneNumber = school.getPhoneNumber();
            String website = school.getWebsite();
            String apClasses = school.getAdvancedplacementCourses();
            String variety = school.getPctStuEnoughVariety();
            String sports = school.getSchool_sports();
            String location = school.getLocation();

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
