package Api;


import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import DB.Dao.PairDao;
import DB.Dao.RewardDao;
import DB.RewardType;
import com.BetterTogether.app.Logic.Pair;

import DB.Dao.PersonDao;
import com.BetterTogether.app.Logic.Reward;
import com.BetterTogether.app.Logic.Threshold;
import DB.ApiResponseHelpers.PairResponse;
import DB.ApiResponseHelpers.PersonResponse;
import DB.ApiResponseHelpers.ResponsePojoConverter;
import DB.ApiResponseHelpers.RewardResponse;
import DB.ApiResponseHelpers.ThresholdResponse;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class TestRest {

    private PersonDao personDao;
    private PairDao pairDao;
    private Retrofit API;
    private RewardDao rewardDao;

    @Rule
    public MockWebServer mockBackend = new MockWebServer();

    @Before
    public void createDb(){
        HttpUrl url = mockBackend.url("/");
        //String url = ("http://10.52.131.216:5000/");
        API = new Retrofit.Builder()
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(JacksonConverterFactory.create())
                .baseUrl(url)
                .build();

        personDao = API.create(PersonDao.class);
        pairDao = API.create(PairDao.class);
        rewardDao = API.create(RewardDao.class);

    }

    @Test
    public void mockingOk(){
        assertTrue(API != null);
    }

    @After
    public void closeDb() throws IOException {
      mockBackend.close();
    }

    private void addUserResponse(){
        mockBackend.enqueue(
                new MockResponse().setBody("{" +
                        "\"username\": \"ohald\"" +
                        ", \"name\": \"Oyvor\"" +
                        ", \"active\" : \"true\" }")
        );

    }

    private void addUserListResponse(){
        mockBackend.enqueue(
                new MockResponse().setBody("[ {" +
                        "\"username\": \"ohald\"" +
                        ", \"name\": \"Oyvor\"" +
                        ", \"active\" : \"true\" } ]")
        );

    }

    private void addPairResponse(){
        mockBackend.enqueue(
                new MockResponse().setBody("{" +
                        "\"person1\": \"esog\"" +
                        ", \"person2\": \"ohald\"" +
                        ", \"date\" : \"10000000\"}")
        );
    }

    private void addPairListResponse(){
        mockBackend.enqueue(
                new MockResponse().setBody("[{" +
                        "\"person1\": \"esog\"" +
                        ", \"person2\": \"ohald\"" +
                        ", \"date\" : \"10000000\"}]")
        );
    }

    private void addRewardResponse(){
        mockBackend.enqueue(
                new MockResponse().setBody("[{" +
                        "\"date\": \"10000\"" +
                        ", \"reward_type\": \"pizza\"" +
                        ", \"used_reward\" : \"false\"}]")
        );
    }

    private void addThresholdResponse(){
        mockBackend.enqueue(
                new MockResponse().setBody("[{" +
                        "\"reward_type\": \"pizza\"" +
                        ", \"threshold\" : \"50\"}]")
        );
    }

    private void addNumberResponse(){
        mockBackend.enqueue(
                new MockResponse().setBody("1")
        );
    }

    @Test
    public void canReadSingleNumberResponse() throws IOException {
        addNumberResponse();
        Integer i = rewardDao.numberOfUnusedRewards(RewardType.PIZZA).execute().body();
        assertThat(i, equalTo(1));
    }

    @Test
    public void canReadThresholdResponse() throws IOException {
        addThresholdResponse();
        ThresholdResponse r = rewardDao.setThreshold
                (ResponsePojoConverter.thresholdToThresholdResponse
                        (new Threshold(RewardType.PIZZA, 50))).execute().body().get(0);

        assertThat(r.getRewardtype(), equalTo(RewardType.PIZZA));
        assertThat(r.getThreshold(), equalTo(50));
    }


    @Test
    public void canReadRewardResponse() throws IOException{
        addRewardResponse();
        RewardResponse r = ResponsePojoConverter.rewardToRewardResponse(new Reward(new Date(), RewardType.PIZZA));
        RewardResponse res = rewardDao.addReward(r).execute().body().get(0);

        assertEquals(res.getDate(), "10000");
        assertEquals(res.getRewardtype(), RewardType.PIZZA);
        assertEquals(res.getUsedreward(), false);
    }


    @Test
    public void getPersonFromApiGivesPersonWithSameFields() throws IOException {
        addUserResponse();
        PersonResponse p = personDao.getPerson("ohald").execute().body();

        assertThat(p.getUsername(), equalTo("ohald"));
        assertThat(p.getName(), equalTo("Oyvor"));
        assertThat(p.getActive(), equalTo(true));
    }

    @Test
    public void getListOfPersonsWithOnePersonResponseFromApiGivesListOfPersonsWithSizeOne() throws IOException{
        addUserListResponse();

        List<PersonResponse> response = personDao.getAllPersons().execute().body();
        assertThat(response.size(), equalTo(1));
    }


    @Test
    public void addPairToApiReturnsPairResponse() throws IOException{
        addPairResponse();
        Pair p = new Pair("esog", "ohald", new Date(10000000));
        PairResponse p1 = pairDao.insertPair(
                ResponsePojoConverter.pairToPairResponse(p)).execute().body();

        assertThat(p1.getDate(), equalTo(Long.toString(p.getDate().getTime())));
        assertThat(p1.getPerson1(), equalTo(p.getPerson1()));
        assertThat(p1.getPerson2(), equalTo(p.getPerson2()));
    }

    @Test
    public void getAllPairsWithOnePairResponseReturnsListOfPairsWithLengthOne() throws IOException{
        addPairListResponse();
        assertThat(pairDao.getHistory().execute().body().size(), equalTo(1));
    }



}
