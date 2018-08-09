package com.bettertogether.app;


import java.util.ArrayList;
import java.util.List;

import db.ApiClient;
import db.CallbackWrapper;
import db.dao.PairDao;
import db.dao.PersonDao;
import db.dao.RewardDao;
import db.RewardType;

import db.responseparsers.ResponsePojoConverter;
import db.responseparsers.RewardResponse;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DataManager {

    private PersonDao personDao;
    private PairDao pairDao;
    private RewardDao rewardDao;

    private List<Pair> allPairs;
    private List<Person> activeUsers;

    private DataUpdateListener listener;

    public DataManager(String token, DataUpdateListener listener) {
        Retrofit apiClient = ApiClient.getRetrofitInstance(token);
        personDao = apiClient.create(PersonDao.class);
        pairDao = apiClient.create(PairDao.class);
        rewardDao = apiClient.create(RewardDao.class);

        this.listener = listener;
        allPairs = new ArrayList<>();
        activeUsers = new ArrayList<>();

        validateToken();

    }

    private boolean isValidResponse(Throwable t, Response r) {
        if (r == null) {
            listener.responseError(-1, t.getMessage());
            return false;
        } else if (r.code() == 200) {
            return true;
        } else {
            listener.responseError(r.code(), r.message());
            return false;
        }
    }

    private void validateToken() {
        // This is an arbitrary choice of request, to see what
        // responses we get.
        pairDao.getHistory().enqueue(new CallbackWrapper<>((throwable, response) -> {
            if (isValidResponse(throwable, response))
                initializeData();
        }));

    }

    private void initializeData() {
        updatePairs();
        updateActiveUsers();
    }

    private void updatePairs() {
        pairDao.getHistory().enqueue(
                new CallbackWrapper<>((throwable, response) -> {
                    if(isValidResponse(throwable, response)) {
                        this.allPairs = ResponsePojoConverter.pairResponseToPair(response.body());
                        listener.updateStatus();

                    }
                }));

    }

    public void addPair(Pair pair) {
        pairDao.insertPair(ResponsePojoConverter.pairToPairResponse(pair)).enqueue(
                new CallbackWrapper<>((throwable, response) -> {
                    if (isValidResponse(throwable, response))
                        updatePairs();
                }
                ));
    }

    private void updateActiveUsers() {
        personDao.getAllActivePersons().enqueue(
                new CallbackWrapper<>((throwable, response) -> {
                    activeUsers = ResponsePojoConverter.personResponseToPerson(response.body());
                    listener.updateGrid();
                }));
    }


    public List<Pair> getAllPairs() {
        return allPairs;
    }

    public List<Person> getActiveUsers() {
        return activeUsers;
    }

}
