package db.dao;

import java.util.List;


import db.responseparsers.PersonResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface PersonDao {

    @GET("/api/user/get/{username}")
    Call<PersonResponse> getPerson(@Path("username") String user);

    @GET("/api/user/active")
    Call<List<PersonResponse>> getAllActivePersons();

}
