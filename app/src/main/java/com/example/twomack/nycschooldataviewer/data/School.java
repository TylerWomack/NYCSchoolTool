package com.example.twomack.nycschooldataviewer.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by twomack on 3/21/18.
 */

public class School {

    @SerializedName("dbn")
    @Expose
    private String dbn;
    @SerializedName("num_of_sat_test_takers")
    @Expose
    private String numOfSatTestTakers;
    @SerializedName("sat_critical_reading_avg_score")
    @Expose
    private String satCriticalReadingAvgScore;
    @SerializedName("sat_math_avg_score")
    @Expose
    private String satMathAvgScore;
    @SerializedName("sat_writing_avg_score")
    @Expose
    private String satWritingAvgScore;
    @SerializedName("school_name")
    @Expose
    private String schoolName;

    public String getDbn() {
        return dbn;
    }

    public void setDbn(String dbn) {
        this.dbn = dbn;
    }

    public String getNumOfSatTestTakers() {
        return numOfSatTestTakers;
    }

    public void setNumOfSatTestTakers(String numOfSatTestTakers) {
        this.numOfSatTestTakers = numOfSatTestTakers;
    }

    public String getSatCriticalReadingAvgScore() {
        if (satCriticalReadingAvgScore == null)
            return  "n/a";
        if (satCriticalReadingAvgScore.equals("s") || satCriticalReadingAvgScore.equals(""))
            return "n/a";
        return satCriticalReadingAvgScore;
    }

    public void setSatCriticalReadingAvgScore(String satCriticalReadingAvgScore) {
        this.satCriticalReadingAvgScore = satCriticalReadingAvgScore;
    }

    public String getSatMathAvgScore() {
        if (satMathAvgScore == null)
            return "n/a";
        if (satMathAvgScore.equals("s") || satMathAvgScore.equals(""))
            return "n/a";
        return satMathAvgScore;
    }

    public void setSatMathAvgScore(String satMathAvgScore) {
        this.satMathAvgScore = satMathAvgScore;
    }

    public String getTotalScore(){
        if (satMathAvgScore.equals("s") || satCriticalReadingAvgScore.equals("s") || satMathAvgScore == null || satCriticalReadingAvgScore == null){
            return "n/a";
        }
        int mathScore = Integer.valueOf(satMathAvgScore);
        int englishScore = Integer.valueOf(satCriticalReadingAvgScore);
        int total = mathScore + englishScore;
        return String.valueOf(total);
    }

    public String getSatWritingAvgScore()
    {
        if (satWritingAvgScore == null)
            return "n/a";

        if (satWritingAvgScore.equals("s"))
            return "n/a";
        return satWritingAvgScore;
    }

    public void setSatWritingAvgScore(String satWritingAvgScore) {
        this.satWritingAvgScore = satWritingAvgScore;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }


}