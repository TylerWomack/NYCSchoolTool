package com.example.twomack.nycschooldataviewer;
import java.util.List;

/**
 * Created by swaitkus on 4/4/18.
 */

public class ApplicationDataModule {
    private List<DetailedSchool> searchData;
    private List<Integer> integerList;
    private String myString;
    private DetailedSchool school;
    private int timesSet = 0;

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


    public List<Integer> getIntegerList() {
        return integerList;
    }

    public void setIntegerList(List<Integer> integerList) {
        this.integerList = integerList;
    }

    public DetailedSchool getSchool() {
        return school;
    }

    public void setSchool(DetailedSchool school) {
        this.school = school;
    }

}