package DB;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://10.52.130.150:5000";

    public static Retrofit getRetrofitInstance(String token) {
        //token for access
        Interceptor i = chain -> {
            Request request = chain.request();
            HttpUrl url = request.url()
                    .newBuilder()
                    .addQueryParameter("token", token).build();
            request = request.newBuilder().url(url).build();
            return chain.proceed(request);
        };


        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(new OkHttpClient.Builder().addInterceptor(i).build())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

}
