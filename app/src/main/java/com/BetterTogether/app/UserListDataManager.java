package com.BetterTogether.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Observable;

import DB.DatabaseThreadHandler;
import DB.RewardType;
import DB.Tables.Pair;
import DB.Tables.Person;
import DB.Tables.Reward;
import JSONReader.ImageReader;

public class UserListDataManager extends Observable {

    private DatabaseThreadHandler handler;
    private List<Person> allUsers;
    private List<Person> activeUsers;

    private List<Pair> allPairs;

    private List<Pair> pizzaPairs;
    private List<Pair> cakePairs;
    private int cakeThreshold;
    private int pizzaThreshold;

    private int unusedCake;
    private int unusedPizza;

    UserListDataManager(DatabaseThreadHandler handler) {
        this.handler = handler;
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

    @SuppressLint("CheckResult")
    void updatePairs() {
        handler.getPairHistory(new Date(new GregorianCalendar(1900, 01, 01,
                00, 00, 00).getTimeInMillis()))
                .subscribe(pairs -> {
                    this.allPairs = pairs;
                    setChanged();
                    notifyObservers();
                });

        handler.getPairsSinceLastReward(RewardType.PIZZA).subscribe(pairs -> {
                this.pizzaPairs = pairs;
                setChanged();
                notifyObservers();
    });

        handler.getPairsSinceLastReward(RewardType.CAKE).subscribe(pairs -> {
                this.cakePairs = pairs;
                setChanged();
                notifyObservers();
        });

    }

    @SuppressLint("CheckResult")
    void updateThresholds() {
        handler.getThreshold(RewardType.PIZZA).subscribe(threshold ->
                this.pizzaThreshold = threshold);

        handler.getThreshold(RewardType.CAKE).subscribe(threshold ->
                this.cakeThreshold = threshold);
    }

    @SuppressLint("CheckResult")
    void updateUnusedRewards() {
        handler.getUnusedRewardsCount(RewardType.CAKE).subscribe(count ->
                this.unusedCake = count);

        handler.getUnusedRewardsCount(RewardType.PIZZA).subscribe(count ->
                this.unusedPizza = count);
    }

    @SuppressLint("CheckResult")
    void updateActiveUsers() {
        handler.allActivePersons().subscribe(
                persons -> {
                    activeUsers = persons;
                    setChanged();
                    notifyObservers();
                });
    }


    @SuppressLint("CheckResult")
    void updateAllUsers() {
        handler.allPersons().subscribe(
                persons -> {
                    allUsers = persons;
                });
    }

    @SuppressLint("CheckResult")
    void addReward(RewardType type) {
        handler.addNewReward(new Reward(new Date(), type)).subscribe(longs -> {
            updatePairs();
            updateUnusedRewards();
        });
    }

    @SuppressLint("CheckResult")
    void addUser(String userName, String firstName, String lastName, byte[] img) {
        Person newUser = new Person(userName, firstName, lastName, img, true);
        handler.allPersons().subscribe(persons -> {
            if (persons.contains(newUser)) {
                //Toast.makeText(userListFragment.getContext(), "User already exists: " + userName, Toast.LENGTH_SHORT).show();
                return;
            }
            handler.addPerson(newUser).subscribe(num -> {
                //Toast.makeText(userListFragment.getContext()
                //        , "Welcome " + newUser.getFirstName(), Toast.LENGTH_SHORT).show();
                updateActiveUsers();
            });
        });
    }

    @SuppressLint("CheckResult")
    void editUser(Person person, String firstName, String lastName, Bitmap image) {
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setImage(ImageReader.bitmapToByte(image));
        handler.udpatePerson(person).subscribe(integer -> {
            Log.d("Room", "User edited");
            updateActiveUsers();
        }, error -> error.printStackTrace());
    }

    @SuppressLint("CheckResult")
    void addPair(Pair pair) {
        handler.addPair(pair).subscribe(
                longs -> {
                   // Toast.makeText(userListFragment.getContext(),
                    //        "Added pair programming with: " + pair.getPerson1() +
                    //                " and " + pair.getPerson2(), Toast.LENGTH_SHORT).show();
                    updatePairs();
                },
                error -> error.printStackTrace());
    }


    @SuppressLint("CheckResult")
    public void setUseVariableToTrue(RewardType rewardType) {
        handler.getEarliestUnusedReward(rewardType).subscribe(rewards -> {
            rewards.get(0).setUsedReward(true);
            handler.useReward(rewards.get(0)).subscribe(integer -> {
                Log.d("Room", "Used reward");
                updateUnusedRewards();
            }, error -> Log.d("Room", "failed to edit reward"));

        });
    }

    public void refreshDB(Context context) {
        handler.refreshDB(context);
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