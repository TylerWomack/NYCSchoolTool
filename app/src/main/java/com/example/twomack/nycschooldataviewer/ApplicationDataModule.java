package com.example.twomack.nycschooldataviewer;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

/**
 * Created by swaitkus on 4/4/18.
 */

public class ApplicationDataModule {
    private List<DetailedSchool> searchData;
    private String myString;
    private DetailedSchool school;
    private int timesSet = 0;
    private MutableLiveData<List<School>> schools;
    private MutableLiveData<List<DetailedSchool>> detailedSchools;
    private MutableLiveData<List<DetailedSchool>> displaySchools;

    private MutableLiveData<List<Integer>> filterRequirements;


    public MutableLiveData<List<Integer>> getFilterRequirements() {
        if(filterRequirements == null) {
            filterRequirements = new MutableLiveData<>();
        }
        return filterRequirements;
    }

    public void setFilterRequirements(MutableLiveData<List<Integer>> filterRequirements) {
        if(this.filterRequirements == null) {
            this.filterRequirements = new MutableLiveData<>();
        }
        this.filterRequirements = filterRequirements;
    }

    public LiveData<List<School>> getSimpleSchools() {
        if(schools == null) {
            schools = new MutableLiveData<>();
        }
        return schools;
    }

    public LiveData<List<DetailedSchool>> getDetailSchoolList() {
        if(detailedSchools == null) {
            detailedSchools = new MutableLiveData<>();
        }
        return detailedSchools;
    }

    public LiveData<List<DetailedSchool>> getDisplaySchoolList() {
        if(displaySchools == null) {
            displaySchools = new MutableLiveData<>();
        }
        return displaySchools;
    }


    public void setDetailedSchools(List<DetailedSchool> detailedSchools) {
        if(this.detailedSchools == null) {
            this.detailedSchools = new MutableLiveData<>();
        }
        this.detailedSchools.setValue(detailedSchools);
    }

    public void setDisplaySchools(List<DetailedSchool> displaySchools) {
        if(this.displaySchools == null) {
            this.displaySchools = new MutableLiveData<>();
        }
        this.displaySchools.setValue(displaySchools);
    }

    public void setSchools(List<School> schools) {
        if(this.schools == null) {
            this.schools = new MutableLiveData<>();
        }
        this.schools.setValue(schools);
    }

    public List<DetailedSchool> getSearchData() {
        return searchData;
    }

    public void setSearchData(List<DetailedSchool> list) {
        searchData = list;
        timesSet++;
    }

    public String getString() {
        return myString;
    }

    public void setString(String string) {
        myString = string;
    }



    public DetailedSchool getSchool() {
        return school;
    }

    public void setSchool(DetailedSchool school) {
        this.school = school;
    }

}