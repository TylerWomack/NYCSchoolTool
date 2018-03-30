package com.example.twomack.nycschooldataviewer;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by twomack on 3/21/18.
 */

public interface NYCService {

    @GET("734v-jeq5.json?dbn={schoolId}")
    Call<List<School>> getSchoolById(@Path("schoolId") int schoolId);

    @GET("734v-jeq5.json")
    Observable<List<School>> getSchoolByName(@Query("school_name") String schoolName);

    @GET("734v-jeq5.json")
    Observable<List<School>> getSchoolByDbn(@Query("dbn") String dbn);

    @GET("734v-jeq5.json")
    Observable<List<School>> getAllSchools();

    @GET("97mf-9njv.json")
    Observable<List<DetailedSchool>> getAllSchoolsDetailed();

    @GET("97mf-9njv.json")
    Observable<List<DetailedSchool>> getSchoolDetailsByDBN(@Query("dbn") String schoolDbn);

    @GET("97mf-9njv.json")
    Observable<List<DetailedSchool>> getSchoolDetailsFromName(@Query("school_name") String school_name);



}
