package db;

import retrofit2.Callback;

import retrofit2.Call;
import retrofit2.Response;

public class CallbackWrapper<T> implements Callback<T> {
    private Wrapper<T> wrapper;

    public CallbackWrapper(Wrapper<T> wrapper) {
        this.wrapper = wrapper;
    }

    public void onFailure(Call<T> call, Throwable t) {
        wrapper.onResult(t, null);
    }

    public void onResponse(Call<T> call, Response<T> response) {
        wrapper.onResult(null, response);
    }

    public interface Wrapper<T> {
        void onResult(Throwable t, Response<T> response);
    }
}
