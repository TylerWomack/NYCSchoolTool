package com.example.twomack.nycschooldataviewer.data;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.twomack.nycschooldataviewer.data.DetailedSchool;
import com.example.twomack.nycschooldataviewer.data.School;
import com.example.twomack.nycschooldataviewer.data.District;

import java.util.List;


public class ApplicationDataModule {
    private List<DetailedSchool> searchData;
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

    public void setFilterRequirements(List<Integer> filterRequirements){
        if (this.filterRequirements == null){
            this.filterRequirements = new MutableLiveData<>();
        }
        this.filterRequirements.setValue(filterRequirements);
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

    public List<DetailedSchool> getCurrentlyDisplayedSchools(){
            return getDisplaySchoolList().getValue();
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
    }

    public DetailedSchool getSchoolFromName(String name){
        for (DetailedSchool school : searchData){
            if (school.getSchoolName().equals(name))
                return school;
        }
        return null;
    }
}