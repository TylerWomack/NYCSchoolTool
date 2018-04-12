package com.example.twomack.nycschooldataviewer;

import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.Polygon;
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
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

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

        addSchoolIcons();
        hasIcons = true;
        addSchoolZonesLayer();
        buildDistricts();
        List<District> districts = MainApplication.getApplicationDataModule().getSchoolDistricts().getValue();
        colorBySafety(districts);
    }

    public void buildDistricts(){
        //dbnsAndDistricts is a mapping of DetailedSchool dbns and their school districts used to create Districts
        HashMap<String, Integer> dbnsAndDistricts = getDistrictDataFromDisk();
        if (dbnsAndDistricts == null){
            //this is how we generate the data for the first time. Do this if you haven't saved it to disk yet. This takes a few seconds.
            dbnsAndDistricts = findSchoolDistricts();
            saveDistrictsToDisk(dbnsAndDistricts);
        }

        //building districts from our mapping of dbns and district #s.
        if (dbnsAndDistricts != null){
            //adding district data to our DetailedSchools
            setDistrictData(dbnsAndDistricts);
            List<DetailedSchool> searchData = MainApplication.getApplicationDataModule().getSearchData();
            SchoolDataUtility schoolDataUtility = new SchoolDataUtility();
            //this creates districts and saves them in MainApplicationDataModule
            schoolDataUtility.buildDistricts(searchData);
        }
    }

    public void addSchoolIcons(){
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

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(findAverageLatitude(latitudes), findAverageLongitude(longitudes))));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(findAverageLatitude(latitudes), findAverageLongitude(longitudes)), 11f));
    }

    public void addSchoolZonesLayer(){
        geoJsonLayer = getGeoJsonLayer();
        geoJsonLayer.addLayerToMap();
    }

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

    public HashMap<String, Integer> findSchoolDistricts(){

        geoJsonLayer = getGeoJsonLayer();
        HashMap<String, Integer> dbnsAndDistricts = new HashMap<>();
        List<DetailedSchool> schoolsToCheck = new ArrayList<>(MainApplication.getApplicationDataModule().getSearchData());

        for (Iterator featureIterator = geoJsonLayer.getFeatures().iterator(); featureIterator.hasNext();){
            GeoJsonFeature feature = (GeoJsonFeature) featureIterator.next();
            GeoJsonMultiPolygon multiPolygon = (GeoJsonMultiPolygon) feature.getGeometry();
            List<GeoJsonPolygon> polygonList = multiPolygon.getPolygons();
            for (GeoJsonPolygon polygon : polygonList){

                List<LatLng> latLngs = polygon.getOuterBoundaryCoordinates();

                SchoolDistrictUtility schoolDistrictUtility = new SchoolDistrictUtility(latLngs);

                for (Iterator<DetailedSchool> iterator = schoolsToCheck.iterator(); iterator.hasNext();){
                    DetailedSchool school = iterator.next();
                    Boolean schoolIsInPolygon = findIfSchoolIsInPolygon(school, schoolDistrictUtility);

                    if (schoolIsInPolygon){
                        String district = feature.getProperty("school_dist");
                        dbnsAndDistricts.put(school.getDbn(), Integer.valueOf(district));
                        iterator.remove();
                    }
                }
            }
        }
        return dbnsAndDistricts;
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

    public void SATButtonClicked(MenuItem m){
        colorBySAT(MainApplication.getApplicationDataModule().getSchoolDistricts().getValue());
    }

    public void safetyButtonClicked(MenuItem m){
        colorBySafety(MainApplication.getApplicationDataModule().getSchoolDistricts().getValue());
    }

    public void graduationButtonClicked(MenuItem m){
        colorByGraduation(MainApplication.getApplicationDataModule().getSchoolDistricts().getValue());
    }

    public void toggleIcons(MenuItem m){

        if (!hasIcons){
            addSchoolIcons();
            hasIcons = true;
        }else {
            mMap.clear();
            hasIcons = false;
        }
    }

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
            changeDistrictColor(district, SchoolDistrictUtility.colorPicker(highestSAT, lowestSAT, average, district.getAverageSAT()));
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
        double average = 0;
        for (Double score : safetyList){
            totalScore = totalScore + score;
            schoolCount++;
        }

        average = totalScore/schoolCount;

        for (District district : districts){
            if (district.getAverageSAT() != null)
                changeDistrictColor(district, SchoolDistrictUtility.colorPicker(highestSafety, lowestSafety, average, district.getPercentageOfStudentsSafe()));
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
        double average = 0;
        for (Double score : gradList){
            totalScore = totalScore + score;
            schoolCount++;
        }

        average = totalScore/schoolCount;

        for (District district : districts){
            if (district.getAverageSAT() != null)
                changeDistrictColor(district, SchoolDistrictUtility.colorPicker(highestGrad, lowestGrad, average, district.getAverageGraduationRate()));
        }
    }

    public Boolean findIfSchoolIsInPolygon(DetailedSchool school, SchoolDistrictUtility schoolDistrictUtility){

        String lat = school.getLatitude();
        String lon = school.getLongitude();

        Double latitude = null;
        Double longitude = null;

        if (lat != null && lon != null) {
            latitude = Double.valueOf(lat);
            longitude = Double.valueOf(lon);
        }

        if (latitude != null && longitude != null) {
            Boolean contains = schoolDistrictUtility.contains(Double.valueOf(school.getLatitude()), Double.valueOf(school.getLongitude()));

            if (contains){
                return true;
            }
        }
        return false;
    }

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

}
