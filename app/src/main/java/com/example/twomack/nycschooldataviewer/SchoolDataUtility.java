package com.example.twomack.nycschooldataviewer;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by twomack on 3/29/18.
 */

public class SchoolDataUtility {

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

}
