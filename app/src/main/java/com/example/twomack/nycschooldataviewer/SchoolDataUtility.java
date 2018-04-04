package com.example.twomack.nycschooldataviewer;

import android.arch.lifecycle.MutableLiveData;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by twomack on 3/29/18.
 */

public class SchoolDataUtility {

    //todo:change this name
    public MutableLiveData<List<DetailedSchool>> xxSearchData;

    public void setSearchData(List<DetailedSchool> list){
        xxSearchData.setValue(list);
    }

    public List<DetailedSchool> getSearchData(){
        return xxSearchData.getValue();
    }

    public double findDistance(Double latitudeSchool, Double longitudeSchool, Double yourLatitude, Double yourLongitude) {

        final int R = 6371; // Radius of the earth
        double latDistance = Math.toRadians(yourLatitude - latitudeSchool);
        double lonDistance = Math.toRadians(yourLongitude - longitudeSchool);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(latitudeSchool)) * Math.cos(Math.toRadians(yourLatitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);
        return Math.sqrt(distance);
    }

    //returns a List of schools, sorted by distance. Omits all schools who have null latitude or longitude data.
    public List<DetailedSchool> getSchoolDistances(List<DetailedSchool> list, String yourLatitude, String yourLongitude) {

        double yourLat = Double.valueOf(yourLatitude);
        double yourLon = Double.valueOf(yourLongitude);

        List<DetailedSchool> result = null;
        List<DetailedSchool> distances = null;
        //todo:evaluate this wrapper
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

            distances = list.stream().filter(item -> item.getLatitude() != null)
                    .filter(item -> item.getLongitude() != null)
                    .map(item -> item.setDistanceAndGetThis(findDistance(Double.valueOf(item.getLatitude()), Double.valueOf(item.getLongitude()), yourLat, yourLon)))
                    .collect(Collectors.toList());

            result = sortListByDistance(distances);
        }
        return result;
    }

    private List<DetailedSchool> sortListByDistance(List<DetailedSchool> list) {
        Collections.sort(list, (DetailedSchool school, DetailedSchool t1) -> {
            if (school.getDistanceInMeters() > t1.getDistanceInMeters()) {
                return 1;
            } else {
                return -1;
            }
        });
        return list;
    }

    public List<DetailedSchool> sortListBySafety(List<DetailedSchool> list){
        Collections.sort(list, (DetailedSchool school, DetailedSchool t1) -> {
            if (school.getPctStuSafeDouble() < t1.getPctStuSafeDouble()) {
                return 1;
            } else if(school.getPctStuSafeDouble() > t1.getPctStuSafeDouble()){
                return -1;
            }else {
                return 0;
            }
        });
        return list;
    }

    public List<DetailedSchool> sortListBySAT(List<DetailedSchool> list){
        Collections.sort(list, (DetailedSchool school, DetailedSchool t1) -> {
                if (school.getTotalSATDouble() < t1.getTotalSATDouble()) {
                    return 1;
                } else {
                    return -1;
                }
        });
        return list;
    }

    public List<DetailedSchool> appendSATScores(List<School> simpleSchools, List<DetailedSchool> detailedSchools){

        for (int i = 0; i < detailedSchools.size(); i++){
            String dbnDetailed = detailedSchools.get(i).getDbn();

            if (dbnDetailed == null)
                return null;

            for (int x = 0; x < simpleSchools.size(); x++){
                //if the simple school has a dbn...
                if (simpleSchools.get(x).getDbn() != null && !simpleSchools.get(x).getDbn().isEmpty()){
                    //if we have a match
                    if (simpleSchools.get(x).getDbn().equals(dbnDetailed)){
                        detailedSchools.get(i).setMathSATScore(simpleSchools.get(x).getSatMathAvgScore());
                        detailedSchools.get(i).setEnglishSATScore(simpleSchools.get(x).getSatCriticalReadingAvgScore());
                        detailedSchools.get(i).setWritingSATScore(simpleSchools.get(x).getSatWritingAvgScore());
                        detailedSchools.get(i).setNumberOfTestTakers(simpleSchools.get(x).getNumOfSatTestTakers());
                        detailedSchools.get(i).setTotalSATScore();
                    }
                }
            }
        }
        return detailedSchools;
    }

    public int findNumberOfAPClasses(DetailedSchool school){

        String APString = school.getAdvancedplacementCourses();

        if (APString == null)
            return 0;

        if (APString.isEmpty() || APString.length() == 0)
            return 0;

        int commas = 0;
        for(int i = 0; i < APString.length(); i++) {
            if(APString.charAt(i) == ',') commas++;
        }

        //accounting for the fact that in a list of AP classes, you'll have one less comma than classes (the last class won't have a comma)
        if (commas > 0)
            commas++;

        return commas;
    }

    /*
    Filters
     */

    private List<DetailedSchool> removeSelectedSchools(List<DetailedSchool> returnList, ArrayList<Integer> toRemove){
        for (int z = toRemove.size() -1; z > -1; z--){
            int remove = toRemove.get(z);
            returnList.remove(remove);
        }

        return returnList;
    }

    public List<DetailedSchool> filterBySafety(List<DetailedSchool> list, int requirement){

        List<DetailedSchool> returnList = list;
        ArrayList<Integer> toRemove = new ArrayList<>();

        for (int i = 0; i < list.size(); i++){
            if (list.get(i).getPctStuSafe().isEmpty() || list.get(i).getPctStuSafe() == null) {
                toRemove.add(i);
                i++;
            }else {
                if (Double.valueOf(list.get(i).getPctStuSafe()) < requirement){
                    toRemove.add(i);
                }
            }
        }
        return removeSelectedSchools(returnList, toRemove);
    }

    public List<DetailedSchool> filterBySAT(List<DetailedSchool> list, int requirement){

        List<DetailedSchool> returnList = list;
        ArrayList<Integer> toRemove = new ArrayList<>();

        for (int i = 0; i < list.size(); i++){
            if (list.get(i).getTotalSATDouble() < requirement){
                toRemove.add(i);
            }
        }

        return removeSelectedSchools(returnList, toRemove);
    }



    public List<DetailedSchool> filterByDistance(List<DetailedSchool> list, int requirement){

        List<DetailedSchool> returnList = list;
        ArrayList<Integer> toRemove = new ArrayList<>();

        for (int i = 0; i < list.size(); i++){
            if (list.get(i).getDistanceInMiles().isEmpty() || list.get(i).getDistanceInMiles() == null) {
                toRemove.add(i);
                i++;
            }else {
                if (Double.valueOf(list.get(i).getDistanceInMiles()) < requirement){
                    toRemove.add(i);
                }
            }
        }
        return removeSelectedSchools(returnList, toRemove);

    }

    public List<DetailedSchool> filterByGraduationRate(List<DetailedSchool> list, int requirement){

        List<DetailedSchool> returnList = list;
        ArrayList<Integer> toRemove = new ArrayList<>();

        for (int i = 0; i < list.size(); i++){

            if (list.get(i).getGraduationRate().isEmpty() || list.get(i).getGraduationRate() == null) {
                toRemove.add(i);
                i++;
            }else {
                if (Double.valueOf(list.get(i).getGraduationRate()) < requirement){
                    toRemove.add(i);
                }
            }
        }
        return removeSelectedSchools(returnList, toRemove);

    }

    public List<DetailedSchool> filterByAPClassesOffered(List<DetailedSchool> list, int requirement){

        List<DetailedSchool> returnList = list;
        ArrayList<Integer> toRemove = new ArrayList<>();

        for (int i = 0; i < list.size(); i++){

                if (findNumberOfAPClasses(list.get(i)) < requirement)
                    toRemove.add(i);
        }
        return removeSelectedSchools(returnList, toRemove);


    }

    public List<DetailedSchool> filterByCollegeCareerRate(List<DetailedSchool> list, int requirement){

        List<DetailedSchool> returnList = list;
        ArrayList<Integer> toRemove = new ArrayList<>();

        for (int i = 0; i < list.size(); i++){

            if (list.get(i).getCollegeCareerRate().isEmpty() || list.get(i).getCollegeCareerRate() == null){
                toRemove.add(i);
                i++;
            }else {
                if (Double.valueOf(list.get(i).getCollegeCareerRate()) < requirement){
                    toRemove.add(i);
                }
            }
        }
        return removeSelectedSchools(returnList, toRemove);

    }

    public List<DetailedSchool> filterByBoroughAllowed(List<DetailedSchool> list, int m, int brk, int brx, int qn, int st){



        if (m == 0)
        list = removeBorough(list, "Manhattan");
        if (brk == 0)
            list = removeBorough(list, "Brooklyn");
        if (brx == 0)
            list = removeBorough(list, "Bronx");
        if (qn == 0)
            list = removeBorough(list, "Queens");
        if (st == 0)
            list = removeBorough(list, "Staten is");

        return list;


    }

    public List<DetailedSchool> removeBorough(List<DetailedSchool> list, String borough){

        Iterator<DetailedSchool> iterator = list.iterator();
        while (iterator.hasNext()){
            DetailedSchool school = iterator.next();
            if (school.getBorough().contains(borough))
            iterator.remove();
        }

        return list;
    }

    /**
     *
     * @param list An unfiltered list
     * @param safetyRequirement your requirement for % of students that feel safe
     * @param SATRequirement your requirement for min SAT
     * @param graduationRequirement your requirement for min % of students that graduate
     * @param APRequirement your requirement for min number of AP classes offered
     * @param collegeRequirement your requirement for min % of students on a college career track
     * @return a filtered list.
     */
    public List<DetailedSchool> applyAllFilters(List<DetailedSchool> list, int safetyRequirement, int SATRequirement, int graduationRequirement, int APRequirement, int collegeRequirement, int manhattanAllowed, int brooklynAllowed, int bronxAllowed, int queensAllowed, int statenIsAllowed ){
        List<DetailedSchool> toReturn = list;
        toReturn = filterBySafety(toReturn, safetyRequirement);
        toReturn = filterBySAT(toReturn, SATRequirement);
        toReturn = filterByGraduationRate(toReturn, graduationRequirement);
        toReturn = filterByAPClassesOffered(toReturn, APRequirement);
        toReturn = filterByCollegeCareerRate(toReturn, collegeRequirement);
        toReturn = filterByBoroughAllowed(toReturn, manhattanAllowed, brooklynAllowed, bronxAllowed, queensAllowed, statenIsAllowed);
        return toReturn;
    }
}
