package db.dao;

import java.util.List;

import db.RewardType;
import db.responseparsers.RewardResponse;
import db.responseparsers.ThresholdResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface RewardDao {


    @GET("/api/threshold/get/{reward_type}")
    Call<List<ThresholdResponse>> getThreshold(@Path("reward_type") RewardType type);

    @POST("/api/reward/add")
    Call<List<RewardResponse>> addReward(@Body RewardResponse reset);

    @PUT("/api/threshold/update/{reward_type}")
    Call<List<ThresholdResponse>> setThreshold(@Body ThresholdResponse newThreshold);

}
