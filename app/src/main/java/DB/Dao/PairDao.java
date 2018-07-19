package DB.Dao;

import java.util.Date;
import java.util.List;

import DB.RewardType;
import DB.ApiResponseHelpers.PairResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PairDao {

    @GET("api/pair/all")
    Call<List<PairResponse>> getHistory();

    @POST("api/pair/all/after_date/{date}")
    Call<List<PairResponse>> getPairsFromDate(@Path("date") @Body Date date);

    @GET("api/pair/all/after_last_reward/{reward_type}")
    Call<List<PairResponse>> getPairsSinceLastReward(@Path("reward_type") RewardType rewardType);

    @GET("/api/pair/with_user/<username>")
    Call<List<PairResponse>> getPairProgrammingPairs(@Path("username") String person);

    @GET("/api/pair/at_date/get/<date>")
    Call<PairResponse> getPair(@Path("date") Date date);

    @POST("/api/pair/add")
    Call<PairResponse> insertPair(@Body PairResponse pair);

    @PUT("/api/pair/at_date/update/<date>")
    PairResponse updatePair(@Body PairResponse pair);


    //undefined in API
    //Integer getPairProgrammingTotalFromDate(Date date);

    //undefined in API
    //@DELETE
    //Pair deletePair(Pair pair);

}
