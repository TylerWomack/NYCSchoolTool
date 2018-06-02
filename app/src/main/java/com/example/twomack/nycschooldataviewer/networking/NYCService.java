package com.example.twomack.nycschooldataviewer.networking;

import com.example.twomack.nycschooldataviewer.data.DetailedSchool;
import com.example.twomack.nycschooldataviewer.data.School;

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
    @GET("734v-jeq5.json")
    Observable<List<School>> getAllSchoolsSAT();

    @GET("97mf-9njv.json")
    Observable<List<DetailedSchool>> getAllSchoolsDetailed();
}
