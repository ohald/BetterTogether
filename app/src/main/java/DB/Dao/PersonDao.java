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

    @GET("/user/all")
    Call<List<PersonResponse>> getAllPersons();

    @GET("/user/get/{username}")
    Call<PersonResponse> getPerson(@Path("username") String user);

    @GET("/user/active")
    Call<List<PersonResponse>> getAllActivePersons();

    @POST("/user/add")
    Call<PersonResponse> insertPerson(@Body PersonResponse person);

    @PUT("/user/update")
    Call<PersonResponse> updatePerson(@Body PersonResponse person);

    @DELETE("/user/delete/{username}")
    Call<PersonResponse> deletePerson(PersonResponse person);
}
