package DB.Dao;

import java.util.List;

import DB.RewardType;
import DB.ApiResponseHelpers.PairResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PairDao {

    @GET("/api/pair/all")
    Call<List<PairResponse>> getHistory();

    @GET("/api/pair/all/after_last_reward/{reward_type}")
    Call<List<PairResponse>> getPairsSinceLastReward(@Path("reward_type") RewardType rewardType);

    @POST("/api/pair/add")
    Call<PairResponse> insertPair(@Body PairResponse pair);

}
