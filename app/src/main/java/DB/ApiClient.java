package DB;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://bettertogether.azurewebsites.net";

    //precondition: token is never empty in host
    private static String token = "";

    public static Retrofit getRetrofitInstance(String token) {
        if (token.equals(ApiClient.token) && retrofit != null) {
            return retrofit;
        }

        ApiClient.token = token;

        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(new OkHttpClient.Builder()
                        .addInterceptor(getTokenInterceptor(token))
                        .build())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();


        return retrofit;
    }


    private static Interceptor getTokenInterceptor(String token){
        return chain -> {
            Request request = chain.request();
            HttpUrl url = request.url()
                    .newBuilder()
                    .addQueryParameter("token", token).build();
            request = request.newBuilder().url(url).build();
            return chain.proceed(request);
        };
    }

}
