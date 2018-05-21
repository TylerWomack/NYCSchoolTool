package com.example.twomack.nycschooldataviewer.data;

import android.util.Log;

import java.util.List;

/**
 * Created by twomack on 4/10/18.
 */

public class District {

    private double averageGraduationRate;
    private double averageSAT;
    private int districtNumber;
    private List<DetailedSchool> schoolsInDistrict;
    private double percentageOfStudentsSafe;


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


            String SATString = String.format("%.0f", totalSAT/schoolsCounted);
            averageSAT = Double.valueOf(SATString);
            return averageSAT;
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

            averageGraduationRate = totalGradNumber/schoolsCounted;
            return averageGraduationRate;
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
            String safetyString = String.format("%.0f", aggregateSafetyPercentage/countOfSchools);
            percentageOfStudentsSafe = Double.valueOf(safetyString);
            return percentageOfStudentsSafe;
        }else{
            Log.e("error", "can't return a safety score for this district");
            return null;
        }
    }
}
