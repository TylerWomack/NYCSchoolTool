package com.example.twomack.nycschooldataviewer.utilities;

import android.location.Location;

import com.example.twomack.nycschooldataviewer.data.DetailedSchool;
import com.example.twomack.nycschooldataviewer.data.School;
import com.example.twomack.nycschooldataviewer.data.District;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by twomack on 3/29/18.
 */

public class SchoolDataUtility {

    //region sorting methods

    //todo: figure out how this works

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
                //if they are tied
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

    public List<DetailedSchool> sortListByGraduation(List<DetailedSchool> list){
        Collections.sort(list, (DetailedSchool school, DetailedSchool t1) -> {
            if (school.getGraduationRateDouble() < t1.getGraduationRateDouble()) {
                return 1;
            } else {
                return -1;
            }
        });
        return list;
    }

    public List<DetailedSchool> sortListByCollegeRate(List<DetailedSchool> list){
        Collections.sort(list, (DetailedSchool school, DetailedSchool t1) -> {
            if (school.getCollegeCareerRateDouble() < t1.getCollegeCareerRateDouble()) {
                return 1;
            } else {
                return -1;
            }
        });
        return list;
    }

    public List<DetailedSchool> sortListByNumberOfAPs(List<DetailedSchool> list){
        Collections.sort(list, (DetailedSchool school, DetailedSchool t1) -> {
            if (findNumberOfAPClasses(school) < findNumberOfAPClasses(t1)) {
                return 1;
            } else if (findNumberOfAPClasses(school) > findNumberOfAPClasses(t1)){
                return -1;
            }else {
                return 0;
            }
        });
        return list;
    }

    public List<DetailedSchool> sortListByNeighborhood(List<DetailedSchool> list){
        Collections.sort(list, (DetailedSchool school, DetailedSchool t1) -> {
            if (school.getNeighborhood().compareTo(t1.getNeighborhood()) > 0) {
                return 1;
            } else if (school.getNeighborhood().compareTo(t1.getNeighborhood()) < 0){
                return -1;
            }else {
                return 0;
            }
        });
        return list;
    }
    //endregion

    //region filter methods
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

    private List<DetailedSchool> removeSelectedSchools(List<DetailedSchool> returnList, ArrayList<Integer> toRemove){
        for (int z = toRemove.size() -1; z > -1; z--){
            int remove = toRemove.get(z);
            returnList.remove(remove);
        }

        return returnList;
    }

    private List<DetailedSchool> filterBySafety(List<DetailedSchool> list, int requirement){

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
        return removeSelectedSchools(list, toRemove);
    }

    private List<DetailedSchool> filterBySAT(List<DetailedSchool> list, int requirement){

        ArrayList<Integer> toRemove = new ArrayList<>();

        for (int i = 0; i < list.size(); i++){
            if (list.get(i).getTotalSATDouble() < requirement){
                toRemove.add(i);
            }
        }

        return removeSelectedSchools(list, toRemove);
    }

    private List<DetailedSchool> filterByDistance(List<DetailedSchool> list, int requirement){

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
        return removeSelectedSchools(list, toRemove);

    }

    private List<DetailedSchool> filterByGraduationRate(List<DetailedSchool> list, int requirement){

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
        return removeSelectedSchools(list, toRemove);

    }

    private List<DetailedSchool> filterByAPClassesOffered(List<DetailedSchool> list, int requirement){

        ArrayList<Integer> toRemove = new ArrayList<>();

        for (int i = 0; i < list.size(); i++){

            if (findNumberOfAPClasses(list.get(i)) < requirement)
                toRemove.add(i);
        }
        return removeSelectedSchools(list, toRemove);


    }

    private List<DetailedSchool> filterByCollegeCareerRate(List<DetailedSchool> list, int requirement){

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
        return removeSelectedSchools(list, toRemove);

    }

    public List<DetailedSchool> filterByName(List<DetailedSchool> list, CharSequence charSequence){
        List<DetailedSchool> returnList = new ArrayList<>(list);
        for (DetailedSchool school : list ){
            if (!school.getSchoolName().toLowerCase().contains(charSequence)){
                returnList.remove(school);
            }
        }

        return returnList;
    }

    private List<DetailedSchool> filterByBoroughAllowed(List<DetailedSchool> list, int m, int brk, int brx, int qn, int st){

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

    private List<DetailedSchool> removeBorough(List<DetailedSchool> list, String borough){

        Iterator<DetailedSchool> iterator = list.iterator();
        while (iterator.hasNext()){
            DetailedSchool school = iterator.next();
            if (school.getBorough().contains(borough))
                iterator.remove();
        }

        return list;
    }
    //endregion


    /**
     * Combines the data from our two api calls. Appends SAT scores from simpleSchools to detailedSchools
     * @param simpleSchools a list of Schools (retrieved from our first API call - these are mostly just SAT scores)
     * @param detailedSchools a list of DetailedSchools (retrieved from our second API call, these contain almost everything you'd want to know about a school, except SAT scores)
     * @return a list of DetailedSchools that have been updated to reflect their SAT scores.
     */
    public List<DetailedSchool> appendSATScores(List<School> simpleSchools, List<DetailedSchool> detailedSchools){

        for (int i = 0; i < detailedSchools.size(); i++){
            String dbnDetailed = detailedSchools.get(i).getDbn();

            if (dbnDetailed == null)
                return null;

            for (int x = 0; x < simpleSchools.size(); x++){
                //if the simple school has a dbn...
                if (simpleSchools.get(x).getId() != null && !simpleSchools.get(x).getId().isEmpty()){
                    //if we have a match
                    if (simpleSchools.get(x).getId().equals(dbnDetailed)){
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

    /**
     * This method parses a string (a comma separated list of the AP classes offered at the school)
     * and derives the number of AP classes offered by finding the number of commas and adding one.
     * @param school a DetailedSchool
     * @return the number of AP classes offered at the school
     */
    private int findNumberOfAPClasses(DetailedSchool school){

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

    /**
     *
     * @param detailedSchools a list of detailedSchools (that have had school district integers added to them)
     * @return a list of Districts that contain average SAT scores, graduation rates, and safety ratings.
     */
    public List<District> buildDistricts(List<DetailedSchool> detailedSchools){

        Set<Integer> districtNumbers = new HashSet<>();
        //getting a Set of all the districts. Putting it in a set prevents duplicates.
        for (DetailedSchool school : detailedSchools){
            districtNumbers.add(school.getSchoolDistrict());
        }

        ArrayList<Integer> districtNumberList = new ArrayList<>(districtNumbers);
        List<District> districts = new ArrayList<>();
        for (Integer districtNumber : districtNumberList){
            if(districtNumber != null)
                districts.add(new District(districtNumber));
        }

        //here, we now should have an ArrayList of Districts, named districts.
        for (District district : districts){
            addSchoolsToDistrict(district, detailedSchools);
        }

            districts = addSATToDistricts(districts);
            districts = addSafetyToDistricts(districts);
            districts = addGraduationRateToDistricts(districts);

            return districts;
            //MainApplication.getApplicationDataModule().setSchoolDistricts(districts);
    }

    //

    /**
     * Finds the schools that belong to the district and adds them.
     * @param district an empty school district
     * @param schools a list of all schools (schools should have school district integers added to them)
     */
    private void addSchoolsToDistrict(District district, List<DetailedSchool> schools){
        ArrayList<DetailedSchool> schoolsToAdd = new ArrayList<>();
        for (DetailedSchool school : schools){

            if (school.getSchoolDistrict() != null && school.getSchoolDistrict() == district.getDistrictNumber()){
                schoolsToAdd.add(school);
            }
        }
        district.setSchoolsInDistrict(schoolsToAdd);
    }

    /**
     * Finds the distance between the user and a school
     * <br>
     * Tested using http://tjpeiffer.com/crowflies.html
     * @return distance between you and the school (in meters)
     */
    private double findSchoolDistance(Double latitudeSchool, Double longitudeSchool, Double yourLatitude, Double yourLongitude) {

        Location loc1 = new Location("");
        Location loc2 = new Location("");

        loc1.setLatitude(latitudeSchool);
        loc1.setLongitude(longitudeSchool);
        loc2.setLatitude(yourLatitude);
        loc2.setLongitude(yourLongitude);


        return (double) loc1.distanceTo(loc2);

        /*
         * See: https://community.esri.com/groups/coordinate-reference-systems/blog/2017/10/05/haversine-formula
         * Source: https://stackoverflow.com/questions/28510115/java-math-toradiansangle-vs-hard-calculated

        final int R = 6371; // Radius of the earth
        double latDistance = Math.toRadians(yourLatitude - latitudeSchool); //x2 - x1.
        double lonDistance = Math.toRadians(yourLongitude - longitudeSchool); // y2 - y1
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) //haversine
                + Math.cos(Math.toRadians(latitudeSchool)) * Math.cos(Math.toRadians(yourLatitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2); //haversine
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double test = R * c * 1000;
        return R * c * 1000; // convert to meters
        */

    }

    /**
     * Finds the distance between the user and each school, and adds it the the school objects. Sorts the list by distance and returns it.
     * @param list The list of DetailedSchools to sort
     * @param yourLatitude user's latitude
     * @param yourLongitude user's longitude
     * @return Returns a List of schools (with distance data now added), sorted by distance. Omits all schools who have null latitude or longitude data.
     */
    public List<DetailedSchool> getSchoolDistances(List<DetailedSchool> list, String yourLatitude, String yourLongitude) {

        double yourLat = Double.valueOf(yourLatitude);
        double yourLon = Double.valueOf(yourLongitude);

        List<DetailedSchool> result = null;
        List<DetailedSchool> distances;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

            distances = list.stream().filter(item -> item.getLatitude() != null)
                    .filter(item -> item.getLongitude() != null)
                    .map(item -> item.setDistanceAndGetThis(findSchoolDistance(Double.valueOf(item.getLatitude()), Double.valueOf(item.getLongitude()), yourLat, yourLon)))
                    .collect(Collectors.toList());

            result = sortListByDistance(distances);
        }else{
            for (DetailedSchool school: list){
                if (school.getLongitude() != null){
                    school.setDistanceAndGetThis(findSchoolDistance(Double.valueOf(school.getLatitude()), Double.valueOf(school.getLongitude()), yourLat, yourLon));
                }
            }
            return sortListByDistance(list);
        }
        return result;
    }

    //region methods to add stats to Districts
    private List<District> addSATToDistricts(List<District> districts){

        for (District district : districts){
            district.getAverageSAT();
        }
        //region unused proposed ranking method
        /*
        Collections.sort(districts, (District district, District district2) -> {
            if (district.getAverageSAT() == null && district2.getAverageSAT() != null){
                return -1;
            }else if (district.getAverageSAT() != null && district2.getAverageSAT() == null){
                return 1;
            }else if(district.getAverageSAT() == null && district2.getAverageSAT() == null){
                return 0;
            }

            if (district.getAverageSAT() > district2.getAverageSAT()){
                return 1;
            }else if(district2.getAverageSAT() > district.getAverageSAT()){
                return -1;
            }else return 0;
                }
        );
        int position = districts.size();
        for (District district : districts){
            district.setSATRank(position);
            position--;
        }
        */
        //endregion me me
        return districts;
    }

    private List<District> addSafetyToDistricts(List<District> districts){
        for (District district : districts){
            district.getPercentageOfStudentsSafe();
        }
        //region unused proposed ranking method
        /*
        Collections.sort(districts, (District district, District district2) -> {
                    if (district.getPercentageOfStudentsSafe() == null && district2.getPercentageOfStudentsSafe() != null){
                        return -1;
                    }else if (district.getPercentageOfStudentsSafe() != null && district2.getPercentageOfStudentsSafe() == null){
                        return 1;
                    }else if(district.getPercentageOfStudentsSafe() == null && district2.getPercentageOfStudentsSafe() == null){
                        return 0;
                    }

                    if (district.getPercentageOfStudentsSafe() > district2.getPercentageOfStudentsSafe()){
                        return 1;
                    }else if(district2.getPercentageOfStudentsSafe() > district.getPercentageOfStudentsSafe()){
                        return -1;
                    }else return 0;
                }
        );
        int position = districts.size();
        for (District district : districts){
            district.setSafetyRank(position);
            position--;
        }*/
        //endregion
        return districts;
    }

    private List<District> addGraduationRateToDistricts(List<District> districts){
        for (District district : districts){
            district.getAverageGraduationRate();
        }
        //region unused proposed ranking method
        /*
        Collections.sort(districts, (District district, District district2) -> {
                    if (district.getAverageGraduationRate() == null && district2.getAverageGraduationRate() != null){
                        return -1;
                    }else if (district.getAverageGraduationRate() != null && district2.getAverageGraduationRate() == null){
                        return 1;
                    }else if(district.getAverageGraduationRate() == null && district2.getAverageGraduationRate() == null){
                        return 0;
                    }

                    if (district.getAverageGraduationRate() > district2.getAverageGraduationRate()){
                        return 1;
                    }else if(district2.getAverageGraduationRate() > district.getAverageGraduationRate()){
                        return -1;
                    }else return 0;
                }
        );
        int position = districts.size();
        for (District district : districts){
            district.setGraduationRank(position);
            position--;
        }
        */
        //endregion
        return districts;
    }
    //endregion
}
