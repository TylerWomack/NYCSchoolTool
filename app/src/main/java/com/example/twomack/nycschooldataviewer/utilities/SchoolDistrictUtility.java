package com.example.twomack.nycschooldataviewer.utilities;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by twomack on 4/9/18.
 */

public class SchoolDistrictUtility {

        private final Point[] points; // Points making up the boundary

    public SchoolDistrictUtility(List<LatLng> latLngList){

        Point[] constructPoints = new Point[latLngList.size()];

        for (int i = 0; i < latLngList.size(); i++){
            constructPoints[i] = new Point(latLngList.get(i).latitude, latLngList.get(i).longitude);
        }

        this.points = constructPoints;
    }

    public boolean contains(Double latitude, Double longitude) {

            Point test = new Point(latitude, longitude);
            int i;
            int j;
            boolean result = false;
            for (i = 0, j = points.length - 1; i < points.length; j = i++) {
                if ((points[i].y > test.y) != (points[j].y > test.y) &&
                        (test.x < (points[j].x - points[i].x) * (test.y - points[i].y) / (points[j].y-points[i].y) + points[i].x)) {
                    result = !result;
                }
            }
            return result;
        }

    public class Point {
        public final double x;
        public final double y;

        public Point(double x, double y){
            this.x = x;
            this.y = y;
        }

    }

    public static int colorPicker(double bestValueInData, double worstValueInData, double averageValue, double actualValue){
        int colorInt = 0;
        int alpha = 200;

        int[] grey = new int[3];
        grey[0] = 210;
        grey[1] = 210;
        grey[2] = 210;

        int[] reddest = new int[3];
        reddest[0] = 210;
        reddest[1] = 110;
        reddest[2] = 110;

        int[] greenest = new int[3];
        greenest[0] = 110;
        greenest[1] = 210;
        greenest[2] = 110;

        if (actualValue == averageValue)
            colorInt = Color.argb(alpha, grey[0], grey[1], grey[2]);

        if (actualValue > averageValue){
            double actualClearance = actualValue - averageValue;

            //example: if the average SAT score is 1000, the best is 1400, and ours is 1200, this should return 50% (aka we're halfway between the average and the best)
            double percentAbove = actualClearance/(bestValueInData - averageValue);

            int thisRed = grey[0] - (int) (percentAbove * (grey[0] - greenest[0]));
            int thisGreen = grey[1] - (int) (percentAbove * (grey[1] - greenest[1]));
            int thisBlue = grey[2] - (int) (percentAbove * (grey[2] - greenest[2]));

            colorInt = Color.argb(alpha, thisRed, thisGreen, thisBlue);
        }

        if (actualValue < averageValue){
            double actualClearance = averageValue - actualValue;
            double percentBelow = actualClearance/(averageValue - worstValueInData);

            int thisRed = grey[0] - (int) (percentBelow * (grey[0] - reddest[0]));
            int thisGreen = grey[1] - (int) (percentBelow * (grey[1] - reddest[1]));
            int thisBlue = grey[2] - (int) (percentBelow * (grey[2] - reddest[2]));
            colorInt = Color.argb(alpha, thisRed, thisGreen, thisBlue);
        }

        return colorInt;
    }
}


