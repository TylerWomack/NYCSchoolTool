package com.example.twomack.nycschooldataviewer;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.content.Context;
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

import com.example.twomack.nycschooldataviewer.recyclerview.adapters.MainRecyclerViewAdapter;
import com.example.twomack.nycschooldataviewer.viewmodel.MainViewModel;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;
import com.jakewharton.rxbinding2.support.v7.widget.SearchViewQueryTextEvent;

import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MainActivity extends AppCompatActivity implements MainRecyclerViewAdapter.OnSchoolSelectedListener {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 11254;
    private RecyclerView mRecyclerView;
    private MainRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Networker networker;
    private MainViewModel viewModel;
    private Boolean searchDataIsSet;
    private List<DetailedSchool> searchData;
    public String[] usersLocation;

    @Nullable @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.loading_screen);
        ButterKnife.bind(this);
        searchDataIsSet = false;
        networker = new Networker();
        configureObservables();
        progressBar.setProgress(25);

        //here we're starting a process that will make two network calls - first one for data about NYC schools in general, then one for SAT scores.
        //the data is then combined and locked (see setSearchData()), and can retrieved by calling getSearchData().
        //setContentView(R.layout.loading_screen);
        networker.allSchoolsDetailed();
    }

    public void setUpMainView(){
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        CompositeDisposableModule compositeDisposableModule = new CompositeDisposableModule();
        viewModel = new MainViewModel(compositeDisposableModule);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MainRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        setSupportActionBar(toolbar);

    }

    /**
     * This function is designed be set only one time. Its intended use is for the app to make api calls in onCreate, gather the necessary data, and set it here.
     * When the data is set, the program can then draw from it whenever necessary to receive a clean set of NYC school data. Note that setSearchData can only be called
     * once with any effect - any further calls would do nothing.
     * @param list A list of our complete data set of NYC schools, with appended SAT scores.
     */
    public void setSearchData(List<DetailedSchool> list) {
        if (searchDataIsSet) {
            return;
        }
        searchData = list;
        searchDataIsSet = true;
    }

    /**
     * This function is designed be set only one time. Its intended use is for the app to make api calls in onCreate, gather the necessary data, and set it here.
     * When the data is set, the program can then draw from it whenever necessary to receive a clean set of NYC school data.
     */
    public List<DetailedSchool> getSearchData() {
        return searchData;
    }

    public LiveData<List<School>> getSATSchoolList() {
        return networker.getSATSchoolList();
    }

    public LiveData<List<DetailedSchool>> getDetailedSchoolList() {
        return networker.getDetailedSchoolList();
    }

    public LiveData<List<DetailedSchool>> getDisplaySchoolList() {
        return networker.getDisplaySchoolList();
    }

    private void configureObservables() {
        getDetailedSchoolList().observe(this, new android.arch.lifecycle.Observer<List<DetailedSchool>>() {
            @Override
            public void onChanged(@Nullable List<DetailedSchool> schools) {
                progressBar.setProgress(70);
                //when you get the detailed data, begins a request for the SAT data.
                networker.allSchoolsSAT();
            }
        });
        getSATSchoolList().observe(this, new android.arch.lifecycle.Observer<List<School>>() {
            @Override
            public void onChanged(@Nullable List<School> schools) {
                progressBar.setProgress(80);
                SchoolDataUtility schoolDataUtility = new SchoolDataUtility();
                List<DetailedSchool> detailedList = getDetailedSchoolList().getValue();
                List<DetailedSchool> finalData = schoolDataUtility.appendSATScores(schools, detailedList);
                //here we are done making network calls: our data has been finalized, and everything can pull from our searchData now.
                setSearchData(finalData);
                //calling setDisplaySchoolList updates our main UI by passing new data to the RecyclerView.
                setUpMainView();
                networker.setDisplaySchoolList(getSearchData());
            }
        });
        //this is where we update the RecyclerView. That occurs when this observer is triggered.
        getDisplaySchoolList().observe(this, new android.arch.lifecycle.Observer<List<DetailedSchool>>() {
            @Override
            public void onChanged(@Nullable List<DetailedSchool> detailedSchools) {
                //setContentView(R.layout.activity_main);
                mAdapter.setSchoolList(detailedSchools);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //finds the searchView
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();

        viewModel.getDisposable().add(RxSearchView.queryTextChangeEvents(searchView)
                .map(new Function<SearchViewQueryTextEvent, CharSequence>() {
                    @Override
                    public CharSequence apply(SearchViewQueryTextEvent searchViewQueryTextEvent) throws Exception {

                        if (searchViewQueryTextEvent.isSubmitted()) {
                            return searchViewQueryTextEvent.queryText();
                        }
                        return "";
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        //charSequence is your search you're about to execute.
                        if (charSequence.length() == 0) {
                            return;
                        }

                        searchForSchoolName(charSequence);

                        //dismiss keyboard and close search view
                        searchView.setIconified(true);
                        toolbar.collapseActionView();
                    }
                }));
        return super.onPrepareOptionsMenu(menu);
    }

    //todo: this currently filters the list, removing all schools without the searched name. I'd prefer it to sort it instead? Also, this shouldn't be case sensitive.
    public void searchForSchoolName(CharSequence charSequence) {

        //this is done to eliminate case-sensitivity
        String chars = charSequence.toString();
        chars = chars.toLowerCase();
        CharSequence searchChars = chars;

        List<DetailedSchool> list = getSearchData();
        List<DetailedSchool> result = null;
        //todo: fix this. Either write it in a different way so you don't use the wrapper or add exception handling.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = list.stream().filter(item -> item.getSchoolName().toLowerCase().contains(searchChars))
                    .collect(Collectors.toList());
        }
        networker.setDisplaySchoolList(result);
        //todo: handling if no results found.
    }

    public void viewSchoolsByDistance(MenuItem m) {

        if (usersLocation != null) {
            //changing this list parameter would change the result of the returned values. In other words, you could run more complex searches by
            //limiting this list first to say, Manhattan, or schools with average SAT scores > 1200, then sorting by distance. Currently, by calling
            //getSearchData, you're retrieving a fresh dataset, which precludes more advanced filtering. You could overload the method, have it accept List as well?
            List<DetailedSchool> list = getSearchData();
            SchoolDataUtility su = new SchoolDataUtility();
            List<DetailedSchool> schoolsByDistance = su.getSchoolDistances(list, usersLocation[0], usersLocation[1]);
            //this ends up populating this list to the RecycleView.
            networker.setDisplaySchoolList(schoolsByDistance);
        } else {
            setUsersLocation();
        }
    }

    public void viewSchoolsBySATScore(MenuItem m) {
        SchoolDataUtility schoolDataUtility = new SchoolDataUtility();
        List<DetailedSchool> sortedBySAT = schoolDataUtility.sortListBySAT(getSearchData());
        networker.setDisplaySchoolList(sortedBySAT);
    }

    public void viewSchoolsBySafety(MenuItem m) {
        SchoolDataUtility schoolDataUtility = new SchoolDataUtility();
        List<DetailedSchool> sortedBySafety = schoolDataUtility.sortListBySafety(getSearchData());
        networker.setDisplaySchoolList(sortedBySafety);
    }

    public void setUsersLocation() {
        Location location = null;
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //in other words, if permission hasn't already been granted...
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[1];
            permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        } else {

            location = findLastLocation();

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

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Location location = null;

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

    public Location findLastLocation() {
        LocationManager mLocationManager;
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
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
        return bestLocation;
    }

    public void notifyUserGPS(){
        // notify user
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(this.getResources().getString(R.string.gps_network_not_enabled));
        dialog.setPositiveButton(this.getResources().getString(R.string.open_loc_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);
                //get gps
            }
        });
        dialog.setNegativeButton(this.getString(R.string.Cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub

            }
        });
        dialog.show();

    }

    @Override
    public void onSchoolClicked(int position) {

        String dbn = getDisplaySchoolList().getValue().get(position).getDbn();
        String schoolName = getDisplaySchoolList().getValue().get(position).getSchoolName();
        String gradesServed = getDisplaySchoolList().getValue().get(position).getGrades2018();
        String numStudents = getDisplaySchoolList().getValue().get(position).getTotalStudents();
        String gradRate = getDisplaySchoolList().getValue().get(position).getGraduationRate();
        String collegeCareerRate = getDisplaySchoolList().getValue().get(position).getCollegeCareerRate();
        String safeRate = getDisplaySchoolList().getValue().get(position).getPctStuSafe();
        String borough = getDisplaySchoolList().getValue().get(position).getBorough();
        String neighborhood = getDisplaySchoolList().getValue().get(position).getNeighborhood();
        String description = getDisplaySchoolList().getValue().get(position).getOverviewParagraph();
        String attendance = getDisplaySchoolList().getValue().get(position).getAttendanceRate();
        String totalSAT = getDisplaySchoolList().getValue().get(position).getTotalSATScore();
        String englishSAT = getDisplaySchoolList().getValue().get(position).getEnglishSATScore();
        String mathSAT = getDisplaySchoolList().getValue().get(position).getMathSATScore();
        String location = getDisplaySchoolList().getValue().get(position).getLocation();

        Intent intent = new Intent(this, SchoolDetailActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("dbn", dbn);
        intent.putExtra("schoolName", schoolName);
        intent.putExtra("gradesServed", gradesServed);
        intent.putExtra("numStudents", numStudents);
        intent.putExtra("gradRate", gradRate);
        intent.putExtra("collegeCareerRate", collegeCareerRate);
        intent.putExtra("safeRate", safeRate);
        intent.putExtra("borough", borough);
        intent.putExtra("neighborhood", neighborhood);
        intent.putExtra("description", description);
        intent.putExtra("attendance", attendance);
        intent.putExtra("totalSAT", totalSAT);
        intent.putExtra("englishSAT", englishSAT);
        intent.putExtra("mathSAT", mathSAT);
        intent.putExtra("location", location);
        startActivity(intent);
    }
}
