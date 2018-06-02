package com.example.twomack.nycschooldataviewer.utilities;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by twomack on 4/9/18.
 */

public class SchoolDistrictUtility {

    /**
     *
     * @param latitude the latitude of a school
     * @param longitude the longitude of a school
     * @param boundaryPoints the boundary points of the polygon we are testing
     * @return True if the school is located within the polygon, otherwise false.
     */
    public boolean contains(Double latitude, Double longitude, Point[] boundaryPoints) {
            Point schoolLocation = new Point(latitude, longitude);
            int i;
            int j;
            boolean result = false;
            //we're looping through all of the boundary points of the shape. point j is one step ahead of point i (the next boundary point).
            for (i = 0, j = boundaryPoints.length - 1; i < boundaryPoints.length; j = i++) {
                //for a point to be inside the shape, its y coordinate must be greater than one of the boundary points and less than another boundary point
                if ((boundaryPoints[i].y > schoolLocation.y) != (boundaryPoints[j].y > schoolLocation.y) &&

                        //https://stackoverflow.com/questions/8721406/how-to-determine-if-a-point-is-inside-a-2d-convex-polygon
                        //imagine drawing lines to the right of our school. If it intersects the polygon an odd number of times it is in the polygon.
                        //if it intersects the polygon an even number of times, it must be outside of the polygon.

                        //if we're here, you know that the point is within the y plane of the polygon.
                        //imagine drawing a line from our school to the right, forever. If we're here, we know that if we draw a horizontal line at our school,
                        //we'll intersect a wall of the polygon between points i and j. The question is, is the intersection point to the left or the right of our school?
                        (schoolLocation.x <
                                //the intersection point is calculated below
                                (boundaryPoints[j].x - boundaryPoints[i].x) * (schoolLocation.y - boundaryPoints[i].y) / (boundaryPoints[j].y- boundaryPoints[i].y) + boundaryPoints[i].x)) {
                    //flip the result (an odd number of intersections will lead to a positive result, an even number will lead to a negative result)
                    result = !result;
                }
            }
            return result;
        }

    /**
     * This is a utility method that converts a List of LatLng into an array of Points.
     * @param districtBoundaryPoints a list of latitudes and longitudes that make up the boundary points of a geojson polygon.
     * @return an array of Points
     */
    public Point[] getBoundaryPoints(List<LatLng> districtBoundaryPoints){
        Point[] boundaryPoints = new Point[districtBoundaryPoints.size()];

        for (int i = 0; i < districtBoundaryPoints.size(); i++){
            boundaryPoints[i] = new Point(districtBoundaryPoints.get(i).latitude, districtBoundaryPoints.get(i).longitude);
        }
        return boundaryPoints;
    }

    /**
     * a simple holder of x and y coordinates.
     */
    public class Point {
        final double x;
        final double y;

        Point(double x, double y){
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Example: This method first finds whether or not the value we are testing for is average, below average, or above average. If the value is, for example, above average, it
     * finds how far away the value is between the average and the best value in the set. If it is, for example, halfway between the best and the average value, it returns a color
     * that is halfway between green and grey. If the value is halfway between the worst value in the set and average, it returns a color that is halfway between red and grey. This way, values towards t
     * the top of the spectrum are greener, values towards the average are greyer, and values towards the low end are redder.
     * @param bestValueInData the best value in the set
     * @param worstValueInData the worst value in the set
     * @param averageValue the average (mean) value of the set
     * @param actualValue the value we are picking a color for
     * @return a color that is scaled appropriately from red to grey to green, depending on where the tested value lies within the range of possible values.
     */
    public int colorPicker(double bestValueInData, double worstValueInData, double averageValue, double actualValue){
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

            //example: if the average SAT score is 1000, the best is 1400, and ours is 1200, this should return .5 (aka we're halfway between the average and the best)
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


