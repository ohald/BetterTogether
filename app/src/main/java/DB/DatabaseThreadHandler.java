package DB;

import android.content.Context;
import android.util.Log;

import java.util.Date;
import java.util.List;
import DB.Dao.PairDao;
import DB.Dao.PersonDao;
import DB.Dao.RewardDao;
import DB.Tables.Pair;
import DB.Tables.Person;
import DB.Tables.Reward;
import DB.Tables.Threshold;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DatabaseThreadHandler {

    private SQLiteDB db;
    private PersonDao personDao;
    private PairDao pairDao;
    private RewardDao rewDao;

    public DatabaseThreadHandler(Context context){
        db = SQLiteDB.getInstance(context);
        personDao = db.personDao();
        pairDao = db.pairDao();
        rewDao = db.rewardDao();
    }

    public Maybe<List<Person>> allPersons(){
        return Maybe.fromCallable(()
                -> personDao.getAllPersons())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Long> addPair(Pair pair) {
        return Single.fromCallable(()
                -> pairDao.insertPair(pair))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<List<Pair>> getPairHistory(Date date){
        return Maybe.fromCallable(()
                -> pairDao.getHistory(date))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Integer> setThreshold(Threshold threshold){
        return Single.fromCallable(()
                -> rewDao.setThreshold(threshold))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<Date> getLastReward(RewardType type){
        return Maybe.fromCallable(()
                -> rewDao.getLastRewardDate(type))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Long> addNewReward(Reward reward){
        return Single.fromCallable(()
                -> rewDao.addReward(reward))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<Person> getPerson(String username){
        return Maybe.fromCallable(()
                -> personDao.getPerson(username))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Integer> getThreshold(RewardType type){
        return Single.fromCallable(()
                -> rewDao.getThreshold(type))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
