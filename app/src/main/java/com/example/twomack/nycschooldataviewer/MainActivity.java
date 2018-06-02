package com.example.twomack.nycschooldataviewer;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.twomack.nycschooldataviewer.data.DetailedSchool;
import com.example.twomack.nycschooldataviewer.data.School;
import com.example.twomack.nycschooldataviewer.networking.Networker;
import com.example.twomack.nycschooldataviewer.recyclerview.adapters.MainRecyclerViewAdapter;
import com.example.twomack.nycschooldataviewer.utilities.SchoolDataUtility;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;
import com.jakewharton.rxbinding2.support.v7.widget.SearchViewQueryTextEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.example.twomack.nycschooldataviewer.data.MainApplication.getApplicationDataModule;

public class MainActivity extends AppCompatActivity implements MainRecyclerViewAdapter.OnSchoolSelectedListener {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 11254;
    private MainRecyclerViewAdapter mAdapter;
    private Networker networker;
    public String[] usersLocation;
    SchoolDataUtility schoolDataUtility;

    @Nullable @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Nullable @BindView(R.id.loading_text)
    TextView loadingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);

        ButterKnife.bind(this);
        networker = Networker.getInstance();
        schoolDataUtility = new SchoolDataUtility();
        configureObservables();
        if (progressBar != null) {
            progressBar.setProgress(25);
        }

        //here we're starting a process that will make two network calls - first one for data about NYC schools in general, then one for SAT scores.
        //the data is then combined and locked (see setSearchData()), and can retrieved by calling getSearchData().
        //setContentView(R.layout.loading_screen);
        networker.fetchAllSchoolsDetailed();
    }



    /**
     * (Called after you've retrieved data from the network)
     *
     * This method sets the content view, binds variables with Butterknife, removes the title from the toolbar and sets up your recyclerView.
     */
    public void setUpMainView(){
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        RecyclerView mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MainRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        if (toolbar != null) {
            toolbar.setTitle("");
        }
        setSupportActionBar(toolbar);
    }

    public List<DetailedSchool> getSearchData() { return getApplicationDataModule().getSearchData(); }

    public void setSearchData(List<DetailedSchool> list){ getApplicationDataModule().setSearchData(list);}

    public List<DetailedSchool> getCurrentlyDisplayedSchools(){ return getApplicationDataModule().getCurrentlyDisplayedSchools(); }

    /**
     * This method sets three observers.
     * 1. An observer on detailSchoolList - when the network finishes downloading our list of detailed schools (and updates our list of detailed schools), this observer will update our progress bar on our loading screen,
     * and will fire off a second network request for simple SAT scores.
     * 2. An observer for simple SAT scores - when we've retrieved our second network request (SAT scores) this observer calls a method in our SchoolDataUtility to add these scores to our list of DetailedSchools.
     * Our data retrieval is now done, so it then calls methods to create the UI. It also calls a method 'setSearchData()', this is a list that we can pull from (and is never modified after it has been set initially) when we want data for our searches and filters.
     * 3. An observer that updates the recyclerView when our livedata for display schools is changed (ie: when a filter or a sort has been applied)
     */
    private void configureObservables() {
        getApplicationDataModule().getDetailSchoolList().observe(this, (List<DetailedSchool> schools) -> {
            if(schools != null) {
                if (progressBar != null){
                    progressBar.setProgress(70);
                }
                //when you get the detailed data, begins a request for the SAT data.
                networker.fetchAllSchoolsSAT();
            }else{
                launchErrorScreen();
            }
        });

        getApplicationDataModule().getSimpleSchools().observe(this, new android.arch.lifecycle.Observer<List<School>>() {
            @Override
            public void onChanged(@Nullable List<School> schools) {
                if(schools != null) {
                    if (progressBar != null){
                        progressBar.setProgress(80);
                    }
                    List<DetailedSchool> detailedList = getApplicationDataModule().getDetailSchoolList().getValue();
                    List<DetailedSchool> finalData = schoolDataUtility.appendSATScores(schools, detailedList);

                    //here we are done making network calls: our data has been finalized, and everything can pull from our searchData now.
                    setSearchData(finalData);
                    //calling setDisplaySchoolList updates our main UI by passing new data to the RecyclerView.
                    setUpMainView();
                    getApplicationDataModule().setDisplaySchools(getSearchData());
                }
            }
        });
        //this is where we update the RecyclerView. That occurs when this observer is triggered.
        getApplicationDataModule().getDisplaySchoolList().observe(this, detailedSchools -> {
            if(detailedSchools != null) {
                mAdapter.setSchoolList(detailedSchools);
            }
        });
    }

    /**
     * Sets an observer on LiveData that holds the information taken from the filter activity. When the user hits submit on the filter activity,
     * the filter requirements are updated and this observer is notified. This activity calls the method applyAllFilters in the SchoolDataUtility class.
     */
    public void setUpFilterObservable(){
        getApplicationDataModule().getFilterRequirements().observe(this, new android.arch.lifecycle.Observer<List<Integer>>() {
            @Override
            public void onChanged(@Nullable List<Integer> integers) {
                if(integers != null) {
                    List<DetailedSchool> toDisplay;
                    toDisplay = new ArrayList<>(getSearchData());
                    toDisplay = schoolDataUtility.applyAllFilters(toDisplay, integers.get(0), integers.get(1), integers.get(2), integers.get(3), integers.get(4), integers.get(5), integers.get(6), integers.get(7), integers.get(8), integers.get(9));
                    getApplicationDataModule().setDisplaySchools(toDisplay);
                    if (toDisplay.size() == 0){
                        launchErrorScreen();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method functions as a listener for the search feature. Once it determines that the user has submitted a search, (searchViewQueryTextEvent.isSubmitted()),
     * and the search is for at least one Character, it calls the method searchForSchoolName().
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //finds the searchView
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();

        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(RxSearchView.queryTextChangeEvents(searchView)
                .map(new Function<SearchViewQueryTextEvent, CharSequence>() {
                    @Override
                    public CharSequence apply(SearchViewQueryTextEvent searchViewQueryTextEvent) {

                        if (searchViewQueryTextEvent.isSubmitted()) {
                            return searchViewQueryTextEvent.queryText();
                        }
                        return "";
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) {
                        //charSequence is your search you're about to execute.
                        if (charSequence.length() == 0) {
                            return;
                        }

                        searchForSchoolName(charSequence);

                        //dismiss keyboard and close search view
                        searchView.setIconified(true);
                        if (toolbar != null) {
                            toolbar.collapseActionView();
                            compositeDisposable.dispose();
                        }
                    }
                }));
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     *
     * @param charSequence - what the user is searching for, submitted in the search bar.
     * This method prepares the search data by changing it to all-lowercase, (eliminating an earlier bug where searches were case-sensitive)
     * And calls a method in our SchoolDataUtility to filter the display list down to only the schools that contain that search in their name.
     *
     */
    public void searchForSchoolName(CharSequence charSequence) {

        //this is done to eliminate case-sensitivity
        CharSequence searchChars = charSequence.toString().toLowerCase();

        List<DetailedSchool> list = getSearchData();
        List<DetailedSchool> result;
         result = schoolDataUtility.filterByName(list, searchChars);

        getApplicationDataModule().setDisplaySchools(result);
        if (result.size() == 0){
            launchErrorScreen();
        }
    }

    //region menu clickListeners

    /**
     * If the app has the correct permissions and location services turned on, it sorts the schools by distance. Otherwise, it requests permissions and location services accordingly.
     */
    public void viewSchoolsByDistance(MenuItem m) {

        if (usersLocation != null) {
            List<DetailedSchool> schoolsByDistance = schoolDataUtility.getSchoolDistances(getCurrentlyDisplayedSchools(), usersLocation[0], usersLocation[1]);
            //this ends up populating this list to the RecycleView via observers.
            getApplicationDataModule().setDisplaySchools((schoolsByDistance));
        } else {
            //setUsersLocation includes a callback to viewSchoolsByDistance once it sets usersLocation - in other words,
            // if permission is granted and location services is turned on, it sorts the list and displays it.
            setUsersLocation();
        }
    }

    public void viewSchoolsBySATScore(MenuItem m) {
        List<DetailedSchool> sortedBySAT = schoolDataUtility.sortListBySAT(getCurrentlyDisplayedSchools());
        getApplicationDataModule().setDisplaySchools((sortedBySAT));
    }

    public void viewSchoolsBySafety(MenuItem m) {
        List<DetailedSchool> sortedBySafety = schoolDataUtility.sortListBySafety(getCurrentlyDisplayedSchools());
        getApplicationDataModule().setDisplaySchools(sortedBySafety);
    }

    public void viewSchoolsByGraduationRate(MenuItem m){
        List<DetailedSchool> sortedByGraduation = schoolDataUtility.sortListByGraduation(getCurrentlyDisplayedSchools());
        getApplicationDataModule().setDisplaySchools(sortedByGraduation);
    }

    public void viewSchoolsByCollegeRate(MenuItem m){
        List<DetailedSchool> sortedByCollege = schoolDataUtility.sortListByCollegeRate(getCurrentlyDisplayedSchools());
        getApplicationDataModule().setDisplaySchools(sortedByCollege);
    }

    public void viewSchoolsByAPNumber(MenuItem m){
        List<DetailedSchool> sortedByAP = schoolDataUtility.sortListByNumberOfAPs(getCurrentlyDisplayedSchools());
        getApplicationDataModule().setDisplaySchools(sortedByAP);
    }

    public void viewSchoolsByNeighborhood(MenuItem m){
        List<DetailedSchool> sortListByNeighborhood = schoolDataUtility.sortListByNeighborhood(getCurrentlyDisplayedSchools());
        getApplicationDataModule().setDisplaySchools(sortListByNeighborhood);
    }

    /**
     * starts the FilterActivity.
     */
    public void filterMenuButtonClicked(MenuItem m){
        setUpFilterObservable();
        Intent intent = new Intent(this, FilterActivity.class);
        startActivity(intent);
    }
    //endregion

    //region location methods

    /**
     * If location permissions haven't been granted, it requests them (and sets the users location in the permissions result if they were granted subsequently)
     * If permissions are ok, it attempts to find the users location, and if that fails, notifies the user that their location services may not be on.
     */
    public void setUsersLocation() {
        //in other words, if permission hasn't already been granted...
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[1];
            permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        } else {

            Location location = findLastLocation();

            if (location != null) {
                updateLocUtil(location);
                //now that we have the location, we're updating the ui
                viewSchoolsByDistance(null);
            } else {
                Log.e("warning", "location is still not set");
                notifyUserGPS();
            }
        }
    }

    /**
     * If permissions have been granted, this makes a call to viewSchoolsByDistance (which sorts the schools by distance and updates the UI).
     * If it is still unable to find the users' location despite being given permission, it suggests that the user turn on location services.
     *
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Location location;

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            location = findLastLocation();

            if (location != null) {
                updateLocUtil(location);
                //now that we have the locations, we're updating the ui
                viewSchoolsByDistance(null);
            } else {
                notifyUserGPS();
            }
        }
    }

    /**
     * Given the location of the user, this method simply extracts and formats the latitude and longitude and updates an instance variable.
     * @param location the location of the user.
     */
    private void updateLocUtil(Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        String strLongitude = String.valueOf(longitude);
        String strLatitude = String.valueOf(latitude);

        String[] locationString = new String[2];
        locationString[0] = strLatitude;
        locationString[1] = strLongitude;
        usersLocation = locationString;
    }

    /**
     *
     * @return the last known location of the user.
     *
     * Finds the last known location of the user. Tests all three location providers (gps, network, and passive) and returns the most accurate reading.
     */
    public Location findLastLocation() {
        LocationManager mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = null;
        if (mLocationManager != null) {
            providers = mLocationManager.getProviders(true);
        }
        Location bestLocation = null;
        if (providers != null) {
            for (String provider : providers) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return null;
                }
                Location l = mLocationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
        }
        return bestLocation;
    }

    /**
     * Notifies the user that location services may not be active, and starts an activity that gives them the option to it on.
     */
    public void notifyUserGPS(){
        // notify user
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(this.getResources().getString(R.string.gps_network_not_enabled));
        dialog.setPositiveButton(this.getResources().getString(R.string.open_loc_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);
                //get gps
            }
        });
        dialog.show();
    }
    //endregion

    /**
     * When a school in the recyclerView is clicked, this method launches the SchoolDetailActivity (shows a screen with school details).
     */
    @Override
    public void onSchoolClicked(int position) {
        Intent intent = new Intent(this, SchoolDetailActivity.class);
        intent.putExtra("school", getApplicationDataModule().getDisplaySchoolList().getValue().get(position));
        startActivity(intent);
    }

    public void launchErrorScreen(){
        Intent intent = new Intent(this, ErrorActivity.class);
        startActivity(intent);
    }

    public void showOnMap(MenuItem m){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
