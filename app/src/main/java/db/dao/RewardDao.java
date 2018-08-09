package db.dao;

import java.util.List;

import db.responseparsers.RewardResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface RewardDao {


    @POST("/api/reward/add")
    Call<List<RewardResponse>> addReward(@Body RewardResponse reset);

}
