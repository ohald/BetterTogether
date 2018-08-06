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
    private List<Pair> pizzaPairs;
    private List<Pair> cakePairs;
    private int cakeThreshold;
    private int pizzaThreshold;

    private int unusedCake;
    private int unusedPizza;

    private DataUpdateListener listener;

    public DataManager(String token, DataUpdateListener listener) {
        Retrofit apiClient = ApiClient.getRetrofitInstance(token);
        personDao = apiClient.create(PersonDao.class);
        pairDao = apiClient.create(PairDao.class);
        rewardDao = apiClient.create(RewardDao.class);

        this.listener = listener;

        cakeThreshold = 10000;
        pizzaThreshold = 10000;
        unusedCake = -1;
        unusedPizza = -1;
        pizzaPairs = new ArrayList<>();
        cakePairs = new ArrayList<>();
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
        rewardDao.getThreshold(RewardType.PIZZA).enqueue(new CallbackWrapper<>((throwable, response) -> {
            if (isValidResponse(throwable, response))
                initializeData();
        }));

    }

    private void createRewardIfReached(RewardType type) {
        if (pizzaPairs.size() >= pizzaThreshold && type == RewardType.PIZZA) {
            addReward(RewardType.PIZZA);
        }
        if (cakePairs.size() >= cakeThreshold && type == RewardType.CAKE) {
            addReward(RewardType.CAKE);
        }
    }

    private void initializeData() {
        updatePairs();
        updateThresholds();
        updateUnusedRewards();
        updateActiveUsers();
    }

    private void updatePairs() {
        pairDao.getHistory().enqueue(
                new CallbackWrapper<>((throwable, response) -> {
                    if (isValidResponse(throwable, response)) {
                        this.allPairs = ResponsePojoConverter.pairResponseToPair(response.body());
                        listener.updateStatus();
                    }
                }));

        pairDao.getPairsSinceLastReward(RewardType.PIZZA).enqueue(
                new CallbackWrapper<>((throwable, response) -> {
                    if (isValidResponse(throwable, response)) {
                        this.pizzaPairs = ResponsePojoConverter.pairResponseToPair(response.body());
                        listener.updateStatus();
                        createRewardIfReached(RewardType.PIZZA);
                    }
                })
        );

        pairDao.getPairsSinceLastReward(RewardType.CAKE).enqueue(
                new CallbackWrapper<>((throwable, response) -> {
                    if (isValidResponse(throwable, response)) {
                        this.cakePairs = ResponsePojoConverter.pairResponseToPair(response.body());
                        listener.updateStatus();
                        createRewardIfReached(RewardType.CAKE);
                    }
                })
        );

    }


    private void updateThresholds() {
        rewardDao.getThreshold(RewardType.PIZZA).enqueue(
                new CallbackWrapper<>((throwable, response) -> {
                    if (isValidResponse(throwable, response)) {
                        this.pizzaThreshold = response.body().get(0).getThreshold();
                        listener.updateStatus();
                    }
                }));
        rewardDao.getThreshold(RewardType.CAKE).enqueue(
                new CallbackWrapper<>((throwable, response) -> {
                    if (isValidResponse(throwable, response)) {
                        this.cakeThreshold = response.body().get(0).getThreshold();
                        listener.updateStatus();
                    }
                }));
    }


    private void updateUnusedRewards() {
        rewardDao.numberOfUnusedRewards(RewardType.PIZZA).enqueue(
                new CallbackWrapper<>((throwable, response) -> {
                    this.unusedPizza = response.body();
                    listener.updateStatus();

                })
        );
        rewardDao.numberOfUnusedRewards(RewardType.CAKE).enqueue(
                new CallbackWrapper<>((throwable, response) -> {
                    this.unusedCake = response.body();
                    listener.updateStatus();
                })
        );
    }

    private void updateActiveUsers() {
        personDao.getAllActivePersons().enqueue(
                new CallbackWrapper<>((throwable, response) -> {
                    if (isValidResponse(throwable, response)) {
                        activeUsers = ResponsePojoConverter.personResponseToPerson(response.body());
                        listener.updateGrid();
                    }
                }));
    }


    private void addReward(RewardType type) {
        RewardResponse res = ResponsePojoConverter.rewardToRewardResponse(
                new Reward(type));

        rewardDao.addReward(res).enqueue(
                new CallbackWrapper<>((throwable, response) -> {
                    if (isValidResponse(throwable, response)) {
                        updatePairs();
                        updateUnusedRewards();
                        listener.updateStatus();
                        listener.rewardReached(type);
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


    public void setUseVariableToTrue(RewardType rewardType) {
        rewardDao.updateReward(rewardType.toString()).enqueue(
                new CallbackWrapper<>((throwable, response) -> {
                    if (isValidResponse(throwable, response))
                        updateUnusedRewards();
                }
                )
        );

    }


    public List<Pair> getAllPairs() {
        return allPairs;
    }

    public int getUnusedCake() {
        return unusedCake;
    }

    public int getUnusedPizza() {
        return unusedPizza;
    }

    public List<Person> getActiveUsers() {
        return activeUsers;
    }

}
