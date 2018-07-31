package DB.Dao;

import java.util.List;


import DB.ApiResponseHelpers.PersonResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface PersonDao {

    @GET("/api/user/all")
    Call<List<PersonResponse>> getAllPersons();

    @GET("/api/user/get/{username}")
    Call<PersonResponse> getPerson(@Path("username") String user);

    @GET("/api/user/active")
    Call<List<PersonResponse>> getAllActivePersons();

}
