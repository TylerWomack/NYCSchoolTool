package com.example.twomack.nycschooldataviewer.data;

import android.util.Log;

import java.util.List;
import java.util.Locale;

/**
 * Created by twomack on 4/10/18.
 */

public class District {

    private int districtNumber;
    private List<DetailedSchool> schoolsInDistrict;


    public District(int districtNumber){
        this.districtNumber = districtNumber;
    }

    public int getDistrictNumber() {
        return districtNumber;
    }

    public Double getAverageSAT() {
        if(schoolsInDistrict == null || schoolsInDistrict.size() == 0)
            return null;
        double totalSAT = 0;
        double schoolsCounted = 0;
        for (DetailedSchool school : schoolsInDistrict){
            if(school.getTotalSATDouble() != -1){
                totalSAT = totalSAT + school.getTotalSATDouble();
                schoolsCounted++;
            }
        }
        if (schoolsCounted != 0){


            String SATString = String.format(Locale.getDefault(), "%.0f", totalSAT/schoolsCounted);
            return Double.valueOf(SATString);
        }else{
            Log.e("error", "can't return a valid average SAT for this district");
            return null;
        }
    }

    public Double getAverageGraduationRate() {
        if(schoolsInDistrict == null || schoolsInDistrict.size() == 0)
            return null;
        double totalGradNumber = 0;
        double schoolsCounted = 0;
        for (DetailedSchool school : schoolsInDistrict){
            if(school.getGraduationRateDouble() != -1){
                totalGradNumber = totalGradNumber + school.getGraduationRateDouble();
                schoolsCounted++;
            }
        }
        if (schoolsCounted != 0){

            return totalGradNumber / schoolsCounted;
        }else{
            Log.e("error", "can't return a valid average graduation rate for this district");
            return null;
        }
    }

    public void setSchoolsInDistrict(List<DetailedSchool> schools){
        this.schoolsInDistrict = schools;
    }

    public Double getPercentageOfStudentsSafe(){

        double aggregateSafetyPercentage = 0;
        int countOfSchools = 0;


        if(schoolsInDistrict == null || schoolsInDistrict.size() == 0)
            return null;
        for (DetailedSchool school : schoolsInDistrict){
            if (school.getPctStuSafeDouble() == -1){
                continue;
            }
            aggregateSafetyPercentage = aggregateSafetyPercentage + school.getPctStuSafeDouble();
            countOfSchools++;
        }
        if (countOfSchools != 0){
            String safetyString = String.format(Locale.getDefault(), "%.0f", aggregateSafetyPercentage/countOfSchools);
            return Double.valueOf(safetyString);
        }else{
            Log.e("error", "can't return a safety score for this district");
            return null;
        }
    }
}
