package com.BetterTogether.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import DB.ApiClient;
import DB.CallbackWrapper;
import DB.Dao.PairDao;
import DB.Dao.PersonDao;
import DB.Dao.RewardDao;
import DB.RewardType;
import com.BetterTogether.app.Logic.Pair;
import com.BetterTogether.app.Logic.Person;
import com.BetterTogether.app.Logic.Reward;
import com.BetterTogether.app.Logic.Threshold;
import DB.ApiResponseHelpers.ResponsePojoConverter;
import DB.ApiResponseHelpers.RewardResponse;
import retrofit2.Retrofit;

public class DataManager extends Observable {

    private Retrofit apiClient;
    private PersonDao personDao;
    private PairDao pairDao;
    private RewardDao rewardDao;


    private List<Person> allUsers;
    private List<Person> activeUsers;

    private List<Pair> allPairs;

    private List<Pair> pizzaPairs;
    private List<Pair> cakePairs;
    private int cakeThreshold;
    private int pizzaThreshold;

    private int unusedCake;
    private int unusedPizza;

    public DataManager() {
        this(ApiClient.getRetrofitInstance());
    }

    public DataManager(Retrofit apiClient) {
        this.apiClient = apiClient;
        personDao = apiClient.create(PersonDao.class);
        pairDao = apiClient.create(PairDao.class);
        rewardDao = apiClient.create(RewardDao.class);

        cakeThreshold = 10000;
        pizzaThreshold = 10000;
        unusedCake = -1;
        unusedPizza = -1;
        pizzaPairs = new ArrayList<>();
        cakePairs = new ArrayList<>();
        allPairs = new ArrayList<>();
        allUsers = new ArrayList<>();
        activeUsers = new ArrayList<>();

        //get data from database
        updatePairs();
        updateThresholds();
        updateUnusedRewards();
        updateAllUsers();
        updateActiveUsers();
    }

    private void updatePairs() {
        pairDao.getHistory().enqueue(
                new CallbackWrapper<>((throwable, response) -> {
                    this.allPairs = ResponsePojoConverter.pairResponseToPair(response.body());
                    setChanged();
                    notifyObservers();
                }));

        pairDao.getPairsSinceLastReward(RewardType.PIZZA).enqueue(
                new CallbackWrapper<>((throwable, response) -> {
                    this.pizzaPairs = ResponsePojoConverter.pairResponseToPair(response.body());
                    setChanged();
                    notifyObservers();
                })
        );

        pairDao.getPairsSinceLastReward(RewardType.CAKE).enqueue(
                new CallbackWrapper<>((throwable, response) -> {
                    this.cakePairs = ResponsePojoConverter.pairResponseToPair(response.body());
                    setChanged();
                    notifyObservers();
                })
        );

    }

    public void setNewThresholdValue(Threshold t){
        rewardDao.setThreshold(ResponsePojoConverter.thresholdToThresholdResponse(t)).enqueue(
                new CallbackWrapper<>((throwable, response) -> {
                    if(response.body().size() == 0) {
                        throwable.printStackTrace();
                        return;
                    }

                    if(response.body().get(0).getRewardtype() == RewardType.PIZZA)
                        pizzaThreshold = response.body().get(0).getThreshold();
                    else
                        cakeThreshold = response.body().get(0).getThreshold();
                    setChanged();
                    notifyObservers();
                })
        );
    }



    private void updateThresholds() {
        rewardDao.getThreshold(RewardType.PIZZA).enqueue(
                new CallbackWrapper<>((throwable, response) ->
                    this.pizzaThreshold = response.body().get(0).getThreshold()
                ));
        rewardDao.getThreshold(RewardType.CAKE).enqueue(
                new CallbackWrapper<>((throwable, response) ->
                this.cakeThreshold = response.body().get(0).getThreshold())
        );
    }


    private void updateUnusedRewards() {
        rewardDao.numberOfUnusedRewards(RewardType.PIZZA).enqueue(
                new CallbackWrapper<>((throwable, response) -> {
                    this.unusedPizza = response.body();
                    setChanged();
                    notifyObservers();
                })
        );
        rewardDao.numberOfUnusedRewards(RewardType.CAKE).enqueue(
                new CallbackWrapper<>((throwable, response)-> {
                    this.unusedCake = response.body();
                    setChanged();
                    notifyObservers();
                })
        );
    }

    private void updateActiveUsers() {
        personDao.getAllActivePersons().enqueue(
                new CallbackWrapper<>((throwable, response) -> {
                    if(response.body() == null){
                        throwable.printStackTrace();
                        return;
                    }
                    activeUsers = ResponsePojoConverter.personResponseToPerson(response.body());
                    setChanged();
                    notifyObservers();
                }));
    }


    private void updateAllUsers() {
        personDao.getAllPersons().enqueue(
                new CallbackWrapper<>((throwable, response) -> {
                    allUsers = ResponsePojoConverter.personResponseToPerson(response.body());
                    setChanged();
                    notifyObservers();
                })
        );

    }


    public void addReward(RewardType type) {
        RewardResponse res = ResponsePojoConverter.rewardToRewardResponse(
                new Reward(new Date(), type));

        rewardDao.addReward(res).enqueue(
                new CallbackWrapper<>((throwable, response) -> {
                    updatePairs();
                    updateUnusedRewards();
                    setChanged();
                    notifyObservers();

        }));
    }

    public void addUser(String userName, String name, byte[] img) {
        Person newUser = new Person(userName, name, img, true);

        if (!allUsers.contains(newUser))
            personDao.insertPerson(ResponsePojoConverter.personToPersonResponse(newUser)).enqueue(
                    new CallbackWrapper<>((throwable, response) -> {
                        updateActiveUsers();

                    }));

    }

    public void addUser(Person person) {
        addUser(person.getUsername(), person.getName(), person.getImage());
    }

    public void editUser(Person person) {
        personDao.updatePerson(ResponsePojoConverter.personToPersonResponse(person)).enqueue(
                new CallbackWrapper<>((throwable, response) ->
                        updateActiveUsers()
                ));
    }


    public void addPair(Pair pair) {
        pairDao.insertPair(ResponsePojoConverter.pairToPairResponse(pair)).enqueue(
                new CallbackWrapper<>((throwable, response) ->
                        updatePairs()
                ));
    }


    public void setUseVariableToTrue(RewardType rewardType) {
        rewardDao.updateReward(rewardType.toString()).enqueue(
                new CallbackWrapper<>((throwable, response) -> {
                    updateUnusedRewards();
                })
        );

    }

    public List<Person> getAllUsers() {
        return allUsers;
    }

    public List<Pair> getAllPairs() {
        return allPairs;
    }

    public List<Pair> getPizzaPairs() {
        return pizzaPairs;
    }

    public List<Pair> getCakePairs() {
        return cakePairs;
    }

    public int getCakeThreshold() {
        return cakeThreshold;
    }

    public int getPizzaThreshold() {
        return pizzaThreshold;
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