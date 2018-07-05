package com.BetterTogether.app;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import DB.DatabaseThreadHandler;
import DB.RewardType;
import DB.Tables.Pair;
import DB.Tables.Person;
import DB.Tables.Reward;
import JSONReader.ImageReader;

public class UserListDataManager {

    private DatabaseThreadHandler handler;
    private UserListFragment userListFragment;
    private List<Person> users;


    UserListDataManager(UserListFragment userListFragment, DatabaseThreadHandler handler) {
        this.userListFragment = userListFragment;
        this.handler = handler;
    }

    @SuppressLint("CheckResult")
    void getPairs() {
        handler.getPairHistory(new Date(new GregorianCalendar(1900, 01, 01,
                00, 00, 00).getTimeInMillis()))
                .subscribe(pairs -> userListFragment.setAllPairs(pairs));

        handler.getPairsSinceLastReward(RewardType.PIZZA).subscribe(pairs ->
                userListFragment.setPizzaPairs(pairs));

        handler.getPairsSinceLastReward(RewardType.CAKE).subscribe(pairs ->
                userListFragment.setCakePairs(pairs));
    }

    @SuppressLint("CheckResult")
    void getThresholds() {
        handler.getThreshold(RewardType.PIZZA).subscribe(threshold ->
                userListFragment.setPizzaThreshold(threshold));

        handler.getThreshold(RewardType.CAKE).subscribe(threshold ->
                userListFragment.setCakeThreshold(threshold));
    }

    @SuppressLint("CheckResult")
    void getUnusedRewards() {
        handler.getUnusedRewardsCount(RewardType.CAKE).subscribe(count ->
                userListFragment.setUnusedCake(count));

        handler.getUnusedRewardsCount(RewardType.PIZZA).subscribe(count ->
                userListFragment.setUnusedPizza(count));
    }

    @SuppressLint("CheckResult")
    void getActiveUsers() {
        handler.allActivePersons().subscribe(
                persons -> {
                    userListFragment.setUpGridView(persons);
                    users = persons;
                },
                error -> Toast.makeText(userListFragment.getContext(),
                        "Failed loading users from database", Toast.LENGTH_SHORT).show());
    }

    @SuppressLint("CheckResult")
    void addReward(RewardType type) {
        handler.addNewReward(new Reward(new Date(), type)).subscribe(longs -> {
            getPairs();
            getUnusedRewards();
        });
    }

    @SuppressLint("CheckResult")
    void addUser(String userName, String firstName, String lastName, byte[] img) {
        Person newUser = new Person(userName, firstName, lastName, img, true);
        if (users.contains(newUser)){
            Toast.makeText(userListFragment.getContext(), "User already exists", Toast.LENGTH_SHORT).show();
            return;
        }
        handler.addPerson(newUser).subscribe(num -> {
            Toast.makeText(userListFragment.getContext()
                    , "Welcome " + newUser.getFirstName(), Toast.LENGTH_SHORT).show();
            getActiveUsers();

        });
    }

    @SuppressLint("CheckResult")
    void editUser(Person person, String firstName, String lastName, Bitmap image) {
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setImage(ImageReader.bitmapToByte(image));
        handler.udpatePerson(person).subscribe(integer ->
                Log.d("Room", "User edited"), error -> error.printStackTrace());
    }

    @SuppressLint("CheckResult")
    void addPair(Pair pair) {
        handler.addPair(pair).subscribe(
                longs -> {
                    Toast.makeText(userListFragment.getContext(),
                            "Added pair programming with: " + pair.getPerson1() +
                                    " and " + pair.getPerson2(), Toast.LENGTH_SHORT).show();
                    getPairs();
                },
                error -> Toast.makeText(userListFragment.getContext(),
                        "Something went wrong while inserting to database.",
                        Toast.LENGTH_SHORT).show());
    }
}
