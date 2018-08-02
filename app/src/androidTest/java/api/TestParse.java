package api;


import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import db.dao.PairDao;
import db.dao.RewardDao;
import db.RewardType;
import com.bettertogether.app.Pair;

import db.dao.PersonDao;
import com.bettertogether.app.Reward;
import com.bettertogether.app.Threshold;
import db.responseparsers.PairResponse;
import db.responseparsers.PersonResponse;
import db.responseparsers.ResponsePojoConverter;
import db.responseparsers.RewardResponse;
import db.responseparsers.ThresholdResponse;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class TestParse {

    private PersonDao personDao;
    private PairDao pairDao;
    private Retrofit API;
    private RewardDao rewardDao;

    @Rule
    public MockWebServer mockBackend = new MockWebServer();

    @Before
    public void createDb(){
        HttpUrl url = mockBackend.url("/");
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
    public void canParseSingleNumberResponse() throws IOException {
        addNumberResponse();
        Integer i = rewardDao.numberOfUnusedRewards(RewardType.PIZZA).execute().body();
        assertThat(i, equalTo(1));
    }

    @Test
    public void canParseThresholdResponse() throws IOException {
        addThresholdResponse();
        ThresholdResponse r = rewardDao.setThreshold
                (ResponsePojoConverter.thresholdToThresholdResponse
                        (new Threshold(RewardType.PIZZA, 50))).execute().body().get(0);

        assertThat(r.getRewardType(), equalTo(RewardType.PIZZA));
        assertThat(r.getThreshold(), equalTo(50));
    }


    @Test
    public void canParseRewardResponse() throws IOException{
        addRewardResponse();
        RewardResponse r = ResponsePojoConverter.rewardToRewardResponse(
                new Reward(
                        Long.toString(System.currentTimeMillis()/1000),
                        RewardType.PIZZA));
        RewardResponse res = rewardDao.addReward(r).execute().body().get(0);

        assertThat(res.getDate(), equalTo("10000"));
        assertThat(res.getRewardType(), equalTo(RewardType.PIZZA));
        assertThat(res.getUsedReward(), equalTo(false));
    }


    @Test
    public void canParsePersonResponse() throws IOException {
        addUserResponse();
        PersonResponse p = personDao.getPerson("ohald").execute().body();

        assertThat(p.getUsername(), equalTo("ohald"));
        assertThat(p.getName(), equalTo("Oyvor"));
        assertThat(p.getActive(), equalTo(true));
    }

    @Test
    public void canParseListOfPeople() throws IOException{
        addUserListResponse();

        List<PersonResponse> response = personDao.getAllActivePersons().execute().body();
        assertThat(response.size(), equalTo(1));
    }


    @Test
    public void canParsePairResponse() throws IOException{
        addPairResponse();
        Pair p = new Pair("esog", "ohald", "10000000");
        PairResponse r = pairDao.insertPair(
                ResponsePojoConverter.pairToPairResponse(p)).execute().body();

        assertThat(r.getDate(), equalTo(p.getDate()));
        assertThat(r.getPerson1(), equalTo(p.getPerson1()));
        assertThat(r.getPerson2(), equalTo(p.getPerson2()));
    }

    @Test
    public void canParseListOfPairs() throws IOException{
        addPairListResponse();
        assertThat(pairDao.getHistory().execute().body().size(), equalTo(1));
    }

}
