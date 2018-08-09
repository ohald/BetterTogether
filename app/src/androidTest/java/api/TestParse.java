package api;


import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import db.Dao;

import com.bettertogether.app.Pair;

import db.RewardType;
import db.responseparsers.PairResponse;
import db.responseparsers.PersonResponse;
import db.responseparsers.ResponsePojoConverter;
import db.responseparsers.RewardResponse;
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

    private Dao dao;
    private Retrofit API;

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

        dao = API.create(Dao.class);

    }

    @Test
    public void mockingOk(){
        assertTrue(API != null);
    }

    @After
    public void closeDb() throws IOException {
      mockBackend.close();
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



    @Test
    public void canParseRewardResponse() throws IOException{
        addRewardResponse();
        RewardResponse res = dao.getRewards().execute().body().get(0);

        assertThat(res.getRewardType(), equalTo(RewardType.PIZZA));
        assertThat(res.getUsedReward(), equalTo(false));
    }

    @Test
    public void canParsePersonResponse() throws IOException {
        addUserListResponse();
        PersonResponse p = dao.getAllActivePersons().execute().body().get(0);

        assertThat(p.getUsername(), equalTo("ohald"));
        assertThat(p.getName(), equalTo("Oyvor"));
        assertThat(p.getActive(), equalTo(true));
    }

    @Test
    public void canParseListOfPeople() throws IOException{
        addUserListResponse();

        List<PersonResponse> response = dao.getAllActivePersons().execute().body();
        assertThat(response.size(), equalTo(1));
    }


    @Test
    public void canParsePairResponse() throws IOException{
        addPairResponse();
        Pair p = new Pair("esog", "ohald");
        PairResponse r = dao.insertPair(
                ResponsePojoConverter.pairToPairResponse(p)).execute().body();

        assertThat(r.getPerson1(), equalTo(p.getPerson1()));
        assertThat(r.getPerson2(), equalTo(p.getPerson2()));
    }

    @Test
    public void canParseListOfPairs() throws IOException{
        addPairListResponse();
        assertThat(dao.getHistory().execute().body().size(), equalTo(1));
    }

}
