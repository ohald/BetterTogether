package db;

import java.util.List;

import db.responseparsers.PairResponse;
import db.responseparsers.PersonResponse;
import db.responseparsers.RewardResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Dao {

    @GET("/api/pair/all")
    Call<List<PairResponse>> getHistory();

    @POST("/api/pair/add")
    Call<PairResponse> insertPair(@Body PairResponse pair);

    @GET("/api/user/active")
    Call<List<PersonResponse>> getAllActivePersons();

    @GET("/api/reward/all")
    Call<List<RewardResponse>> getRewards();

}
