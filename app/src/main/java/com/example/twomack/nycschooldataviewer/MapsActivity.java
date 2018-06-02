package com.example.twomack.nycschooldataviewer;

import android.content.Context;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.twomack.nycschooldataviewer.data.DetailedSchool;
import com.example.twomack.nycschooldataviewer.data.District;
import com.example.twomack.nycschooldataviewer.data.MainApplication;
import com.example.twomack.nycschooldataviewer.utilities.SchoolDataUtility;
import com.example.twomack.nycschooldataviewer.utilities.SchoolDistrictUtility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonMultiPolygon;
import com.google.maps.android.data.geojson.GeoJsonPolygon;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;

import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GeoJsonLayer geoJsonLayer;
    Boolean hasIcons;
    List<District> districts;
    SchoolDistrictUtility schoolDistrictUtility;

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ButterKnife.bind(this);
        schoolDistrictUtility = new SchoolDistrictUtility();
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setTitle("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maps_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        addSchoolIconsAndFocusCamera();
        addSchoolZonesLayer();
        //buildDistricts gets the districts from disk if available, or it generates and saves them to disk.
        districts = getDistricts();

        //on the initial load, the map is colored to reflect SAT scores
        colorBySAT(districts);
    }

    //region menu clickListeners
    public void SATButtonClicked(MenuItem m){
        colorBySAT(districts);
    }

    public void safetyButtonClicked(MenuItem m){
        colorBySafety(districts);
    }

    public void graduationButtonClicked(MenuItem m){
        colorByGraduation(districts);
    }
    //endregion

    /**
     * This method creates and displays an icon for all of the currently displayed schools (the schools that were not removed by the filter in the previous activity),
     * It retrieves the location of the school, adds a marker, and sets a click listener for each marker that will display the name of the school and navigate to the details page if clicked.
     */
    public void addSchoolIconsAndFocusCamera(){
        List<DetailedSchool> displayedSchools = MainApplication.getApplicationDataModule().getCurrentlyDisplayedSchools();
        ArrayList<Double> latitudes = new ArrayList<>();
        ArrayList<Double> longitudes = new ArrayList<>();

        for (DetailedSchool school : displayedSchools){

            if (school.getLatitude() != null && school.getLongitude() != null && !school.getLongitude().equals("") && !school.getLatitude().equals("")){
                latitudes.add(Double.valueOf(school.getLatitude()));
                longitudes.add(Double.valueOf(school.getLongitude()));
                LatLng latLng = new LatLng(Double.valueOf(school.getLatitude()), Double.valueOf(school.getLongitude()));
                mMap.addMarker(new MarkerOptions().position(latLng).title(school.getSchoolName() + " (click for details)"));

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        String originalTitle = marker.getTitle();
                        String title = originalTitle.substring(0, originalTitle.length() - 20);
                        DetailedSchool school = MainApplication.getApplicationDataModule().getSchoolFromName(title);
                        if (school != null){
                            Intent intent = new Intent(getApplicationContext(), SchoolDetailActivity.class);
                            intent.putExtra("school", school);
                            startActivity(intent);
                        }
                    }
                });
            }
        }

        LatLng latLng = new LatLng(findAverageLatitude(latitudes), findAverageLongitude(longitudes));
        //orients camera to center on the average latitude and longitude of the schools on the map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //zooms in
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f));
        hasIcons = true;
    }

    //region adding school zones to map

    public void addSchoolZonesLayer(){
        geoJsonLayer = getGeoJsonLayer();
        geoJsonLayer.addLayerToMap();
    }

    /**
     * If the geoJsonLayer has already been created, it returns it. Otherwise, it creates it using a GeoJson file in the raw folder.
     * @return a geoJsonLayer (school zone polygons)
     */
    public GeoJsonLayer getGeoJsonLayer(){
        if (geoJsonLayer == null){
            try {
                geoJsonLayer = new GeoJsonLayer(mMap, R.raw.sdistricts, getApplicationContext());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            return geoJsonLayer;
        }
        return geoJsonLayer;
    }
    //endregion

    /**
     * Checks the disk for saved data related to school districts (a mapping of schools and their district). If it isn't found, it creates this mapping and saves it to disk.
     * Using the map of schools and districts (which has either been retrieved from disk or freshly created), it appends school district information to
     * our list of schools in the application data module. It then creates and returns a list of Districts.
     *
     * @return a list of Districts
     */
    public List<District> getDistricts(){

        //dbnsAndDistricts is a mapping of DetailedSchool dbns and their school districts used to create Districts
        HashMap<String, Integer> dbnsAndDistricts = getDistrictDataFromDisk();

        if (dbnsAndDistricts == null){
            //this is how we generate the data for the first time. Do this if you haven't saved it to disk yet. This takes less than three seconds on most devices.
            dbnsAndDistricts = generateSchoolDistrictData();
            saveDistrictsToDisk(dbnsAndDistricts);
        }

        //building districts from our mapping of dbns and district #s.
        if (dbnsAndDistricts != null){
            //adding district data to our DetailedSchools
            setDistrictData(dbnsAndDistricts);
            List<DetailedSchool> searchData = MainApplication.getApplicationDataModule().getSearchData();
            SchoolDataUtility schoolDataUtility = new SchoolDataUtility();
            //this creates districts and saves them in MainApplicationDataModule
            return schoolDataUtility.buildDistricts(searchData);
        }
        return null;
    }

    //region methods to generate school district data

    /**
     *
     * This method iterates over all of the school districts in our geoJsonLayer. Each of the school districts is composed of one of more polygons (up to 21 different polygons).
     * This method iterates over each of the polygons that compose a school district, and tests every school against every polygon - if the school is in the polygon, we know it is in
     * the school district. Once a school has been found within a polygon, we cease testing it to save memory.
     * @return a hashmap containing school ids and associated school districts.
     */
    public HashMap<String, Integer> generateSchoolDistrictData(){

        geoJsonLayer = getGeoJsonLayer();
        HashMap<String, Integer> dbnsAndDistricts = new HashMap<>();
        List<DetailedSchool> schoolsToCheck = new ArrayList<>(MainApplication.getApplicationDataModule().getSearchData());

        for (GeoJsonFeature feature : geoJsonLayer.getFeatures()) {

            //each feature is a school district
            GeoJsonMultiPolygon multiPolygon = (GeoJsonMultiPolygon) feature.getGeometry();
            List<GeoJsonPolygon> polygonList = multiPolygon.getPolygons();

            //iterating over all the polygons that make up a school district
            for (GeoJsonPolygon district : polygonList) {

                List<LatLng> districtBoundaryPoints = district.getOuterBoundaryCoordinates();

                SchoolDistrictUtility.Point[] boundaryPoints = schoolDistrictUtility.getBoundaryPoints(districtBoundaryPoints);
                //iterating over all the schools
                for (Iterator<DetailedSchool> iterator = schoolsToCheck.iterator(); iterator.hasNext(); ) {
                    DetailedSchool school = iterator.next();
                    Boolean schoolIsInPolygon = findIfSchoolIsInDistrict(school, boundaryPoints);

                    //if the school is in the district, add it to our hashmap, and remove the school from the list of schools to check.
                    if (schoolIsInPolygon) {
                        String districtName = feature.getProperty("school_dist");
                        dbnsAndDistricts.put(school.getDbn(), Integer.valueOf(districtName));
                        iterator.remove();
                    }
                }
            }
        }
        return dbnsAndDistricts;
    }

    /**
     * Formats data and calls a method in SchoolDistrictUtility to find if a school is located in a given polygon.
     * @param school the school we are testing
     * @param boundaryPoints the boundary points of the polygon we are testing
     * @return true if the school is found within the polygon, otherwise false.
     */
    public Boolean findIfSchoolIsInDistrict(DetailedSchool school, SchoolDistrictUtility.Point[] boundaryPoints){

        String lat = school.getLatitude();
        String lon = school.getLongitude();

        Double latitude = null;
        Double longitude = null;

        if (lat != null && lon != null) {
            latitude = Double.valueOf(lat);
            longitude = Double.valueOf(lon);
        }

        if (latitude != null && longitude != null) {
            Boolean contains = schoolDistrictUtility.contains(Double.valueOf(school.getLatitude()), Double.valueOf(school.getLongitude()), boundaryPoints);

            if (contains){
                return true;
            }
        }
        return false;
    }
    //endregion

    /**
     * If the map doesn't have icons, it adds them. If it does, it removes them.
     */
    public void toggleIcons(MenuItem m){

        if (!hasIcons){
            addSchoolIconsAndFocusCamera();
            hasIcons = true;
        }else {
            mMap.clear();
            addSchoolZonesLayer();
            colorBySAT(districts);
            hasIcons = false;
        }
    }

    //region map coloring methods
    public void colorBySAT(List<District> districts){
        double highestSAT = 0;
        double lowestSAT = 10000;

        ArrayList<Double> SATs = new ArrayList<>();

        //finding highest, lowest, and middle
        for (District district : districts){
            if (district.getAverageSAT() == null)
                continue;
            if (district.getAverageSAT() > highestSAT)
                highestSAT = district.getAverageSAT();
            if (district.getAverageSAT() < lowestSAT)
                lowestSAT = district.getAverageSAT();

            SATs.add(district.getAverageSAT());
        }

        double totalScore = 0;
        double schoolCount = 0;
        double average = 0;
        for (Double score : SATs){
            totalScore = totalScore + score;
            schoolCount++;
        }

        average = totalScore/schoolCount;

        for (District district : districts){
            if (district.getAverageSAT() != null)
            changeDistrictColor(district, schoolDistrictUtility.colorPicker(highestSAT, lowestSAT, average, district.getAverageSAT()));
        }
    }

    public void colorBySafety(List<District> districts){
        double highestSafety = 0;
        double lowestSafety = 1000;

        ArrayList<Double> safetyList = new ArrayList<>();

        //finding highest, lowest, and middle
        for (District district : districts){
            if (district.getPercentageOfStudentsSafe() == null)
                continue;
            if (district.getPercentageOfStudentsSafe() > highestSafety)
                highestSafety = district.getPercentageOfStudentsSafe();
            if (district.getPercentageOfStudentsSafe() < lowestSafety)
                lowestSafety = district.getPercentageOfStudentsSafe();

            safetyList.add(district.getPercentageOfStudentsSafe());
        }

        double totalScore = 0;
        double schoolCount = 0;
        double average;
        for (Double score : safetyList){
            totalScore = totalScore + score;
            schoolCount++;
        }

        average = totalScore/schoolCount;

        for (District district : districts){
            if (district.getAverageSAT() != null)
                changeDistrictColor(district, schoolDistrictUtility.colorPicker(highestSafety, lowestSafety, average, district.getPercentageOfStudentsSafe()));
        }
    }

    public void colorByGraduation(List<District> districts){
        double highestGrad = 0;
        double lowestGrad = 10000;

        ArrayList<Double> gradList = new ArrayList<>();

        //finding highest, lowest, and middle
        for (District district : districts){
            if (district.getAverageGraduationRate() == null)
                continue;
            if (district.getAverageGraduationRate() > highestGrad)
                highestGrad = district.getAverageGraduationRate();
            if (district.getAverageGraduationRate() < lowestGrad)
                lowestGrad = district.getAverageGraduationRate();

            gradList.add(district.getAverageGraduationRate());
        }

        double totalScore = 0;
        double schoolCount = 0;
        double average;
        for (Double score : gradList){
            totalScore = totalScore + score;
            schoolCount++;
        }

        average = totalScore/schoolCount;

        for (District district : districts){
            if (district.getAverageSAT() != null)
                changeDistrictColor(district, schoolDistrictUtility.colorPicker(highestGrad, lowestGrad, average, district.getAverageGraduationRate()));
        }
    }

    public void changeDistrictColor(District district, int color){

        GeoJsonPolygonStyle polygonStyle = new GeoJsonPolygonStyle();
        polygonStyle.setFillColor(color);
        geoJsonLayer = getGeoJsonLayer();

        for (Iterator featureIterator = geoJsonLayer.getFeatures().iterator(); featureIterator.hasNext();){
            //for our purposes, a feature is a district
            GeoJsonFeature feature = (GeoJsonFeature) featureIterator.next();
            String featureDistrictNumber = feature.getProperty("school_dist");
            int featureDistrict = Integer.parseInt(featureDistrictNumber);
            //if we've found the our district's feature (polygon)
            if (district.getDistrictNumber() == featureDistrict){
                feature.setPolygonStyle(polygonStyle);
            }
        }
    }
    //endregion

    //region utility methods for camera orientation
    public double findAverageLatitude(ArrayList<Double> latitudes){
        double sum = 0;
        int count = 0;
        for (double lat : latitudes){
           sum = sum + lat;
           count++;
       }
       return sum/count;
    }

    public double findAverageLongitude(ArrayList<Double> longitudes){
        double sum = 0;
        int count = 0;
        for (double lon : longitudes){
            sum = sum + lon;
            count++;
        }
        return sum/count;
    }
    //endregion

    //region methods for saving/loading school district data
    public void saveDistrictsToDisk(HashMap<String, Integer> dbnsAndDistricts){
        String filename = "districtData";
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(dbnsAndDistricts);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Integer> getDistrictDataFromDisk(){
        String filename = "districtData";
        FileInputStream inputStream;
        HashMap<String, Integer> dbnsAndDistricts = null;
        try {
            inputStream = openFileInput(filename);
            openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(inputStream);
            dbnsAndDistricts = (HashMap<String, Integer>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return dbnsAndDistricts;
    }

    public void setDistrictData(HashMap<String, Integer> dbnsAndDistricts){

        if (!dbnsAndDistricts.isEmpty()){
            List<DetailedSchool> schools = new ArrayList<>(MainApplication.getApplicationDataModule().getSearchData());
            for (DetailedSchool school : schools){
                if (dbnsAndDistricts.containsKey(school.getDbn())){
                    school.setSchoolDistrict(dbnsAndDistricts.get(school.getDbn()));
                }
            }
            MainApplication.getApplicationDataModule().setSearchData(schools);
        }
    }
    //endregion
}
