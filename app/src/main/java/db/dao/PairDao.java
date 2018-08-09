package db.dao;

import java.util.List;

import db.RewardType;
import db.responseparsers.PairResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PairDao {

    @GET("/api/pair/all")
    Call<List<PairResponse>> getHistory();

    @POST("/api/pair/add")
    Call<PairResponse> insertPair(@Body PairResponse pair);

}
