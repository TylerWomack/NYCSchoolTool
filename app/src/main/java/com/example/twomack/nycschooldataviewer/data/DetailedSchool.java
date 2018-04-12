package com.example.twomack.nycschooldataviewer.data;

import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by twomack on 3/26/18.
 */

public class DetailedSchool implements Serializable{

    private Integer schoolDistrict;
    private String academicopportunities1;
    private String academicopportunities2;
    private String academicopportunities3;
    private String academicopportunities4;
    private String academicopportunities5;
    private String addtlInfo1;
    private String admissionspriority11;
    private String admissionspriority12;
    private String advancedplacement_courses;
    private String attendance_rate;
    private String bbl;
    private String bin;
    private String boro;
    private String borough;
    private String building_code;
    private String bus;
    private String census_tract;
    private String city;
    private String code1;
    private String code2;
    private String college_career_rate;
    private String community_board;
    private String council_district;
    private String dbn;
    private String ell_programs;
    private String endTime;
    private String extracurricular_activities;
    private String fax_number;
    private String finalgrades;
    private String grade9geapplicants1;
    private String grade9geapplicants2;
    private String grade9geapplicantsperseat1;
    private String grade9geapplicantsperseat2;
    private String grade9gefilledflag1;
    private String grade9gefilledflag2;
    private String grade9swdapplicants1;
    private String grade9swdapplicants2;
    private String grade9swdapplicantsperseat1;
    private String grade9swdapplicantsperseat2;
    private String grade9swdfilledflag1;
    private String grade9swdfilledflag2;
    private String grades2018;
    private String graduation_rate;
    private String interest1;
    private String interest2;
    private String languageClasses;
    private String latitude;
    private String location;
    private String longitude;
    private String method1;
    private String method2;
    private String neighborhood;
    private String nta;
    private String offer_rate1;
    private String overview_paragraph;
    private String pct_stu_enough_variety;
    private String pct_stu_safe;
    private String phone_number;
    private String prgdesc1;
    private String prgdesc2;
    private String primary_address_line1;
    private String program1;
    private String program2;
    private String psal_sports_boys;
    private String psal_sports_girls;
    private String requirement1_1;
    private String requirement2_1;
    private String requirement3_1;
    private String requirement4_1;
    private String requirement5_1;
    private String school_10th_seats;
    private String school_accessibility_description;
    private String school_email;
    private String school_name;
    private String school_sports;
    private String seats101;
    private String seats102;
    private String seats9ge1;
    private String seats9ge2;
    private String seats9swd1;
    private String seats9swd2;
    private String start_time;
    private String state_code;
    private String subway;
    private String total_students;
    private String website;
    private String zip;
    private double distance;
    private String totalSATScore;
    private String mathSATScore;
    private String englishSATScore;
    private String writingSATScore;
    private String numberOfTestTakers;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public Integer getSchoolDistrict() {
        return schoolDistrict;
    }

    public void setSchoolDistrict(Integer schoolDistrict) {
        this.schoolDistrict = schoolDistrict;
    }


    public String getSchool_sports() {

        String allSports = "";

        if (getPsalSportsGirls() != null)
            allSports = allSports + "Girls: " + getPsalSportsGirls();

        if (getPsalSportsBoys() != null)
            allSports = allSports + " Boys: " + getPsalSportsBoys();

        if (school_sports != null)
            allSports = allSports + " All: " + school_sports;

        return allSports;
    }



    public String getRequirement5_1() {
        return requirement5_1;
    }

    public void setRequirement5_1(String requirement5_1) {
        this.requirement5_1 = requirement5_1;
    }

    public String getRequirement1_1() {
        return requirement1_1;
    }

    public void setRequirement1_1(String requirement1_1) {
        this.requirement1_1 = requirement1_1;
    }


    public String getRequirement2_1() {
        return requirement2_1;
    }

    public void setRequirement2_1(String requirement2_1) {
        this.requirement2_1 = requirement2_1;
    }

    public String getRequirement3_1() {
        return requirement3_1;
    }

    public void setRequirement3_1(String requirement3_1) {
        this.requirement3_1 = requirement3_1;
    }

    public String getRequirement4_1() {
        return requirement4_1;
    }

    public void setRequirement4_1(String requirement4_1) {
        this.requirement4_1 = requirement4_1;
    }


    public String getOffer_rate1() {
        return offer_rate1;
    }

    public void setOffer_rate1(String offer_rate1) {
        this.offer_rate1 = offer_rate1;
    }

    public String getAcademicopportunities1() {
        return academicopportunities1;
    }

    public void setAcademicopportunities1(String academicopportunities1) {
        this.academicopportunities1 = academicopportunities1;
    }

    public String getAcademicopportunities2() {
        return academicopportunities2;
    }

    public void setAcademicopportunities2(String academicopportunities2) {
        this.academicopportunities2 = academicopportunities2;
    }

    public String getAcademicopportunities3() {
        return academicopportunities3;
    }

    public void setAcademicopportunities3(String academicopportunities3) {
        this.academicopportunities3 = academicopportunities3;
    }

    public String getAcademicopportunities4() {
        return academicopportunities4;
    }

    public void setAcademicopportunities4(String academicopportunities4) {
        this.academicopportunities4 = academicopportunities4;
    }

    public String getAcademicopportunities5() {
        return academicopportunities5;
    }

    public void setAcademicopportunities5(String academicopportunities5) {
        this.academicopportunities5 = academicopportunities5;
    }

    public String getAddtlInfo1() {
        return addtlInfo1;
    }

    public void setAddtlInfo1(String addtlInfo1) {
        this.addtlInfo1 = addtlInfo1;
    }

    public String getAdmissionspriority11() {
        return admissionspriority11;
    }

    public void setAdmissionspriority11(String admissionspriority11) {
        this.admissionspriority11 = admissionspriority11;
    }

    public String getAdmissionspriority12() {
        return admissionspriority12;
    }

    public void setAdmissionspriority12(String admissionspriority12) {
        this.admissionspriority12 = admissionspriority12;
    }

    public String getAdvancedplacementCourses() {
        return advancedplacement_courses;
    }

    public void setAdvancedplacementCourses(String advancedplacementCourses) {
        this.advancedplacement_courses = advancedplacement_courses;
    }

    public String getAttendanceRate() {

        if (attendance_rate != null){
            return convertToDecimalForm(attendance_rate);
        }else return "";
    }

    public void setAttendanceRate(String attendanceRate) {
        this.attendance_rate = attendance_rate;
    }

    public String getBbl() {
        return bbl;
    }

    public void setBbl(String bbl) {
        this.bbl = bbl;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getBoro() {
        return boro;
    }

    public void setBoro(String boro) {
        this.boro = boro;
    }

    public String getBorough() {
        if (borough != null){
            StringBuilder sb = new StringBuilder(borough);
            for (int i = 0; i < borough.length(); i++){
                sb.setCharAt(i, Character.toLowerCase(sb.charAt(i)));
            }
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            return sb.toString();
        }else return "";
    }

    public void setBorough(String borough) {


        this.borough = borough;
    }

    public String getBuildingCode() {
        return building_code;
    }

    public void setBuildingCode(String building_code) {
        this.building_code = building_code;
    }

    public String getBus() {
        return bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }

    public String getCensusTract() {
        return census_tract;
    }

    public void setCensusTract(String census_tract) {
        this.census_tract = census_tract;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCode1() {
        return code1;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public String getCollegeCareerRate() {
        if (college_career_rate != null){
            return convertToDecimalForm(college_career_rate);
        }else return "";

    }

    public void setCollegeCareerRate(String college_career_rate) {
        this.college_career_rate = college_career_rate;
    }

    public String getCommunityBoard() {
        return community_board;
    }

    public void setCommunityBoard(String community_board) {
        this.community_board = community_board;
    }

    public String getCouncilDistrict() {
        return council_district;
    }

    public void setCouncilDistrict(String council_district) {
        this.council_district = council_district;
    }

    public String getDbn() {
        return dbn;
    }

    public void setDbn(String dbn) {
        this.dbn = dbn;
    }

    public String getEllPrograms() {
        return ell_programs;
    }

    public void setEllPrograms(String ell_programs) {
        this.ell_programs = ell_programs;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getExtracurricularActivities() {
        return extracurricular_activities;
    }

    public void setExtracurricularActivities(String extracurricular_activitiesctivities) {
        this.extracurricular_activities = extracurricular_activitiesctivities;
    }

    public String getFaxNumber() {
        return fax_number;
    }

    public void setFaxNumber(String fax_number) {
        this.fax_number = fax_number;
    }

    public String getFinalgrades() {
        return finalgrades;
    }

    public void setFinalgrades(String finalgrades) {
        this.finalgrades = finalgrades;
    }

    public String getGrade9geapplicants1() {
        return grade9geapplicants1;
    }

    public void setGrade9geapplicants1(String grade9geapplicants1) {
        this.grade9geapplicants1 = grade9geapplicants1;
    }

    public String getGrade9geapplicants2() {
        return grade9geapplicants2;
    }

    public void setGrade9geapplicants2(String grade9geapplicants2) {
        this.grade9geapplicants2 = grade9geapplicants2;
    }

    public String getGrade9geapplicantsperseat1() {
        return grade9geapplicantsperseat1;
    }

    public void setGrade9geapplicantsperseat1(String grade9geapplicantsperseat1) {
        this.grade9geapplicantsperseat1 = grade9geapplicantsperseat1;
    }

    public String getGrade9geapplicantsperseat2() {
        return grade9geapplicantsperseat2;
    }

    public void setGrade9geapplicantsperseat2(String grade9geapplicantsperseat2) {
        this.grade9geapplicantsperseat2 = grade9geapplicantsperseat2;
    }

    public String getGrade9gefilledflag1() {
        return grade9gefilledflag1;
    }

    public void setGrade9gefilledflag1(String grade9gefilledflag1) {
        this.grade9gefilledflag1 = grade9gefilledflag1;
    }

    public String getGrade9gefilledflag2() {
        return grade9gefilledflag2;
    }

    public void setGrade9gefilledflag2(String grade9gefilledflag2) {
        this.grade9gefilledflag2 = grade9gefilledflag2;
    }

    public String getGrade9swdapplicants1() {
        return grade9swdapplicants1;
    }

    public void setGrade9swdapplicants1(String grade9swdapplicants1) {
        this.grade9swdapplicants1 = grade9swdapplicants1;
    }

    public String getGrade9swdapplicants2() {
        return grade9swdapplicants2;
    }

    public void setGrade9swdapplicants2(String grade9swdapplicants2) {
        this.grade9swdapplicants2 = grade9swdapplicants2;
    }

    public String getGrade9swdapplicantsperseat1() {
        return grade9swdapplicantsperseat1;
    }

    public void setGrade9swdapplicantsperseat1(String grade9swdapplicantsperseat1) {
        this.grade9swdapplicantsperseat1 = grade9swdapplicantsperseat1;
    }

    public String getGrade9swdapplicantsperseat2() {
        return grade9swdapplicantsperseat2;
    }

    public void setGrade9swdapplicantsperseat2(String grade9swdapplicantsperseat2) {
        this.grade9swdapplicantsperseat2 = grade9swdapplicantsperseat2;
    }

    public String getGrade9swdfilledflag1() {
        return grade9swdfilledflag1;
    }

    public void setGrade9swdfilledflag1(String grade9swdfilledflag1) {
        this.grade9swdfilledflag1 = grade9swdfilledflag1;
    }

    public String getGrade9swdfilledflag2() {
        return grade9swdfilledflag2;
    }

    public void setGrade9swdfilledflag2(String grade9swdfilledflag2) {
        this.grade9swdfilledflag2 = grade9swdfilledflag2;
    }

    public String getGrades2018() {
        return grades2018;
    }

    public void setGrades2018(String grades2018) {
        this.grades2018 = grades2018;
    }

    public String getGraduationRate() {
        if (graduation_rate != null){
            return convertToDecimalForm(graduation_rate);
        }else return "";
    }

    public String convertToDecimalForm(String unconverted){
        double rate = Double.valueOf(unconverted);
        rate = rate * 100;
        String to_return = String.format("%.0f", rate);
        to_return = to_return;
        return to_return;
    }

    public void setGraduationRate(String graduation_rate) {
        this.graduation_rate = graduation_rate;
    }

    public String getInterest1() {
        return interest1;
    }

    public void setInterest1(String interest1) {
        this.interest1 = interest1;
    }

    public String getInterest2() {
        return interest2;
    }

    public void setInterest2(String interest2) {
        this.interest2 = interest2;
    }

    public String getLanguageClasses() {
        return languageClasses;
    }

    public void setLanguageClasses(String languageClasses) {
        this.languageClasses = languageClasses;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMethod1() {
        return method1;
    }

    public void setMethod1(String method1) {
        this.method1 = method1;
    }

    public String getMethod2() {
        return method2;
    }

    public void setMethod2(String method2) {
        this.method2 = method2;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getNta() {
        return nta;
    }

    public void setNta(String nta) {
        this.nta = nta;
    }

    public String getOverviewParagraph() {
        return overview_paragraph;
    }

    public void setOverviewParagraph(String overview_paragraph) {
        this.overview_paragraph = overview_paragraph;
    }

    public String getPctStuEnoughVariety() {
        return convertToDecimalForm(pct_stu_enough_variety);
    }

    public double getPctStuEnoughVarietyDouble(){

        String variety = getPctStuEnoughVariety();
        if (variety != null && !variety.isEmpty()){
            return Double.valueOf(variety);
        }else {
            Log.e("error", "can't return this as a double");
            return -1;
        }

    }

    public void setPctStuEnoughVariety(String pct_stu_enough_variety) {
        this.pct_stu_enough_variety = pct_stu_enough_variety;
    }

    public String getPctStuSafe() {
        if (pct_stu_safe != null){
            return convertToDecimalForm(pct_stu_safe);
        }else return "";
    }


    //todo: why not just store this as a double in the first place?
    public double getPctStuSafeDouble(){

        String safe = getPctStuSafe();
        if (safe != null && !safe.isEmpty()){
            return Double.valueOf(safe);
        }else {
            Log.e("error", "can't return this as a double");
            return -1;
        }

    }

    public void setPctStuSafe(String pct_stu_safe) {
        this.pct_stu_safe = pct_stu_safe;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public void setPhoneNumber(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPrgdesc1() {
        return prgdesc1;
    }

    public void setPrgdesc1(String prgdesc1) {
        this.prgdesc1 = prgdesc1;
    }

    public String getPrgdesc2() {
        return prgdesc2;
    }

    public void setPrgdesc2(String prgdesc2) {
        this.prgdesc2 = prgdesc2;
    }

    public String getPrimaryAddressLine1() {
        return primary_address_line1;
    }

    public void setPrimaryAddressLine1(String primary_address_line1) {
        this.primary_address_line1 = primary_address_line1;
    }

    public String getProgram1() {
        return program1;
    }

    public void setProgram1(String program1) {
        this.program1 = program1;
    }

    public String getProgram2() {
        return program2;
    }

    public void setProgram2(String program2) {
        this.program2 = program2;
    }

    public String getPsalSportsBoys() {
        return psal_sports_boys;
    }

    public void setPsalSportsBoys(String psal_sports_boys) {
        this.psal_sports_boys = psal_sports_boys;
    }

    public String getPsalSportsGirls() {
        return psal_sports_girls;
    }

    public void setPsalSportsGirls(String psal_sports_girls) {
        this.psal_sports_girls = psal_sports_girls;
    }

    public String getSchool10thSeats() {
        return school_10th_seats;
    }

    public void setSchool10thSeats(String school_10th_seats) {
        this.school_10th_seats = school_10th_seats;
    }

    public String getSchoolAccessibilityDescription() {
        return school_accessibility_description;
    }

    public void setSchoolAccessibilityDescription(String school_accessibility_description) {
        this.school_accessibility_description = school_accessibility_description;
    }

    public String getSchoolEmail() {
        return school_email;
    }

    public void setSchoolEmail(String school_email) {
        this.school_email = school_email;
    }

    public String getSchoolName() {
        return school_name;
    }

    public void setSchoolName(String school_name) {
        this.school_name = school_name;
    }

    public String getSeats101() {
        return seats101;
    }

    public void setSeats101(String seats101) {
        this.seats101 = seats101;
    }

    public String getSeats102() {
        return seats102;
    }

    public void setSeats102(String seats102) {
        this.seats102 = seats102;
    }

    public String getSeats9ge1() {
        return seats9ge1;
    }

    public void setSeats9ge1(String seats9ge1) {
        this.seats9ge1 = seats9ge1;
    }

    public String getSeats9ge2() {
        return seats9ge2;
    }

    public void setSeats9ge2(String seats9ge2) {
        this.seats9ge2 = seats9ge2;
    }

    public String getSeats9swd1() {
        return seats9swd1;
    }

    public void setSeats9swd1(String seats9swd1) {
        this.seats9swd1 = seats9swd1;
    }

    public String getSeats9swd2() {
        return seats9swd2;
    }

    public void setSeats9swd2(String seats9swd2) {
        this.seats9swd2 = seats9swd2;
    }

    public String getStartTime() {
        return start_time;
    }

    public void setStartTime(String start_time) {
        this.start_time = start_time;
    }

    public String getStateCode() {
        return state_code;
    }

    public void setStateCode(String state_code) {
        this.state_code = state_code;
    }

    public String getSubway() {
        return subway;
    }

    public void setSubway(String subway) {
        this.subway = subway;
    }

    public String getTotalStudents() {
        return total_students;
    }

    public void setTotalStudents(String totalStudents) {
        this.total_students = totalStudents;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public double getDistanceInMeters(){
        return distance;
    }

    public double setAndGetDistanceInMeters(double distance){
        this.distance = distance;
        return distance;
    }

    public DetailedSchool setDistanceAndGetThis(double distance){
        this.distance = distance;
        return this;
    }

    public void setDistanceInMeters(double distance){
        this.distance = distance;
    }

    public String getDistanceInMiles(){

        return String.format("%.1f", distance/1609.34);

    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public void setTotalSATScore(){ this.totalSATScore = getTotalSATScore();}

    public String getTotalSATScore(){

        if (mathSATScore == null || englishSATScore == null)
            return "";

        if (mathSATScore.isEmpty() || englishSATScore.isEmpty()){
            Log.e("error", "can't return total SAT score if either half is empty!");
            return "";
        }

        //testing for things like "n/a" or other non-numeric entries
        if (!android.text.TextUtils.isDigitsOnly(mathSATScore) || !android.text.TextUtils.isDigitsOnly(englishSATScore)){
            return "";
        }



        double mathScore = Double.valueOf(mathSATScore);
        double englishScore = Double.valueOf(englishSATScore);
        double totalScore = mathScore + englishScore;


        if (totalScore < 400){
            Log.e("impossible value", "SAT scores shouldn't go below 400");
        }

        String totalScoreString = String.format("%.0f", totalScore);
        return totalScoreString;
    }

    public void setEnglishSATScore(String score){this.englishSATScore = score;}

    public String getEnglishSATScore(){return englishSATScore;}

    public void setMathSATScore(String score){this.mathSATScore = score;}

    public String getMathSATScore(){return mathSATScore;}

    public void setWritingSATScore(String score){this.writingSATScore = score;}

    public String getWritingSATScore(){return writingSATScore;}

    public void setNumberOfTestTakers(String number){this.numberOfTestTakers = number;}

    public String getNumberOfTestTakers(){return numberOfTestTakers;}

    public double getTotalSATDouble(){
        String score = getTotalSATScore();
        if (score != null && !score.isEmpty()){
            return Double.valueOf(score);
        }else {
            Log.e("error", "can't return this score as a double");
            return -1;
        }

    }

    public double getGraduationRateDouble(){
        String rate = getGraduationRate();
        if (rate != null && !rate.isEmpty()){
            return Double.valueOf(rate);
        }else {
            Log.e("error", "can't return this graduation rate as a double");
            return -1;
        }
    }

    public double getCollegeCareerRateDouble(){
        String rate = getCollegeCareerRate();
        if (rate != null && !rate.isEmpty()){
            return Double.valueOf(rate);
        }else {
            Log.e("error", "can't return this college rate as a double");
            return -1;
        }
    }
}
